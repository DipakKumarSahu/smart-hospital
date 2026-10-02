import api from "./axios";

export const authApi = {
  // Login
  login: (credentials) => api.post("/auth/login", credentials),

  // Register new patient
  registerPatient: (data) => api.post("/auth/register/patient", data),
};
