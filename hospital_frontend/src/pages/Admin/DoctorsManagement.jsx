import React, { useState, useEffect } from "react";
import Layout from "../../layout/Layout";
import Card from "../../components/common/Card";
import Button from "../../components/common/Button";
import { doctorApi } from "../../api/doctorApi";
import { authApi } from "../../api/authApi";
import { toast } from "react-toastify";
import { Plus, Search, UserCheck, UserX, Edit } from "lucide-react";
import AddDoctorModal from "./AddDoctorModal";

const DoctorsManagement = () => {
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);

  useEffect(() => {
    fetchDoctors();
  }, [keyword]);

  const fetchDoctors = async () => {
    setLoading(true);
    try {
      // 👇 Admin wala naya API call karo jo saare (active + inactive) laayega
      const res = await doctorApi.getAllForAdmin({
        keyword: keyword || null,
        page: 0,
        size: 50,
      });
      setDoctors(res.data.data.content || []);
    } catch (error) {
      toast.error("Failed to load doctors");
    } finally {
      setLoading(false);
    }
  };

  const handleToggleStatus = async (doc) => {
    try {
      if (doc.active) {
        // Deactivate (Soft delete)
        await authApi.toggleDoctorStatusByAdmin(doc.id);
        toast.success("Doctor deactivated successfully");
      } else {
        // Activate (Update active=true)
        await authApi.updateDoctorByAdmin(doc.id, {
          departmentId: doc.departmentId,
          specialization: doc.specialization,
          licenseNumber: doc.licenseNumber,
          experienceYears: doc.experienceYears,
          consultationFee: doc.consultationFee,
          active: true,
        });
        toast.success("Doctor activated successfully");
      }
      fetchDoctors(); // Refresh list
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to update status");
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-2xl font-semibold text-slate-800">
              Doctors Management
            </h1>
            <p className="text-sm text-slate-500 mt-1">
              Manage hospital doctors and their profiles
            </p>
          </div>
          <Button
            onClick={() => {
              setSelectedDoctor(null);
              setIsModalOpen(true);
            }}
          >
            <Plus size={16} /> Add Doctor
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
              placeholder="Search doctors by name or specialization..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-300 focus:border-[#0284c7] outline-none"
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
          ) : doctors.length === 0 ? (
            <div className="text-center py-10 text-slate-500">
              No doctors found.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm text-left">
                <thead className="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
                  <tr>
                    <th className="px-4 py-3">Doctor</th>
                    <th className="px-4 py-3">Specialization</th>
                    <th className="px-4 py-3">Department</th>
                    <th className="px-4 py-3">Fee</th>
                    <th className="px-4 py-3">Status</th>
                    <th className="px-4 py-3 text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {doctors.map((doc) => (
                    <tr key={doc.id} className="hover:bg-slate-50">
                      <td className="px-4 py-3">
                        <p className="font-medium text-slate-800">
                          {doc.staffName}
                        </p>
                        <p className="text-xs text-slate-500">
                          {doc.doctorCode}
                        </p>
                      </td>
                      <td className="px-4 py-3 text-slate-600">
                        {doc.specialization}
                      </td>
                      <td className="px-4 py-3 text-slate-600">
                        {doc.departmentName}
                      </td>
                      <td className="px-4 py-3 text-slate-600">
                        ₹{doc.consultationFee}
                      </td>
                      <td className="px-4 py-3">
                        <span
                          className={`px-2 py-1 text-xs rounded-full ${
                            doc.active
                              ? "bg-green-100 text-green-700"
                              : "bg-red-100 text-red-700"
                          }`}
                        >
                          {doc.active ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-right flex items-center justify-end gap-2">
                        <button
                          onClick={() => {
                            setSelectedDoctor(doc);
                            setIsModalOpen(true);
                          }}
                          className="p-1.5 rounded-lg border text-blue-600 border-blue-200 hover:bg-blue-50 transition-colors"
                          title="Edit"
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          onClick={() => handleToggleStatus(doc)}
                          className={`p-1.5 rounded-lg border transition-colors ${
                            doc.active
                              ? "text-red-600 border-red-200 hover:bg-red-50"
                              : "text-green-600 border-green-200 hover:bg-green-50"
                          }`}
                          title={doc.active ? "Deactivate" : "Activate"}
                        >
                          {doc.active ? (
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

      {/* Add/Edit Modal */}
      <AddDoctorModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onRefresh={fetchDoctors}
        doctor={selectedDoctor}
      />
    </Layout>
  );
};

export default DoctorsManagement;
