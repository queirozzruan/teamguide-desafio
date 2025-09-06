import React, { useState, useEffect } from "react";
import { Toaster } from "react-hot-toast";
import Header from "./components/Header";
import ContratoForm from "./components/ContratoForm";
import ContratoList from "./components/ContratoList";
import { useContratos } from "./hooks/useContratos";
import { ContratoRequest } from "./types/contrato";

function App() {
  const [currentView, setCurrentView] = useState<"form" | "list">("form");
  const { loading, contratos, criarContrato, listarContratos, baixarPdf } =
    useContratos();

  useEffect(() => {
    if (currentView === "list") { // form ou lista
      listarContratos();
    }
  }, [currentView, listarContratos]);

  const handleCreateContrato = async (contrato: ContratoRequest) => {
    await criarContrato(contrato);
    setCurrentView("list");
  };

  const handleViewChange = (view: "form" | "list") => {
    setCurrentView(view);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Toaster position="top-right" />

      <Header currentView={currentView} onViewChange={handleViewChange} />

      <main className="py-8">
        {currentView === "form" ? (
          <ContratoForm onSubmit={handleCreateContrato} loading={loading} />
        ) : (
          <ContratoList
            contratos={contratos}
            onBaixarPdf={baixarPdf}
            loading={loading}
          />
        )}
      </main>
    </div>
  );
}

export default App;
