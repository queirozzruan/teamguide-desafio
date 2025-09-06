package com.example.contrato_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoRequestDTO {
    //Dados do Contratante ---

    @NotBlank(message = "Este campo é obrigatório")
    private String contratanteNome;

    @NotBlank(message = "Este campo é obrigatório")
    private String contratanteCpfCnpj;

    @Email(message = "Este email não é válido")
    private String contratanteEmail;

    //Dados do Contratado ---
    @NotBlank(message = "Este campo é obrigatório")
    private String contratadoNome;

    @NotBlank(message = "Este campo é obrigatório")
    private String contratadoCpfCnpj;
    @Email(message = "Este email não é válido")
    private String contratadoEmail;

    //Dados referente ao contrato ---

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;
    private LocalDate dataInicio;
    private Integer prazoMeses;
    private String cidade;
    private String uf;
    private String descricaoServico;

    // Construtor vazio (JPA) ---
    public ContratoRequestDTO () {

    }

    // Getters e Setters ---


    public String getContratanteCpfCnpj() {
        return contratanteCpfCnpj;
    }

    public void setContratanteCpfCnpj(String contratanteCpfCnpj) {
        this.contratanteCpfCnpj = contratanteCpfCnpj;
    }

    public String getContratanteNome() {
        return contratanteNome;
    }

    public void setContratanteNome(String contratanteNome) {
        this.contratanteNome = contratanteNome;
    }

    public String getContratadoNome() {
        return contratadoNome;
    }

    public void setContratadoNome(String contratadoNome) {
        this.contratadoNome = contratadoNome;
    }

    public String getContratanteEmail() {
        return contratanteEmail;
    }

    public void setContratanteEmail(String contratanteEmail) {
        this.contratanteEmail = contratanteEmail;
    }

    public String getContratadoCpfCnpj() {
        return contratadoCpfCnpj;
    }

    public void setContratadoCpfCnpj(String contratadoCpfCnpj) {
        this.contratadoCpfCnpj = contratadoCpfCnpj;
    }

    public String getContratadoEmail() {
        return contratadoEmail;
    }

    public void setContratadoEmail(String contratadoEmail) {
        this.contratadoEmail = contratadoEmail;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }


}
