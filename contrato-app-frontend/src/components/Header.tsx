import React from "react";
import { FileText, Plus, List } from "lucide-react";

interface HeaderProps {
  currentView: "form" | "list";
  onViewChange: (view: "form" | "list") => void;
}

// nav entre telas

const Header: React.FC<HeaderProps> = ({ currentView, onViewChange }) => {
  return (
    <header className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-3">
            <FileText className="h-8 w-8 text-primary-600" />
            <h1 className="text-2xl font-bold text-gray-900">
              Team Guide - Sistema de Contratos
            </h1>
          </div>

          <nav className="flex space-x-2">
            <button
              onClick={() => onViewChange("form")}
              className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors duration-200 ${
                currentView === "form"
                  ? "bg-primary-100 text-primary-700"
                  : "text-gray-600 hover:text-gray-900 hover:bg-gray-100"
              }`}
            >
              <Plus size={20} />
              <span>Novo Contrato</span>
            </button>

            <button
              onClick={() => onViewChange("list")}
              className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors duration-200 ${
                currentView === "list"
                  ? "bg-primary-100 text-primary-700"
                  : "text-gray-600 hover:text-gray-900 hover:bg-gray-100"
              }`}
            >
              <List size={20} />
              <span>Meus Contratos</span>
            </button>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;
