import React, { useState, useEffect } from "react";
import Layout from "../../layout/Layout";
import Card from "../../components/common/Card";
import Button from "../../components/common/Button";
import { staffApi } from "../../api/staffApi";
import { toast } from "react-toastify";
import { Plus, Search, UserCheck, UserX } from "lucide-react";
import AddStaffModal from "./AddStaffModal";

const StaffManagement = () => {
  const [staff, setStaff] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    fetchStaff();
  }, [keyword]);

  const fetchStaff = async () => {
    setLoading(true);
    try {
      const res = await staffApi.getAll({
        keyword: keyword || null,
        page: 0,
        size: 50,
      });
      setStaff(res.data.data.content || []);
    } catch (error) {
      toast.error("Failed to load staff");
    } finally {
      setLoading(false);
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await staffApi.toggleStatus(id);
      toast.success("Status updated");
      fetchStaff();
    } catch (error) {
      toast.error("Failed to update status");
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-2xl font-semibold text-slate-800">
              Staff Management
            </h1>
            <p className="text-sm text-slate-500 mt-1">
              Manage hospital staff and doctors
            </p>
          </div>
          <Button onClick={() => setIsModalOpen(true)}>
            <Plus size={16} /> Add Staff
          </Button>
        </div>

        {/* Search */}
        <Card>
          <div className="relative">
            <Search
              className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
              size={18}
            />
            <input
              type="text"
              placeholder="Search by name, email, or employee code..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] focus:ring-2 focus:ring-[#0284c7]/10 outline-none"
            />
          </div>
        </Card>

        {/* Table */}
        <Card>
          {loading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="h-12 bg-slate-100 animate-pulse rounded-lg"
                />
              ))}
            </div>
          ) : staff.length === 0 ? (
            <div className="text-center py-10 text-slate-500">
              No staff found.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm text-left">
                <thead className="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
                  <tr>
                    <th className="px-4 py-3">Employee</th>
                    <th className="px-4 py-3">Role</th>
                    <th className="px-4 py-3">Designation</th>
                    <th className="px-4 py-3">Status</th>
                    <th className="px-4 py-3 text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {staff.map((s) => (
                    <tr key={s.id} className="hover:bg-slate-50">
                      <td className="px-4 py-3">
                        <p className="font-medium text-slate-800">
                          {s.fullName}
                        </p>
                        <p className="text-xs text-slate-500">
                          {s.email} • {s.employeeCode}
                        </p>
                      </td>
                      <td className="px-4 py-3 text-slate-600">
                        {s.role.replace("ROLE_", "")}
                      </td>
                      <td className="px-4 py-3 text-slate-600">
                        {s.designation}
                      </td>
                      <td className="px-4 py-3">
                        <span
                          className={`px-2 py-1 text-xs rounded-full ${s.active ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}
                        >
                          {s.active ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-right">
                        <button
                          onClick={() => handleToggleStatus(s.id)}
                          className={`p-1.5 rounded-lg border transition-colors ${s.active ? "text-red-600 border-red-200 hover:bg-red-50" : "text-green-600 border-green-200 hover:bg-green-50"}`}
                          title={s.active ? "Deactivate" : "Activate"}
                        >
                          {s.active ? (
                            <UserX size={16} />
                          ) : (
                            <UserCheck size={16} />
                          )}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card>
      </div>

      <AddStaffModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onRefresh={fetchStaff}
      />
    </Layout>
  );
};

export default StaffManagement;
