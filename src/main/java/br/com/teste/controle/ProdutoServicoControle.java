package br.com.teste.controle;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teste.servico.ProdutoServicoServico;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class ProdutoServicoControle {
    
    private final ProdutoServicoServico produtoServicoServico;

}
