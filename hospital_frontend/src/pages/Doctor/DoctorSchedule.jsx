import React, { useState, useEffect } from "react";
import Layout from "../../layout/Layout";
import Card from "../../components/common/Card";
import Button from "../../components/common/Button";
import { doctorScheduleApi } from "../../api/doctorScheduleApi";
import { toast } from "react-toastify";
import {
  Plus,
  Calendar,
  Clock,
  Trash2,
  Edit2,
  AlertCircle,
} from "lucide-react";

const DAYS = [
  "MONDAY",
  "TUESDAY",
  "WEDNESDAY",
  "THURSDAY",
  "FRIDAY",
  "SATURDAY",
  "SUNDAY",
];

const DoctorSchedule = () => {
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [saving, setSaving] = useState(false);

  // Form State
  const [formData, setFormData] = useState({
    dayOfWeek: "MONDAY",
    startTime: "09:00",
    endTime: "17:00",
    slotDurationMinutes: 30,
  });

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = async () => {
    try {
      const res = await doctorScheduleApi.getAll({ size: 100 });
      // Basic filtering (assuming backend returns all, we filter for UI)
      // Backend will pick logged-in doctor's schedule
      setSchedules(res.data?.data?.content || []);
    } catch (error) {
      toast.error("Failed to load schedules");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      // Note: We don't send doctorId. Backend uses JWT to identify the doctor.
      await doctorScheduleApi.create({
        dayOfWeek: formData.dayOfWeek,
        startTime: formData.startTime + ":00", // Add seconds for LocalTime
        endTime: formData.endTime + ":00",
        slotDurationMinutes: parseInt(formData.slotDurationMinutes),
      });

      toast.success("Schedule created and slots generated.");
      setShowForm(false);
      fetchSchedules();
    } catch (error) {
      toast.error(error.userMessage || "Failed to create schedule");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure? This will remove unbooked slots."))
      return;
    try {
      await doctorScheduleApi.delete(id);
      toast.success("Schedule deleted");
      fetchSchedules();
    } catch (error) {
      toast.error(error.userMessage || "Failed to delete schedule");
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-2xl font-semibold text-slate-800">
              My Schedule
            </h1>
            <p className="text-sm text-slate-500 mt-1">
              Manage your weekly availability
            </p>
          </div>
          <Button onClick={() => setShowForm(!showForm)}>
            {showForm ? (
              "Cancel"
            ) : (
              <>
                <Plus size={16} /> Add Schedule
              </>
            )}
          </Button>
        </div>

        {/* Add Form */}
        {showForm && (
          <Card>
            <h3 className="text-lg font-semibold text-slate-800 mb-4">
              Add New Availability
            </h3>
            <form
              onSubmit={handleSubmit}
              className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 items-end"
            >
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Day of Week
                </label>
                <select
                  name="dayOfWeek"
                  value={formData.dayOfWeek}
                  onChange={handleChange}
                  className="w-full px-3 py-2 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-1 focus:ring-[#0284c7] text-sm bg-white"
                >
                  {DAYS.map((day) => (
                    <option key={day} value={day}>
                      {day}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Start Time
                </label>
                <input
                  type="time"
                  name="startTime"
                  value={formData.startTime}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-1 focus:ring-[#0284c7] text-sm bg-white"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  End Time
                </label>
                <input
                  type="time"
                  name="endTime"
                  value={formData.endTime}
                  onChange={handleChange}
                  required
                  className="w-full px-3 py-2 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-1 focus:ring-[#0284c7] text-sm bg-white"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Slot Duration (mins)
                </label>
                <select
                  name="slotDurationMinutes"
                  value={formData.slotDurationMinutes}
                  onChange={handleChange}
                  className="w-full px-3 py-2 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-1 focus:ring-[#0284c7] text-sm bg-white"
                >
                  <option value={15}>15 minutes</option>
                  <option value={30}>30 minutes</option>
                  <option value={60}>60 minutes</option>
                </select>
              </div>

              <div className="md:col-span-2 lg:col-span-4 flex justify-end mt-2">
                <Button type="submit" isLoading={saving}>
                  Save Schedule
                </Button>
              </div>
            </form>
          </Card>
        )}

        {/* Schedule List */}
        <Card>
          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="h-16 bg-slate-100 rounded-lg animate-pulse"
                />
              ))}
            </div>
          ) : schedules.length === 0 ? (
            <div className="text-center py-12">
              <Clock className="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <h3 className="text-base font-medium text-slate-800">
                No schedules yet
              </h3>
              <p className="text-sm text-slate-500 mt-1 mb-4">
                Add your working hours to generate slots for patients.
              </p>
              <Button onClick={() => setShowForm(true)}>
                <Plus size={16} /> Create Schedule
              </Button>
            </div>
          ) : (
            <div className="divide-y divide-slate-100">
              {schedules.map((schedule) => (
                <div
                  key={schedule.id}
                  className="py-4 flex items-center justify-between"
                >
                  <div className="flex items-center gap-4">
                    <div className="w-10 h-10 rounded-lg bg-[#0284c7]/10 flex items-center justify-center text-[#0284c7]">
                      <Calendar size={20} />
                    </div>
                    <div>
                      <p className="text-sm font-semibold text-slate-800">
                        {schedule.dayOfWeek}
                      </p>
                      <p className="text-sm text-slate-500 flex items-center gap-2 mt-0.5">
                        <Clock size={12} />
                        {schedule.startTime} to {schedule.endTime}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <span className="text-xs bg-slate-100 text-slate-600 px-2 py-1 rounded">
                      {schedule.slotDurationMinutes} min slots
                    </span>
                    <button
                      onClick={() => handleDelete(schedule.id)}
                      className="p-2 text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                      title="Delete Schedule"
                    >
                      <Trash2 size={16} />
                    </button>
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

export default DoctorSchedule;
