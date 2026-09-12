import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api/axios";
import { toast } from "react-toastify";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const userData = localStorage.getItem("user");
    if (token && userData) {
      try {
        setUser(JSON.parse(userData));
      } catch (error) {
        console.error("Error parsing user data:", error);
        localStorage.removeItem("user");
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await api.post("/auth/login", { email, password });
      console.log("Login response:", response.data);

      // Extract data from response
      const { data } = response.data;
      const { accessToken } = data;

      if (!accessToken) {
        throw new Error("No access token received");
      }

      // Store token
      localStorage.setItem("accessToken", accessToken);

      // Since user data is not coming from login API,
      // we need to fetch user profile separately
      try {
        // Try to get user profile
        const profileResponse = await api.get("/patient/profile");
        const userData = profileResponse.data.data;

        // Store user data
        localStorage.setItem("user", JSON.stringify(userData));
        setUser(userData);

        toast.success(`Welcome back, ${userData.fullName || "User"}! 🎉`);
        return { success: true };
      } catch (profileError) {
        // If profile fetch fails, create a basic user object
        // You might need to adjust this based on your backend
        const basicUser = {
          email: email,
          fullName: email.split("@")[0],
          roles: ["PATIENT"], // Default role
        };
        localStorage.setItem("user", JSON.stringify(basicUser));
        setUser(basicUser);
        toast.success("Login successful!");
        return { success: true };
      }
    } catch (error) {
      console.error("Login error:", error);
      const message =
        error.response?.data?.message || error.message || "Login failed";
      toast.error(message);
      return { success: false, error: message };
    }
  };

  const register = async (userData) => {
    try {
      await api.post("/auth/register", userData);
      toast.success("Registration successful! Please login.");
      return { success: true };
    } catch (error) {
      console.error("Registration error:", error);
      const message = error.response?.data?.message || "Registration failed";
      toast.error(message);
      return { success: false, error: message };
    }
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("user");
    setUser(null);
    toast.info("Logged out successfully");
    window.location.href = "/login";
  };

  const value = {
    user,
    setUser,
    loading,
    login,
    register,
    logout,
    isAuthenticated: !!user && !!localStorage.getItem("accessToken"),
    isAdmin: user?.roles?.includes("ADMIN"),
    isPatient: user?.roles?.includes("PATIENT"),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
