package br.com.teste.modelo;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade que representa um produto ou serviço no sistema.
 * Armazena as informações básicas necessárias para identificação e uso
 * nos pedidos, como nome e preço.
 */
@Getter
@Setter
@Entity
@Table(name = "produtos_servicos")
public class ProdutoServico {
    
    /** Identificador único do produto/serviço. */
    @Id
    @GeneratedValue
    private UUID id;

    /** Nome do produto ou serviço, obrigatório. */
    @NotBlank
    private String nome;

    /** Preço unitário do produto ou serviço, obrigatório. */
    @NotNull
    private Float preco;

    /** Representa se o objeto é produto (true) ou serviço (false). */
    @NotNull
    private Boolean ehProduto;

    /** Representa se o objeto está ou não desativado. */
    @NotNull
    private Boolean estahDesativado;

}
