import api from "./axios";

export const staffApi = {
  // Get paginated staff list with search
  getAll: (params) => api.get("/staff", { params }),

  // Create new staff
  create: (data) => api.post("/staff", data),

  // Toggle Active/Inactive status
  toggleStatus: (id) => api.patch(`/staff/${id}/toggle-status`),

  // Get dashboard stats
  getStats: () => api.get("/staff/stats"),
};
