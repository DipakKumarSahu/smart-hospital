import React, { useState, useMemo } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";
import {
  Mail,
  Lock,
  User,
  Phone,
  Calendar,
  MapPin,
  Users,
  ArrowLeft,
  Eye,
  EyeOff,
  AlertCircle,
  Check,
  X,
  Heart,
  Shield,
} from "lucide-react";

const GENDERS = [
  { value: "MALE", label: "Male" },
  { value: "FEMALE", label: "Female" },
  { value: "OTHER", label: "Other" },
];

const BLOOD_GROUPS = [
  { value: "A_POSITIVE", label: "A+" },
  { value: "A_NEGATIVE", label: "A-" },
  { value: "B_POSITIVE", label: "B+" },
  { value: "B_NEGATIVE", label: "B-" },
  { value: "AB_POSITIVE", label: "AB+" },
  { value: "AB_NEGATIVE", label: "AB-" },
  { value: "O_POSITIVE", label: "O+" },
  { value: "O_NEGATIVE", label: "O-" },
];

const Register = () => {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phoneNumber: "",
    password: "",
    confirmPassword: "",
    dateOfBirth: "",
    gender: "",
    bloodGroup: "",
    address: "",
    emergencyContactName: "",
    emergencyContactNumber: "",
  });

  // Password requirements
  const passwordChecks = useMemo(() => {
    const pwd = formData.password;
    return {
      length: pwd.length >= 8 && pwd.length <= 20,
      uppercase: /[A-Z]/.test(pwd),
      lowercase: /[a-z]/.test(pwd),
      number: /[0-9]/.test(pwd),
      special: /[!@#$%^&*(),.?":{}|<>]/.test(pwd),
    };
  }, [formData.password]);

  const passwordStrength = useMemo(() => {
    const passedCount = Object.values(passwordChecks).filter(Boolean).length;
    if (passedCount === 0) return { level: 0, label: "", color: "" };
    if (passedCount <= 2) return { level: 1, label: "Weak", color: "red" };
    if (passedCount <= 3) return { level: 2, label: "Fair", color: "amber" };
    if (passedCount <= 4) return { level: 3, label: "Good", color: "blue" };
    return { level: 4, label: "Strong", color: "green" };
  }, [passwordChecks]);

  const passwordsMatch =
    formData.password && formData.password === formData.confirmPassword;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Validation
    if (
      !formData.fullName ||
      !formData.email ||
      !formData.phoneNumber ||
      !formData.dateOfBirth ||
      !formData.gender ||
      !formData.bloodGroup ||
      !formData.emergencyContactName ||
      !formData.emergencyContactNumber
    ) {
      setError("Please fill in all required fields");
      return;
    }

    if (!Object.values(passwordChecks).every(Boolean)) {
      setError("Password does not meet all requirements");
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);
    try {
      // Remove confirmPassword - backend doesn't expect it
      const { confirmPassword, ...payload } = formData;
      const result = await registerUser(payload);

      if (result.success) {
        navigate("/login", {
          state: {
            message:
              "Account created. Check your email to verify your account.",
          },
        });
      } else {
        setError(result.error || "Registration failed");
      }
    } catch (err) {
      setError(err.userMessage || err.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  const Requirement = ({ met, label }) => (
    <div className="flex items-center gap-2 text-xs">
      {met ? (
        <Check size={13} className="text-green-600 shrink-0" />
      ) : (
        <X size={13} className="text-slate-300 shrink-0" />
      )}
      <span className={met ? "text-slate-700" : "text-slate-400"}>{label}</span>
    </div>
  );

  return (
    <div className="min-h-screen bg-slate-50 py-8 px-4">
      <div className="w-full max-w-2xl mx-auto">
        {/* Header */}
        <div className="text-center mb-6">
          <div className="flex justify-center mb-4">
            <div className="w-12 h-12 rounded-lg bg-[#0284c7] flex items-center justify-center">
              <Heart className="w-6 h-6 text-white" />
            </div>
          </div>
          <h1 className="text-2xl font-semibold text-slate-800">
            Create Patient Account
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Register to book appointments and access your medical records
          </p>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 sm:p-8">
          <form onSubmit={handleSubmit} className="space-y-5">
            {/* ===== Personal Information ===== */}
            <div>
              <h3 className="text-sm font-semibold text-slate-700 mb-3">
                Personal Information
              </h3>

              <div className="space-y-4">
                {/* Full Name */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Full Name <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <User
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                      size={17}
                    />
                    <input
                      type="text"
                      name="fullName"
                      value={formData.fullName}
                      onChange={handleChange}
                      placeholder="Enter your full name"
                      className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                      disabled={loading}
                      autoComplete="name"
                    />
                  </div>
                </div>

                {/* Email */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Email Address <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <Mail
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                      size={17}
                    />
                    <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      placeholder="you@example.com"
                      className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                      disabled={loading}
                      autoComplete="email"
                    />
                  </div>
                </div>

                {/* Phone */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Phone Number <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <Phone
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                      size={17}
                    />
                    <input
                      type="tel"
                      name="phoneNumber"
                      value={formData.phoneNumber}
                      onChange={handleChange}
                      placeholder="10-digit mobile number"
                      className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                      disabled={loading}
                      autoComplete="tel"
                    />
                  </div>
                </div>

                {/* DOB + Gender */}
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1.5">
                      Date of Birth <span className="text-red-500">*</span>
                    </label>
                    <div className="relative">
                      <Calendar
                        className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none"
                        size={17}
                      />
                      <input
                        type="date"
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        max={new Date().toISOString().split("T")[0]}
                        className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800"
                        disabled={loading}
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1.5">
                      Gender <span className="text-red-500">*</span>
                    </label>
                    <select
                      name="gender"
                      value={formData.gender}
                      onChange={handleChange}
                      className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800"
                      disabled={loading}
                    >
                      <option value="">Select gender</option>
                      {GENDERS.map((g) => (
                        <option key={g.value} value={g.value}>
                          {g.label}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                {/* Blood Group */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Blood Group <span className="text-red-500">*</span>
                  </label>
                  <select
                    name="bloodGroup"
                    value={formData.bloodGroup}
                    onChange={handleChange}
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800"
                    disabled={loading}
                  >
                    <option value="">Select blood group</option>
                    {BLOOD_GROUPS.map((b) => (
                      <option key={b.value} value={b.value}>
                        {b.label}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Address */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Address
                  </label>
                  <div className="relative">
                    <MapPin
                      className="absolute left-3 top-3 text-slate-400"
                      size={17}
                    />
                    <textarea
                      name="address"
                      value={formData.address}
                      onChange={handleChange}
                      placeholder="Enter your address"
                      rows={2}
                      className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400 resize-none"
                      disabled={loading}
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* ===== Emergency Contact ===== */}
            <div className="pt-2 border-t border-slate-100">
              <h3 className="text-sm font-semibold text-slate-700 mb-3 mt-4 flex items-center gap-2">
                <Users size={15} className="text-slate-500" />
                Emergency Contact
              </h3>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Contact Name <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="text"
                    name="emergencyContactName"
                    value={formData.emergencyContactName}
                    onChange={handleChange}
                    placeholder="Contact person's name"
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                    disabled={loading}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Contact Number <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="tel"
                    name="emergencyContactNumber"
                    value={formData.emergencyContactNumber}
                    onChange={handleChange}
                    placeholder="Contact number"
                    className="w-full px-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                    disabled={loading}
                  />
                </div>
              </div>
            </div>

            {/* ===== Account Security ===== */}
            <div className="pt-2 border-t border-slate-100">
              <h3 className="text-sm font-semibold text-slate-700 mb-3 mt-4 flex items-center gap-2">
                <Shield size={15} className="text-slate-500" />
                Account Security
              </h3>

              {/* Password */}
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Password <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <Lock
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                      size={17}
                    />
                    <input
                      type={showPassword ? "text" : "password"}
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                      placeholder="Create a strong password"
                      className="w-full pl-10 pr-12 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                      disabled={loading}
                      autoComplete="new-password"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none"
                      disabled={loading}
                      aria-label={
                        showPassword ? "Hide password" : "Show password"
                      }
                      tabIndex={-1}
                    >
                      {showPassword ? <EyeOff size={17} /> : <Eye size={17} />}
                    </button>
                  </div>

                  {/* Strength Indicator */}
                  {formData.password && (
                    <div className="mt-3">
                      <div className="flex gap-1 mb-2">
                        {[1, 2, 3, 4].map((level) => (
                          <div
                            key={level}
                            className={`h-1 flex-1 rounded-full transition-colors ${
                              level <= passwordStrength.level
                                ? passwordStrength.color === "red"
                                  ? "bg-red-500"
                                  : passwordStrength.color === "amber"
                                    ? "bg-amber-500"
                                    : passwordStrength.color === "blue"
                                      ? "bg-blue-500"
                                      : "bg-green-500"
                                : "bg-slate-200"
                            }`}
                          />
                        ))}
                      </div>

                      <div className="flex items-center justify-between mb-2">
                        <span className="text-xs text-slate-500">
                          Password strength
                        </span>
                        <span
                          className={`text-xs font-medium ${
                            passwordStrength.color === "red"
                              ? "text-red-600"
                              : passwordStrength.color === "amber"
                                ? "text-amber-600"
                                : passwordStrength.color === "blue"
                                  ? "text-blue-600"
                                  : "text-green-600"
                          }`}
                        >
                          {passwordStrength.label}
                        </span>
                      </div>

                      <div className="bg-slate-50 rounded-lg p-3 space-y-1.5 border border-slate-100">
                        <Requirement
                          met={passwordChecks.length}
                          label="Between 8 and 20 characters"
                        />
                        <Requirement
                          met={passwordChecks.uppercase}
                          label="One uppercase letter"
                        />
                        <Requirement
                          met={passwordChecks.lowercase}
                          label="One lowercase letter"
                        />
                        <Requirement
                          met={passwordChecks.number}
                          label="One number"
                        />
                        <Requirement
                          met={passwordChecks.special}
                          label="One special character"
                        />
                      </div>
                    </div>
                  )}
                </div>

                {/* Confirm Password */}
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1.5">
                    Confirm Password <span className="text-red-500">*</span>
                  </label>
                  <div className="relative">
                    <Lock
                      className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                      size={17}
                    />
                    <input
                      type={showConfirmPassword ? "text" : "password"}
                      name="confirmPassword"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      placeholder="Re-enter your password"
                      className={`w-full pl-10 pr-12 py-2.5 rounded-lg border transition-colors bg-white text-slate-800 placeholder:text-slate-400 focus:ring-2 ${
                        formData.confirmPassword
                          ? passwordsMatch
                            ? "border-green-300 focus:border-green-500 focus:ring-green-500/10"
                            : "border-red-300 focus:border-red-500 focus:ring-red-500/10"
                          : "border-slate-300 focus:border-[#0284c7] focus:ring-[#0284c7]/10"
                      }`}
                      disabled={loading}
                      autoComplete="new-password"
                    />
                    <button
                      type="button"
                      onClick={() =>
                        setShowConfirmPassword(!showConfirmPassword)
                      }
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none"
                      disabled={loading}
                      aria-label={
                        showConfirmPassword ? "Hide password" : "Show password"
                      }
                      tabIndex={-1}
                    >
                      {showConfirmPassword ? (
                        <EyeOff size={17} />
                      ) : (
                        <Eye size={17} />
                      )}
                    </button>
                  </div>

                  {formData.confirmPassword && (
                    <div className="flex items-center gap-1.5 mt-2">
                      {passwordsMatch ? (
                        <>
                          <Check size={13} className="text-green-600" />
                          <span className="text-xs text-green-600">
                            Passwords match
                          </span>
                        </>
                      ) : (
                        <>
                          <X size={13} className="text-red-600" />
                          <span className="text-xs text-red-600">
                            Passwords do not match
                          </span>
                        </>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Error */}
            {error && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-3 flex items-start gap-2">
                <AlertCircle
                  size={16}
                  className="text-red-600 mt-0.5 shrink-0"
                />
                <p className="text-sm text-red-800">{error}</p>
              </div>
            )}

            {/* Submit */}
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-[#0284c7] hover:bg-[#0369a1] text-white font-medium py-2.5 rounded-lg transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <>
                  <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                  Creating account...
                </>
              ) : (
                "Create Account"
              )}
            </button>
          </form>

          {/* Footer */}
          <div className="mt-6 pt-6 border-t border-slate-100 text-center">
            <p className="text-sm text-slate-600">
              Already have an account?{" "}
              <Link
                to="/login"
                className="text-[#0284c7] hover:text-[#0369a1] font-medium hover:underline"
              >
                Sign in
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
