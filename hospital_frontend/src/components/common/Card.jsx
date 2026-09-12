import React from "react";
import { motion } from "framer-motion";

const Card = ({
  children,
  className = "",
  hover = false,
  glass = false,
  ...props
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className={`
        rounded-xl p-6
        ${glass ? "glass-effect" : "bg-white"}
        shadow-md
        ${hover ? "hover:shadow-xl hover:-translate-y-1 transition-all duration-300" : ""}
        ${className}
      `}
      {...props}
    >
      {children}
    </motion.div>
  );
};

export default Card;
