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
 * Entidade que representa um item no sistema.
 * Armazena as informações básicas necessárias para identificação e uso.
 */
@Getter
@Setter
@Entity
@Table(name = "itens")
public class Item {
    
    /** Identificador único do item. */
    @Id
    @GeneratedValue
    private UUID id;

    /** Nome do item. */
    @NotBlank
    private String nome;

    /** Preço do item. */
    @NotNull
    private Float preco;

}
