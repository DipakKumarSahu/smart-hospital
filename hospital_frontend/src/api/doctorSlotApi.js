import api from "./axios";

export const doctorSlotApi = {
  // Get slots for a specific date (Doctor can see all status)
  getSlotsByDate: (doctorId, slotDate, params) =>
    api.get("/appointment-slots", {
      params: { doctorId, slotDate, ...params },
    }),

  // Block slots (time range)
  blockSlots: (data) => api.patch("/appointment-slots/block", data),

  // Unblock slots
  unblockSlots: (data) => api.patch("/appointment-slots/unblock", data),

  // Update single slot status (if needed)
  updateSlotStatus: (id, data) => api.put(`/appointment-slots/${id}`, data),
};
