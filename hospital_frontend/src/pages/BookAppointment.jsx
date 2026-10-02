import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import {
  Calendar,
  Clock,
  User,
  Stethoscope,
  FileText,
  ChevronRight,
  CheckCircle,
  MapPin,
  Video,
  ArrowLeft,
} from "lucide-react";
import { VISIT_TYPES } from "../utils/constants";

const BookAppointment = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState(1);

  const [formData, setFormData] = useState({
    doctorId: "",
    appointmentSlotId: "",
    visitType: "OFFLINE",
    reasonForVisit: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Client-side validation
    if (
      !formData.doctorId ||
      !formData.appointmentSlotId ||
      !formData.reasonForVisit
    ) {
      toast.error("Please fill in all required fields");
      return;
    }

    if (formData.reasonForVisit.length > 500) {
      toast.error("Reason cannot exceed 500 characters");
      return;
    }

    setLoading(true);
    try {
      const response = await patientApi.bookAppointment({
        doctorId: parseInt(formData.doctorId),
        appointmentSlotId: parseInt(formData.appointmentSlotId),
        visitType: formData.visitType,
        reasonForVisit: formData.reasonForVisit.trim(),
      });

      const appointment = response.data.data;
      toast.success("Appointment booked. Proceeding to payment.");

      // Redirect to payment page
      navigate(`/payment/${appointment.id}`);
    } catch (error) {
      // Use backend message from axios interceptor
      toast.error(error.userMessage || "Failed to book appointment");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-6"
        >
          <button
            onClick={() => navigate("/dashboard")}
            className="text-sm text-gray-500 hover:text-primary-600 mb-3 flex items-center gap-1"
          >
            <ArrowLeft size={16} />
            Back to Dashboard
          </button>
          <h1 className="text-3xl font-bold flex items-center gap-3">
            <Calendar className="w-8 h-8 text-primary-500" />
            Book Appointment
          </h1>
          <p className="text-gray-500 mt-1">
            Schedule your appointment with a doctor
          </p>
        </motion.div>

        {/* Progress Steps */}
        <div className="flex items-center justify-center gap-2 mb-8">
          {[
            { num: 1, label: "Doctor & Slot" },
            { num: 2, label: "Details" },
            { num: 3, label: "Confirm" },
          ].map((s, i) => (
            <React.Fragment key={s.num}>
              <div className="flex items-center gap-2">
                <div
                  className={`w-8 h-8 rounded-full flex items-center justify-center font-semibold text-sm transition-all ${
                    step >= s.num
                      ? "bg-primary-500 text-white"
                      : "bg-gray-200 text-gray-500"
                  }`}
                >
                  {step > s.num ? <CheckCircle size={18} /> : s.num}
                </div>
                <span
                  className={`text-sm font-medium hidden sm:block ${
                    step >= s.num ? "text-primary-600" : "text-gray-400"
                  }`}
                >
                  {s.label}
                </span>
              </div>
              {i < 2 && (
                <div
                  className={`w-8 sm:w-12 h-0.5 ${step > s.num ? "bg-primary-500" : "bg-gray-200"}`}
                />
              )}
            </React.Fragment>
          ))}
        </div>

        <form onSubmit={handleSubmit}>
          {/* STEP 1: Doctor & Slot */}
          {step === 1 && (
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
            >
              <Card>
                <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <Stethoscope size={20} className="text-primary-500" />
                  Doctor & Slot Information
                </h3>

                <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                  <p className="text-sm text-blue-800">
                    💡 <strong>Note:</strong> Doctor aur Slot selection feature
                    abhi development mein hai. Filhal aap manually ID's enter
                    kar sakte hain.
                  </p>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Doctor ID *
                    </label>
                    <input
                      type="number"
                      name="doctorId"
                      value={formData.doctorId}
                      onChange={handleChange}
                      placeholder="e.g., 1"
                      className="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Appointment Slot ID *
                    </label>
                    <input
                      type="number"
                      name="appointmentSlotId"
                      value={formData.appointmentSlotId}
                      onChange={handleChange}
                      placeholder="e.g., 1"
                      className="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                      required
                    />
                  </div>
                </div>
              </Card>

              <div className="flex justify-end mt-4">
                <Button
                  type="button"
                  onClick={() => setStep(2)}
                  disabled={!formData.doctorId || !formData.appointmentSlotId}
                >
                  Next
                  <ChevronRight size={18} />
                </Button>
              </div>
            </motion.div>
          )}

          {/* STEP 2: Details */}
          {step === 2 && (
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
            >
              <Card>
                <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <FileText size={20} className="text-primary-500" />
                  Appointment Details
                </h3>

                <div className="space-y-4">
                  {/* Visit Type */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Visit Type *
                    </label>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                      {VISIT_TYPES.map((type) => (
                        <motion.button
                          key={type.value}
                          type="button"
                          whileHover={{ scale: 1.02 }}
                          whileTap={{ scale: 0.98 }}
                          onClick={() =>
                            setFormData((prev) => ({
                              ...prev,
                              visitType: type.value,
                            }))
                          }
                          className={`p-4 rounded-xl border-2 transition-all flex items-center gap-3 ${
                            formData.visitType === type.value
                              ? "border-primary-500 bg-primary-50"
                              : "border-gray-200 hover:border-primary-300"
                          }`}
                        >
                          {type.value === "ONLINE" ? (
                            <Video className="text-primary-500" size={24} />
                          ) : (
                            <MapPin className="text-primary-500" size={24} />
                          )}
                          <div className="text-left">
                            <p className="font-semibold">{type.label}</p>
                          </div>
                        </motion.button>
                      ))}
                    </div>
                  </div>

                  {/* Reason */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Reason for Visit * (Max 500 characters)
                    </label>
                    <textarea
                      name="reasonForVisit"
                      value={formData.reasonForVisit}
                      onChange={handleChange}
                      rows="4"
                      maxLength={500}
                      placeholder="Describe your symptoms or reason for the visit..."
                      className="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500 resize-none"
                      required
                    />
                    <p className="text-xs text-gray-400 mt-1">
                      {formData.reasonForVisit.length}/500 characters
                    </p>
                  </div>
                </div>
              </Card>

              <div className="flex justify-between mt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setStep(1)}
                >
                  Back
                </Button>
                <Button
                  type="button"
                  onClick={() => setStep(3)}
                  disabled={!formData.reasonForVisit}
                >
                  Next
                  <ChevronRight size={18} />
                </Button>
              </div>
            </motion.div>
          )}

          {/* STEP 3: Confirm */}
          {step === 3 && (
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
            >
              <Card>
                <h3 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  <CheckCircle size={20} className="text-green-500" />
                  Confirm Appointment
                </h3>

                {/* Summary */}
                <div className="bg-gradient-to-br from-primary-50 to-blue-50 rounded-xl p-6 mb-6">
                  <h4 className="font-semibold text-primary-800 mb-4">
                    Appointment Summary
                  </h4>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-gray-500 text-xs uppercase">
                        Doctor ID
                      </span>
                      <p className="font-medium text-gray-800">
                        {formData.doctorId}
                      </p>
                    </div>
                    <div>
                      <span className="text-gray-500 text-xs uppercase">
                        Slot ID
                      </span>
                      <p className="font-medium text-gray-800">
                        {formData.appointmentSlotId}
                      </p>
                    </div>
                    <div>
                      <span className="text-gray-500 text-xs uppercase">
                        Visit Type
                      </span>
                      <p className="font-medium text-gray-800">
                        {
                          VISIT_TYPES.find(
                            (t) => t.value === formData.visitType,
                          )?.label
                        }
                      </p>
                    </div>
                    <div className="sm:col-span-2">
                      <span className="text-gray-500 text-xs uppercase">
                        Reason
                      </span>
                      <p className="font-medium text-gray-800">
                        {formData.reasonForVisit}
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                  <p className="text-sm text-yellow-800">
                    ⚠️ <strong>Important:</strong> Appointment book karne ke
                    baad aapko
                    <strong> 10 minutes </strong> ke andar payment karna hoga,
                    warna appointment automatically expire ho jayegi.
                  </p>
                </div>
              </Card>

              <div className="flex justify-between mt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setStep(2)}
                >
                  Back
                </Button>
                <Button type="submit" isLoading={loading}>
                  <CheckCircle size={18} />
                  Book & Proceed to Payment
                </Button>
              </div>
            </motion.div>
          )}
        </form>
      </div>
    </Layout>
  );
};

export default BookAppointment;
