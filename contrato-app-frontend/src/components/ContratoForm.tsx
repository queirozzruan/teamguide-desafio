import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { ContratoRequest } from "../types/contrato";
import {
  cpfMask,
  cnpjMask,
  currencyMask,
  removeCurrencyMask,
} from "../utils/mask";
import LoadingSpinner from "./LoadingSpinner";
import {
  FileText,
  User,
  UserCheck,
  Calendar,
  DollarSign,
  MapPin,
  FileEdit,
} from "lucide-react";

const contratoSchema = z.object({
  contratanteNome: z.string().min(1, "Nome é obrigatório"),
  contratanteCpfCnpj: z.string().min(1, "CPF/CNPJ é obrigatório"),
  contratanteEmail: z.string().email("Email inválido"),
  contratadoNome: z.string().min(1, "Nome é obrigatório"),
  contratadoCpfCnpj: z.string().min(1, "CPF/CNPJ é obrigatório"),
  contratadoEmail: z.string().email("Email inválido"),
  valor: z.string().min(1, "Valor é obrigatório"),
  dataInicio: z.string().min(1, "Data de início é obrigatória"),
  prazoMeses: z.number().min(1, "Prazo deve ser maior que zero"),
  cidade: z.string().min(1, "Cidade é obrigatória"),
  uf: z.string().length(2, "UF é obrigatória"),
  descricaoServico: z.string().min(1, "Descrição do serviço é obrigatória"),
});

type ContratoFormData = z.infer<typeof contratoSchema>;

interface ContratoFormProps {
  onSubmit: (data: ContratoRequest) => Promise<void>;
  loading?: boolean;
}

