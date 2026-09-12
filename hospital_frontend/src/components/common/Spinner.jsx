import React from "react";

const Spinner = ({ size = "md", color = "primary", className = "" }) => {
  const sizes = {
    sm: "w-4 h-4",
    md: "w-8 h-8",
    lg: "w-12 h-12",
    xl: "w-16 h-16",
  };

  const colors = {
    primary: "border-primary-500",
    white: "border-white",
    gray: "border-gray-500",
    success: "border-green-500",
    danger: "border-red-500",
  };

  return (
    <div className={`flex items-center justify-center ${className}`}>
      <div
        className={`
          ${sizes[size]} 
          ${colors[color]} 
          border-4 
          border-t-transparent 
          rounded-full 
          animate-spin
        `}
      />
    </div>
  );
};

export default Spinner;
