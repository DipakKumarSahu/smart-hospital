import React from "react";

export const SkeletonCard = () => (
  <div className="bg-white rounded-lg border border-slate-200 p-4 animate-pulse">
    <div className="flex items-center gap-3">
      <div className="w-10 h-10 rounded-full bg-slate-200" />
      <div className="flex-1 space-y-2">
        <div className="h-4 bg-slate-200 rounded w-1/3" />
        <div className="h-3 bg-slate-200 rounded w-1/2" />
      </div>
    </div>
  </div>
);

export const SkeletonList = ({ count = 4 }) => (
  <div className="space-y-3">
    {Array.from({ length: count }).map((_, i) => (
      <SkeletonCard key={i} />
    ))}
  </div>
);

export const SkeletonSlots = () => (
  <div className="grid grid-cols-3 sm:grid-cols-4 gap-2">
    {Array.from({ length: 8 }).map((_, i) => (
      <div key={i} className="h-10 bg-slate-200 rounded-lg animate-pulse" />
    ))}
  </div>
);
