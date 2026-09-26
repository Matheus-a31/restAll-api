package com.br.RestAll.restaurante.service;

import com.br.RestAll.comum.context.ContextoRestaurante;
import com.br.RestAll.restaurante.dto.AtualizarRestauranteRequest;
import com.br.RestAll.restaurante.dto.RestauranteResponse;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.restaurante.repository.RestauranteRepository;
import com.br.RestAll.usuario.entity.Perfil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;
    private final ContextoRestaurante contextoRestaurante;

    @Transactional
    public RestauranteResponse atualizarRestaurante(AtualizarRestauranteRequest request) {
        Long restauranteId = contextoRestaurante.getRestauranteId();
        Perfil perfil = contextoRestaurante.getPerfil();

        if (restauranteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não está associado a um restaurante.");
        }

        if (perfil != Perfil.DONO && perfil != Perfil.ADMINISTRADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o dono ou administrador pode editar as informações do restaurante.");
        }

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado."));

        if (request.getNome() != null) restaurante.setNome(request.getNome());
        if (request.getTelefone() != null) restaurante.setTelefone(request.getTelefone());
        if (request.getEmail() != null) restaurante.setEmail(request.getEmail());
        if (request.getEndereco() != null) restaurante.setEndereco(request.getEndereco());
        if (request.getStatus() != null) restaurante.setStatus(request.getStatus());

        restaurante = restauranteRepository.save(restaurante);
        return RestauranteResponse.fromEntity(restaurante);
    }
}
