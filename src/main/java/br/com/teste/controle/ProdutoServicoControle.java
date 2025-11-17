package br.com.teste.controle;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teste.modelo.ProdutoServico;
import br.com.teste.servico.ProdutoServicoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Classe de controle da entidade produto/serviço, responsável por receber requisições HTTP
 * do cliente e delegá-las ao serviço correspondente.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos-servicos")
public class ProdutoServicoControle {
    
    /** O serviço de produto/serviço. */
    private final ProdutoServicoServico produtoServicoServico;

    /**
     * Recebe a requisição de cadastro de um novo objeto de produto/serviço.
     * 
     * @param produtoServico o objeto recebido na requisição
     * @return uma resposta contendo o objeto criado
     */
    @PostMapping
    public ResponseEntity<ProdutoServico> cadastrar(
        @Valid @RequestBody ProdutoServico produtoServico
    ) {
        return this.produtoServicoServico.cadastrar(produtoServico);
    }

    /**
     * Retorna todos os objetos de produto/serviço cadastrados no sistema.
     * 
     * @return uma resposta contendo a coleção de objetos
     */
    @GetMapping
    public ResponseEntity<Iterable<ProdutoServico>> listarTodos() {
        return this.produtoServicoServico.listarTodos();
    }

    /**
     * Busca um objeto de produto/serviço a partir do seu ID.
     * 
     * @param id o ID do objeto desejado
     * @return uma resposta contendo o objeto encontrado, se houver
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoServico> buscarPorId(UUID id) {
        return this.produtoServicoServico.buscarPorId(id);
    }

    /**
     * Substitui totalmente um objeto de produto/serviço existente por um novo.
     * 
     * @param id o ID do objeto existente
     * @param produtoServico o novo objeto de produto/serviço
     * @return uma resposta contendo o objeto modificado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoServico> alterarTotal(
        @PathVariable UUID id, @RequestBody ProdutoServico produtoServico
    ) {
        return this.produtoServicoServico.alterarTotal(id, produtoServico);
    }

    /**
     * Aplica alterações parciais ao objeto de produto/serviço existente.
     * 
     * @param id o ID do objeto existente
     * @param produtoServico o objeto contendo os campos a serem atualizados
     * @return uma resposta contendo o objeto modificado
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoServico> alterarParcial(
        @PathVariable UUID id, @RequestBody ProdutoServico produtoServico
    ) {
        return this.produtoServicoServico.alterarTotal(id, produtoServico);
    }

    /**
     * Remove um objeto de produto/serviço do sistema a partir do seu ID.
     * 
     * @param id o ID do objeto a ser removido
     * @return uma resposta indicando o resultado da remoção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        return this.produtoServicoServico.remover(id);
    }

}
