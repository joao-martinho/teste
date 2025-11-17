package br.com.teste.servico;

import org.springframework.stereotype.Service;

import br.com.teste.repositorio.ItemRepositorio;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServico {

    private final ItemRepositorio itemRepositorio;

}