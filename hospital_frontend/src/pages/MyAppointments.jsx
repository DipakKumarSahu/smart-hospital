import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import Spinner from "../components/common/Spinner";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import {
  Calendar,
  Clock,
  Stethoscope,
  Plus,
  X,
  CheckCircle,
  AlertCircle,
  FileText,
  CreditCard,
  Video,
  MapPin,
} from "lucide-react";
import { APPOINTMENT_STATUS, VISIT_TYPES } from "../utils/constants";

const MyAppointments = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("all");

  useEffect(() => {
    if (location.state?.paymentSuccess) {
      toast.success("Payment successful! Your appointment is confirmed. 🎉");
      // Clean state
      window.history.replaceState({}, document.title);
    }
    fetchAppointments();
  }, [filter]);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const response = await patientApi.getMyAppointments({
        page: 0,
        size: 50,
        sort: "appointmentDate,desc",
      });

      let data = response.data.data.content || [];

      // Client-side filter
      if (filter !== "all") {
        data = data.filter((a) => a.status === filter.toUpperCase());
      }

      setAppointments(data);
    } catch (error) {
      console.error("Error fetching appointments:", error);
      toast.error("Failed to load appointments");
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm("Are you sure you want to cancel this appointment?"))
      return;

    try {
      await patientApi.cancelMyAppointment(id);
      toast.success("Appointment cancelled successfully");
      fetchAppointments();
    } catch (error) {
      console.error("Error cancelling appointment:", error);
      toast.error(
        error.response?.data?.message || "Failed to cancel appointment",
      );
    }
  };

  const getStatusBadge = (status) => {
    const config =
      APPOINTMENT_STATUS[status] || APPOINTMENT_STATUS.PENDING_PAYMENT;
    const colors = {
      yellow: "bg-yellow-100 text-yellow-700 border-yellow-200",
      green: "bg-green-100 text-green-700 border-green-200",
      red: "bg-red-100 text-red-700 border-red-200",
      blue: "bg-blue-100 text-blue-700 border-blue-200",
      gray: "bg-gray-100 text-gray-700 border-gray-200",
    };
    return (
      <span
        className={`px-3 py-1 rounded-full text-xs font-medium border ${colors[config.color]}`}
      >
        {config.label}
      </span>
    );
  };

  const getVisitTypeIcon = (visitType) => {
    return visitType === "ONLINE" ? (
      <Video size={14} className="text-primary-500" />
    ) : (
      <MapPin size={14} className="text-primary-500" />
    );
  };

  // Filter counts
  const filterCounts = {
    all: appointments.length,
    pending_payment: appointments.filter((a) => a.status === "PENDING_PAYMENT")
      .length,
    booked: appointments.filter((a) => a.status === "BOOKED").length,
    completed: appointments.filter((a) => a.status === "COMPLETED").length,
    cancelled: appointments.filter((a) => a.status === "CANCELLED").length,
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold flex items-center gap-3">
              <Calendar className="w-8 h-8 text-primary-500" />
              My Appointments
            </h1>
            <p className="text-gray-500 mt-1">
              View and manage your appointments
            </p>
          </div>
          <Button
            onClick={() => navigate("/book-appointment")}
            className="w-full sm:w-auto"
          >
            <Plus size={18} />
            Book New Appointment
          </Button>
        </div>

        {/* Filter Tabs */}
        <div className="flex gap-2 flex-wrap">
          {[
            { key: "all", label: "All" },
            { key: "pending_payment", label: "Pending Payment" },
            { key: "booked", label: "Booked" },
            { key: "completed", label: "Completed" },
            { key: "cancelled", label: "Cancelled" },
          ].map((f) => (
            <button
              key={f.key}
              onClick={() => setFilter(f.key)}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${
                filter === f.key
                  ? "bg-primary-500 text-white shadow-md"
                  : "bg-white text-gray-600 hover:bg-gray-50 border border-gray-200"
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>

        {/* Appointments List */}
        {loading ? (
          <div className="flex items-center justify-center h-64">
            <Spinner size="lg" />
          </div>
        ) : appointments.length === 0 ? (
          <Card className="text-center py-12">
            <Calendar className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-gray-600">
              No Appointments Found
            </h3>
            <p className="text-gray-400 mt-2 mb-4">
              {filter === "all"
                ? "You haven't booked any appointments yet"
                : `No ${filter.replace("_", " ")} appointments`}
            </p>
            <Button onClick={() => navigate("/book-appointment")}>
              <Plus size={18} />
              Book Your First Appointment
            </Button>
          </Card>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
            {appointments.map((apt, index) => (
              <motion.div
                key={apt.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.05 }}
              >
                <Card hover className="relative h-full">
                  <div className="flex gap-4">
                    {/* Icon */}
                    <div className="flex-shrink-0">
                      <div className="w-14 h-14 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center text-white">
                        <Stethoscope size={24} />
                      </div>
                    </div>

                    {/* Details */}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 flex-wrap">
                        <div>
                          <h3 className="font-semibold text-gray-800">
                            {apt.doctorName || `Doctor #${apt.doctorId}`}
                          </h3>
                          <p className="text-xs text-gray-400">
                            {apt.appointmentCode}
                          </p>
                        </div>
                        {getStatusBadge(apt.status)}
                      </div>

                      <div className="grid grid-cols-2 gap-2 mt-3 text-sm">
                        <div className="flex items-center gap-1.5 text-gray-600">
                          <Calendar size={14} className="text-gray-400" />
                          <span className="truncate">
                            {apt.appointmentDate}
                          </span>
                        </div>
                        <div className="flex items-center gap-1.5 text-gray-600">
                          <Clock size={14} className="text-gray-400" />
                          <span className="truncate">
                            {apt.appointmentTime}
                          </span>
                        </div>
                        <div className="flex items-center gap-1.5 text-gray-600 col-span-2">
                          {getVisitTypeIcon(apt.visitType)}
                          <span>
                            {VISIT_TYPES.find((t) => t.value === apt.visitType)
                              ?.label || apt.visitType}
                          </span>
                        </div>
                      </div>

                      <p className="text-xs text-gray-500 mt-2 line-clamp-2">
                        {apt.reasonForVisit}
                      </p>

                      {/* Actions */}
                      <div className="flex gap-2 mt-4 flex-wrap">
                        {apt.status === "PENDING_PAYMENT" && (
                          <Button
                            size="sm"
                            onClick={() => navigate(`/payment/${apt.id}`)}
                            className="text-xs"
                          >
                            <CreditCard size={14} />
                            Pay Now
                          </Button>
                        )}

                        {(apt.status === "PENDING_PAYMENT" ||
                          apt.status === "BOOKED") && (
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => handleCancel(apt.id)}
                            className="text-red-600 border-red-200 hover:bg-red-50 text-xs"
                          >
                            <X size={14} />
                            Cancel
                          </Button>
                        )}

                        {apt.status === "COMPLETED" && (
                          <Button
                            variant="outline"
                            size="sm"
                            className="text-xs"
                          >
                            <FileText size={14} />
                            Prescription
                          </Button>
                        )}
                      </div>
                    </div>
                  </div>
                </Card>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default MyAppointments;
