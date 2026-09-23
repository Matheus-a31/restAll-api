package com.br.RestAll.comum.config;

import com.br.RestAll.usuario.entity.Perfil;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.admin.email:admin@restall.com.br}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        Optional<Usuario> admin = usuarioRepository.findByEmail(adminEmail);

        if (admin.isEmpty()) {
            log.info("Usuário admin não encontrado. Criando admin com as configurações de ambiente...");
            Usuario novoAdmin = Usuario.builder()
                    .nome("Administrador Master")
                    .email(adminEmail)
                    .senha(passwordEncoder.encode(adminPassword))
                    .perfil(Perfil.ADMINISTRADOR)
                    .ativo(true)
                    .build();
            
            usuarioRepository.save(novoAdmin);
            log.info("Usuário admin criado com sucesso.");
        } else {
            // REMOVIDO: Não sobrescrever a senha caso o usuário já exista no banco!
            log.info("Usuário admin já existe. Nenhuma alteração foi realizada.");
        }
    }
}
