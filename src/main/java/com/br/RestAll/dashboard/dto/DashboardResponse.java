package com.br.RestAll.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private BigDecimal totalVendido;
    private BigDecimal totalDespesas;
    private BigDecimal lucroLiquido;
    private Long quantidadeComandas;
    private List<MesaCountDTO> comandasPorMesa;
    private List<ItemVendidoDTO> itensMaisVendidos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MesaCountDTO {
        private Integer mesa;
        private Long quantidade;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemVendidoDTO {
        private Long itemId;
        private String nomeItem;
        private Long quantidade;
        private BigDecimal receitaGerada;
    }
}
