import api from "./axios";

export const departmentApi = {
  // Get all departments
  getAll: () => api.get("/departments"),

  // Get single department by ID
  getById: (id) => api.get(`/departments/${id}`),
};
