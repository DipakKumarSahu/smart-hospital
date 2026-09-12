export const BLOOD_GROUPS = [
  { value: "A_POSITIVE", label: "A+" },
  { value: "A_NEGATIVE", label: "A-" },
  { value: "B_POSITIVE", label: "B+" },
  { value: "B_NEGATIVE", label: "B-" },
  { value: "AB_POSITIVE", label: "AB+" },
  { value: "AB_NEGATIVE", label: "AB-" },
  { value: "O_POSITIVE", label: "O+" },
  { value: "O_NEGATIVE", label: "O-" },
];

export const GENDERS = [
  { value: "MALE", label: "Male" },
  { value: "FEMALE", label: "Female" },
  { value: "OTHER", label: "Other" },
];

export const ROLES = {
  ADMIN: "ADMIN",
  DOCTOR: "DOCTOR",
  NURSE: "NURSE",
  PATIENT: "PATIENT",
};

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    LOGOUT: "/auth/logout",
  },
  PATIENT: {
    BASE: "/patients",
    PROFILE: "/patient/profile",
  },
};

export const STATUS_COLORS = {
  ACTIVE: "green",
  INACTIVE: "red",
  PENDING: "yellow",
  COMPLETED: "blue",
  CANCELLED: "gray",
};

export const PAGINATION = {
  DEFAULT_PAGE: 0,
  DEFAULT_SIZE: 10,
  SIZES: [5, 10, 20, 50],
};

export const DATE_FORMATS = {
  DISPLAY: "MMM d, yyyy",
  DISPLAY_TIME: "MMM d, yyyy h:mm a",
  API: "yyyy-MM-dd",
  API_TIME: "yyyy-MM-dd HH:mm:ss",
};


export const VISIT_TYPES = [
  { value: "ONLINE", label: "Online (Video Call)" },
  { value: "OFFLINE", label: "Offline (In-Person)" },
];

export const APPOINTMENT_STATUS = {
  PENDING_PAYMENT: { label: "Payment Pending", color: "yellow" },
  BOOKED: { label: "Booked", color: "blue" },
  CONFIRMED: { label: "Confirmed", color: "green" },
  CANCELLED: { label: "Cancelled", color: "red" },
  COMPLETED: { label: "Completed", color: "green" },
  NO_SHOW: { label: "No Show", color: "gray" },
  EXPIRED: { label: "Expired", color: "gray" },
};

export const PAYMENT_STATUS = {
  PENDING: { label: "Pending", color: "yellow" },
  PAID: { label: "Paid", color: "green" },
  FAILED: { label: "Failed", color: "red" },
  REFUNDED: { label: "Refunded", color: "gray" },
};

export const SLOT_STATUS = {
  AVAILABLE: "Available",
  BOOKED: "Booked",
  BLOCKED: "Blocked",
  HELD: "Held",
  CANCELLED: "Cancelled",
  COMPLETED: "Completed",
};
