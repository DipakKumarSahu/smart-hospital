import api from "./axios";

export const authApi = {
  // Login
  login: (credentials) => api.post("/auth/login", credentials),

  // Register new patient
  registerPatient: (data) => api.post("/auth/register/patient", data),

  registerDoctorByAdmin: (data) =>
    api.post("/auth/admin/register-doctor", data),

  updateDoctorByAdmin: (id, data) => api.put(`/doctors/${id}`, data),

  //  Naya method: Toggle Doctor Status
  toggleDoctorStatusByAdmin: (id) => api.delete(`/doctors/${id}`), // DELETE backend mein soft delete karta hai
};
