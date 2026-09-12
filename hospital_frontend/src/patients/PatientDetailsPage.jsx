import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Layout from "../layout/Layout";
import PatientDetails from "../patients/PatientDetails";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";

const PatientDetailsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [patient, setPatient] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPatient();
  }, [id]);

  const fetchPatient = async () => {
    setLoading(true);
    try {
      const response = await patientApi.getPatientById(id);
      setPatient(response.data.data);
    } catch (error) {
      console.error("Error fetching patient:", error);
      toast.error("Failed to load patient details");
      navigate("/patients");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (patientId) => {
    navigate(`/patients/edit/${patientId}`);
  };

  const handleBack = () => {
    navigate("/patients");
  };

  return (
    <Layout>
      <PatientDetails
        patient={patient}
        loading={loading}
        onEdit={handleEdit}
        onBack={handleBack}
      />
    </Layout>
  );
};

export default PatientDetailsPage;
