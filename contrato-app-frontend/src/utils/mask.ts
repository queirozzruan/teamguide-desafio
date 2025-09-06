export const cpfMask = (value: string): string => {
  return value
    .replace(/\D/g, "")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})/, "$1-$2")
    .replace(/(-\d{2})\d+?$/, "$1");
};

export const cnpjMask = (value: string): string => {
  return value
    .replace(/\D/g, "")
    .replace(/(\d{2})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1/$2")
    .replace(/(\d{4})(\d)/, "$1-$2")
    .replace(/(-\d{2})\d+?$/, "$1");
};

export const currencyMask = (value: string): string => {
  let valor = value.replace(/\D/g, "");
  valor = (Number(valor) / 100).toFixed(2);
  return "R$ " + valor.replace(".", ",").replace(/(\d)(?=(\d{3})+\,)/g, "$1.");
};

export const removeCurrencyMask = (value: string): number => {
  const numericValue = value
    .replace("R$ ", "")
    .replace(/\./g, "")
    .replace(",", ".");

  return parseFloat(numericValue) || 0;
};
