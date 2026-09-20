package com.br.RestAll.usuario.repository;

import com.br.RestAll.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    java.util.List<Usuario> findByRestauranteIdAndPerfil(Long restauranteId, com.br.RestAll.usuario.entity.Perfil perfil);
}
