import React from "react";
import { Check } from "lucide-react";

const StepIndicator = ({ currentStep, steps }) => {
  return (
    <div className="flex items-center justify-center gap-2 mb-8">
      {steps.map((step, index) => {
        const stepNum = index + 1;
        const isActive = stepNum === currentStep;
        const isCompleted = stepNum < currentStep;

        return (
          <React.Fragment key={stepNum}>
            <div className="flex items-center gap-2">
              <div
                className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium transition-colors ${
                  isCompleted
                    ? "bg-[#0284c7] text-white"
                    : isActive
                      ? "bg-[#0284c7] text-white"
                      : "bg-slate-200 text-slate-500"
                }`}
              >
                {isCompleted ? <Check size={14} /> : stepNum}
              </div>
              <span
                className={`text-sm font-medium hidden sm:block ${
                  isActive || isCompleted ? "text-slate-800" : "text-slate-400"
                }`}
              >
                {step.label}
              </span>
            </div>

            {index < steps.length - 1 && (
              <div
                className={`w-8 sm:w-12 h-px ${
                  isCompleted ? "bg-[#0284c7]" : "bg-slate-200"
                }`}
              />
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default StepIndicator;
