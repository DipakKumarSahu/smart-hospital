import React from "react";
import { AlertCircle } from "lucide-react";

const EmptyState = ({
  icon: Icon = AlertCircle,
  title,
  description,
  action,
}) => (
  <div className="flex flex-col items-center justify-center py-12 px-4 text-center">
    <div className="w-14 h-14 rounded-full bg-slate-100 flex items-center justify-center mb-4">
      <Icon className="w-6 h-6 text-slate-400" />
    </div>
    <h3 className="text-base font-semibold text-slate-700">{title}</h3>
    {description && (
      <p className="text-sm text-slate-500 mt-1.5 max-w-sm">{description}</p>
    )}
    {action && <div className="mt-5">{action}</div>}
  </div>
);

export default EmptyState;
