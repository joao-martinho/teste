package br.com.teste.servico;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.teste.modelo.ProdutoServico;
import br.com.teste.repositorio.ProdutoServicoRepositorio;
import lombok.RequiredArgsConstructor;

/**
 * Classe de serviço da entidade produto/serviço, que recebe requisições do controle,
 * processa regras de negócio e delega para o repositório.
 */
@Service
@RequiredArgsConstructor
public class ProdutoServicoServico {
    
    /** O repositório de produto/serviço. */
    private final ProdutoServicoRepositorio produtoServicoRepositorio;

    /**
     * Cadastra um objeto de produto/serviço.
     * 
     * @param produtoServico o objeto de produto/serviço
     * @return uma resposta ao cliente e o objeto criado
     */
    public ResponseEntity<ProdutoServico> cadastrar(ProdutoServico produtoServico) {
        return new ResponseEntity<>(
            produtoServicoRepositorio.save(produtoServico), HttpStatus.CREATED
        );
    }

    /**
     * Lista todos os objetos de produto/serviço presentes no banco de dados.
     * 
     * @return uma resposta ao cliente e os objetos obtidos, se houver
     */
    public ResponseEntity<Iterable<ProdutoServico>> listarTodos() {
        return new ResponseEntity<>(
            produtoServicoRepositorio.findAll(), HttpStatus.OK
        );
    }

    /**
     * Busca um objeto de produto/serviço específico a partir do seu ID.
     * 
     * @param id o ID do produto/serviço
     * @return uma resposta ao cliente e o objeto obtido, se houver
     */
    public ResponseEntity<ProdutoServico> buscarPorId(UUID id) {
        Optional<ProdutoServico> optional = produtoServicoRepositorio.findById(id);
        return optional.map(
            produtoServico -> new ResponseEntity<>(produtoServico, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * Substitui um objeto de produto/serviço no banco de dados por um novo objeto.
     * 
     * @param id o ID do objeto existente
     * @param produtoServico o novo objeto de produto/serviço
     * @return uma resposta ao cliente e o objeto alterado
     */
    public ResponseEntity<ProdutoServico> alterarTotal(UUID id, ProdutoServico produtoServico) {
        Optional<ProdutoServico> optional = produtoServicoRepositorio.findById(id);
        if (optional.isPresent()) {
            produtoServico.setId(id);

            return new ResponseEntity<>(
                produtoServicoRepositorio.save(produtoServico), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Aplica alterações em um objeto no banco de dados sem sobrescrevê-lo.
     * 
     * @param id o ID do objeto existente
     * @param produtoServico objeto de produto/serviço contendo as alterações
     * @return uma resposta ao cliente e o objeto alterado
     */
    public ResponseEntity<ProdutoServico> alterarParcial(UUID id, ProdutoServico produtoServico) {
        Optional<ProdutoServico> optional = produtoServicoRepositorio.findById(id);
        if (optional.isPresent()) {
            ProdutoServico existente = optional.get();
            
            if (produtoServico.getNome() != null) existente.setNome(produtoServico.getNome());
            if (produtoServico.getPreco() != null) existente.setPreco(produtoServico.getPreco());
            if (produtoServico.getEhProduto() != null) existente.setEhProduto(produtoServico.getEhProduto());
            if (produtoServico.getEstahDesativado() != null) existente.setEstahDesativado(produtoServico.getEstahDesativado());
        
            return new ResponseEntity<>(
                produtoServicoRepositorio.save(existente), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Remove um objeto de produto/serviço a partir no seu ID.
     * 
     * @param id o ID do objeto a ser removido
     * @return uma resposta ao cliente
     */
    public ResponseEntity<Void> remover(UUID id) {
        if (produtoServicoRepositorio.existsById(id)) {
            produtoServicoRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
