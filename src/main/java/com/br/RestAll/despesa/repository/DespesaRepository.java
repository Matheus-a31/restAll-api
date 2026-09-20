package com.br.RestAll.despesa.repository;

import com.br.RestAll.despesa.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByRestauranteId(Long restauranteId);

    List<Despesa> findByRestauranteIdAndDataDespesaBetween(Long restauranteId, LocalDate inicio, LocalDate fim);

    @Query("SELECT SUM(d.valor) FROM Despesa d WHERE d.restaurante.id = :restauranteId AND d.dataDespesa BETWEEN :inicio AND :fim")
    BigDecimal sumValorByRestauranteIdAndDataDespesaBetween(@Param("restauranteId") Long restauranteId, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
