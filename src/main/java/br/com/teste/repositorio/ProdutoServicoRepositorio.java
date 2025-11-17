package br.com.teste.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.teste.modelo.ProdutoServico;

public interface ProdutoServicoRepositorio extends JpaRepository<ProdutoServico, UUID> {
    
}
