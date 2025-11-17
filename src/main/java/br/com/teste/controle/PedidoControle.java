package br.com.teste.controle;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.teste.modelo.Pedido;
import br.com.teste.servico.PedidoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controle REST responsável por receber requisições relacionadas a pedidos
 * e repassá-las ao serviço correspondente.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoControle {

    /** Serviço da entidade pedido. */
    private final PedidoServico pedidoServico;

    /**
     * Recebe requisição para cadastrar um pedido.
     * 
     * @param pedido o pedido enviado pelo cliente
     * @return resposta contendo o objeto criado
     */
    @PostMapping
    public ResponseEntity<Pedido> cadastrar(@Valid @RequestBody Pedido pedido) {
        return this.pedidoServico.cadastrar(pedido);
    }

    /**
     * Recebe requisição para listar todos os pedidos.
     * 
     * @return todos os pedidos armazenados
     */
    @GetMapping
    public ResponseEntity<Iterable<Pedido>> listarTodos() {
        return this.pedidoServico.listarTodos();
    }

    /**
     * Recebe requisição para buscar um pedido pelo seu ID.
     * 
     * @param id o ID informado na URL
     * @return o pedido encontrado ou erro
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable UUID id) {
        return this.pedidoServico.buscarPorId(id);
    }

    /**
     * Recebe requisição para substituir completamente um pedido.
     * 
     * @param id o ID do pedido
     * @param pedido o novo objeto do pedido
     * @return o pedido atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> alterarTotal(
        @PathVariable UUID id, @RequestBody Pedido pedido
    ) {
        return this.pedidoServico.alterarTotal(id, pedido);
    }

    /**
     * Recebe requisição para alterar parcialmente um pedido.
     * 
     * @param id o ID do pedido
     * @param pedido objeto contendo as alterações parciais
     * @return o pedido atualizado
     */
    @PutMapping("/{id}/parcial")
    public ResponseEntity<Pedido> alterarParcial(
        @PathVariable UUID id, @RequestBody Pedido pedido
    ) {
        return this.pedidoServico.alterarParcial(id, pedido);
    }

    /**
     * Recebe requisição para remover um pedido do banco.
     * 
     * @param id o ID do pedido
     * @return resposta informando sucesso ou falha
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        return this.pedidoServico.remover(id);
    }
}
