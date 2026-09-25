package com.br.RestAll.comum.security;

import com.br.RestAll.usuario.entity.Perfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class UsuarioDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String senha;
    private final Perfil perfil;
    private final Long restauranteId;
    private final String cargo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        java.util.List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
        if (cargo != null && cargo.equalsIgnoreCase("ESTOQUISTA")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ESTOQUISTA"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
