import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Layout from "../../layout/Layout"; // Path check kar lena
import Card from "../../components/common/Card";
import Button from "../../components/common/Button";
import { useAuth } from "../../context/AuthContext";
import { doctorScheduleApi } from "../../api/doctorScheduleApi";
import { Calendar, Clock, Users, ArrowRight, TrendingUp } from "lucide-react";

const DoctorDashboard = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [schedules, setSchedules] = useState([]);

  // Fetch doctor's schedules for quick stats
  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      // Assuming this API returns schedules for the logged-in doctor
      const response = await doctorScheduleApi.getAll({ size: 100 });
      // Basic filter if backend returns all
      const mySchedules = (response.data?.data?.content || []).filter(
        (s) => s.doctorId === user?.id, // Agar user object mein id hai
      );
      setSchedules(mySchedules);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Welcome Banner */}
        <div className="bg-white rounded-xl border border-slate-200 p-6 md:p-8 shadow-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-semibold text-slate-800">
              Welcome, Dr. {user?.fullName || "Doctor"}
            </h1>
            <p className="text-sm text-slate-500 mt-1">
              Here is your schedule overview for today.
            </p>
          </div>
          <div className="flex gap-3">
            <Button
              variant="outline"
              onClick={() => navigate("/doctor/schedule")}
            >
              <Clock size={16} />
              Manage Schedule
            </Button>
            <Button onClick={() => navigate("/doctor/slots")}>
              View Slots
              <ArrowRight size={16} />
            </Button>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card hover>
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-lg bg-[#0284c7]/10 flex items-center justify-center text-[#0284c7]">
                <Calendar size={24} />
              </div>
              <div>
                <p className="text-sm text-slate-500 font-medium">
                  Active Days
                </p>
                <p className="text-2xl font-semibold text-slate-800">
                  {loading ? "-" : schedules.length}
                </p>
              </div>
            </div>
          </Card>

          <Card hover>
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-lg bg-green-100 flex items-center justify-center text-green-600">
                <Clock size={24} />
              </div>
              <div>
                <p className="text-sm text-slate-500 font-medium">
                  Weekly Hours
                </p>
                <p className="text-2xl font-semibold text-slate-800">
                  {loading ? "-" : schedules.length * 5} hrs
                </p>
              </div>
            </div>
          </Card>

          <Card hover>
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-lg bg-blue-100 flex items-center justify-center text-blue-600">
                <Users size={24} />
              </div>
              <div>
                <p className="text-sm text-slate-500 font-medium">
                  Appointments
                </p>
                <p className="text-2xl font-semibold text-slate-800">View</p>
              </div>
            </div>
          </Card>
        </div>

        {/* Weekly Schedule Overview */}
        <Card>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-slate-800 flex items-center gap-2">
              <TrendingUp size={20} className="text-[#0284c7]" />
              My Weekly Schedule
            </h2>
          </div>

          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="h-12 bg-slate-100 rounded-lg animate-pulse"
                />
              ))}
            </div>
          ) : schedules.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-slate-500 text-sm">No schedule set yet.</p>
              <Button
                variant="outline"
                className="mt-4"
                onClick={() => navigate("/doctor/schedule")}
              >
                Create Schedule
              </Button>
            </div>
          ) : (
            <div className="space-y-3">
              {schedules.map((sched) => (
                <div
                  key={sched.id}
                  className="flex items-center justify-between p-3 rounded-lg border border-slate-100 bg-slate-50"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-white flex items-center justify-center border border-slate-200">
                      <Calendar size={16} className="text-[#0284c7]" />
                    </div>
                    <div>
                      <p className="text-sm font-medium text-slate-800">
                        {sched.dayOfWeek}
                      </p>
                      <p className="text-xs text-slate-500">
                        {sched.slotDurationMinutes} min slots
                      </p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium text-slate-800">
                      {sched.startTime} - {sched.endTime}
                    </p>
                    <p
                      className={`text-xs ${sched.active ? "text-green-600" : "text-red-500"}`}
                    >
                      {sched.active ? "Active" : "Inactive"}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>
    </Layout>
  );
};

export default DoctorDashboard;
