import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import Card from "../components/common/Card";
import {
  Users,
  UserPlus,
  Activity,
  Calendar,
  TrendingUp,
  Heart,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { patientApi } from "../api/patientApi";
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

  useEffect(() => {
    if (isAdmin) {
      patientApi.getPatients({ page: 0, size: 1 }).then((res) => {
        const data = res.data.data;
        setStats((prev) => ({
          ...prev,
          totalPatients: data.totalElements || 0,
        }));
      });
    }
  }, [isAdmin]);

  const statsData = [
    {
      title: "Total Patients",
      value: stats.totalPatients,
      icon: Users,
      color: "from-primary-500 to-primary-600",
      change: "+12%",
    },
    {
      title: "New Patients",
      value: stats.newPatients || 24,
      icon: UserPlus,
      color: "from-green-500 to-green-600",
      change: "+8%",
    },
    {
      title: "Active Patients",
      value: stats.activePatients || 156,
      icon: Activity,
      color: "from-blue-500 to-blue-600",
      change: "+5%",
    },
    {
      title: "Appointments",
      value: stats.appointments || 43,
      icon: Calendar,
      color: "from-purple-500 to-purple-600",
      change: "+15%",
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
          className="bg-gradient-to-r from-primary-500 to-primary-600 rounded-2xl p-6 text-white shadow-xl"
        >
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold">
                Welcome back, {user?.fullName || "User"}! 👋
              </h1>
              <p className="text-primary-100 mt-2">
                Here's what's happening with your hospital today
              </p>
            </div>
            <Heart className="w-12 h-12 text-white/30" />
          </div>
        </motion.div>

        {/* Stats Grid */}
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
                    <p className="text-sm text-gray-500 font-medium">
                      {stat.title}
                    </p>
                    <p className="text-2xl font-bold mt-1">{stat.value}</p>
                    <p className="text-xs text-green-500 mt-1 flex items-center">
                      <TrendingUp size={12} className="mr-1" />
                      {stat.change}
                    </p>
                  </div>
                  <div
                    className={`bg-gradient-to-br ${stat.color} p-3 rounded-xl text-white`}
                  >
                    <stat.icon size={24} />
                  </div>
                </div>
              </Card>
            </motion.div>
          ))}
        </div>

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.4 }}
          >
            <Card>
              <h3 className="text-lg font-semibold mb-4">
                Weekly Patient Visits
              </h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={chartData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Bar
                      dataKey="patients"
                      fill="#0ea5e9"
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
            transition={{ delay: 0.5 }}
          >
            <Card>
              <h3 className="text-lg font-semibold mb-4">
                Monthly Visits Trend
              </h3>
              <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={activityData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Line
                      type="monotone"
                      dataKey="visits"
                      stroke="#0ea5e9"
                      strokeWidth={3}
                      dot={{ fill: "#0ea5e9" }}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </Card>
          </motion.div>
        </div>

        {/* Recent Activity */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.6 }}
        >
          <Card>
            <h3 className="text-lg font-semibold mb-4">Recent Activity</h3>
            <div className="space-y-3">
              {[1, 2, 3, 4].map((_, index) => (
                <div
                  key={index}
                  className="flex items-center gap-4 p-3 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <div className="w-10 h-10 rounded-full bg-primary-100 flex items-center justify-center text-primary-600">
                    <UserPlus size={20} />
                  </div>
                  <div className="flex-1">
                    <p className="font-medium">New patient registered</p>
                    <p className="text-sm text-gray-500">2 minutes ago</p>
                  </div>
                  <span className="text-xs bg-green-100 text-green-600 px-2 py-1 rounded-full">
                    New
                  </span>
                </div>
              ))}
            </div>
          </Card>
        </motion.div>
      </div>
    </Layout>
  );
};

export default Dashboard;
