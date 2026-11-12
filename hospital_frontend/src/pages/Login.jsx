import React, { useState, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";
import {
  Heart,
  Mail,
  Lock,
  ArrowRight,
  Eye,
  EyeOff,
  AlertCircle,
} from "lucide-react";
import Tilt from "react-parallax-tilt";
import { TypeAnimation } from "react-type-animation";

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");

  // Email verification redirect handling
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const verified = searchParams.get("verified");
    const reason = searchParams.get("reason");

    if (verified === "true") {
      toast.success("Email verified. You can now sign in.");
      window.history.replaceState({}, "", "/login");
    } else if (verified === "false") {
      toast.error(reason || "Email verification failed. Please try again.");
      window.history.replaceState({}, "", "/login");
    }
  }, [searchParams]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Client-side validation
    if (!email || !password) {
      setError("Please fill in all fields");
      return;
    }

    setLoading(true);

    try {
      const result = await login(email, password);

      if (result.success) {
        // 👇 Naya Role Check (ADMIN ya ROLE_ADMIN dono handle karega)
        const isAdmin = result.user?.roles?.some(
          (role) => role === "ADMIN" || role === "ROLE_ADMIN",
        );
        const isDoctor = result.user?.roles?.some(
          (role) => role === "DOCTOR" || role === "ROLE_DOCTOR",
        );

        // Redirect based on role
        let redirectPath = "/profile"; // Default for Patient
        if (isAdmin) redirectPath = "/dashboard";
        else if (isDoctor) redirectPath = "/doctor/dashboard";

        navigate(redirectPath, { replace: true });
      } else {
        setError(result.error || "Invalid email or password");
      }
    } catch (err) {
      setError(err.userMessage || err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
      <div className="w-full max-w-6xl grid lg:grid-cols-2 gap-8 items-center">
        {/* Left Side - Branding */}
        <div className="hidden lg:block space-y-6">
          <Tilt tiltMaxAngleX={4} tiltMaxAngleY={4}>
            <div className="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm">
              <div className="flex items-center gap-3 mb-6">
                <div className="w-11 h-11 rounded-lg bg-[#0284c7] flex items-center justify-center">
                  <Heart className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h1 className="text-2xl font-semibold text-slate-800">
                    Smart Hospital
                  </h1>
                  <p className="text-sm text-slate-500">
                    Patient Management System
                  </p>
                </div>
              </div>

              <TypeAnimation
                sequence={[
                  "Manage patient records efficiently",
                  2000,
                  "Schedule appointments in seconds",
                  2000,
                  "Access medical history securely",
                  2000,
                ]}
                wrapper="p"
                speed={50}
                repeat={Infinity}
                className="text-lg text-slate-600 mb-6 min-h-[28px]"
              />

              <p className="text-sm text-slate-500 leading-relaxed">
                A comprehensive platform for managing patient records,
                appointments, and medical data. Built for healthcare
                professionals who value efficiency and accuracy.
              </p>

              <div className="mt-8 grid grid-cols-2 gap-3">
                {[
                  { label: "Patient Records", value: "Centralized" },
                  { label: "Appointments", value: "Real-time" },
                  { label: "Data Security", value: "Encrypted" },
                  { label: "Access Control", value: "Role-based" },
                ].map((item) => (
                  <div
                    key={item.label}
                    className="bg-slate-50 rounded-lg p-3 border border-slate-100"
                  >
                    <p className="text-xs text-slate-500">{item.label}</p>
                    <p className="text-sm font-medium text-slate-700 mt-0.5">
                      {item.value}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          </Tilt>
        </div>

        {/* Right Side - Login Form */}
        <div className="w-full max-w-md mx-auto">
          <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-8">
            <div className="text-center mb-8">
              <div className="lg:hidden flex justify-center mb-4">
                <div className="w-12 h-12 rounded-lg bg-[#0284c7] flex items-center justify-center">
                  <Heart className="w-6 h-6 text-white" />
                </div>
              </div>
              <h2 className="text-2xl font-semibold text-slate-800">
                Welcome Back
              </h2>
              <p className="text-sm text-slate-500 mt-1">
                Sign in to your account to continue
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1.5">
                  Email Address
                </label>
                <div className="relative">
                  <Mail
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                    size={18}
                  />
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="you@hospital.com"
                    className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                    disabled={loading}
                    autoComplete="email"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1.5">
                  Password
                </label>
                <div className="relative">
                  <Lock
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                    size={18}
                  />
                  <input
                    type={showPassword ? "text" : "password"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Enter your password"
                    className="w-full pl-10 pr-12 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 transition-colors bg-white text-slate-800 placeholder:text-slate-400"
                    disabled={loading}
                    autoComplete="current-password"
                  />
                  <button
                    type="button"
                    onClick={togglePasswordVisibility}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors focus:outline-none"
                    disabled={loading}
                    aria-label={
                      showPassword ? "Hide password" : "Show password"
                    }
                    tabIndex={-1}
                  >
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </div>

              {error && (
                <div className="bg-red-50 border border-red-200 rounded-lg p-3 flex items-start gap-2">
                  <AlertCircle
                    size={16}
                    className="text-red-600 mt-0.5 shrink-0"
                  />
                  <p className="text-sm text-red-800">{error}</p>
                </div>
              )}

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-[#0284c7] hover:bg-[#0369a1] text-white font-medium py-2.5 rounded-lg transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    Signing in...
                  </>
                ) : (
                  <>
                    Sign In
                    <ArrowRight size={18} />
                  </>
                )}
              </button>
            </form>

            <div className="mt-6 pt-6 border-t border-slate-100 text-center">
              <p className="text-sm text-slate-600">
                Don't have an account?{" "}
                <Link
                  to="/register"
                  className="text-[#0284c7] hover:text-[#0369a1] font-medium hover:underline"
                >
                  Create one
                </Link>
              </p>
            </div>
          </div>

          <p className="text-center text-xs text-slate-400 mt-6">
            Smart Hospital Management System
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
