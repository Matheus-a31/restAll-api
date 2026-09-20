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
}
