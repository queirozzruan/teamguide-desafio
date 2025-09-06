package com.example.contrato_service.repository;

import com.example.contrato_service.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    // extende do jparepo, ganha metodos.

}
