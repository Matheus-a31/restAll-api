package com.br.RestAll.comum.context;

import com.br.RestAll.comum.security.UsuarioDetails;
import com.br.RestAll.usuario.entity.Perfil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextoRestaurante {

    public UsuarioDetails getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return (UsuarioDetails) authentication.getPrincipal();
    }

    public Long getUsuarioId() {
        return getUsuarioAutenticado().getId();
    }

    public Long getRestauranteId() {
        return getUsuarioAutenticado().getRestauranteId();
    }

    public Perfil getPerfil() {
        return getUsuarioAutenticado().getPerfil();
    }
}
