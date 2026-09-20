package com.br.RestAll.comum.config;

import com.br.RestAll.usuario.entity.Perfil;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@restall.com.br";
        Optional<Usuario> admin = usuarioRepository.findByEmail(adminEmail);

        if (admin.isEmpty()) {
            log.info("Usuário admin não encontrado. Criando admin padrão...");
            Usuario novoAdmin = Usuario.builder()
                    .nome("Administrador Master")
                    .email(adminEmail)
                    .senha(passwordEncoder.encode("admin123"))
                    .perfil(Perfil.ADMINISTRADOR)
                    .ativo(true)
                    .build();
            
            usuarioRepository.save(novoAdmin);
            log.info("Usuário admin criado com sucesso.");
        } else {
            log.info("Usuário admin já existe. Atualizando a senha para garantir o acesso...");
            Usuario usuarioExistente = admin.get();
            usuarioExistente.setSenha(passwordEncoder.encode("admin123"));
            usuarioRepository.save(usuarioExistente);
            log.info("Senha do admin atualizada com sucesso.");
        }
    }
}