const ContratoForm: React.FC<ContratoFormProps> = ({
  onSubmit,
  loading = false,
}) => {
  const {
    register, //registra os inputs
    handleSubmit, // envio do form
    setValue, // define os valores
    watch,
    formState: { errors },
  } = useForm<ContratoFormData>({
    resolver: zodResolver(contratoSchema), //validacao com o zod
    defaultValues: {
      prazoMeses: 0,
    },
  });

  const [maskedValues, setMaskedValues] = useState({
    contratanteCpfCnpj: "",
    contratadoCpfCnpj: "",
    valor: "",
  });

  const handleCpfCnpjChange = (
    field: "contratanteCpfCnpj" | "contratadoCpfCnpj",
    value: string
  ) => {
    const maskedValue = value.length <= 14 ? cpfMask(value) : cnpjMask(value);
    setMaskedValues((prev) => ({ ...prev, [field]: maskedValue }));
    setValue(field, value.replace(/\D/g, ""), { shouldValidate: true });
  };

  const handleValorChange = (value: string) => {
    const maskedValue = currencyMask(value);
    setMaskedValues((prev) => ({ ...prev, valor: maskedValue }));
    setValue("valor", value.replace(/\D/g, ""), { shouldValidate: true });
  };

  const onFormSubmit = async (data: ContratoFormData) => {
    const contratoData: ContratoRequest = {
      ...data,
      valor: Number(removeCurrencyMask(maskedValues.valor)),
      prazoMeses: Number(data.prazoMeses),
    };

    await onSubmit(contratoData);
  };

  if (loading) {
    return <LoadingSpinner text="Criando contrato..." />;
  }

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-sm border">
        <div className="px-6 py-4 border-b">
          <h2 className="text-2xl font-semibold text-gray-900 flex items-center space-x-2">
            <FileText className="h-6 w-6" />
            <span>Novo Contrato</span>
          </h2>
        </div>

        <form onSubmit={handleSubmit(onFormSubmit)} className="p-6 space-y-6">
        
          <div className="bg-gray-50 p-6 rounded-lg">
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center space-x-2">
              <User className="h-5 w-5" />
              <span>Dados do Contratante</span>
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Nome Completo *
                </label>
                <input
                  {...register("contratanteNome")}
                  className="input-field"
                  placeholder="Nome do contratante"
                />
                {errors.contratanteNome && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratanteNome.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  CPF/CNPJ *
                </label>
                <input
                  value={maskedValues.contratanteCpfCnpj}
                  onChange={(e) =>
                    handleCpfCnpjChange("contratanteCpfCnpj", e.target.value)
                  }
                  className="input-field"
                  placeholder="000.000.000-00"
                  maxLength={18}
                />
                {errors.contratanteCpfCnpj && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratanteCpfCnpj.message}
                  </p>
                )}
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Email *
                </label>
                <input
                  {...register("contratanteEmail")}
                  type="email"
                  className="input-field"
                  placeholder="email@exemplo.com"
                />
                {errors.contratanteEmail && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratanteEmail.message}
                  </p>
                )}
              </div>
            </div>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg">
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center space-x-2">
              <UserCheck className="h-5 w-5" />
              <span>Dados do Contratado</span>
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Nome Completo *
                </label>
                <input
                  {...register("contratadoNome")}
                  className="input-field"
                  placeholder="Nome do contratado"
                />
                {errors.contratadoNome && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratadoNome.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  CPF/CNPJ *
                </label>
                <input
                  value={maskedValues.contratadoCpfCnpj}
                  onChange={(e) =>
                    handleCpfCnpjChange("contratadoCpfCnpj", e.target.value)
                  }
                  className="input-field"
                  placeholder="000.000.000-00"
                  maxLength={18}
                />
                {errors.contratadoCpfCnpj && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratadoCpfCnpj.message}
                  </p>
                )}
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Email *
                </label>
                <input
                  {...register("contratadoEmail")}
                  type="email"
                  className="input-field"
                  placeholder="email@exemplo.com"
                />
                {errors.contratadoEmail && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.contratadoEmail.message}
                  </p>
                )}
              </div>
            </div>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg">
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center space-x-2">
              <FileEdit className="h-5 w-5" />
              <span>Dados do Contrato</span>
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Valor (R$) *
                </label>
                <input
                  value={maskedValues.valor}
                  onChange={(e) => handleValorChange(e.target.value)}
                  className="input-field"
                  placeholder="R$ 0,00"
                />
                {errors.valor && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.valor.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Data de Início *
                </label>
                <input
                  {...register("dataInicio")}
                  type="date"
                  className="input-field"
                />
                {errors.dataInicio && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.dataInicio.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Prazo (meses) *
                </label>
                <input
                  {...register("prazoMeses", { valueAsNumber: true })}
                  type="number"
                  min="1"
                  className="input-field"
                  placeholder="12"
                />
                {errors.prazoMeses && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.prazoMeses.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Cidade *
                </label>
                <input
                  {...register("cidade")}
                  className="input-field"
                  placeholder="São Paulo"
                />
                {errors.cidade && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.cidade.message}
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  UF *
                </label>
                <select {...register("uf")} className="input-field">
                  <option value="">Selecione</option>
                  <option value="AC">AC</option>
                  <option value="AL">AL</option>
                  <option value="AM">AM</option>
                  <option value="AP">AP</option>
                  <option value="BA">BA</option>
                  <option value="CE">CE</option>
                  <option value="DF">DF</option>
                  <option value="ES">ES</option>
                  <option value="GO">GO</option>
                  <option value="MA">MA</option>
                  <option value="MG">MG</option>
                  <option value="MS">MS</option>
                  <option value="MT">MT</option>
                  <option value="PA">PA</option>
                  <option value="PB">PB</option>
                  <option value="PE">PE</option>
                  <option value="PI">PI</option>
                  <option value="PR">PR</option>
                  <option value="RJ">RJ</option>
                  <option value="RN">RN</option>
                  <option value="RO">RO</option>
                  <option value="RR">RR</option>
                  <option value="RS">RS</option>
                  <option value="SC">SC</option>
                  <option value="SE">SE</option>
                  <option value="SP">SP</option>
                  <option value="TO">TO</option>
                </select>
                {errors.uf && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.uf.message}
                  </p>
                )}
              </div>

              <div className="md:col-span-3">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Descrição do Serviço *
                </label>
                <textarea
                  {...register("descricaoServico")}
                  rows={4}
                  className="input-field"
                  placeholder="Descreva os serviços a serem prestados..."
                />
                {errors.descricaoServico && (
                  <p className="mt-1 text-sm text-red-600">
                    {errors.descricaoServico.message}
                  </p>
                )}
              </div>
            </div>
          </div>

          <div className="flex justify-end">
            <button
              type="submit"
              className="btn-primary flex items-center space-x-2"
            >
              <FileText size={20} />
              <span>Criar Contrato e Enviar para Assinatura</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ContratoForm;
