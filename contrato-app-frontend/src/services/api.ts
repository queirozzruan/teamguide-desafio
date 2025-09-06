import axios from "axios";
import { ContratoRequest, ContratoResponse } from "../types/contrato";

const API_BASE_URL = "http://localhost:8080";


// instacia do axios
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 30000,
});

// tratamento de erros
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.error("Não autorizado");
    } else if (error.response?.status === 404) {
      console.error("Recurso não encontrado");
    } else if (error.code === "ECONNABORTED") {
      console.error("Timeout na requisição");
    }
    return Promise.reject(error);
  }
);

// servico que cria o novo controto
export const contratoService = {
  criarContrato: async (
    contrato: ContratoRequest
  ): Promise<ContratoResponse> => {
    const response = await api.post<ContratoResponse>("/contratos", contrato);
    return response.data;
  },

  listarContratos: async (): Promise<ContratoResponse[]> => {
    const response = await api.get<ContratoResponse[]>("/contratos");
    return response.data;
  },

  buscarContratoPorId: async (id: number): Promise<ContratoResponse> => {
    const response = await api.get<ContratoResponse>(`/contratos/${id}`);
    return response.data;
  },

  baixarPdf: async (id: number): Promise<Blob> => {
    const response = await api.get(`/contratos/${id}/pdf`, {
      responseType: "blob",
    });
    return response.data;
  },
};

export default api;
