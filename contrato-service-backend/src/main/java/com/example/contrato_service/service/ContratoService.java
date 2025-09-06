package com.example.contrato_service.service;

import com.example.contrato_service.dto.ContratoRequestDTO;
import com.example.contrato_service.dto.ContratoResponseDTO;
import com.example.contrato_service.model.Contrato;
import com.example.contrato_service.repository.ContratoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratoService {

    private static final Logger logger = LoggerFactory.getLogger(ContratoService.class);

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ClicksignService clicksignService;

    public ContratoResponseDTO criarContrato(ContratoRequestDTO request) {
        try {
            logger.info("Iniciando criação de contrato para: {}", request.getContratanteNome());

            Contrato contrato = new Contrato();

            contrato.setContratanteNome(request.getContratanteNome());
            contrato.setContratanteCpfCnpj(request.getContratanteCpfCnpj());
            contrato.setContratanteEmail(request.getContratanteEmail());

            contrato.setContratadoNome(request.getContratadoNome());
            contrato.setContratadoCpfCnpj(request.getContratadoCpfCnpj());
            contrato.setContratadoEmail(request.getContratadoEmail());


            contrato.setValor(request.getValor());
            contrato.setDataInicio(request.getDataInicio());
            contrato.setPrazoMeses(request.getPrazoMeses());
            contrato.setCidade(request.getCidade());
            contrato.setUf(request.getUf());
            contrato.setDescricaoServico(request.getDescricaoServico());


            contrato.setStatusAssinatura("CRIADO");
            contrato.setCriadoEm(LocalDateTime.now());

            logger.debug("Contrato preenchido - aguardando persistência");

            // obtendo o id
            Contrato contratoSalvo = contratoRepository.save(contrato);
            logger.info("Contrato persistido com ID: {}", contratoSalvo.getId());

            // gera o pdf com o id do contrato
            logger.debug("Gerando PDF para contrato ID: {}", contratoSalvo.getId());
            byte[] pdfBytes = gerarPdf(contratoSalvo);
            logger.info("PDF gerado com sucesso - Tamanho: {} bytes", pdfBytes.length);

            // envia pra clicksign
            logger.info("Enviando contrato ID: {} para Clicksign", contratoSalvo.getId());
            String clicksignKey = clicksignService.criarPedidoAssinatura(pdfBytes, contratoSalvo);
            logger.info("Documento criado na Clicksign - Key: {}", clicksignKey);

            // atualiza o contrato com os dados da clicksing
            contratoSalvo.setStatusAssinatura("AGUARDANDO_ASSINATURA");
            contratoSalvo.setClicksignDocumentKey(clicksignKey);

            logger.info("Contrato salvo com ID: {}", contratoSalvo.getId());
            logger.info("PDF gerado: {} bytes", pdfBytes.length);

            // retornar o response DTO
            ContratoResponseDTO response = converterParaResponseDTO(contratoSalvo);
            logger.info("Contrato criado com sucesso - ID: {}, Clicksign: {}", response.getId(), response.getClicksignDocumentKey());

            return response;

        } catch (Exception e) {
            logger.error("ERRO ao criar contrato para {}: {}",
                    request.getContratanteNome(), e.getMessage(), e);
            throw new RuntimeException("Falha ao criar contrato: " + e.getMessage(), e);
        }
    }

    public List<ContratoResponseDTO> listarContratos() {
        logger.info("Listando todos os contratos");
        List<Contrato> contratos = contratoRepository.findAll();
        logger.debug("Encontrados {} contratos", contratos.size());
        return contratos.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public ContratoResponseDTO converterParaResponseDTO(Contrato contrato) {
        ContratoResponseDTO responseDTO = new ContratoResponseDTO();

        responseDTO.setId(contrato.getId());
        responseDTO.setContratanteNome(contrato.getContratanteNome());
        responseDTO.setContratadoNome(contrato.getContratadoNome());
        responseDTO.setValor(contrato.getValor());
        responseDTO.setDataInicio(contrato.getDataInicio());
        responseDTO.setStatusAssinatura(contrato.getStatusAssinatura());
        responseDTO.setClicksignDocumentKey(contrato.getClicksignDocumentKey());
        responseDTO.setCriadoEm(contrato.getCriadoEm());

        return responseDTO;
    }

    // geracao do pdf, com o iText
    public byte[] gerarPdf(Contrato contrato) {
        try {

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // titulo do documento
            Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph p = new Paragraph("Contrato de Prestação de Serviços", titulo);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(new Paragraph(""));

            // conteudo
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font regular = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("Contratante:", bold));
            document.add(new Paragraph("Nome: " + contrato.getContratanteNome(), regular));
            document.add(new Paragraph("CPF/CNPJ: " + contrato.getContratanteCpfCnpj(), regular));
            document.add(new Paragraph("Email: " + contrato.getContratanteEmail(), regular));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Contratado:", bold));
            document.add(new Paragraph("Nome: " + contrato.getContratadoNome(), regular));
            document.add(new Paragraph("CPF/CNPJ: " + contrato.getContratadoCpfCnpj(), regular));
            document.add(new Paragraph("Email: " + contrato.getContratadoEmail(), regular));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Objetivo do Contrato:", bold));
            document.add(new Paragraph(contrato.getDescricaoServico(), regular));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Valor do Contrato: R$ " + contrato.getValor(), regular));
            document.add(new Paragraph("Data de Início: " + contrato.getDataInicio(), regular));
            document.add(new Paragraph("Prazo (meses): " + contrato.getPrazoMeses(), regular));
            document.add(new Paragraph("Cidade/UF: " + contrato.getCidade() + " - " + contrato.getUf(), regular));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Assinatura Digital", bold));
            document.add(new Paragraph("O presente contrato será assinado de forma eletrônica através da plataforma Clicksign.", regular));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Signatários:", bold));
            document.add(new Paragraph("Contratante: " + contrato.getContratanteNome() + " - CPF/CNPJ: " + contrato.getContratanteCpfCnpj(), regular));
            document.add(new Paragraph("Contratado: " + contrato.getContratadoNome() + " - CPF/CNPJ: " + contrato.getContratadoCpfCnpj(), regular));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Assinatura eletrônica via plataforma Clicksign.", regular));

            document.close();

            logger.debug("PDF gerado com sucesso para contrato ID: {}", contrato.getId());
            return out.toByteArray();

        } catch (Exception e) {
            logger.error("ERRO ao gerar PDF para contrato ID {}: {}",
                    contrato.getId(), e.getMessage(), e);
            throw new RuntimeException("Não foi possível gerar o PDF do contrato: " + e.getMessage(), e);
        }
    }
}