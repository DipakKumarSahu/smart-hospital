import React, { useState, useEffect } from "react";
import Layout from "../layout/Layout";
import PatientProfile from "../patients/PatientProfile";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";
import { useAuth } from "../context/AuthContext";

const Profile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    setLoading(true);
    try {
      const response = await patientApi.getMyProfile();
      console.log("Profile response:", response.data);
      setProfile(response.data.data);
    } catch (error) {
      console.error("Error fetching profile:", error);
      toast.error("Failed to load profile");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async (data) => {
    try {
      // Only send the fields that UpdateMyProfileRequest expects
      const updateData = {
        phoneNumber: data.phoneNumber,
        address: data.address,
        emergencyContactName: data.emergencyContactName,
        emergencyContactNumber: data.emergencyContactNumber,
        profileImageUrl: data.profileImageUrl || "",
      };

      const response = await patientApi.updateMyProfile(updateData);
      setProfile(response.data.data);
      toast.success("Profile updated successfully!");
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating profile:", error);
      toast.error("Failed to update profile");
      throw error;
    }
  };

  return (
    <Layout>
      <div className="py-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-6">My Profile</h1>
        <PatientProfile
          profile={profile}
          loading={loading}
          onUpdate={handleUpdate}
          isEditing={isEditing}
          setIsEditing={setIsEditing}
        />
      </div>
    </Layout>
  );
};

export default Profile;
