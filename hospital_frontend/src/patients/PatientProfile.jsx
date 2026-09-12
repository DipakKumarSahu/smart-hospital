import React, { useState } from "react";
import { motion } from "framer-motion";
import {
  User,
  Mail,
  Phone,
  MapPin,
  Heart,
  AlertCircle,
  Camera,
  Save,
  X,
  UserCheck,
  Edit2,
} from "lucide-react";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import Spinner from "../components/common/Spinner";

const PatientProfile = ({
  profile,
  loading,
  onUpdate,
  isEditing,
  setIsEditing,
}) => {
  const [formData, setFormData] = useState(profile || {});
  const [saving, setSaving] = useState(false);

  React.useEffect(() => {
    if (profile) {
      setFormData(profile);
    }
  }, [profile]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await onUpdate(formData);
    } catch (error) {
      console.error("Error updating profile:", error);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!profile) {
    return (
      <Card className="text-center py-12">
        <User className="w-16 h-16 text-gray-300 mx-auto mb-4" />
        <h3 className="text-lg font-semibold text-gray-600">No Profile Data</h3>
      </Card>
    );
  }

  const InfoField = ({
    label,
    value,
    name,
    type = "text",
    editable = false,
  }) => (
    <div className="space-y-1">
      <label className="text-xs font-medium text-gray-400 uppercase tracking-wider">
        {label}
      </label>
      {isEditing && editable ? (
        <input
          type={type}
          name={name}
          value={formData[name] || ""}
          onChange={handleChange}
          className="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500 transition-all duration-200"
        />
      ) : (
        <p className="text-sm text-gray-800 font-medium">{value || "N/A"}</p>
      )}
    </div>
  );

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-4xl mx-auto"
    >
      {/* Profile Header */}
      <div className="relative">
        <div className="h-32 bg-gradient-to-r from-primary-500 to-primary-600 rounded-t-2xl" />
        <div className="flex flex-col sm:flex-row items-center sm:items-end gap-4 px-6 -mt-10">
          <div className="relative">
            <div className="w-24 h-24 rounded-full bg-white p-1 shadow-lg">
              <div className="w-full h-full rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center text-white text-3xl font-bold">
                {profile.fullName?.charAt(0) || "P"}
              </div>
            </div>
            <button className="absolute bottom-0 right-0 p-1.5 bg-white rounded-full shadow-md hover:shadow-lg transition-shadow">
              <Camera size={16} className="text-gray-600" />
            </button>
          </div>
          <div className="flex-1 text-center sm:text-left">
            <h2 className="text-2xl font-bold text-gray-800">
              {profile.fullName}
            </h2>
            <p className="text-sm text-gray-500 flex items-center justify-center sm:justify-start gap-2">
              <UserCheck size={16} className="text-green-500" />
              {profile.active ? "Active Patient" : "Inactive"}
            </p>
            <p className="text-xs text-gray-400 mt-1">{profile.patientCode}</p>
          </div>
          <div className="pb-2">
            {!isEditing ? (
              <Button onClick={() => setIsEditing(true)}>
                <Edit2 size={18} />
                Edit Profile
              </Button>
            ) : (
              <div className="flex gap-2">
                <Button variant="outline" onClick={() => setIsEditing(false)}>
                  <X size={18} />
                  Cancel
                </Button>
                <Button onClick={handleSubmit} isLoading={saving}>
                  <Save size={18} />
                  Save
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Profile Info */}
      <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4 flex items-center gap-2">
            <User size={18} className="text-primary-500" />
            Personal Information
          </h3>
          <div className="space-y-4">
            <InfoField label="Patient Code" value={profile.patientCode} />
            <InfoField label="Full Name" value={profile.fullName} />
            <InfoField label="Email" value={profile.email} />
            <InfoField
              label="Phone Number"
              value={profile.phoneNumber}
              name="phoneNumber"
              type="tel"
              editable
            />
            <InfoField
              label="Address"
              value={profile.address}
              name="address"
              editable
            />
          </div>
        </Card>

        <Card>
          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4 flex items-center gap-2">
            <Heart size={18} className="text-red-500" />
            Medical Information
          </h3>
          <div className="space-y-4">
            <InfoField label="Date of Birth" value={profile.dateOfBirth} />
            <InfoField label="Gender" value={profile.gender} />
            <InfoField
              label="Blood Group"
              value={profile.bloodGroup?.replace("_", " ")}
            />
          </div>

          <hr className="my-4" />

          <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-4 flex items-center gap-2">
            <AlertCircle size={18} className="text-orange-500" />
            Emergency Contact
          </h3>
          <div className="space-y-4">
            <InfoField
              label="Contact Name"
              value={profile.emergencyContactName}
              name="emergencyContactName"
              editable
            />
            <InfoField
              label="Contact Number"
              value={profile.emergencyContactNumber}
              name="emergencyContactNumber"
              type="tel"
              editable
            />
          </div>
        </Card>
      </div>
    </motion.div>
  );
};

export default PatientProfile;
