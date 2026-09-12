import { useState, useEffect, useCallback } from "react";
import { patientApi } from "../api/patientApi";
import { toast } from "react-toastify";

export const usePatients = (initialPage = 0, initialSize = 10) => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(initialPage);
  const [size, setSize] = useState(initialSize);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [keyword, setKeyword] = useState("");

  const fetchPatients = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await patientApi.getPatients({
        keyword: keyword || null,
        page: page,
        size: size,
      });
      const data = response.data.data;
      setPatients(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
    } catch (err) {
      setError(err.message || "Failed to fetch patients");
      toast.error("Failed to load patients");
    } finally {
      setLoading(false);
    }
  }, [page, size, keyword]);

  useEffect(() => {
    fetchPatients();
  }, [fetchPatients]);

  const searchPatients = useCallback((searchTerm) => {
    setKeyword(searchTerm);
    setPage(0);
  }, []);

  const changePage = useCallback((newPage) => {
    setPage(newPage);
  }, []);

  const changeSize = useCallback((newSize) => {
    setSize(newSize);
    setPage(0);
  }, []);

  const refresh = useCallback(() => {
    fetchPatients();
  }, [fetchPatients]);

  const deletePatient = useCallback(
    async (id) => {
      try {
        await patientApi.deletePatient(id);
        toast.success("Patient deleted successfully");
        refresh();
        return true;
      } catch (error) {
        toast.error("Failed to delete patient");
        return false;
      }
    },
    [refresh],
  );

  return {
    patients,
    loading,
    error,
    page,
    totalPages,
    totalElements,
    keyword,
    searchPatients,
    changePage,
    changeSize,
    refresh,
    deletePatient,
  };
};
