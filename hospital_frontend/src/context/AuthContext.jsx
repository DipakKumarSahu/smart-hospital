import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axios";
import { authApi } from "../api/authApi";
import { toast } from "react-toastify";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};

// Token decode helper
const decodeToken = (token) => {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join(""),
    );
    return JSON.parse(jsonPayload);
  } catch {
    return null;
  }
};

const isTokenExpired = (token) => {
  if (!token) return true;
  const decoded = decodeToken(token);
  if (!decoded || !decoded.exp) return true;
  return decoded.exp * 1000 < Date.now();
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const userData = localStorage.getItem("user");

    if (token && userData && !isTokenExpired(token)) {
      try {
        setUser(JSON.parse(userData));
      } catch {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("user");
      }
    } else {
      localStorage.removeItem("accessToken");
      localStorage.removeItem("user");
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await authApi.login({ email, password });
      const { accessToken } = response.data.data;

      localStorage.setItem("accessToken", accessToken);

      // 👇 1. Token Decode karo
      const decoded = decodeToken(accessToken);

      // 👇 2. Token se roles nikaalo (Spring Security inhe "roles" ya "authorities" mein bhejta hai)
      let userRoles = decoded?.roles || decoded?.authorities || [];

      // Agar roles object format mein hain (e.g. [{authority: "ROLE_ADMIN"}]), toh unhe string banao
      if (userRoles.length > 0 && typeof userRoles[0] === "object") {
        userRoles = userRoles.map((r) => r.authority || r.name);
      }

      // 👇 3. Roles clean karo (Agar backend ROLE_ prefix bhej raha hai, toh hata do)
      userRoles = userRoles.map((role) => role.replace("ROLE_", ""));

      // 👇 4. Agar roles khaali hain (token mein nahi hain), toh email se guess karo (Fallback)
      if (userRoles.length === 0) {
        if (email.includes("admin")) userRoles = ["ADMIN"];
        else if (email.includes("doctor")) userRoles = ["DOCTOR"];
        else userRoles = ["PATIENT"];
      }

      // 👇 5. User Data save karo (Profile API call hatadi)
      const userData = {
        email: email,
        fullName: decoded?.sub
          ? decoded.sub.split("@")[0]
          : email.split("@")[0],
        roles: userRoles, // Ab yahan ADMIN/DOCTOR/PATIENT sahi aayega
      };

      localStorage.setItem("user", JSON.stringify(userData));
      setUser(userData);

      toast.success(`Welcome back, ${userData.fullName}`);
      return { success: true, user: userData };
    } catch (error) {
      const message =
        error.response?.data?.message || "Invalid email or password";
      return { success: false, error: message };
    }
  };

  const register = async (userData) => {
    try {
      await authApi.registerPatient(userData);
      toast.success("Registration successful. Please sign in.");
      return { success: true };
    } catch (error) {
      const message = error.response?.data?.message || "Registration failed";
      return { success: false, error: message };
    }
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    setUser(null);
    toast.info("Logged out");
    window.location.href = "/login";
  };

  const value = {
    user,
    setUser,
    loading,
    login,
    register,
    logout,
    isAuthenticated:
      !!user &&
      !!localStorage.getItem("accessToken") &&
      !isTokenExpired(localStorage.getItem("accessToken")),
    isAdmin: user?.roles?.includes("ADMIN"),
    isDoctor: user?.roles?.includes("DOCTOR"),
    isPatient: user?.roles?.includes("PATIENT"),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
