package br.com.teste.servico;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.teste.modelo.Pedido;
import br.com.teste.repositorio.PedidoRepositorio;

/**
 * Classe de testes dedicada à verificação do comportamento da camada de serviço
 * responsável pelos objetos {@link Pedido}. Confere se as operações de cadastro,
 * consulta, alteração e remoção respondem adequadamente às situações previstas.
 */
@ExtendWith(MockitoExtension.class)
public class PedidoServicoTest {

    @Mock
    private PedidoRepositorio pedidoRepositorio;

    @InjectMocks
    private PedidoServico pedidoServico;

    private Pedido pedido;
    private UUID id;

    /**
     * Inicializa objetos básicos utilizados pelos testes.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        pedido = new Pedido();
        pedido.setId(id);
    }

    /**
     * Testa o cadastro de um pedido, garantindo retorno correto.
     */
    @Test
    void deveCadastrar() {
        when(pedidoRepositorio.save(pedido)).thenReturn(pedido);
        ResponseEntity<Pedido> resposta = pedidoServico.cadastrar(pedido);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(pedido, resposta.getBody());
    }

    /**
     * Testa a listagem completa de pedidos.
     */
    @Test
    void deveListarTodos() {
        Iterable<Pedido> lista = mock(Iterable.class);
        when(pedidoRepositorio.findAll()).thenReturn((List<Pedido>) lista);
        ResponseEntity<Iterable<Pedido>> resposta = pedidoServico.listarTodos();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(lista, resposta.getBody());
    }

    /**
     * Testa a busca por um pedido existente.
     */
    @Test
    void deveBuscarPorIdExistente() {
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.of(pedido));
        ResponseEntity<Pedido> resposta = pedidoServico.buscarPorId(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(pedido, resposta.getBody());
    }

    /**
     * Testa a resposta à tentativa de buscar um pedido inexistente.
     */
    @Test
    void deveRetornarNotFoundAoBuscarPorIdInexistente() {
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Pedido> resposta = pedidoServico.buscarPorId(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a substituição integral de um pedido existente.
     */
    @Test
    void deveAlterarTotalExistente() {
        Pedido novo = new Pedido();
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepositorio.save(novo)).thenReturn(novo);
        ResponseEntity<Pedido> resposta = pedidoServico.alterarTotal(id, novo);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(novo, resposta.getBody());
    }

    /**
     * Testa a tentativa de substituição total de um pedido ausente.
     */
    @Test
    void deveRetornarNotFoundAoAlterarTotalInexistente() {
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Pedido> resposta = pedidoServico.alterarTotal(id, pedido);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a aplicação de alterações parciais sobre um pedido existente.
     */
    @Test
    void deveAlterarParcialExistente() {
        Pedido parcial = new Pedido();
        parcial.setPrecoBase((float) 100.0);
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.of(pedido));
        when(pedidoRepositorio.save(pedido)).thenReturn(pedido);
        ResponseEntity<Pedido> resposta = pedidoServico.alterarParcial(id, parcial);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    /**
     * Testa a tentativa de alteração parcial de um pedido inexistente.
     */
    @Test
    void deveRetornarNotFoundAoAlterarParcialInexistente() {
        when(pedidoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Pedido> resposta = pedidoServico.alterarParcial(id, pedido);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a remoção de um pedido presente no banco.
     */
    @Test
    void deveRemoverExistente() {
        when(pedidoRepositorio.existsById(id)).thenReturn(true);
        ResponseEntity<Void> resposta = pedidoServico.remover(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(pedidoRepositorio).deleteById(id);
    }

    /**
     * Testa a tentativa de remoção de um pedido ausente.
     */
    @Test
    void deveRemoverInexistente() {
        when(pedidoRepositorio.existsById(id)).thenReturn(false);
        ResponseEntity<Void> resposta = pedidoServico.remover(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}

