package com.example.contrato_service.service;

import com.example.contrato_service.model.Contrato;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ClicksignService {

    private static final Logger logger = LoggerFactory.getLogger(ClicksignService.class);

    @Value("${CLICKSIGN_TOKEN}")
    private String clicksignToken;

    private final String BASE_URL = "https://app.clicksign.com/api/v3";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void debugConfig() {
        logger.info("Token: {}", clicksignToken != null ? "***" + clicksignToken.substring(Math.max(0, clicksignToken.length() - 4)) : "NULL");
        logger.info(" URL: {}", BASE_URL);
    }

    public String criarPedidoAssinatura(byte[] pdfBytes, Contrato contrato) {
        try {
            //metodo enviar pdf e criar processo de assinatura
            RestTemplate restTemplate = new RestTemplate();

            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode dataNode = rootNode.putObject("data");
            dataNode.put("type", "envelopes");

            ObjectNode attributes = dataNode.putObject("attributes");
            attributes.put("name", "Contrato entre " + contrato.getContratanteNome() + " e " + contrato.getContratadoNome());
            attributes.put("auto_close", true);
            attributes.put("block_after_refusal", true);
            attributes.put("locale", "pt-BR");

            ArrayNode signers = attributes.putArray("signers");

            ObjectNode signer1 = signers.addObject();
            signer1.put("name", contrato.getContratanteNome());
            signer1.put("email", contrato.getContratanteEmail());
            signer1.put("delivery", "email");
            signer1.put("sign_as", "sign");
            ArrayNode auths1 = signer1.putArray("auths");
            auths1.add("email");

            ObjectNode signer2 = signers.addObject();
            signer2.put("name", contrato.getContratadoNome());
            signer2.put("email", contrato.getContratadoEmail());
            signer2.put("delivery", "email");
            signer2.put("sign_as", "sign");
            ArrayNode auths2 = signer2.putArray("auths");
            auths2.add("email");

            String dataJson = objectMapper.writeValueAsString(rootNode);
            // transforma o jason em string


            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("data", dataJson);

            //tanto envia json (data) quanto o pdf (file)

            ByteArrayResource fileResource = new ByteArrayResource(pdfBytes) {
                @Override
                public String getFilename() {
                    return "contrato_" + contrato.getId() + ".pdf";
                }
            };
            body.add("file", fileResource);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            // cabeçalhos da requisição

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


            String url = BASE_URL + "/envelopes?access_token=" + clicksignToken;

            logger.info("Enviando envelope para Clicksign - Contrato ID: {}", contrato.getId());
            logger.info("URL: {}", url.replace(clicksignToken, "***TOKEN***"));

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            logger.info("📥 Status: {} | Body: {}", response.getStatusCode(), response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro - Status: {} | Body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Erro ao criar envelope - Status: " + response.getStatusCode() + " | " + response.getBody());
            }

            String envelopeId = extrairValorJsonV3(response.getBody(), "id");
            logger.info("Envelope criado com sucesso: {}", envelopeId);

            return envelopeId;

        } catch (HttpClientErrorException e) {
            logger.error("Erro HTTP - Status: {} | Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Erro HTTP Clicksign: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Erro geral: {}", e.getMessage(), e);
            throw new RuntimeException("Erro Clicksign: " + e.getMessage(), e);
        }
    }

    private String extrairValorJsonV3(String json, String campo) {
        // consulta um status do envelope ja criado.
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode dataNode = root.path("data");
            return dataNode.path(campo).asText();
        } catch (Exception e) {
            logger.error("Erro ao extrair campo '{}' do JSON: {}", campo, e.getMessage());
            logger.error("JSON recebido: {}", json);
            throw new RuntimeException("Erro extrair JSON campo '" + campo + "': " + e.getMessage());
        }
    }

    public String verificarStatusDocumento(String envelopeId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.api+json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = BASE_URL + "/envelopes/" + envelopeId + "?access_token=" + clicksignToken;

            logger.info("Verificando status do envelope: {}", envelopeId);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode dataNode = root.path("data");
                JsonNode attributes = dataNode.path("attributes");
                String status = attributes.path("status").asText();

                logger.info("Status do envelope {}: {}", envelopeId, status);
                return status;
            }

            logger.error("Erro verificar status - Status: {} | Body: {}", response.getStatusCode(), response.getBody());
            throw new RuntimeException("Erro verificar status: " + response.getStatusCode() + " - " + response.getBody());

        } catch (Exception e) {
            logger.error("Erro verificar status do envelope {}: {}", envelopeId, e.getMessage(), e);
            throw new RuntimeException("Erro verificar status: " + e.getMessage(), e);
        }
    }

    @PostConstruct
    public void testarConexao() {
        try {
            String testUrl = BASE_URL + "/envelopes?access_token=" + clicksignToken + "&limit=1";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.api+json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    testUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // get simples na api da click sign.

            logger.info("Teste conexão Clicksign: {} | Conectado com sucesso!", response.getStatusCode());
        } catch (HttpClientErrorException e) {
            logger.error("Teste conexão falhou - Status: {} | Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Teste conexão falhou: {}", e.getMessage());
        }
    }
}