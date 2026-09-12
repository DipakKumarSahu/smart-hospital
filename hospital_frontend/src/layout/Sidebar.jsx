import React from "react";
import { NavLink } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import {
  LayoutDashboard,
  Users,
  Calendar,
  Pill,
  FileText,
  Settings,
  HelpCircle,
  Heart,
  User,
  LogOut,
  ChevronLeft,
  ChevronRight,
  ClipboardList,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Sidebar = ({ isOpen, setIsOpen, mobileOpen, setMobileOpen }) => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  // Role-based menu items
  const adminMenuItems = [
    { path: "/dashboard", icon: LayoutDashboard, label: "Dashboard" },
    { path: "/patients", icon: Users, label: "Patients" },
    { path: "/appointments", icon: Calendar, label: "Appointments" },
    { path: "/reports", icon: ClipboardList, label: "Reports" },
  ];

  const patientMenuItems = [
    { path: "/profile", icon: User, label: "My Profile" },
    { path: "/my-appointments", icon: Calendar, label: "My Appointments" },
    { path: "/book-appointment", icon: Calendar, label: "Book Appointment" },
    { path: "/my-prescriptions", icon: FileText, label: "Prescriptions" },
  ];

  const menuItems = isAdmin ? adminMenuItems : patientMenuItems;

  const bottomItems = [
    { path: "/settings", icon: Settings, label: "Settings" },
    { path: "/help", icon: HelpCircle, label: "Help" },
  ];

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const SidebarContent = () => (
    <div className="h-full flex flex-col bg-white border-r border-gray-200">
      {/* Logo */}
      <div className="flex items-center justify-between p-4 border-b border-gray-200">
        <div className="flex items-center gap-3">
          <Heart className="w-8 h-8 text-primary-500" />
          <AnimatePresence>
            {isOpen && (
              <motion.span
                initial={{ opacity: 0, x: -10 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: -10 }}
                className="text-xl font-bold bg-gradient-to-r from-primary-600 to-primary-400 bg-clip-text text-transparent"
              >
                Smart Hospital
              </motion.span>
            )}
          </AnimatePresence>
        </div>
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="hidden lg:block p-1 rounded-lg hover:bg-gray-100 transition-colors"
        >
          {isOpen ? <ChevronLeft size={20} /> : <ChevronRight size={20} />}
        </button>
      </div>

      {/* User Info */}
      <div
        className={`p-4 border-b border-gray-200 ${!isOpen && "text-center"}`}
      >
        <div
          className={`flex items-center ${isOpen ? "gap-3" : "justify-center"}`}
        >
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center text-white font-bold">
            {user?.fullName?.charAt(0) || "U"}
          </div>
          <AnimatePresence>
            {isOpen && (
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="flex-1 min-w-0"
              >
                <p className="text-sm font-semibold truncate">
                  {user?.fullName || "User"}
                </p>
                <p className="text-xs text-gray-500 truncate">
                  {isAdmin ? "Administrator" : "Patient"}
                </p>
              </motion.div>
            )}
          </AnimatePresence>
        </div>
      </div>

      {/* Menu Items */}
      <nav className="flex-1 overflow-y-auto p-2 space-y-1">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => `
              flex items-center ${isOpen ? "gap-3 px-4" : "justify-center px-2"}
              py-3 rounded-lg transition-all duration-200
              ${
                isActive
                  ? "bg-gradient-to-r from-primary-500 to-primary-600 text-white shadow-lg"
                  : "text-gray-600 hover:bg-primary-50 hover:text-primary-600"
              }
            `}
          >
            <item.icon size={20} />
            <AnimatePresence>
              {isOpen && (
                <motion.span
                  initial={{ opacity: 0, width: 0 }}
                  animate={{ opacity: 1, width: "auto" }}
                  exit={{ opacity: 0, width: 0 }}
                  className="text-sm font-medium whitespace-nowrap"
                >
                  {item.label}
                </motion.span>
              )}
            </AnimatePresence>
          </NavLink>
        ))}
      </nav>

      {/* Bottom Menu */}
      <div className="border-t border-gray-200 p-2 space-y-1">
        {bottomItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => `
              flex items-center ${isOpen ? "gap-3 px-4" : "justify-center px-2"}
              py-3 rounded-lg transition-all duration-200
              ${
                isActive
                  ? "bg-gradient-to-r from-primary-500 to-primary-600 text-white shadow-lg"
                  : "text-gray-600 hover:bg-primary-50 hover:text-primary-600"
              }
            `}
          >
            <item.icon size={20} />
            <AnimatePresence>
              {isOpen && (
                <motion.span
                  initial={{ opacity: 0, width: 0 }}
                  animate={{ opacity: 1, width: "auto" }}
                  exit={{ opacity: 0, width: 0 }}
                  className="text-sm font-medium whitespace-nowrap"
                >
                  {item.label}
                </motion.span>
              )}
            </AnimatePresence>
          </NavLink>
        ))}

        <button
          onClick={handleLogout}
          className={`
            w-full flex items-center ${isOpen ? "gap-3 px-4" : "justify-center px-2"}
            py-3 rounded-lg transition-all duration-200
            text-red-600 hover:bg-red-50
          `}
        >
          <LogOut size={20} />
          <AnimatePresence>
            {isOpen && (
              <motion.span
                initial={{ opacity: 0, width: 0 }}
                animate={{ opacity: 1, width: "auto" }}
                exit={{ opacity: 0, width: 0 }}
                className="text-sm font-medium whitespace-nowrap"
              >
                Logout
              </motion.span>
            )}
          </AnimatePresence>
        </button>
      </div>
    </div>
  );

  return (
    <>
      <aside
        className={`
        hidden lg:block fixed top-0 left-0 h-full z-40
        transition-all duration-300
        ${isOpen ? "w-64" : "w-20"}
      `}
      >
        <SidebarContent />
      </aside>

      <AnimatePresence>
        {mobileOpen && (
          <motion.aside
            initial={{ x: -300, opacity: 0 }}
            animate={{ x: 0, opacity: 1 }}
            exit={{ x: -300, opacity: 0 }}
            transition={{ type: "spring", damping: 25 }}
            className="fixed top-0 left-0 h-full w-64 z-50 lg:hidden shadow-2xl"
          >
            <SidebarContent />
          </motion.aside>
        )}
      </AnimatePresence>
    </>
  );
};

export default Sidebar;
