package com.br.RestAll.comanda.repository;

import com.br.RestAll.comanda.entity.Comanda;
import com.br.RestAll.comanda.entity.StatusComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByRestauranteId(Long restauranteId);

    Optional<Comanda> findByIdAndRestauranteId(Long id, Long restauranteId);

    boolean existsByMesaAndRestauranteIdAndStatus(Integer mesa, Long restauranteId, StatusComanda status);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(c.valorTotal) FROM Comanda c WHERE c.restaurante.id = :restauranteId AND c.status = 'FECHADA' AND c.dataAbertura BETWEEN :inicio AND :fim")
    java.math.BigDecimal sumValorTotalByRestauranteIdAndDataAberturaBetween(@org.springframework.data.repository.query.Param("restauranteId") Long restauranteId, @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio, @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(c) FROM Comanda c WHERE c.restaurante.id = :restauranteId AND c.dataAbertura BETWEEN :inicio AND :fim")
    Long countByRestauranteIdAndDataAberturaBetween(@org.springframework.data.repository.query.Param("restauranteId") Long restauranteId, @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio, @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);

    @org.springframework.data.jpa.repository.Query("SELECT c.mesa AS mesa, COUNT(c) AS quantidade FROM Comanda c WHERE c.restaurante.id = :restauranteId AND c.dataAbertura BETWEEN :inicio AND :fim GROUP BY c.mesa")
    List<com.br.RestAll.dashboard.dto.MesaCountProjection> countComandasPorMesa(@org.springframework.data.repository.query.Param("restauranteId") Long restauranteId, @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio, @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim);
}
