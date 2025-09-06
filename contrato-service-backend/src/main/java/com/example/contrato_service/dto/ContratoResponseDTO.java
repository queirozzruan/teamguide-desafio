package com.example.contrato_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContratoResponseDTO {

    private Long id;
    private String contratanteNome;
    private String contratadoNome;
    private BigDecimal valor;
    private LocalDate dataInicio;
    private String statusAssinatura;
    private String clicksignDocumentKey;
    private LocalDateTime criadoEm;

    //Contrutor vazio do JPA ---

    public ContratoResponseDTO() {

    }

    //Getters e Setters ---


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

    public String getContratadoNome() {
        return contratadoNome;
    }

    public void setContratadoNome(String contratadoNome) {
        this.contratadoNome = contratadoNome;
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

    public String getStatusAssinatura() {
        return statusAssinatura;
    }

    public void setStatusAssinatura(String statusAssinatura) {
        this.statusAssinatura = statusAssinatura;
    }

    public String getClicksignDocumentKey() {
        return clicksignDocumentKey;
    }

    public void setClicksignDocumentKey(String clicksignDocumentKey) {
        this.clicksignDocumentKey = clicksignDocumentKey;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
