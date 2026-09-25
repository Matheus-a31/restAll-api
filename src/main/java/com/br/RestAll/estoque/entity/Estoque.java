package com.br.RestAll.estoque.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.br.RestAll.restaurante.entity.Restaurante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estoque")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column (name = "nome_produto", nullable = false, length = 255)
    private String nomeProduto;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Integer quantidade;

    @Column (name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "unidade_medida", nullable = false, length = 50)
    private String unidadeMedida;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private java.time.LocalDateTime criadoEm;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private java.time.LocalDateTime atualizadoEm;

}
