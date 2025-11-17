package br.com.teste.servico;

import org.springframework.stereotype.Service;

import br.com.teste.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServico {
    
    private final PedidoRepositorio pedidoRepositorio;

}
