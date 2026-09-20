package com.br.RestAll.comanda.repository;

import com.br.RestAll.comanda.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByComandaId(Long comandaId);

    @org.springframework.data.jpa.repository.Query("SELECT p.item.id AS itemId, p.item.nome AS nomeItem, SUM(p.quantidade) AS quantidade, SUM(p.valorTotal) AS receitaGerada " +
           "FROM Pedido p WHERE p.comanda.restaurante.id = :restauranteId AND p.dataPedido BETWEEN :inicio AND :fim " +
           "GROUP BY p.item.id, p.item.nome ORDER BY SUM(p.quantidade) DESC")
    List<com.br.RestAll.dashboard.dto.ItemVendidoProjection> findTopItensVendidos(@org.springframework.data.repository.query.Param("restauranteId") Long restauranteId, @org.springframework.data.repository.query.Param("inicio") java.time.LocalDateTime inicio, @org.springframework.data.repository.query.Param("fim") java.time.LocalDateTime fim, org.springframework.data.domain.Pageable pageable);

}
