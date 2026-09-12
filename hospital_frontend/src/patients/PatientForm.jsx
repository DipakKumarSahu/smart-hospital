import React from "react";
import { useForm } from "react-hook-form";
import { motion } from "framer-motion";
import Input from "../components/common/Input";
import Button from "../components/common/Button";
import Card from "../components/common/Card";
import {
  User,
  Calendar,
  Phone,
  MapPin,
  Heart,
  AlertCircle,
  UserPlus,
  Mail,
} from "lucide-react";

const PatientForm = ({ patient, onSubmit, loading, isEdit }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm({
    defaultValues: patient || {},
  });

  const bloodGroups = [
    "A_POSITIVE",
    "A_NEGATIVE",
    "B_POSITIVE",
    "B_NEGATIVE",
    "AB_POSITIVE",
    "AB_NEGATIVE",
    "O_POSITIVE",
    "O_NEGATIVE",
  ];
  const genders = ["MALE", "FEMALE", "OTHER"];

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <Card>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Personal Information */}
          <div className="md:col-span-2">
            <h3 className="text-lg font-semibold text-gray-700 flex items-center gap-2">
              <User size={20} />
              Personal Information
            </h3>
            <div className="h-px bg-gray-200 my-3"></div>
          </div>

          <div className="md:col-span-2">
            <Input
              label="Patient Code"
              value={patient?.patientCode || "Auto-generated"}
              disabled
              className="bg-gray-50"
            />
          </div>

          <Input
            label="Full Name"
            placeholder="John Doe"
            icon={<User size={18} />}
            error={errors.fullName?.message}
            {...register("fullName", {
              required: "Full name is required",
            })}
          />

          <Input
            label="Email"
            type="email"
            placeholder="john@example.com"
            icon={<Mail size={18} />}
            error={errors.email?.message}
            {...register("email", {
              required: "Email is required",
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: "Invalid email address",
              },
            })}
          />

          <Input
            label="Phone Number"
            placeholder="+1234567890"
            icon={<Phone size={18} />}
            error={errors.phoneNumber?.message}
            {...register("phoneNumber", {
              required: "Phone number is required",
            })}
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Date of Birth
            </label>
            <input
              type="date"
              className={`
                w-full px-4 py-2.5 rounded-lg border border-gray-300
                focus:ring-2 focus:ring-primary-500 focus:border-primary-500
                transition-all duration-200
                ${errors.dateOfBirth ? "border-red-500 ring-2 ring-red-500/20" : ""}
              `}
              {...register("dateOfBirth", {
                required: "Date of birth is required",
              })}
            />
            {errors.dateOfBirth && (
              <p className="text-sm text-red-500 mt-1">
                {errors.dateOfBirth.message}
              </p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Gender
            </label>
            <select
              className={`
                w-full px-4 py-2.5 rounded-lg border border-gray-300
                focus:ring-2 focus:ring-primary-500 focus:border-primary-500
                transition-all duration-200
                ${errors.gender ? "border-red-500 ring-2 ring-red-500/20" : ""}
              `}
              {...register("gender", {
                required: "Gender is required",
              })}
            >
              <option value="">Select Gender</option>
              {genders.map((g) => (
                <option key={g} value={g}>
                  {g}
                </option>
              ))}
            </select>
            {errors.gender && (
              <p className="text-sm text-red-500 mt-1">
                {errors.gender.message}
              </p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Blood Group
            </label>
            <select
              className={`
                w-full px-4 py-2.5 rounded-lg border border-gray-300
                focus:ring-2 focus:ring-primary-500 focus:border-primary-500
                transition-all duration-200
                ${errors.bloodGroup ? "border-red-500 ring-2 ring-red-500/20" : ""}
              `}
              {...register("bloodGroup", {
                required: "Blood group is required",
              })}
            >
              <option value="">Select Blood Group</option>
              {bloodGroups.map((bg) => (
                <option key={bg} value={bg}>
                  {bg.replace("_", " ")}
                </option>
              ))}
            </select>
            {errors.bloodGroup && (
              <p className="text-sm text-red-500 mt-1">
                {errors.bloodGroup.message}
              </p>
            )}
          </div>

          <div className="md:col-span-2">
            <Input
              label="Address"
              placeholder="123 Main St, City, Country"
              icon={<MapPin size={18} />}
              {...register("address")}
            />
          </div>

          {/* Emergency Contact */}
          <div className="md:col-span-2">
            <h3 className="text-lg font-semibold text-gray-700 flex items-center gap-2 mt-4">
              <AlertCircle size={20} />
              Emergency Contact
            </h3>
            <div className="h-px bg-gray-200 my-3"></div>
          </div>

          <Input
            label="Emergency Contact Name"
            placeholder="Jane Doe"
            icon={<User size={18} />}
            error={errors.emergencyContactName?.message}
            {...register("emergencyContactName", {
              required: "Emergency contact name is required",
            })}
          />

          <Input
            label="Emergency Contact Number"
            placeholder="+1234567890"
            icon={<Phone size={18} />}
            error={errors.emergencyContactNumber?.message}
            {...register("emergencyContactNumber", {
              required: "Emergency contact number is required",
            })}
          />

          {isEdit && (
            <div className="md:col-span-2">
              <label className="flex items-center gap-3 cursor-pointer">
                <input
                  type="checkbox"
                  className="w-4 h-4 text-primary-600 rounded focus:ring-primary-500"
                  {...register("active")}
                />
                <span className="text-sm font-medium text-gray-700">
                  Active Status
                </span>
              </label>
            </div>
          )}
        </div>
      </Card>

      <div className="flex flex-col sm:flex-row gap-3 justify-end">
        <Button
          type="button"
          variant="outline"
          onClick={() => window.history.back()}
        >
          Cancel
        </Button>
        <Button type="submit" isLoading={loading}>
          {isEdit ? "Update Patient" : "Create Patient"}
        </Button>
      </div>
    </form>
  );
};

export default PatientForm;
