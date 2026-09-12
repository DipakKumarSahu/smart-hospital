import axios from "axios";
import { toast } from "react-toastify";

const api = axios.create({
  baseURL: "/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message || "Something went wrong!";

    if (error.response?.status === 401) {
      localStorage.removeItem("accessToken");
      window.location.href = "/login";
      toast.error("Session expired. Please login again.");
    } else {
      toast.error(message);
    }

    return Promise.reject(error);
  },
);

export default api;
