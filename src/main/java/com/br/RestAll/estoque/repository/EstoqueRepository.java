package com.br.RestAll.estoque.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.br.RestAll.estoque.entity.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    List<Estoque> findByRestauranteId(Long restauranteId);

    List<Estoque> findByRestauranteIdAndDataValidadeBetween(Long restauranteId, LocalDate inicio, LocalDate fim);

    @Query ("SELECT SUM(e.quantidade) FROM Estoque e WHERE e.restaurante.id = :restauranteId AND e.dataValidade BETWEEN :inicio AND :fim")
    Integer sumQuantidadeByRestauranteIdAndDataValidadeBetween(Long restauranteId, LocalDate inicio, LocalDate fim);
}
