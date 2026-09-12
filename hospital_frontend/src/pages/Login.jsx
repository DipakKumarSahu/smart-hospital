import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";
import {
  Heart,
  Mail,
  Lock,
  UserPlus,
  ArrowRight,
  Eye,
  EyeOff,
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

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      toast.error("Please fill in all fields");
      return;
    }

    setLoading(true);
    try {
      const result = await login(email, password);
      console.log("Login result:", result);

      if (result.success) {
        console.log("Login successful, navigating to dashboard...");
        // Force navigation
        setTimeout(() => {
          navigate("/dashboard");
        }, 100);
      }
    } catch (error) {
      console.error("Login error:", error);
      toast.error("Login failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center p-4">
      <div className="w-full max-w-6xl grid lg:grid-cols-2 gap-8 items-center">
        {/* Left Side - Branding */}
        <div className="hidden lg:block space-y-6">
          <Tilt
            tiltMaxAngleX={5}
            tiltMaxAngleY={5}
            glareEnable
            glareMaxOpacity={0.1}
          >
            <div className="bg-white/30 backdrop-blur-lg rounded-3xl p-8 border border-white/20">
              <div className="flex items-center gap-3 mb-6">
                <Heart className="w-12 h-12 text-blue-500 animate-pulse" />
                <h1 className="text-4xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
                  Smart Hospital
                </h1>
              </div>

              <TypeAnimation
                sequence={[
                  "Welcome to Smart Hospital Management System",
                  2000,
                  "Patient Management Made Easy",
                  2000,
                  "Secure & Efficient Healthcare Platform",
                  2000,
                ]}
                wrapper="h2"
                speed={50}
                repeat={Infinity}
                className="text-2xl font-semibold text-gray-800 mb-4"
              />

              <p className="text-gray-600 text-lg leading-relaxed">
                Experience the future of healthcare management with our
                comprehensive hospital management system. Streamline patient
                care, appointments, and medical records all in one place.
              </p>

              <div className="mt-8 grid grid-cols-2 gap-4">
                {[
                  "24/7 Support",
                  "Secure Data",
                  "Easy Access",
                  "Smart Analytics",
                ].map((feature) => (
                  <div
                    key={feature}
                    className="bg-white/50 rounded-xl p-3 text-center"
                  >
                    <span className="text-sm font-medium text-gray-700">
                      {feature}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </Tilt>
        </div>

        {/* Right Side - Login Form */}
        <div className="w-full max-w-md mx-auto">
          <div className="bg-white/80 backdrop-blur-xl rounded-2xl shadow-2xl p-8 border border-white/20">
            <div className="text-center mb-8">
              <div className="lg:hidden flex justify-center mb-4">
                <Heart className="w-12 h-12 text-blue-500" />
              </div>
              <h2 className="text-2xl font-bold text-gray-800">Welcome Back</h2>
              <p className="text-gray-500 mt-2">
                Sign in to your account to continue
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                  Email Address
                </label>
                <div className="relative">
                  <Mail
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                    size={18}
                  />
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="doctor@hospital.com"
                    className="w-full pl-10 pr-4 py-3 rounded-xl border border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all duration-200 bg-white/50"
                    required
                    disabled={loading}
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                  Password
                </label>
                <div className="relative">
                  <Lock
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                    size={18}
                  />
                  <input
                    type={showPassword ? "text" : "password"}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="••••••••"
                    className="w-full pl-10 pr-12 py-3 rounded-xl border border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all duration-200 bg-white/50"
                    required
                    disabled={loading}
                  />
                  <button
                    type="button"
                    onClick={togglePasswordVisibility}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                    disabled={loading}
                  >
                    {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                  </button>
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full btn-gradient py-3.5 text-base flex items-center justify-center gap-2 group"
              >
                {loading ? (
                  <>
                    <div className="spinner w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    Signing in...
                  </>
                ) : (
                  <>
                    Sign In
                    <ArrowRight
                      className="group-hover:translate-x-1 transition-transform"
                      size={18}
                    />
                  </>
                )}
              </button>
            </form>

            <div className="mt-6 text-center">
              <p className="text-gray-600">
                Don't have an account?{" "}
                <Link
                  to="/register"
                  className="text-blue-600 hover:text-blue-700 font-semibold hover:underline transition-all"
                >
                  Create Account
                </Link>
              </p>
              <div className="mt-4 flex items-center justify-center gap-2 text-sm text-gray-400">
                <UserPlus size={16} />
                <span>Demo: admin@hospital.com / password</span>
              </div>
            </div>
          </div>

          <p className="text-center text-gray-400 text-sm mt-6">
            © 2026 Smart Hospital Management System
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
