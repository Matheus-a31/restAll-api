package com.br.RestAll.dashboard.service;

import com.br.RestAll.comanda.repository.ComandaRepository;
import com.br.RestAll.comanda.repository.PedidoRepository;
import com.br.RestAll.dashboard.dto.DashboardResponse;
import com.br.RestAll.dashboard.dto.ItemVendidoProjection;
import com.br.RestAll.dashboard.dto.MesaCountProjection;
import com.br.RestAll.dashboard.dto.PeriodoDashboard;
import com.br.RestAll.despesa.repository.DespesaRepository;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ComandaRepository comandaRepository;
    private final PedidoRepository pedidoRepository;
    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    private Restaurante getRestauranteDoUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        if (usuario.getRestaurante() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não está associado a nenhum restaurante");
        }
        return usuario.getRestaurante();
    }

    public DashboardResponse getDashboardData(PeriodoDashboard periodo) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        Long restauranteId = restaurante.getId();

        LocalDate dataInicioDate;
        LocalDate dataFimDate = LocalDate.now();

        switch (periodo) {
            case DIA:
                dataInicioDate = dataFimDate;
                break;
            case SEMANA:
                dataInicioDate = dataFimDate.minusDays(7);
                break;
            case MES:
                dataInicioDate = dataFimDate.with(TemporalAdjusters.firstDayOfMonth());
                break;
            case ANO:
                dataInicioDate = dataFimDate.with(TemporalAdjusters.firstDayOfYear());
                break;
            default:
                dataInicioDate = dataFimDate;
        }

        LocalDateTime inicio = dataInicioDate.atStartOfDay();
        LocalDateTime fim = dataFimDate.atTime(LocalTime.MAX);

        // 1. Receita (Vendas)
        BigDecimal totalVendido = comandaRepository.sumValorTotalByRestauranteIdAndDataAberturaBetween(restauranteId, inicio, fim);
        if (totalVendido == null) totalVendido = BigDecimal.ZERO;

        // 2. Despesas
        BigDecimal totalDespesas = despesaRepository.sumValorByRestauranteIdAndDataDespesaBetween(restauranteId, dataInicioDate, dataFimDate);
        if (totalDespesas == null) totalDespesas = BigDecimal.ZERO;

        // 3. Lucro
        BigDecimal lucroLiquido = totalVendido.subtract(totalDespesas);

        // 4. Quantidade de Comandas
        Long quantidadeComandas = comandaRepository.countByRestauranteIdAndDataAberturaBetween(restauranteId, inicio, fim);

        // 5. Comandas por Mesa
        List<MesaCountProjection> mesasProjection = comandaRepository.countComandasPorMesa(restauranteId, inicio, fim);
        List<DashboardResponse.MesaCountDTO> comandasPorMesa = mesasProjection.stream()
                .map(m -> new DashboardResponse.MesaCountDTO(m.getMesa(), m.getQuantidade()))
                .collect(Collectors.toList());

        // 6. Itens mais vendidos (Top 10)
        List<ItemVendidoProjection> itensProjection = pedidoRepository.findTopItensVendidos(restauranteId, inicio, fim, PageRequest.of(0, 10));
        List<DashboardResponse.ItemVendidoDTO> itensMaisVendidos = itensProjection.stream()
                .map(i -> new DashboardResponse.ItemVendidoDTO(i.getItemId(), i.getNomeItem(), i.getQuantidade(), i.getReceitaGerada()))
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalVendido(totalVendido)
                .totalDespesas(totalDespesas)
                .lucroLiquido(lucroLiquido)
                .quantidadeComandas(quantidadeComandas)
                .comandasPorMesa(comandasPorMesa)
                .itensMaisVendidos(itensMaisVendidos)
                .build();
    }
}
