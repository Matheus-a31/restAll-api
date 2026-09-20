package com.br.RestAll.dashboard.dto;

import java.math.BigDecimal;

public interface ItemVendidoProjection {
    Long getItemId();
    String getNomeItem();
    Long getQuantidade();
    BigDecimal getReceitaGerada();
}
