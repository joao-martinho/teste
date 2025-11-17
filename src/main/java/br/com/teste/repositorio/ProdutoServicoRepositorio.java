package br.com.teste.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.teste.modelo.ProdutoServico;

/**
 * Repositório responsável por acessar e manipular os dados da entidade
 * produto/serviço no banco de dados, fornecendo operações prontas de CRUD.
 */
@Repository
public interface ProdutoServicoRepositorio extends JpaRepository<ProdutoServico, UUID> {
    
}
