export interface ContratoRequest {
  contratanteNome: string;
  contratanteCpfCnpj: string;
  contratanteEmail: string;
  contratadoNome: string;
  contratadoCpfCnpj: string;
  contratadoEmail: string;
  valor: number;
  dataInicio: string;
  prazoMeses: number;
  cidade: string;
  uf: string;
  descricaoServico: string;
}

export interface ContratoResponse {
  id: number;
  contratanteNome: string;
  contratadoNome: string;
  valor: number;
  dataInicio: string;
  statusAssinatura: string;
  clicksignDocumentKey: string;
  criadoEm: string;
}

export type StatusAssinatura =
  | "CRIADO"
  | "AGUARDANDO_ASSINATURA"
  | "ASSINADO"
  | "CANCELADO";
