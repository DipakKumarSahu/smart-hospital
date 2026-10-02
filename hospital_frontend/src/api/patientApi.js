import api from "./axios";

export const patientApi = {
  // =====================================================
  // PATIENT PROFILE
  // =====================================================
  getMyProfile: () => api.get("/patient/profile"),
  updateMyProfile: (data) => api.put("/patient/profile", data),

  // =====================================================
  // APPOINTMENTS
  // =====================================================

  // Book new appointment (creates appointment with PENDING_PAYMENT status)
  bookAppointment: (data) => api.post("/patient/appointments", data),

  // Get my appointments (paginated)
  getMyAppointments: (params) => api.get("/patient/appointments", { params }),

  // Get single appointment by ID
  getMyAppointmentById: (id) => api.get(`/patient/appointments/${id}`),

  // Cancel appointment
  cancelMyAppointment: (id) => api.patch(`/patient/appointments/${id}/cancel`),

  // =====================================================
  // PAYMENTS
  // =====================================================

  // Create payment intent for appointment
  createPayment: (appointmentId) => api.post("/payments", { appointmentId }),

  // Get payment by appointment ID
  getPaymentByAppointment: (appointmentId) =>
    api.get(`/payments/appointment/${appointmentId}`),

  // Get payment by ID
  getPaymentById: (id) => api.get(`/payments/${id}`),

  confirmPayment: (paymentIntentId) => 
    api.post('/payments/confirm', { paymentIntentId }),
};
