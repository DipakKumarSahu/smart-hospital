import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  Eye,
  Edit,
  Trash2,
  User,
  Mail,
  Phone,
  Search,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";
import Card from "../components/common/Card";
import Button from "../components/common/Button";
import Spinner from "../components/common/Spinner";

const PatientList = ({
  patients,
  loading,
  onEdit,
  onDelete,
  onView,
  onSearch,
  page,
  totalPages,
  onPageChange,
}) => {
  const [searchTerm, setSearchTerm] = useState("");

  const handleSearch = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    if (onSearch) {
      onSearch(value);
    }
  };

  const getStatusBadge = (active) => {
    return active ? (
      <span className="px-2.5 py-1 text-xs font-medium rounded-full bg-green-100 text-green-700 border border-green-200">
        Active
      </span>
    ) : (
      <span className="px-2.5 py-1 text-xs font-medium rounded-full bg-red-100 text-red-700 border border-red-200">
        Inactive
      </span>
    );
  };

  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {[1, 2, 3, 4, 5, 6].map((n) => (
          <Card key={n} className="animate-pulse">
            <div className="flex items-start gap-4">
              <div className="w-16 h-16 rounded-full bg-gray-200"></div>
              <div className="flex-1 space-y-2">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                <div className="h-3 bg-gray-200 rounded w-2/3"></div>
              </div>
            </div>
          </Card>
        ))}
      </div>
    );
  }

  if (patients.length === 0) {
    return (
      <Card className="text-center py-12">
        <User className="w-16 h-16 text-gray-300 mx-auto mb-4" />
        <h3 className="text-lg font-semibold text-gray-600">
          No Patients Found
        </h3>
        <p className="text-gray-400 mt-2">
          Try adjusting your search or filters
        </p>
      </Card>
    );
  }

  return (
    <div className="space-y-4">
      {/* Search Bar */}
      <div className="relative">
        <Search
          className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
          size={18}
        />
        <input
          type="text"
          placeholder="Search patients by name or email..."
          value={searchTerm}
          onChange={handleSearch}
          className="w-full pl-10 pr-4 py-2.5 rounded-lg border border-gray-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500 transition-all duration-200"
        />
      </div>

      {/* Patient Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <AnimatePresence>
          {patients.map((patient, index) => (
            <motion.div
              key={patient.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, scale: 0.9 }}
              transition={{ delay: index * 0.05 }}
              whileHover={{ y: -4 }}
            >
              <Card hover className="relative group overflow-hidden">
                <div className="absolute top-0 right-0 w-20 h-20 bg-gradient-to-br from-primary-100 to-primary-200 rounded-bl-full opacity-30" />

                <div className="flex items-start gap-4 relative">
                  {/* Avatar */}
                  <div className="w-16 h-16 rounded-full bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center text-white font-bold text-xl shadow-md flex-shrink-0">
                    {patient.fullName?.charAt(0) || "P"}
                  </div>

                  {/* Info */}
                  <div className="flex-1 min-w-0">
                    <h3 className="font-semibold text-gray-800 truncate">
                      {patient.fullName}
                    </h3>
                    <p className="text-sm text-gray-500 flex items-center gap-1 truncate">
                      <Mail size={14} className="flex-shrink-0" />
                      {patient.email || "No email"}
                    </p>
                    <p className="text-sm text-gray-500 flex items-center gap-1 truncate">
                      <Phone size={14} className="flex-shrink-0" />
                      {patient.phoneNumber || "No phone"}
                    </p>
                    <div className="flex items-center gap-2 mt-2 flex-wrap">
                      <span className="px-2 py-0.5 text-xs font-medium rounded-full bg-primary-50 text-primary-700 border border-primary-200">
                        {patient.patientCode || "N/A"}
                      </span>
                      {getStatusBadge(patient.active)}
                    </div>
                  </div>
                </div>

                {/* Action Buttons */}
                <div className="absolute bottom-3 right-3 flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => onView && onView(patient.id)}
                    className="p-1.5 hover:bg-primary-50 hover:text-primary-600"
                    title="View Details"
                  >
                    <Eye size={16} />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => onEdit && onEdit(patient.id)}
                    className="p-1.5 hover:bg-blue-50 hover:text-blue-600"
                    title="Edit"
                  >
                    <Edit size={16} />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => onDelete && onDelete(patient.id)}
                    className="p-1.5 hover:bg-red-50 hover:text-red-600"
                    title="Delete"
                  >
                    <Trash2 size={16} />
                  </Button>
                </div>
              </Card>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-center gap-2 mt-6">
          <Button
            variant="outline"
            size="sm"
            disabled={page === 0}
            onClick={() => onPageChange && onPageChange(page - 1)}
            className="px-3"
          >
            <ChevronLeft size={16} />
          </Button>

          <div className="flex items-center gap-1">
            {[...Array(Math.min(totalPages, 5))].map((_, i) => {
              let pageNum = i;
              if (totalPages > 5 && page > 2) {
                pageNum = page - 2 + i;
                if (pageNum >= totalPages) {
                  pageNum = totalPages - 5 + i;
                }
              }
              if (pageNum >= 0 && pageNum < totalPages) {
                return (
                  <button
                    key={pageNum}
                    onClick={() => onPageChange && onPageChange(pageNum)}
                    className={`
                      px-3 py-1.5 rounded-lg text-sm font-medium transition-all duration-200
                      ${
                        page === pageNum
                          ? "bg-primary-500 text-white shadow-md"
                          : "hover:bg-gray-100 text-gray-600"
                      }
                    `}
                  >
                    {pageNum + 1}
                  </button>
                );
              }
              return null;
            })}
          </div>

          <Button
            variant="outline"
            size="sm"
            disabled={page >= totalPages - 1}
            onClick={() => onPageChange && onPageChange(page + 1)}
            className="px-3"
          >
            <ChevronRight size={16} />
          </Button>
        </div>
      )}
    </div>
  );
};

export default PatientList;
