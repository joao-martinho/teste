package br.com.teste.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue
    private UUID id;

    private List<UUID> itens = new ArrayList<>();

    @NotNull
    private Float precoBase;

    @NotNull
    private Float desconto;

    @NotNull
    private Float precoFinal;

}
