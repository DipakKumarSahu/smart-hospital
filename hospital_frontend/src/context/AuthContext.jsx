import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axios";
import { authApi } from "../api/authApi";  // 👈 Ye import karo
import { toast } from "react-toastify";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
};

// Token decode helper
const decodeToken = (token) => {
  try {
    return JSON.parse(atob(token.split(".")[1]));
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

      // Fetch profile after login
      let userData = null;
      try {
        const profileResponse = await api.get("/patient/profile", {
          headers: { Authorization: `Bearer ${accessToken}` },
        });
        userData = profileResponse.data.data;
        userData.roles = ["PATIENT"];
      } catch {
        userData = {
          email,
          fullName: email.split("@")[0],
          roles: ["PATIENT"],
        };
      }

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
      // 👇 Use authApi.registerPatient
      await authApi.registerPatient(userData);
      toast.success("Registration successful. Please sign in.");
      return { success: true };
    } catch (error) {
      const message =
        error.response?.data?.message || "Registration failed";
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
    isPatient: user?.roles?.includes("PATIENT"),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};