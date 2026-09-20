package com.br.RestAll.cardapio.repository;

import com.br.RestAll.cardapio.entity.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {
    List<ItemCardapio> findByRestauranteId(Long restauranteId);
    Optional<ItemCardapio> findByIdAndRestauranteId(Long id, Long restauranteId);
}
