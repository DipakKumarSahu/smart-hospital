import React, { forwardRef } from "react";
import { motion } from "framer-motion";

const Input = forwardRef(
  ({ label, error, className = "", icon, ...props }, ref) => {
    return (
      <motion.div
        className="space-y-1"
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.3 }}
      >
        {label && (
          <label className="block text-sm font-medium text-gray-700 mb-1">
            {label}
          </label>
        )}
        <div className="relative">
          {icon && (
            <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
              {icon}
            </div>
          )}
          <input
            ref={ref}
            className={`
            w-full px-4 py-2.5 rounded-lg border border-gray-300
            bg-white/50 backdrop-blur-sm
            focus:ring-2 focus:ring-primary-500 focus:border-primary-500
            transition-all duration-200
            placeholder:text-gray-400
            disabled:bg-gray-50 disabled:cursor-not-allowed
            ${error ? "border-red-500 ring-2 ring-red-500/20" : "hover:border-gray-400"}
            ${icon ? "pl-10" : ""}
            ${className}
          `}
            {...props}
          />
        </div>
        {error && (
          <motion.p
            className="text-sm text-red-500 mt-1"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
          >
            {error}
          </motion.p>
        )}
      </motion.div>
    );
  },
);

Input.displayName = "Input";

export default Input;
