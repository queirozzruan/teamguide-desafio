export const validateEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const validateCPF = (cpf: string): boolean => {
  const cleaned = cpf.replace(/\D/g, "");
  if (cleaned.length !== 11) return false;

  // validacao do cpf
  if (/^(\d)\1{10}$/.test(cleaned)) return false;

  return true;
};

export const validateCNPJ = (cnpj: string): boolean => {
  const cleaned = cnpj.replace(/\D/g, "");
  return cleaned.length === 14;
};

export const validateRequired = (value: string): boolean => {
  return value.trim().length > 0;
};

export const validateNumber = (value: number): boolean => {
  return value > 0;
};
