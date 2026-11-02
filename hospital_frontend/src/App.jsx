import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AuthProvider, useAuth } from "./context/AuthContext";

// Auth Pages
import Login from "./pages/Login";
import Register from "./pages/Register";

// Admin Pages
import Dashboard from "./pages/Dashboard";
import Patients from "./pages/Patients";
import CreatePatient from "./pages/CreatePatient";
import EditPatient from "./pages/EditPatient";
import PatientDetails from "./patients/PatientDetails"; // Path check kar lena

// Patient Pages
import Profile from "./pages/Profile";
import BookAppointment from "./pages/BookAppointment";
import MyAppointments from "./pages/MyAppointments";
import PaymentPage from "./pages/PaymentPage";

// Doctor Pages
import DoctorDashboard from "./pages/doctor/DoctorDashboard";
import DoctorSchedule from "./pages/doctor/DoctorSchedule";
import DoctorSlots from "./pages/doctor/DoctorSlots";
import DoctorAppointments from "./pages/doctor/DoctorAppointments";

// =====================================================
// Route Protectors
// =====================================================
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading...
      </div>
    );
  return isAuthenticated ? children : <Navigate to="/login" replace />;
};

const AdminRoute = ({ children }) => {
  const { isAuthenticated, isAdmin, loading } = useAuth();
  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading...
      </div>
    );
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (!isAdmin) return <Navigate to="/dashboard" replace />;
  return children;
};

const DoctorRoute = ({ children }) => {
  const { isAuthenticated, user, loading } = useAuth();
  const isDoctor = user?.roles?.includes("DOCTOR");

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        Loading...
      </div>
    );
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (!isDoctor) return <Navigate to="/dashboard" replace />; // Agar doctor nahi hai toh dashboard bhej do
  return children;
};

// =====================================================
// Main App
// =====================================================
function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen bg-slate-50">
          <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light"
          />

          <Routes>
            {/* Public Routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Root Redirect */}
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <Navigate to="/dashboard" replace />
                </ProtectedRoute>
              }
            />

            {/* Admin Routes */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              }
            />
            <Route
              path="/patients"
              element={
                <AdminRoute>
                  <Patients />
                </AdminRoute>
              }
            />
            <Route
              path="/patients/:id"
              element={
                <AdminRoute>
                  <PatientDetails />
                </AdminRoute>
              }
            />
            <Route
              path="/patients/create"
              element={
                <AdminRoute>
                  <CreatePatient />
                </AdminRoute>
              }
            />
            <Route
              path="/patients/edit/:id"
              element={
                <AdminRoute>
                  <EditPatient />
                </AdminRoute>
              }
            />

            {/* Patient Routes */}
            <Route
              path="/book-appointment"
              element={
                <ProtectedRoute>
                  <BookAppointment />
                </ProtectedRoute>
              }
            />
            <Route
              path="/my-appointments"
              element={
                <ProtectedRoute>
                  <MyAppointments />
                </ProtectedRoute>
              }
            />
            <Route
              path="/payment/:appointmentId"
              element={
                <ProtectedRoute>
                  <PaymentPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              }
            />

            {/* Doctor Routes */}
            <Route
              path="/doctor/dashboard"
              element={
                <DoctorRoute>
                  <DoctorDashboard />
                </DoctorRoute>
              }
            />
            <Route
              path="/doctor/schedule"
              element={
                <DoctorRoute>
                  <DoctorSchedule />
                </DoctorRoute>
              }
            />
            <Route
              path="/doctor/slots"
              element={
                <DoctorRoute>
                  <DoctorSlots />
                </DoctorRoute>
              }
            />
            <Route
              path="/doctor/appointments"
              element={
                <DoctorRoute>
                  <DoctorAppointments />
                </DoctorRoute>
              }
            />

            {/* 404 - Catch All */}
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
