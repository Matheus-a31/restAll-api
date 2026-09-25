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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn (name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column (nullable = false, length = 255)
    private String nomeProduto;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Integer quantidade;

    @Column (nullable = false)
    private BigDecimal precoUnitario;


}
