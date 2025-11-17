package br.com.teste.servico;

import org.springframework.stereotype.Service;

import br.com.teste.repositorio.ProdutoServicoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoServicoServico {
    
    private final ProdutoServicoRepositorio produtoServicoRepositorio;

}
