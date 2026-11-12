import React, { useState, useEffect } from "react";
import { X } from "lucide-react";
import Button from "../../components/common/Button";
import { authApi } from "../../api/authApi";
import { departmentApi } from "../../api/departmentApi";
import { toast } from "react-toastify";

const AddDoctorModal = ({ isOpen, onClose, onRefresh, doctor }) => {
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState([]);

  // State initialize: Agar Edit mode hai toh doctor ki details, warna khaali
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phoneNumber: "",
    password: "",
    designation: "",
    departmentId: "",
    specialization: "",
    licenseNumber: "",
    experienceYears: "",
    consultationFee: "",
  });

  const isEditMode = !!doctor;

  useEffect(() => {
    if (isOpen) {
      // Dropdown ke liye departments fetch karo
      departmentApi.getAll().then((res) => setDepartments(res.data.data || []));

      // Agar Edit mode hai, toh form ko bharo
      if (doctor) {
        setFormData({
          fullName: doctor.staffName || "",
          email: doctor.email || "", // Backend se doctor response mein email nahi hai, toh user object se lena padega, filhal blank chhod rahe hain
          phoneNumber: doctor.phoneNumber || "",
          password: "", // Edit mein password nahi badalte
          designation: doctor.designation || "",
          departmentId: doctor.departmentId || "",
          specialization: doctor.specialization || "",
          licenseNumber: doctor.licenseNumber || "",
          experienceYears: doctor.experienceYears || "",
          consultationFee: doctor.consultationFee || "",
        });
      } else {
        // Agar Create mode hai, toh form reset karo
        setFormData({
          fullName: "",
          email: "",
          phoneNumber: "",
          password: "",
          designation: "",
          departmentId: "",
          specialization: "",
          licenseNumber: "",
          experienceYears: "",
          consultationFee: "",
        });
      }
    }
  }, [isOpen, doctor]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (isEditMode) {
        // Update API Call
        await authApi.updateDoctorByAdmin(doctor.id, {
          departmentId: parseInt(formData.departmentId),
          specialization: formData.specialization,
          licenseNumber: formData.licenseNumber,
          experienceYears: parseInt(formData.experienceYears),
          consultationFee: parseFloat(formData.consultationFee),
          active: doctor.active, // Pura status same rakhna hai
        });
        toast.success("Doctor details updated successfully!");
      } else {
        // Create API Call
        await authApi.registerDoctorByAdmin({
          ...formData,
          departmentId: parseInt(formData.departmentId),
          experienceYears: parseInt(formData.experienceYears),
          consultationFee: parseFloat(formData.consultationFee),
        });
        toast.success("Doctor created successfully! Email sent.");
      }
      onRefresh();
      onClose();
    } catch (error) {
      toast.error(error.userMessage || "Failed to save doctor details");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl overflow-hidden">
        <div className="flex items-center justify-between p-4 border-b border-slate-100">
          <h2 className="text-lg font-semibold text-slate-800">
            {isEditMode ? "Edit Doctor Details" : "Add New Doctor"}
          </h2>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-slate-600"
          >
            <X size={20} />
          </button>
        </div>

        <form
          onSubmit={handleSubmit}
          className="p-6 space-y-4 max-h-[80vh] overflow-y-auto"
        >
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Basic Info (Create mein hi dikhana hai, edit mein disabled) */}
            <div className="md:col-span-2">
              <h3 className="text-sm font-semibold text-slate-700 mb-2">
                Basic Information
              </h3>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                Full Name
              </label>
              <input
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                required
                disabled={isEditMode}
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7] disabled:bg-slate-50"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                disabled={isEditMode}
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7] disabled:bg-slate-50"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Phone</label>
              <input
                type="tel"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
                disabled={isEditMode}
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7] disabled:bg-slate-50"
              />
            </div>

            {!isEditMode && (
              <div>
                <label className="block text-sm font-medium mb-1">
                  Password *
                </label>
                <input
                  type="text"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
                  placeholder="Set a temporary password"
                />
              </div>
            )}

            <div>
              <label className="block text-sm font-medium mb-1">
                Designation
              </label>
              <input
                type="text"
                name="designation"
                value={formData.designation}
                onChange={handleChange}
                required
                disabled={isEditMode}
                placeholder="e.g., Senior Cardiologist"
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7] disabled:bg-slate-50"
              />
            </div>

            {/* Doctor Info (Editable in both) */}
            <div className="md:col-span-2 mt-4">
              <h3 className="text-sm font-semibold text-slate-700 mb-2">
                Medical Details
              </h3>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                Department *
              </label>
              <select
                name="departmentId"
                value={formData.departmentId}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
              >
                <option value="">Select Department</option>
                {departments.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.departmentName}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                Specialization *
              </label>
              <input
                type="text"
                name="specialization"
                value={formData.specialization}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                License Number *
              </label>
              <input
                type="text"
                name="licenseNumber"
                value={formData.licenseNumber}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                Experience (Years) *
              </label>
              <input
                type="number"
                name="experienceYears"
                value={formData.experienceYears}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">
                Consultation Fee (₹) *
              </label>
              <input
                type="number"
                name="consultationFee"
                value={formData.consultationFee}
                onChange={handleChange}
                required
                className="w-full px-3 py-2 border rounded-lg focus:border-[#0284c7]"
              />
            </div>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t border-slate-100">
            <Button type="button" variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit" isLoading={loading}>
              {isEditMode ? "Update Doctor" : "Create Doctor"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddDoctorModal;
