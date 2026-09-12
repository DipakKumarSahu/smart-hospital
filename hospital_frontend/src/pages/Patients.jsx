import React from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import PatientList from "../patients/PatientList";
import Button from "../components/common/Button";
import { usePatients } from "../hooks/usePatients";
import { Plus, Users } from "lucide-react";

const Patients = () => {
  const navigate = useNavigate();
  const {
    patients,
    loading,
    page,
    totalPages,
    searchPatients,
    changePage,
    deletePatient,
    refresh,
  } = usePatients();

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this patient?")) {
      await deletePatient(id);
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
          >
            <h1 className="text-3xl font-bold flex items-center gap-3">
              <Users className="w-8 h-8 text-primary-500" />
              Patients
            </h1>
            <p className="text-gray-500 mt-1">
              Manage all patients in the hospital
            </p>
          </motion.div>
          <motion.div
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
          >
            <Button
              onClick={() => navigate("/patients/create")}
              className="w-full sm:w-auto"
            >
              <Plus size={18} />
              Add Patient
            </Button>
          </motion.div>
        </div>

        {/* Patient List */}
        <PatientList
          patients={patients}
          loading={loading}
          onView={(id) => navigate(`/patients/${id}`)}
          onEdit={(id) => navigate(`/patients/edit/${id}`)}
          onDelete={handleDelete}
          onSearch={searchPatients}
          page={page}
          totalPages={totalPages}
          onPageChange={changePage}
        />
      </div>
    </Layout>
  );
};

export default Patients;
