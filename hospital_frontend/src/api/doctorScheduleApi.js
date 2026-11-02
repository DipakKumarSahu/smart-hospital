import api from "./axios";

export const doctorScheduleApi = {
  // Get logged-in doctor's schedules (we will filter it or fetch all)
  getAll: (params) => api.get("/doctor-schedules", { params }),

  // Create schedule (doctorId will be optional now, backend picks from JWT)
  create: (data) => api.post("/doctor-schedules", data),

  // Update schedule
  update: (id, data) => api.put(`/doctor-schedules/${id}`, data),

  // Delete schedule
  delete: (id) => api.delete(`/doctor-schedules/${id}`),
};
