import api from "./axios";

export const doctorApi = {
  // Get all doctors (paginated)
  getAll: (params) => api.get("/doctors", { params }),

  // Get doctor by ID
  getById: (id) => api.get(`/doctors/${id}`),

  // Get doctors by department code
  getByDepartmentCode: (code) => api.get(`/doctors/department/code/${code}`),

  // Get doctors by department ID
  getByDepartmentId: (id) => api.get(`/doctors/department/id/${id}`),

  getAllForAdmin: (params) => api.get("/doctors/admin/all", { params }),
};
