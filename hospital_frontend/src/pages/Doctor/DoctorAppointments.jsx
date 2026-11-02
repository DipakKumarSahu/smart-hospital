import React from "react";
import Layout from "../../layout/Layout";
import Card from "../../components/common/Card";

const DoctorAppointments = () => {
  return (
    <Layout>
      <Card className="text-center py-12">
        <h2 className="text-xl font-semibold text-slate-800">
          My Appointments
        </h2>
        <p className="text-slate-500 mt-2">Coming in next update...</p>
      </Card>
    </Layout>
  );
};
export default DoctorAppointments;
