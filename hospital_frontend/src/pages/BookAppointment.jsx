import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import StepIndicator from "../components/booking/StepIndicator";
import EmptyState from "../components/booking/EmptyState";
import { SkeletonList, SkeletonSlots } from "../components/booking/SkeletonCard";
import { patientApi } from "../api/patientApi";
import { departmentApi } from "../api/departmentApi";
import { doctorApi } from "../api/doctorApi";
import { slotApi } from "../api/slotApi";
import { toast } from "react-toastify";
import {
  ArrowLeft,
  ArrowRight,
  Building2,
  Calendar,
  Check,
  ChevronRight,
  Clock,
  IndianRupee,
  MapPin,
  Stethoscope,
  User,
  Video,
} from "lucide-react";

const VISIT_TYPES = [
  { value: "OFFLINE", label: "In-Person", icon: MapPin },
  { value: "ONLINE", label: "Online", icon: Video },
];

const STEPS = [
  { label: "Department" },
  { label: "Doctor" },
  { label: "Date & Time" },
  { label: "Details" },
];

const BookAppointment = () => {
  const navigate = useNavigate();

  // Step management
  const [currentStep, setCurrentStep] = useState(1);

  // Step 1: Departments
  const [departments, setDepartments] = useState([]);
  const [departmentsLoading, setDepartmentsLoading] = useState(true);
  const [departmentsError, setDepartmentsError] = useState("");
  const [selectedDepartment, setSelectedDepartment] = useState(null);

  // Step 2: Doctors
  const [doctors, setDoctors] = useState([]);
  const [doctorsLoading, setDoctorsLoading] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);

  // Step 3: Slots
  const [selectedDate, setSelectedDate] = useState("");
  const [slots, setSlots] = useState([]);
  const [slotsLoading, setSlotsLoading] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  // Step 4: Details
  const [visitType, setVisitType] = useState("OFFLINE");
  const [reasonForVisit, setReasonForVisit] = useState("");

  // Submission
  const [submitting, setSubmitting] = useState(false);

  // =====================================================
  // Step 1: Load departments
  // =====================================================
  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    setDepartmentsLoading(true);
    setDepartmentsError("");
    try {
      const response = await departmentApi.getAll();
      setDepartments(response.data.data || []);
    } catch (err) {
      setDepartmentsError(
        err.userMessage || "Unable to load departments"
      );
    } finally {
      setDepartmentsLoading(false);
    }
  };

  // =====================================================
  // Step 2: Load doctors when department is selected
  // =====================================================
  useEffect(() => {
    if (selectedDepartment) {
      fetchDoctors(selectedDepartment.departmentCode);
    }
  }, [selectedDepartment]);

  const fetchDoctors = async (departmentCode) => {
    setDoctorsLoading(true);
    try {
      const response = await doctorApi.getByDepartmentCode(departmentCode);
      setDoctors(response.data.data || []);
    } catch (err) {
      toast.error(err.userMessage || "Unable to load doctors");
      setDoctors([]);
    } finally {
      setDoctorsLoading(false);
    }
  };

  // =====================================================
  // Step 3: Load slots when doctor + date selected
  // =====================================================
  useEffect(() => {
    if (selectedDoctor && selectedDate) {
      fetchSlots();
    } else {
      setSlots([]);
      setSelectedSlot(null);
    }
  }, [selectedDoctor, selectedDate]);

  const fetchSlots = async () => {
    setSlotsLoading(true);
    try {
      const response = await slotApi.getAvailableSlots(
        selectedDoctor.id,
        selectedDate
      );
      setSlots(response.data.data || []);
    } catch (err) {
      toast.error(err.userMessage || "Unable to load slots");
      setSlots([]);
    } finally {
      setSlotsLoading(false);
    }
  };

  // =====================================================
  // Navigation handlers
  // =====================================================
  const handleDepartmentSelect = (dept) => {
    setSelectedDepartment(dept);
    setSelectedDoctor(null);
    setSelectedDate("");
    setSelectedSlot(null);
  };

  const handleDoctorSelect = (doctor) => {
    setSelectedDoctor(doctor);
    setSelectedDate("");
    setSelectedSlot(null);
  };

  const handleNext = () => {
    if (currentStep === 1 && selectedDepartment) {
      setCurrentStep(2);
    } else if (currentStep === 2 && selectedDoctor) {
      setCurrentStep(3);
    } else if (
      currentStep === 3 &&
      selectedSlot &&
      selectedDate
    ) {
      setCurrentStep(4);
    }
  };

  const handleBack = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    } else {
      navigate("/dashboard");
    }
  };

  const handleSubmit = async () => {
    if (!reasonForVisit.trim()) {
      toast.error("Please enter a reason for your visit");
      return;
    }

    if (reasonForVisit.length > 500) {
      toast.error("Reason cannot exceed 500 characters");
      return;
    }

    setSubmitting(true);
    try {
      const response = await patientApi.bookAppointment({
        doctorId: selectedDoctor.id,
        appointmentSlotId: selectedSlot.id,
        visitType,
        reasonForVisit: reasonForVisit.trim(),
      });

      const appointment = response.data.data;
      toast.success("Appointment booked. Proceeding to payment.");

      navigate(`/payment/${appointment.id}`);
    } catch (err) {
      toast.error(err.userMessage || "Failed to book appointment");
    } finally {
      setSubmitting(false);
    }
  };

  // =====================================================
  // Helpers
  // =====================================================
  const formatTime = (timeStr) => {
    if (!timeStr) return "";
    // Handle "09:00:00" or "09:00"
    const [hours, minutes] = timeStr.split(":");
    const h = parseInt(hours);
    const ampm = h >= 12 ? "PM" : "AM";
    const displayH = h % 12 || 12;
    return `${displayH}:${minutes} ${ampm}`;
  };

  const formatFee = (fee) => {
    if (fee == null) return "N/A";
    return `₹${Number(fee).toFixed(0)}`;
  };

  const today = new Date().toISOString().split("T")[0];
  const maxDate = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
    .toISOString()
    .split("T")[0];

  // =====================================================
  // Render
  // =====================================================
  return (
    <Layout>
      <div className="max-w-3xl mx-auto">
        {/* Header */}
        <div className="mb-6">
          <button
            onClick={handleBack}
            className="inline-flex items-center gap-1.5 text-sm text-slate-500 hover:text-slate-800 transition-colors mb-3"
          >
            <ArrowLeft size={14} />
            {currentStep === 1 ? "Back to dashboard" : "Previous step"}
          </button>
          <h1 className="text-2xl font-semibold text-slate-800">
            Book Appointment
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Select a department, doctor, and time slot for your visit
          </p>
        </div>

        {/* Step Indicator */}
        <StepIndicator currentStep={currentStep} steps={STEPS} />

        {/* Step Content */}
        <AnimatePresence mode="wait">
          {/* ============= STEP 1: DEPARTMENT ============= */}
          {currentStep === 1 && (
            <motion.div
              key="step1"
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.2 }}
            >
              <Card>
                <div className="flex items-center gap-2 mb-5">
                  <Building2 size={18} className="text-[#0284c7]" />
                  <h2 className="text-base font-semibold text-slate-800">
                    Select Department
                  </h2>
                </div>

                {departmentsLoading ? (
                  <SkeletonList count={4} />
                ) : departmentsError ? (
                  <EmptyState
                    icon={Building2}
                    title="Unable to load departments"
                    description={departmentsError}
                    action={
                      <Button onClick={fetchDepartments} variant="outline">
                        Try again
                      </Button>
                    }
                  />
                ) : departments.length === 0 ? (
                  <EmptyState
                    icon={Building2}
                    title="No departments available"
                    description="There are no departments configured yet."
                  />
                ) : (
                  <div className="space-y-2">
                    {departments
                      .filter((d) => d.active)
                      .map((dept) => (
                        <button
                          key={dept.id}
                          type="button"
                          onClick={() => handleDepartmentSelect(dept)}
                          className={`w-full text-left rounded-lg border p-4 transition-colors flex items-center justify-between gap-3 ${
                            selectedDepartment?.id === dept.id
                              ? "border-[#0284c7] bg-[#0284c7]/5"
                              : "border-slate-200 hover:border-slate-300 bg-white"
                          }`}
                        >
                          <div className="flex items-center gap-3 min-w-0">
                            <div
                              className={`w-9 h-9 rounded-lg flex items-center justify-center shrink-0 ${
                                selectedDepartment?.id === dept.id
                                  ? "bg-[#0284c7] text-white"
                                  : "bg-slate-100 text-slate-500"
                              }`}
                            >
                              <Building2 size={16} />
                            </div>
                            <div className="min-w-0">
                              <p className="text-sm font-medium text-slate-800 truncate">
                                {dept.departmentName}
                              </p>
                              {dept.description && (
                                <p className="text-xs text-slate-500 truncate mt-0.5">
                                  {dept.description}
                                </p>
                              )}
                            </div>
                          </div>
                          {selectedDepartment?.id === dept.id ? (
                            <Check
                              size={18}
                              className="text-[#0284c7] shrink-0"
                            />
                          ) : (
                            <ChevronRight
                              size={16}
                              className="text-slate-400 shrink-0"
                            />
                          )}
                        </button>
                      ))}
                  </div>
                )}
              </Card>

              <div className="flex justify-end mt-5">
                <Button onClick={handleNext} disabled={!selectedDepartment}>
                  Next
                  <ArrowRight size={16} />
                </Button>
              </div>
            </motion.div>
          )}

          {/* ============= STEP 2: DOCTOR ============= */}
          {currentStep === 2 && (
            <motion.div
              key="step2"
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.2 }}
            >
              <Card>
                <div className="flex items-center justify-between mb-5">
                  <div className="flex items-center gap-2">
                    <Stethoscope size={18} className="text-[#0284c7]" />
                    <h2 className="text-base font-semibold text-slate-800">
                      Select Doctor
                    </h2>
                  </div>
                  <span className="text-xs text-slate-500">
                    {selectedDepartment?.departmentName}
                  </span>
                </div>

                {doctorsLoading ? (
                  <SkeletonList count={3} />
                ) : doctors.length === 0 ? (
                  <EmptyState
                    icon={Stethoscope}
                    title="No doctors available"
                    description="This department has no active doctors at the moment. Please try another department."
                    action={
                      <Button
                        variant="outline"
                        onClick={() => setCurrentStep(1)}
                      >
                        Change department
                      </Button>
                    }
                  />
                ) : (
                  <div className="space-y-2">
                    {doctors.map((doctor) => (
                      <button
                        key={doctor.id}
                        type="button"
                        onClick={() => handleDoctorSelect(doctor)}
                        className={`w-full text-left rounded-lg border p-4 transition-colors ${
                          selectedDoctor?.id === doctor.id
                            ? "border-[#0284c7] bg-[#0284c7]/5"
                            : "border-slate-200 hover:border-slate-300 bg-white"
                        }`}
                      >
                        <div className="flex items-center gap-3">
                          <div
                            className={`w-10 h-10 rounded-full flex items-center justify-center shrink-0 text-sm font-medium ${
                              selectedDoctor?.id === doctor.id
                                ? "bg-[#0284c7] text-white"
                                : "bg-slate-100 text-slate-600"
                            }`}
                          >
                            {doctor.staffName?.charAt(0) ||
                              doctor.doctorCode?.charAt(0) ||
                              "D"}
                          </div>
                          <div className="flex-1 min-w-0">
                            <p className="text-sm font-medium text-slate-800 truncate">
                              {doctor.staffName || doctor.doctorCode}
                            </p>
                            <p className="text-xs text-slate-500 truncate mt-0.5">
                              {doctor.specialization}
                              {doctor.experienceYears
                                ? ` · ${doctor.experienceYears} yrs experience`
                                : ""}
                            </p>
                          </div>
                          <div className="text-right shrink-0">
                            <div className="inline-flex items-center gap-0.5 text-sm font-medium text-slate-800">
                              <IndianRupee size={13} />
                              {Number(doctor.consultationFee || 0).toFixed(0)}
                            </div>
                            <p className="text-xs text-slate-400 mt-0.5">
                              Consultation
                            </p>
                          </div>
                          {selectedDoctor?.id === doctor.id && (
                            <Check
                              size={18}
                              className="text-[#0284c7] shrink-0"
                            />
                          )}
                        </div>
                      </button>
                    ))}
                  </div>
                )}
              </Card>

              <div className="flex justify-between mt-5">
                <Button variant="outline" onClick={handleBack}>
                  Back
                </Button>
                <Button onClick={handleNext} disabled={!selectedDoctor}>
                  Next
                  <ArrowRight size={16} />
                </Button>
              </div>
            </motion.div>
          )}

          {/* ============= STEP 3: DATE & SLOT ============= */}
          {currentStep === 3 && (
            <motion.div
              key="step3"
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.2 }}
            >
              <Card>
                <div className="flex items-center gap-2 mb-5">
                  <Calendar size={18} className="text-[#0284c7]" />
                  <h2 className="text-base font-semibold text-slate-800">
                    Select Date & Time
                  </h2>
                </div>

                {/* Selected Doctor Summary */}
                <div className="bg-slate-50 rounded-lg p-3 mb-5 flex items-center gap-3 border border-slate-100">
                  <div className="w-9 h-9 rounded-full bg-[#0284c7] text-white flex items-center justify-center text-sm font-medium shrink-0">
                    {selectedDoctor?.staffName?.charAt(0) || "D"}
                  </div>
                  <div className="min-w-0">
                    <p className="text-sm font-medium text-slate-800 truncate">
                      {selectedDoctor?.staffName}
                    </p>
                    <p className="text-xs text-slate-500 truncate">
                      {selectedDoctor?.specialization}
                    </p>
                  </div>
                </div>

                {/* Date Picker */}
                <div className="mb-5">
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Appointment Date
                  </label>
                  <input
                    type="date"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                    min={today}
                    max={maxDate}
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800"
                  />
                </div>

                {/* Slots */}
                {selectedDate && (
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-2">
                      Available Time Slots
                    </label>

                    {slotsLoading ? (
                      <SkeletonSlots />
                    ) : slots.length === 0 ? (
                      <EmptyState
                        icon={Clock}
                        title="Doctor not available"
                        description="This doctor has no available slots on the selected date. Please try another date."
                      />
                    ) : (
                      <div className="grid grid-cols-3 sm:grid-cols-4 gap-2">
                        {slots.map((slot) => {
                          const isSelected = selectedSlot?.id === slot.id;
                          return (
                            <button
                              key={slot.id}
                              type="button"
                              onClick={() => setSelectedSlot(slot)}
                              className={`py-2 px-3 rounded-lg text-sm font-medium transition-colors border ${
                                isSelected
                                  ? "bg-[#0284c7] text-white border-[#0284c7]"
                                  : "bg-white text-slate-700 border-slate-200 hover:border-[#0284c7] hover:text-[#0284c7]"
                              }`}
                            >
                              {formatTime(slot.startTime)}
                            </button>
                          );
                        })}
                      </div>
                    )}
                  </div>
                )}
              </Card>

              <div className="flex justify-between mt-5">
                <Button variant="outline" onClick={handleBack}>
                  Back
                </Button>
                <Button
                  onClick={handleNext}
                  disabled={!selectedDate || !selectedSlot}
                >
                  Next
                  <ArrowRight size={16} />
                </Button>
              </div>
            </motion.div>
          )}

          {/* ============= STEP 4: DETAILS ============= */}
          {currentStep === 4 && (
            <motion.div
              key="step4"
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.2 }}
            >
              <Card>
                <div className="flex items-center gap-2 mb-5">
                  <User size={18} className="text-[#0284c7]" />
                  <h2 className="text-base font-semibold text-slate-800">
                    Visit Details
                  </h2>
                </div>

                {/* Summary */}
                <div className="bg-slate-50 rounded-lg p-4 mb-5 border border-slate-100">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wider mb-3">
                    Appointment Summary
                  </p>
                  <div className="space-y-2 text-sm">
                    <SummaryRow
                      label="Department"
                      value={selectedDepartment?.departmentName}
                    />
                    <SummaryRow
                      label="Doctor"
                      value={selectedDoctor?.staffName}
                    />
                    <SummaryRow
                      label="Date"
                      value={selectedDate}
                    />
                    <SummaryRow
                      label="Time"
                      value={formatTime(selectedSlot?.startTime)}
                    />
                    <SummaryRow
                      label="Fee"
                      value={formatFee(selectedDoctor?.consultationFee)}
                    />
                  </div>
                </div>

                {/* Visit Type */}
                <div className="mb-5">
                  <label className="block text-sm font-medium text-slate-700 mb-2">
                    Visit Type
                  </label>
                  <div className="grid grid-cols-2 gap-2">
                    {VISIT_TYPES.map((type) => {
                      const Icon = type.icon;
                      const isActive = visitType === type.value;
                      return (
                        <button
                          key={type.value}
                          type="button"
                          onClick={() => setVisitType(type.value)}
                          className={`py-2.5 px-4 rounded-lg text-sm font-medium transition-colors border flex items-center justify-center gap-2 ${
                            isActive
                              ? "bg-[#0284c7] text-white border-[#0284c7]"
                              : "bg-white text-slate-700 border-slate-200 hover:border-[#0284c7]"
                          }`}
                        >
                          <Icon size={16} />
                          {type.label}
                        </button>
                      );
                    })}
                  </div>
                </div>

                {/* Reason */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Reason for Visit <span className="text-red-500">*</span>
                  </label>
                  <textarea
                    value={reasonForVisit}
                    onChange={(e) => setReasonForVisit(e.target.value)}
                    rows={4}
                    maxLength={500}
                    placeholder="Describe your symptoms or reason for the visit"
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400 resize-none"
                  />
                  <p className="text-xs text-slate-400 mt-1.5 text-right">
                    {reasonForVisit.length}/500
                  </p>
                </div>
              </Card>

              <div className="flex justify-between mt-5">
                <Button variant="outline" onClick={handleBack}>
                  Back
                </Button>
                <Button
                  onClick={handleSubmit}
                  isLoading={submitting}
                  disabled={!reasonForVisit.trim()}
                >
                  Book & Pay
                  <ArrowRight size={16} />
                </Button>
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </Layout>
  );
};

// Helper component
const SummaryRow = ({ label, value }) => (
  <div className="flex items-center justify-between">
    <span className="text-slate-500">{label}</span>
    <span className="font-medium text-slate-800">{value || "—"}</span>
  </div>
);

export default BookAppointment;