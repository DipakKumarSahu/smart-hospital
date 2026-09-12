import React from "react";
import { motion } from "framer-motion";
import {
  User,
  Mail,
  Phone,
  Calendar,
  MapPin,
  Heart,
  AlertCircle,
  Activity,
  FileText,
  Edit,
  ArrowLeft,
  UserCheck,
  UserX,
} from "lucide-react";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import Spinner from "../components/common/Spinner";
import { format } from "date-fns";

const PatientDetails = ({ patient, loading, onEdit, onBack }) => {
  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!patient) {
    return (
      <Card className="text-center py-12">
        <User className="w-16 h-16 text-gray-300 mx-auto mb-4" />
        <h3 className="text-lg font-semibold text-gray-600">
          Patient Not Found
        </h3>
        <p className="text-gray-400 mt-2">
          The patient you're looking for doesn't exist
        </p>
        <Button onClick={onBack} className="mt-4">
          <ArrowLeft size={18} />
          Go Back
        </Button>
      </Card>
    );
  }

  const InfoItem = ({ icon: Icon, label, value, className = "" }) => (
    <div
      className={`flex items-start gap-3 p-3 rounded-lg hover:bg-gray-50 transition-colors ${className}`}
    >
      <div className="w-9 h-9 rounded-lg bg-primary-50 flex items-center justify-center text-primary-500 flex-shrink-0">
        <Icon size={18} />
      </div>
      <div className="flex-1 min-w-0">
        <p className="text-xs text-gray-400 font-medium uppercase tracking-wider">
          {label}
        </p>
        <p className="text-sm text-gray-800 font-medium truncate">
          {value || "N/A"}
        </p>
      </div>
    </div>
  );

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-6"
    >
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <Button variant="ghost" onClick={onBack} className="p-2">
            <ArrowLeft size={20} />
          </Button>
          <div>
            <h2 className="text-2xl font-bold text-gray-800">
              {patient.fullName}
            </h2>
            <p className="text-sm text-gray-500">{patient.patientCode}</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button onClick={() => onEdit && onEdit(patient.id)}>
            <Edit size={18} />
            Edit Patient
          </Button>
        </div>
      </div>

      {/* Status Badge */}
      <div className="flex items-center gap-3">
        {patient.active ? (
          <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-green-100 text-green-700 text-sm font-medium">
            <UserCheck size={16} />
            Active
          </span>
        ) : (
          <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-red-100 text-red-700 text-sm font-medium">
            <UserX size={16} />
            Inactive
          </span>
        )}
        <span className="text-sm text-gray-400">
          Member since {format(new Date(), "MMM d, yyyy")}
        </span>
      </div>

      {/* Main Content */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Personal Information */}
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <h3 className="text-lg font-semibold text-gray-800 flex items-center gap-2 mb-4">
              <User size={20} className="text-primary-500" />
              Personal Information
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
              <InfoItem
                icon={User}
                label="Full Name"
                value={patient.fullName}
              />
              <InfoItem icon={Mail} label="Email" value={patient.email} />
              <InfoItem
                icon={Phone}
                label="Phone"
                value={patient.phoneNumber}
              />
              <InfoItem
                icon={Calendar}
                label="Date of Birth"
                value={patient.dateOfBirth}
              />
              <InfoItem icon={Activity} label="Gender" value={patient.gender} />
              <InfoItem
                icon={Heart}
                label="Blood Group"
                value={patient.bloodGroup}
              />
              <InfoItem
                icon={MapPin}
                label="Address"
                value={patient.address}
                className="md:col-span-2"
              />
            </div>
          </Card>

          {/* Emergency Contact */}
          <Card>
            <h3 className="text-lg font-semibold text-gray-800 flex items-center gap-2 mb-4">
              <AlertCircle size={20} className="text-red-500" />
              Emergency Contact
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
              <InfoItem
                icon={User}
                label="Contact Name"
                value={patient.emergencyContactName}
              />
              <InfoItem
                icon={Phone}
                label="Contact Number"
                value={patient.emergencyContactNumber}
              />
            </div>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          <Card>
            <h4 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">
              Quick Actions
            </h4>
            <div className="space-y-2">
              <Button
                variant="outline"
                className="w-full justify-start"
                size="sm"
              >
                <FileText size={16} />
                View Medical Records
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                size="sm"
              >
                <Calendar size={16} />
                Schedule Appointment
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                size="sm"
              >
                <Activity size={16} />
                View Reports
              </Button>
            </div>
          </Card>

          <Card>
            <h4 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4">
              Statistics
            </h4>
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">Total Visits</span>
                <span className="text-sm font-semibold text-gray-800">24</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">Prescriptions</span>
                <span className="text-sm font-semibold text-gray-800">12</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600">Last Visit</span>
                <span className="text-sm font-semibold text-gray-800">
                  {format(new Date(), "MMM d, yyyy")}
                </span>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </motion.div>
  );
};

export default PatientDetails;
