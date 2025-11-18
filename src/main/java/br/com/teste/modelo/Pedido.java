package br.com.teste.modelo;

import java.util.ArrayList;
import java.util.List;
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
 * Entidade que representa um pedido no sistema.
 * Armazena as informações básicas necessárias para identificação e uso.
 */
@Getter
@Setter
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    /** Identificador único do pedido. */
    @Id
    @GeneratedValue
    private UUID id;

    /** Lista de IDs que referenciam itens. */
    private List<UUID> itens = new ArrayList<>();

    /** O nome do cliente, que não corresponde e um objeto no banco de dados. */
    @NotBlank
    private String cliente;

    /** O preço-base, antes do desconto. */
    @NotNull
    private Float precoBase;

    /** O percentual de desconto a ser aplicado no preço-base. */
    @NotNull
    private Float desconto;

    /** O preço a ser pago pelo cliente, depois de aplicado o desconto. */
    @NotNull
    private Float precoFinal;

}
