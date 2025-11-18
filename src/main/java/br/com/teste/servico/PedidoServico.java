package br.com.teste.servico;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.teste.modelo.Pedido;
import br.com.teste.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;

/**
 * Classe de serviço da entidade pedido, que recebe requisições do controle,
 * processa regras de negócio e delega para o repositório.
 */
@Service
@RequiredArgsConstructor
public class PedidoServico {

    /** O repositório de pedidos. */
    private final PedidoRepositorio pedidoRepositorio;

    /**
     * Cadastra um pedido.
     * 
     * @param pedido o pedido enviado pelo cliente
     * @return uma resposta contendo o pedido criado
     */
    public ResponseEntity<Pedido> cadastrar(Pedido pedido) {
        return new ResponseEntity<>(
            pedidoRepositorio.save(pedido), HttpStatus.CREATED
        );
    }

    /**
     * Lista todos os pedidos presentes no banco.
     * 
     * @return uma resposta ao cliente com todos os pedidos encontrados
     */
    public ResponseEntity<Iterable<Pedido>> listarTodos() {
        return new ResponseEntity<>(
            pedidoRepositorio.findAll(), HttpStatus.OK
        );
    }

    /**
     * Busca um pedido pelo seu ID.
     * 
     * @param id o ID do pedido
     * @return o pedido encontrado ou erro se não existir
     */
    public ResponseEntity<Pedido> buscarPorId(UUID id) {
        Optional<Pedido> optional = pedidoRepositorio.findById(id);
        return optional.map(pedido ->
            new ResponseEntity<>(pedido, HttpStatus.OK)
        ).orElseGet(() ->
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * Substitui totalmente um pedido existente.
     * 
     * @param id o ID do pedido
     * @param pedido o novo pedido enviado pelo cliente
     * @return o pedido atualizado
     */
    public ResponseEntity<Pedido> alterarTotal(UUID id, Pedido pedido) {
        Optional<Pedido> optional = pedidoRepositorio.findById(id);
        if (optional.isPresent()) {
            pedido.setId(id);
            return new ResponseEntity<>(
                pedidoRepositorio.save(pedido), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Aplica alterações parciais a um pedido existente.
     * 
     * @param id o ID do pedido
     * @param pedido objeto contendo apenas os campos modificados
     * @return o pedido atualizado
     */
    public ResponseEntity<Pedido> alterarParcial(UUID id, Pedido pedido) {
        Optional<Pedido> optional = pedidoRepositorio.findById(id);
        if (optional.isPresent()) {
            Pedido existente = optional.get();

            if (pedido.getItens() != null) existente.setItens(pedido.getItens());
            if (pedido.getCliente() != null) existente.setCliente(pedido.getCliente());
            if (pedido.getPrecoBase() != null) existente.setPrecoBase(pedido.getPrecoBase());
            if (pedido.getDesconto() != null) existente.setDesconto(pedido.getDesconto());
            if (pedido.getPrecoFinal() != null) existente.setPrecoFinal(pedido.getPrecoFinal());
            if (pedido.getEstahAberto() != null) existente.setEstahAberto(pedido.getEstahAberto());

            return new ResponseEntity<>(
                pedidoRepositorio.save(existente), HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Remove um pedido pelo seu ID.
     * 
     * @param id o ID do pedido
     * @return resposta indicando sucesso ou falha
     */
    public ResponseEntity<Void> remover(UUID id) {
        if (pedidoRepositorio.existsById(id)) {
            pedidoRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
