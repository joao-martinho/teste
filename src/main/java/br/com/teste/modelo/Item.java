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

@Getter
@Setter
@Entity
@Table(name = "itens")
public class Item {
    
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String nome;

    @NotNull
    private Float preco;

}
