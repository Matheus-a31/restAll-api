package com.br.RestAll.comanda.service;

import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
import com.br.RestAll.comanda.dto.PedidoRequestDTO;
import com.br.RestAll.comanda.dto.PedidoResponseDTO;
import com.br.RestAll.comanda.entity.Comanda;
import com.br.RestAll.comanda.entity.Pedido;
import com.br.RestAll.comanda.entity.StatusComanda;
import com.br.RestAll.comanda.entity.StatusPedido;
import com.br.RestAll.comanda.repository.ComandaRepository;
import com.br.RestAll.comanda.repository.PedidoRepository;
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

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ComandaRepository comandaRepository;
    private final ItemCardapioRepository itemCardapioRepository;
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

    public PedidoResponseDTO criar(PedidoRequestDTO dto) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();

        Comanda comanda = comandaRepository.findByIdAndRestauranteId(dto.comandaId(), restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada."));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível adicionar pedidos a uma comanda que não está aberta.");
        }

        ItemCardapio item = itemCardapioRepository.findByIdAndRestauranteId(dto.itemId(), restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item do cardápio não encontrado."));

        BigDecimal precoUnitario = item.getPreco();
        BigDecimal valorTotal = precoUnitario.multiply(new BigDecimal(dto.quantidade()));

        Pedido pedido = Pedido.builder()
                .comanda(comanda)
                .item(item)
                .quantidade(dto.quantidade())
                .observacao(dto.observacao())
                .status(StatusPedido.PREPARANDO)
                .precoUnitario(precoUnitario)
                .valorTotal(valorTotal)
                .dataPedido(LocalDateTime.now())
                .build();

        Pedido salvo = pedidoRepository.save(pedido);

        // Atualizar valor total da comanda
        atualizarValorTotalComanda(comanda);

        return toDTO(salvo);
    }

    public List<PedidoResponseDTO> listarPorComanda(Long comandaId) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        
        Comanda comanda = comandaRepository.findByIdAndRestauranteId(comandaId, restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada."));

        return pedidoRepository.findByComandaId(comanda.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));

        if (!pedido.getComanda().getRestaurante().getId().equals(restaurante.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado.");
        }

        return toDTO(pedido);
    }

    public void remover(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));

        if (!pedido.getComanda().getRestaurante().getId().equals(restaurante.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado.");
        }

        Comanda comanda = pedido.getComanda();
        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível remover pedidos de uma comanda que não está aberta.");
        }

        pedidoRepository.delete(pedido);

        // Atualizar valor total da comanda
        atualizarValorTotalComanda(comanda);
    }

    private void atualizarValorTotalComanda(Comanda comanda) {
        List<Pedido> pedidos = pedidoRepository.findByComandaId(comanda.getId());
        BigDecimal total = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        comanda.setValorTotal(total);
        comandaRepository.save(comanda);
    }

    private PedidoResponseDTO toDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getComanda().getId(),
                pedido.getItem().getId(),
                pedido.getQuantidade(),
                pedido.getObservacao(),
                pedido.getStatus(),
                pedido.getPrecoUnitario(),
                pedido.getValorTotal(),
                pedido.getDataPedido()
        );
    }
}
