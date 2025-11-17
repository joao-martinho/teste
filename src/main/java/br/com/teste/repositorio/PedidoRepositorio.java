package br.com.teste.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teste.modelo.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, UUID> {
    
}
