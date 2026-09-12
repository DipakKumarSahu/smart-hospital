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

// Pages
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Patients from "./pages/Patients";
import CreatePatient from "./pages/CreatePatient";
import EditPatient from "./pages/EditPatient";
import PatientDetails from "./patients/PatientDetails";
import Profile from "./pages/Profile";



import BookAppointment from "./pages/BookAppointment";
import MyAppointments from "./pages/MyAppointments";
import PaymentPage from "./pages/PaymentPage";

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" />;
};

const AdminRoute = ({ children }) => {
  const { isAuthenticated, isAdmin } = useAuth();
  return isAuthenticated && isAdmin ? children : <Navigate to="/dashboard" />;
};

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen">
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
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <Navigate to="/dashboard" />
                </ProtectedRoute>
              }
            />

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


// Routes mein add karo:
<Route path="/book-appointment" element={
  <ProtectedRoute>
    <BookAppointment />
  </ProtectedRoute>
} />

<Route path="/my-appointments" element={
  <ProtectedRoute>
    <MyAppointments />
  </ProtectedRoute>
} />

<Route path="/payment/:appointmentId" element={
  <ProtectedRoute>
    <PaymentPage />
  </ProtectedRoute>
} />

            <Route
              path="/profile"
              element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              }
            />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
