package com.mballem.demoparkapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name="clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
public class ClienteVaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="recibo",nullable = false, unique = true, length = 15)
    private String recibo;
    @Column(name="placa",nullable = false, length = 8)
    private String placa;
    @Column(name="marca",nullable = false, length = 45)
    private String marca;
    @Column(name="modelo",nullable = false, length = 45)
    private String modelo;
    @Column(name="cor",nullable = false, length = 8)
    private String cor;
    @Column(name = "valor", columnDefinition = "decimal(7,2)")
    private BigDecimal valor;
    @Column(name = "desconto", columnDefinition = "decimal(7,2)")
    private BigDecimal desconto;
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
    @Column(name = "data_saida")
    private LocalDateTime dataSaida;


    //abaixo relacionamento
    @ManyToOne
    @JoinColumn(name="id_cliente",nullable = false)
    private Cliente cliente;
    @JoinColumn(name="id_vaga",nullable = false)
    private Vaga vaga;

    //campos auditoria
    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;
    @Column(name = "criado_por")
    @CreatedBy
    private String criadoPor;
    @Column(name = "modificado_por")
    @LastModifiedBy
    private String modificadoPor;
}
