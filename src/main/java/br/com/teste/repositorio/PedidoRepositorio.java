package br.com.teste.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.teste.modelo.Pedido;

/**
 * Repositório responsável pelo acesso aos dados da entidade pedido.
 */
@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, UUID> {    
}
