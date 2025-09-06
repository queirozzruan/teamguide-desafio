import React from "react";
import { Loader2 } from "lucide-react";

interface LoadingSpinnerProps {
  size?: number;
  text?: string;
}

const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  size = 24,
  text = "Processando...",
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-8">
      <Loader2 className="animate-spin text-primary-600" size={size} />
      {text && <p className="mt-2 text-gray-600">{text}</p>}
    </div>
  );
};

export default LoadingSpinner;
