package br.com.teste.servico;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.teste.modelo.Item;
import br.com.teste.repositorio.ItemRepositorio;
import lombok.RequiredArgsConstructor;

/**
 * Classe de serviço da entidade item, que recebe requisições do controle,
 * processa regras de negócio e delega para o repositório.
 */
@Service
@RequiredArgsConstructor
public class ItemServico {
    
    /** O repositório de itens. */
    private final ItemRepositorio itemRepositorio;

    /**
     * Cadastra um item.
     * 
     * @param item o item a ser cadastrado
     * @return uma resposta ao cliente e o objeto criado
     */
    public ResponseEntity<Item> cadastrar(Item item) {
        return new ResponseEntity<>(
            itemRepositorio.save(item), HttpStatus.CREATED
        );
    }

    /**
     * Lista todos os itens presentes no banco de dados.
     * 
     * @return uma resposta ao cliente e os itens encontrados
     */
    public ResponseEntity<Iterable<Item>> listarTodos() {
        return new ResponseEntity<>(
            itemRepositorio.findAll(), HttpStatus.OK
        );
    }

    /**
     * Busca um item específico a partir do seu ID.
     * 
     * @param id o ID do item
     * @return uma resposta contendo o item encontrado ou erro se não houver
     */
    public ResponseEntity<Item> buscarPorId(UUID id) {
        Optional<Item> optional = itemRepositorio.findById(id);
        return optional.map(item ->
            new ResponseEntity<>(item, HttpStatus.OK)
        ).orElseGet(() ->
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * Substitui por completo um item existente no banco de dados.
     * 
     * @param id o ID do item existente
     * @param item o novo item que substituirá o anterior
     * @return uma resposta ao cliente e o item alterado
     */
    public ResponseEntity<Item> alterarTotal(UUID id, Item item) {
        Optional<Item> optional = itemRepositorio.findById(id);
        if (optional.isPresent()) {
            item.setId(id);
            return new ResponseEntity<>(
                itemRepositorio.save(item), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Aplica alterações parciais a um item existente.
     * 
     * @param id o ID do item
     * @param item objeto contendo apenas os campos a serem modificados
     * @return uma resposta ao cliente e o item atualizado
     */
    public ResponseEntity<Item> alterarParcial(UUID id, Item item) {
        Optional<Item> optional = itemRepositorio.findById(id);
        if (optional.isPresent()) {
            Item existente = optional.get();

            if (item.getNome() != null) existente.setNome(item.getNome());
            if (item.getPreco() != null) existente.setPreco(item.getPreco());

            return new ResponseEntity<>(
                itemRepositorio.save(existente), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Remove um item do banco de dados pelo seu ID.
     * 
     * @param id o ID do item a ser removido
     * @return uma resposta indicando sucesso ou falha
     */
    public ResponseEntity<Void> remover(UUID id) {
        if (itemRepositorio.existsById(id)) {
            itemRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
