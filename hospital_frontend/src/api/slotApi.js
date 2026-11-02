import api from "./axios";

export const slotApi = {
  // Get available slots for a doctor on a specific date
  getAvailableSlots: (doctorId, slotDate) =>
    api.get("/appointment-slots/available", {
      params: { doctorId, slotDate },
    }),

  // Get all slots (paginated) for a doctor on a date
  getSlots: (doctorId, slotDate, params) =>
    api.get("/appointment-slots", {
      params: { doctorId, slotDate, ...params },
    }),
};
