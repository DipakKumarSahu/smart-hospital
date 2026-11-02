import api from "./axios";

export const appointmentApi = {
  // Get all appointments (Admin/Doctor)
  getAll: (params) => api.get("/appointments", { params }),

  // Update appointment status (Doctor can mark COMPLETED)
  updateStatus: (id, data) => api.put(`/appointments/${id}`, data),
};
