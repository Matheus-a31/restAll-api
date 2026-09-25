package com.br.RestAll.comum.security;

import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        Long restauranteId = usuario.getRestaurante() != null ? usuario.getRestaurante().getId() : null;

        return new UsuarioDetails(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil(),
                restauranteId,
                usuario.getCargo()
        );
    }
}
