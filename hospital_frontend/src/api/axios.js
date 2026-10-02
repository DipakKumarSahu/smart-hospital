import axios from "axios";

const api = axios.create({
  baseURL: "/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 30000,
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
    // Network error (no response from server)
    if (!error.response) {
      if (error.code === "ECONNABORTED") {
        error.userMessage = "Request timed out. Please try again.";
      } else {
        error.userMessage = "Network error. Check your internet connection.";
      }
      return Promise.reject(error);
    }

    const status = error.response.status;
    const backendData = error.response.data;

    // 👇 Extract meaningful message from backend response
    let userMessage = "An error occurred";

    if (backendData) {
      // If backend returns { message: "..." }
      if (backendData.message) {
        userMessage = backendData.message;
      }
      // If backend returns { error: "..." }
      else if (backendData.error) {
        userMessage = backendData.error;
      }
      // If backend returns { errors: { field: "..." } } (validation errors)
      else if (backendData.errors && typeof backendData.errors === "object") {
        const firstError = Object.values(backendData.errors)[0];
        userMessage = Array.isArray(firstError) ? firstError[0] : firstError;
      }
      // If backend returns plain string
      else if (typeof backendData === "string") {
        userMessage = backendData;
      }
    }

    // 👇 Fallback messages for common status codes
    if (userMessage === "An error occurred") {
      switch (status) {
        case 400:
          userMessage = "Invalid request. Please check your input.";
          break;
        case 401:
          userMessage = "Session expired. Please login again.";
          break;
        case 403:
          userMessage = "You do not have permission for this action.";
          break;
        case 404:
          userMessage = "Resource not found.";
          break;
        case 409:
          userMessage = "Conflict. This record already exists.";
          break;
        case 422:
          userMessage = "Validation failed. Please check your input.";
          break;
        case 500:
          userMessage = "Server error. Please try again later.";
          break;
        case 503:
          userMessage = "Service unavailable. Please try again later.";
          break;
        default:
          userMessage = `Request failed (${status})`;
      }
    }

    // 👇 Handle 401 - clear auth and redirect
    if (status === 401) {
      const currentPath = window.location.pathname;

      if (currentPath !== "/login" && currentPath !== "/register") {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("user");
        window.location.href = "/login";
      }
    }

    // 👇 Attach user-friendly message to error object
    error.userMessage = userMessage;
    error.statusCode = status;

    // 👇 Don't show toast here - let components decide
    // Components will use error.userMessage for display

    return Promise.reject(error);
  },
);

export default api;
