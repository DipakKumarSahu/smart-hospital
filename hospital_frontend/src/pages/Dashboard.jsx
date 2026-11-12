import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import { staffApi } from "../api/staffApi";
import { patientApi } from "../api/patientApi";
import { useAuth } from "../context/AuthContext";
import {
  Users,
  UserPlus,
  Activity,
  Calendar,
  TrendingUp,
  Heart,
  UserCheck,
  UserX,
} from "lucide-react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  LineChart,
  Line,
} from "recharts";

const Dashboard = () => {
  const { user, isAdmin } = useAuth();
  const [stats, setStats] = useState({
    totalPatients: 0,
    newPatients: 0,
    activePatients: 0,
    appointments: 0,
  });

  const [staffStats, setStaffStats] = useState({
    total: 0,
    active: 0,
    inactive: 0,
  });

  useEffect(() => {
    if (isAdmin) {
       if (typeof patientApi.getPatients === "function") {
      patientApi
        .getPatients({ page: 0, size: 1 })
        .then((res) => {
          const data = res.data.data;
          setStats((prev) => ({
            ...prev,
            totalPatients: data.totalElements || 0,
          }));
        })
        .catch(console.error);
      }
    }
  }, [isAdmin]);

  useEffect(() => {
    staffApi
      .getStats()
      .then((res) => setStaffStats(res.data.data))
      .catch(console.error);
  }, []);

  const statsData = [
    {
      title: "Total Patients",
      value: stats.totalPatients,
      icon: Users,
      color: "bg-[#0284c7]", // Sky blue
    },
    {
      title: "New Patients",
      value: stats.newPatients || 0,
      icon: UserPlus,
      color: "bg-green-600",
    },
    {
      title: "Active Patients",
      value: stats.activePatients || 0,
      icon: Activity,
      color: "bg-blue-600",
    },
    {
      title: "Appointments",
      value: stats.appointments || 0,
      icon: Calendar,
      color: "bg-purple-600",
    },
  ];

  const chartData = [
    { name: "Mon", patients: 12 },
    { name: "Tue", patients: 19 },
    { name: "Wed", patients: 15 },
    { name: "Thu", patients: 22 },
    { name: "Fri", patients: 28 },
    { name: "Sat", patients: 18 },
    { name: "Sun", patients: 8 },
  ];

  const activityData = [
    { name: "Jan", visits: 45 },
    { name: "Feb", visits: 52 },
    { name: "Mar", visits: 48 },
    { name: "Apr", visits: 61 },
    { name: "May", visits: 55 },
    { name: "Jun", visits: 67 },
  ];

  return (
    <Layout>
      <div className="space-y-6">
        {/* Welcome Section */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-[#0284c7] rounded-2xl p-6 text-white shadow-sm"
        >
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-semibold">
                Welcome back, {user?.fullName || "User"}
              </h1>
              <p className="text-blue-100 mt-1 text-sm">
                Here is what's happening with your hospital today.
              </p>
            </div>
            <Heart className="w-10 h-10 text-white/20" />
          </div>
        </motion.div>

        {/* Stats Grid (Patients) */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {statsData.map((stat, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <Card hover className="overflow-hidden">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-slate-500 font-medium">
                      {stat.title}
                    </p>
                    <p className="text-2xl font-semibold text-slate-800 mt-1">
                      {stat.value}
                    </p>
                  </div>
                  <div className={`${stat.color} p-3 rounded-xl text-white`}>
                    <stat.icon size={22} />
                  </div>
                </div>
              </Card>
            </motion.div>
          ))}
        </div>

        {/* Staff Stats Card (Admin only) */}
        {isAdmin && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <Card hover>
              <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 rounded-lg bg-[#0284c7]/10 flex items-center justify-center text-[#0284c7]">
                    <Users size={24} />
                  </div>
                  <div>
                    <p className="text-sm text-slate-500 font-medium">
                      Total Hospital Staff
                    </p>
                    <p className="text-2xl font-semibold text-slate-800">
                      {staffStats.total}
                    </p>
                  </div>
                </div>
                <div className="flex gap-4 text-sm">
                  <span className="text-green-600 flex items-center gap-1.5 bg-green-50 px-3 py-1.5 rounded-lg">
                    <UserCheck size={16} /> {staffStats.active} Active
                  </span>
                  <span className="text-red-600 flex items-center gap-1.5 bg-red-50 px-3 py-1.5 rounded-lg">
                    <UserX size={16} /> {staffStats.inactive} Inactive
                  </span>
                </div>
              </div>
            </Card>
          </motion.div>
        )}

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
          >
            <Card>
              <h3 className="text-lg font-semibold text-slate-800 mb-4">
                Weekly Patient Visits
              </h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={chartData}>
                    <CartesianGrid
                      strokeDasharray="3 3"
                      vertical={false}
                      stroke="#e2e8f0"
                    />
                    <XAxis dataKey="name" axisLine={false} tickLine={false} />
                    <YAxis axisLine={false} tickLine={false} />
                    <Tooltip />
                    <Bar
                      dataKey="patients"
                      fill="#0284c7"
                      radius={[4, 4, 0, 0]}
                    />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </Card>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
          >
            <Card>
              <h3 className="text-lg font-semibold text-slate-800 mb-4">
                Monthly Visits Trend
              </h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={activityData}>
                    <CartesianGrid
                      strokeDasharray="3 3"
                      vertical={false}
                      stroke="#e2e8f0"
                    />
                    <XAxis dataKey="name" axisLine={false} tickLine={false} />
                    <YAxis axisLine={false} tickLine={false} />
                    <Tooltip />
                    <Line
                      type="monotone"
                      dataKey="visits"
                      stroke="#0284c7"
                      strokeWidth={3}
                      dot={{ fill: "#0284c7", r: 4 }}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </Card>
          </motion.div>
        </div>
      </div>
    </Layout>
  );
};

export default Dashboard;
