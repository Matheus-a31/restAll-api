package com.br.RestAll.comanda.service;

import com.br.RestAll.comanda.dto.ComandaRequestDTO;
import com.br.RestAll.comanda.dto.ComandaResponseDTO;
import com.br.RestAll.comanda.entity.Comanda;
import com.br.RestAll.comanda.entity.StatusComanda;
import com.br.RestAll.comanda.repository.ComandaRepository;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));
    }

    private Restaurante getRestauranteDoUsuarioLogado() {
        Restaurante restaurante = getUsuarioLogado().getRestaurante();
        if (restaurante == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não pertence a nenhum restaurante.");
        }
        return restaurante;
    }

    public ComandaResponseDTO criar(ComandaRequestDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        Restaurante restaurante = usuarioLogado.getRestaurante();

        if (restaurante == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não pertence a nenhum restaurante.");
        }

        // Bloquear se já existir comanda ABERTA para a mesa neste restaurante
        if (dto.status() == StatusComanda.ABERTA) {
            boolean jaExisteAberta = comandaRepository.existsByMesaAndRestauranteIdAndStatus(dto.mesa(), restaurante.getId(), StatusComanda.ABERTA);
            if (jaExisteAberta) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma comanda aberta para a mesa " + dto.mesa());
            }
        }

        Comanda comanda = Comanda.builder()
                .numero(dto.numero())
                .mesa(dto.mesa())
                .funcionario(usuarioLogado)
                .restaurante(restaurante)
                .status(dto.status())
                .dataAbertura(LocalDateTime.now())
                .valorTotal(BigDecimal.ZERO)
                .build();

        if (dto.status() == StatusComanda.FECHADA) {
            comanda.setDataFechamento(LocalDateTime.now());
        }

        Comanda salva = comandaRepository.save(comanda);
        return toDTO(salva);
    }

    public List<ComandaResponseDTO> listar() {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        return comandaRepository.findByRestauranteId(restaurante.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ComandaResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        Comanda comanda = comandaRepository.findByIdAndRestauranteId(id, restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada."));
        return toDTO(comanda);
    }

    public ComandaResponseDTO atualizar(Long id, ComandaRequestDTO dto) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        Comanda comanda = comandaRepository.findByIdAndRestauranteId(id, restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada."));

        if (dto.status() == StatusComanda.ABERTA && comanda.getStatus() != StatusComanda.ABERTA) {
            boolean jaExisteAberta = comandaRepository.existsByMesaAndRestauranteIdAndStatus(dto.mesa(), restaurante.getId(), StatusComanda.ABERTA);
            if (jaExisteAberta) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma comanda aberta para a mesa " + dto.mesa());
            }
            comanda.setDataFechamento(null);
        } else if (dto.status() == StatusComanda.FECHADA && comanda.getStatus() != StatusComanda.FECHADA) {
            comanda.setDataFechamento(LocalDateTime.now());
        }

        comanda.setNumero(dto.numero());
        comanda.setMesa(dto.mesa());
        comanda.setStatus(dto.status());

        Comanda atualizada = comandaRepository.save(comanda);
        return toDTO(atualizada);
    }

    public void remover(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        Comanda comanda = comandaRepository.findByIdAndRestauranteId(id, restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada."));
        comandaRepository.delete(comanda);
    }

    private ComandaResponseDTO toDTO(Comanda comanda) {
        return new ComandaResponseDTO(
                comanda.getId(),
                comanda.getNumero(),
                comanda.getMesa(),
                comanda.getFuncionario().getId(),
                comanda.getStatus(),
                comanda.getDataAbertura(),
                comanda.getDataFechamento(),
                comanda.getValorTotal()
        );
    }
}
