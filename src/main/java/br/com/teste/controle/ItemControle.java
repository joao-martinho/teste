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

import br.com.teste.modelo.Item;
import br.com.teste.servico.ItemServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controle REST responsável por receber requisições relacionadas a itens
 * e repassá-las ao serviço correspondente.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/itens")
public class ItemControle {
    
    /** Serviço de itens. */
    private final ItemServico itemServico;

    @PostMapping
    public ResponseEntity<Item> cadastrar(@Valid @RequestBody Item item) {
        return this.itemServico.cadastrar(item);
    }

    @GetMapping
    public ResponseEntity<Iterable<Item>> listarTodos() {
        return this.itemServico.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable UUID id) {
        return this.itemServico.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> alterarTotal(
        @PathVariable UUID id, @RequestBody Item item
    ) {
        return this.itemServico.alterarTotal(id, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> alterarParcial(
        @PathVariable UUID id, @RequestBody Item item
    ) {
        return this.itemServico.alterarParcial(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        return this.itemServico.remover(id);
    }
}
