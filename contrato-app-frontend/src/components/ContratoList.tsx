import React, { useEffect } from "react";
import { ContratoResponse } from "../types/contrato";
import { FileText, Download, ExternalLink } from "lucide-react";

interface ContratoListProps {
  contratos: ContratoResponse[];
  onBaixarPdf: (id: number) => void;
  loading?: boolean;
}

const ContratoList: React.FC<ContratoListProps> = ({
  contratos,
  onBaixarPdf,
  loading = false,
}) => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case "AGUARDANDO_ASSINATURA":
        return "bg-yellow-100 text-yellow-800";
      case "ASSINADO":
        return "bg-green-100 text-green-800";
      case "CRIADO":
        return "bg-blue-100 text-blue-800";
      case "CANCELADO":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(value);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("pt-BR");
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto p-6">
        <div className="bg-white rounded-lg shadow-sm border p-8">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/3 mb-6"></div>
            <div className="space-y-3">
              {[...Array(5)].map((_, i) => (
                <div key={i} className="h-12 bg-gray-200 rounded"></div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-sm border">
        <div className="px-6 py-4 border-b">
          <h2 className="text-2xl font-semibold text-gray-900 flex items-center space-x-2">
            <FileText className="h-6 w-6" />
            <span>Meus Contratos</span>
          </h2>
        </div>

        <div className="p-6">
          {contratos.length === 0 ? (
            <div className="text-center py-12">
              <FileText className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">
                Nenhum contrato encontrado
              </h3>
              <p className="text-gray-600">
                Comece criando seu primeiro contrato!
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      ID
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Contratante
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Contratado
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Valor
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Status
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Criado em
                    </th>
                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {contratos.map((contrato) => (
                    <tr key={contrato.id} className="border-b hover:bg-gray-50">
                      <td className="px-4 py-3 text-sm text-gray-900">
                        {contrato.id}
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-900">
                        {contrato.contratanteNome}
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-900">
                        {contrato.contratadoNome}
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-900">
                        {formatCurrency(contrato.valor)}
                      </td>
                      <td className="px-4 py-3">
                        <span
                          className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(
                            contrato.statusAssinatura
                          )}`}
                        >
                          {contrato.statusAssinatura}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-sm text-gray-900">
                        {formatDate(contrato.criadoEm)}
                      </td>
                      <td className="px-4 py-3">
                        <div className="flex space-x-2">
                          <button
                            onClick={() => onBaixarPdf(contrato.id)}
                            className="flex items-center space-x-1 text-sm text-blue-600 hover:text-blue-800"
                          >
                            <Download size={16} />
                            <span>PDF</span>
                          </button>
                          {contrato.clicksignDocumentKey && (
                            <a
                              href={`https://sandbox.clicksign.com/documents/${contrato.clicksignDocumentKey}`}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="flex items-center space-x-1 text-sm text-green-600 hover:text-green-800"
                            >
                              <ExternalLink size={16} />
                              <span>Clicksign</span>
                            </a>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ContratoList;
