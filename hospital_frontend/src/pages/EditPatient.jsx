import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import PatientForm from "../patients/PatientForm";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import { Edit } from "lucide-react";

const EditPatient = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [patient, setPatient] = useState(null);
  const [fetchLoading, setFetchLoading] = useState(true);

  useEffect(() => {
    fetchPatient();
  }, [id]);

  const fetchPatient = async () => {
    try {
      const response = await patientApi.getPatientById(id);
      setPatient(response.data.data);
    } catch (error) {
      console.error("Error fetching patient:", error);
      toast.error("Failed to load patient data");
      navigate("/patients");
    } finally {
      setFetchLoading(false);
    }
  };

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      await patientApi.updatePatient(id, data);
      toast.success("Patient updated successfully");
      navigate(`/patients/${id}`);
    } catch (error) {
      console.error("Error updating patient:", error);
    } finally {
      setLoading(false);
    }
  };

  if (fetchLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center h-64">
          <div className="spinner w-12 h-12"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-6"
      >
        <div className="flex items-center gap-3">
          <Edit className="w-8 h-8 text-primary-500" />
          <div>
            <h1 className="text-3xl font-bold">Edit Patient</h1>
            <p className="text-gray-500 mt-1">
              Update information for {patient?.fullName}
            </p>
          </div>
        </div>

        <PatientForm
          patient={patient}
          onSubmit={onSubmit}
          loading={loading}
          isEdit={true}
        />
      </motion.div>
    </Layout>
  );
};

export default EditPatient;
