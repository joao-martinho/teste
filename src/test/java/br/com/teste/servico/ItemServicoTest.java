package br.com.teste.servico;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.teste.modelo.Item;
import br.com.teste.repositorio.ItemRepositorio;

/**
 * Classe de testes da camada de serviço responsável pela entidade {@link Item}.
 * Verifica o comportamento dos métodos públicos da classe {@link ItemServico},
 * assegurando respostas HTTP corretas, chamadas ao repositório e processamento
 * adequado das regras declaradas.
 */
@ExtendWith(MockitoExtension.class)
public class ItemServicoTest {

    @Mock
    private ItemRepositorio itemRepositorio;

    @InjectMocks
    private ItemServico itemServico;

    private Item item;
    private UUID id;

    /**
     * Inicializa dados de teste antes da execução de cada método.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        item = new Item();
        item.setId(id);
        item.setNome("Teste");
    }

    /**
     * Verifica se o serviço cadastra corretamente um novo item,
     * retornando status HTTP 201 e o objeto persistido.
     */
    @Test
    void deveCadastrar() {
        when(itemRepositorio.save(item)).thenReturn(item);
        ResponseEntity<Item> resposta = itemServico.cadastrar(item);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(item, resposta.getBody());
    }

    /**
     * Verifica a listagem de todos os itens cadastrados.
     */
    @Test
    void deveListarTodos() {
        Iterable<Item> lista = mock(Iterable.class);
        when(itemRepositorio.findAll()).thenReturn((List<Item>) lista);
        ResponseEntity<Iterable<Item>> resposta = itemServico.listarTodos();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(lista, resposta.getBody());
    }

    /**
     * Verifica a busca por ID quando o item existe.
     */
    @Test
    void deveBuscarPorIdExistente() {
        when(itemRepositorio.findById(id)).thenReturn(Optional.of(item));
        ResponseEntity<Item> resposta = itemServico.buscarPorId(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(item, resposta.getBody());
    }

    /**
     * Verifica a resposta adequada quando um item inexistente é buscado.
     */
    @Test
    void deveRetornarNotFoundAoBuscarPorIdInexistente() {
        when(itemRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Item> resposta = itemServico.buscarPorId(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Verifica se a substituição total de um item existente funciona corretamente.
     */
    @Test
    void deveAlterarTotalExistente() {
        Item novo = new Item();
        when(itemRepositorio.findById(id)).thenReturn(Optional.of(item));
        when(itemRepositorio.save(novo)).thenReturn(novo);
        ResponseEntity<Item> resposta = itemServico.alterarTotal(id, novo);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(novo, resposta.getBody());
    }

    /**
     * Verifica a resposta adequada ao tentar substituir um item inexistente.
     */
    @Test
    void deveRetornarNotFoundAoAlterarTotalInexistente() {
        when(itemRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Item> resposta = itemServico.alterarTotal(id, item);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Verifica se alterações parciais são aplicadas corretamente a um item existente.
     */
    @Test
    void deveAlterarParcialExistente() {
        Item parcial = new Item();
        parcial.setNome("Outro nome");
        when(itemRepositorio.findById(id)).thenReturn(Optional.of(item));
        when(itemRepositorio.save(item)).thenReturn(item);
        ResponseEntity<Item> resposta = itemServico.alterarParcial(id, parcial);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Outro nome", resposta.getBody().getNome());
    }

    /**
     * Verifica a resposta ao tentar aplicar alterações parciais em item inexistente.
     */
    @Test
    void deveRetornarNotFoundAoAlterarParcialInexistente() {
        when(itemRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Item> resposta = itemServico.alterarParcial(id, item);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Verifica a remoção de um item existente.
     */
    @Test
    void deveRemoverExistente() {
        when(itemRepositorio.existsById(id)).thenReturn(true);
        ResponseEntity<Void> resposta = itemServico.remover(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(itemRepositorio).deleteById(id);
    }

    /**
     * Verifica a resposta ao tentar remover um item inexistente.
     */
    @Test
    void deveRemoverInexistente() {
        when(itemRepositorio.existsById(id)).thenReturn(false);
        ResponseEntity<Void> resposta = itemServico.remover(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
