package com.example.contrato_service.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos")
public class Contrato {
    // define que é uma entity do jpa, e o nome da table no banco.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Dados do Contratante ---
    private String contratanteNome;
    private String contratanteCpfCnpj;
    private String contratanteEmail;

    //Dados do Contratado ---
    private String contratadoNome;
    private String contratadoCpfCnpj;
    private String contratadoEmail;

    //Dados referente ao contrato ---
    private BigDecimal valor;
    private LocalDate dataInicio;
    private Integer prazoMeses;
    private String cidade;
    private String uf;
    private String descricaoServico;

    //Status no Clicksign ---

    private String clicksignDocumentKey;
    private String statusAssinatura;

    //Timestamps ---
    private LocalDateTime criadoEm;

    // Construtor vazio (JPA) ---
    public Contrato () {

    }

    // Getters e Setters ---


    public String getContratanteNome() {
        return contratanteNome;
    }

    public void setContratanteNome(String contratanteNome) {
        this.contratanteNome = contratanteNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContratanteCpfCnpj() {
        return contratanteCpfCnpj;
    }

    public void setContratanteCpfCnpj(String contratanteCpfCnpj) {
        this.contratanteCpfCnpj = contratanteCpfCnpj;
    }

    public String getContratanteEmail() {
        return contratanteEmail;
    }

    public void setContratanteEmail(String contratanteEmail) {
        this.contratanteEmail = contratanteEmail;
    }

    public String getContratadoNome() {
        return contratadoNome;
    }

    public void setContratadoNome(String contratadoNome) {
        this.contratadoNome = contratadoNome;
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

    public String getClicksignDocumentKey() {
        return clicksignDocumentKey;
    }

    public void setClicksignDocumentKey(String clicksignDocumentKey) {
        this.clicksignDocumentKey = clicksignDocumentKey;
    }

    public String getStatusAssinatura() {
        return statusAssinatura;
    }

    public void setStatusAssinatura(String statusAssinatura) {
        this.statusAssinatura = statusAssinatura;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
