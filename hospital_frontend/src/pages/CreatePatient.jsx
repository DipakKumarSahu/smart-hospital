import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Layout from "../layout/Layout";
import PatientForm from "../patients/PatientForm";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import { UserPlus } from "lucide-react";

const CreatePatient = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      // Note: This assumes you have a separate user creation endpoint
      // and then patient creation with userId
      const userData = {
        fullName: data.fullName,
        email: data.email,
        phoneNumber: data.phoneNumber,
        // You'll need to add proper user creation logic here
      };

      // For now, we'll just show a toast
      toast.info("Patient creation requires user creation first");
      setLoading(false);
    } catch (error) {
      console.error("Error creating patient:", error);
      setLoading(false);
    }
  };

  return (
    <Layout>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-6"
      >
        <div className="flex items-center gap-3">
          <UserPlus className="w-8 h-8 text-primary-500" />
          <div>
            <h1 className="text-3xl font-bold">Create New Patient</h1>
            <p className="text-gray-500 mt-1">
              Add a new patient to the hospital system
            </p>
          </div>
        </div>

        <PatientForm onSubmit={onSubmit} loading={loading} isEdit={false} />
      </motion.div>
    </Layout>
  );
};

export default CreatePatient;
