package com.example.contrato_service.controller;

import com.example.contrato_service.dto.ContratoRequestDTO;
import com.example.contrato_service.dto.ContratoResponseDTO;
import com.example.contrato_service.model.Contrato;
import com.example.contrato_service.repository.ContratoRepository;
import com.example.contrato_service.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contratos") //todas as urls começam com /contratos

public class ContratoController {

    @Autowired
    private ContratoService contratoService;
    // injecao de dependencia

    @Autowired
    private ContratoRepository contratoRepository;

    @PostMapping
    public ResponseEntity<ContratoResponseDTO> criarContrato(@Valid @RequestBody ContratoRequestDTO request) {
        ContratoResponseDTO response = contratoService.criarContrato(request);
        return ResponseEntity.ok(response);
    }
    //mapeia post em /contratos, o valid valida o dto, e o requestbody converte o json em objeto.

    @GetMapping
    public ResponseEntity<List<ContratoResponseDTO>> listarContratos() {
        List<ContratoResponseDTO> contratos = contratoService.listarContratos();
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> buscaPorId(@PathVariable Long id) {
        return contratoRepository.findById(id)
                .map(contrato -> ResponseEntity.ok(contratoService.converterParaResponseDTO(contrato)))
                .orElse(ResponseEntity.notFound().build());
    }
    //mapeia o get /contatos/123, o pathvariable extrai o id da url.

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> atualizarContrato(@PathVariable Long id, @RequestBody ContratoRequestDTO requestDTO) {
        return contratoRepository.findById(id)
                .map(contrato -> {
                    contrato.setContratanteNome(requestDTO.getContratanteNome());
                    contrato.setContratanteCpfCnpj(requestDTO.getContratanteCpfCnpj());
                    contrato.setContratanteEmail(requestDTO.getContratanteEmail());
                    contrato.setContratadoNome(requestDTO.getContratadoNome());
                    contrato.setContratadoCpfCnpj(requestDTO.getContratadoCpfCnpj());
                    contrato.setContratadoEmail(requestDTO.getContratadoEmail());
                    contrato.setValor(requestDTO.getValor());
                    contrato.setDataInicio(requestDTO.getDataInicio());
                    contrato.setPrazoMeses(requestDTO.getPrazoMeses());
                    contrato.setCidade(requestDTO.getCidade());
                    contrato.setUf(requestDTO.getUf());
                    contrato.setDescricaoServico(requestDTO.getDescricaoServico());

                    //contrato.setStatusAssinatura(requestDTO.getStatusAssinatura());


                    Contrato contratoSalvo = contratoRepository.save(contrato);
                    return ResponseEntity.ok(contratoService.converterParaResponseDTO(contratoSalvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContrato(@PathVariable Long id) {
        if (contratoRepository.existsById(id)) {
            contratoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
        return contratoRepository.findById(id)
                .map(contrato -> {
                    byte[] pdfBytes = contratoService.gerarPdf(contrato);

                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=\"contrato_" + contrato.getId() + ".pdf\"")
                            .header("Content-Type", "application/pdf")
                            .body(pdfBytes);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
