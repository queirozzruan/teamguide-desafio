/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: {
          50: "#fff7ed", // Branco
          500: "#f97316", // Laranja 
          600: "#ea580c", // Laranja escuro
          700: "#c2410c", // Laranja mais escuro
        },
      },
    },
  },
  plugins: [],
};
