import { useState, useCallback } from "react";
import { ContratoRequest, ContratoResponse } from "../types/contrato";
import { contratoService } from "../services/api";
import toast from "react-hot-toast";

export const useContratos = () => {
  const [loading, setLoading] = useState(false);
  const [contratos, setContratos] = useState<ContratoResponse[]>([]);

  const criarContrato = useCallback(async (contrato: ContratoRequest) => {
    setLoading(true);
    try {
      // garantia que o valor vai ser number
      const contratoParaEnviar = {
        ...contrato,
        valor: Number(contrato.valor),
      };

      console.log("Enviando contrato:", contratoParaEnviar);


      //tratamento de erro
      const response = await contratoService.criarContrato(contratoParaEnviar);
      toast.success("Contrato criado com sucesso!");
      return response;
    } catch (error: any) {
      console.error("Erro ao criar contrato:", error);

      if (error.response?.status === 400) {
        toast.error("Dados inválidos. Verifique os campos preenchidos.");
      } else if (error.response?.status === 500) {
        toast.error("Erro no servidor. Tente novamente.");
      } else if (error.code === "ECONNABORTED") {
        toast.error("Tempo de conexão esgotado. Verifique sua internet.");
      } else {
        toast.error("Erro ao criar contrato");
      }

      throw error;
    } finally {
      setLoading(false);
    }
  }, []);

  const listarContratos = useCallback(async () => { // metodo pra busca contratos
    setLoading(true);
    try {
      const response = await contratoService.listarContratos();
      setContratos(response);
      return response;
    } catch (error: any) {
      console.error("Erro ao carregar contratos:", error);

      if (error.response?.status === 404) {
        toast.error("Nenhum contrato encontrado.");
      } else {
        toast.error("Erro ao carregar contratos");
      }

      throw error;
    } finally {
      setLoading(false);
    }
  }, []);

  const baixarPdf = useCallback(async (id: number) => {
    try {
      const blob = await contratoService.baixarPdf(id);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `contrato-${id}.pdf`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
      toast.success("PDF baixado com sucesso!");
    } catch (error: any) {
      console.error("Erro ao baixar PDF:", error);

      if (error.response?.status === 404) {
        toast.error("PDF não encontrado para este contrato.");
      } else {
        toast.error("Erro ao baixar PDF");
      }

      throw error;
    }
  }, []);

  return { // retorno do hook
    loading,
    contratos,
    criarContrato,
    listarContratos,
    baixarPdf,
  };
};
