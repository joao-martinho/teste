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

import br.com.teste.modelo.ProdutoServico;
import br.com.teste.repositorio.ProdutoServicoRepositorio;

/**
 * Classe de testes responsável pela validação do comportamento de
 * {@link ProdutoServicoServico}, assegurando que operações de criação,
 * consulta, alteração e remoção funcionem corretamente e retornem respostas
 * HTTP adequadas às condições avaliadas.
 */
@ExtendWith(MockitoExtension.class)
public class ProdutoServicoServicoTest {

    @Mock
    private ProdutoServicoRepositorio produtoRepositorio;

    @InjectMocks
    private ProdutoServicoServico produtoServico;

    private ProdutoServico produto;
    private UUID id;

    /**
     * Inicializa objetos para utilização nos testes.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        produto = new ProdutoServico();
        produto.setId(id);
        produto.setNome("Teste");
    }

    /**
     * Testa o cadastro de um novo produto/serviço.
     */
    @Test
    void deveCadastrar() {
        when(produtoRepositorio.save(produto)).thenReturn(produto);
        ResponseEntity<ProdutoServico> resposta = produtoServico.cadastrar(produto);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(produto, resposta.getBody());
    }

    /**
     * Testa a listagem de todos os objetos de produto/serviço.
     */
    @Test
    void deveListarTodos() {
        Iterable<ProdutoServico> lista = mock(Iterable.class);
        when(produtoRepositorio.findAll()).thenReturn((List<ProdutoServico>) lista);
        ResponseEntity<Iterable<ProdutoServico>> resposta = produtoServico.listarTodos();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(lista, resposta.getBody());
    }

    /**
     * Testa a busca por um objeto existente.
     */
    @Test
    void deveBuscarPorIdExistente() {
        when(produtoRepositorio.findById(id)).thenReturn(Optional.of(produto));
        ResponseEntity<ProdutoServico> resposta = produtoServico.buscarPorId(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(produto, resposta.getBody());
    }

    /**
     * Testa a resposta à busca por objeto inexistente.
     */
    @Test
    void deveRetornarNotFoundAoBuscarPorIdInexistente() {
        when(produtoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<ProdutoServico> resposta = produtoServico.buscarPorId(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a substituição total de um objeto existente.
     */
    @Test
    void deveAlterarTotalExistente() {
        ProdutoServico novo = new ProdutoServico();
        when(produtoRepositorio.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepositorio.save(novo)).thenReturn(novo);
        ResponseEntity<ProdutoServico> resposta = produtoServico.alterarTotal(id, novo);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(novo, resposta.getBody());
    }

    /**
     * Testa tentativa de substituir integralmente objeto inexistente.
     */
    @Test
    void deveRetornarNotFoundAoAlterarTotalInexistente() {
        when(produtoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<ProdutoServico> resposta = produtoServico.alterarTotal(id, produto);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a aplicação de alterações parciais a um produto/serviço existente.
     */
    @Test
    void deveAlterarParcialExistente() {
        ProdutoServico parcial = new ProdutoServico();
        parcial.setNome("Novo nome");
        when(produtoRepositorio.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepositorio.save(produto)).thenReturn(produto);
        ResponseEntity<ProdutoServico> resposta = produtoServico.alterarParcial(id, parcial);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("Novo nome", resposta.getBody().getNome());
    }

    /**
     * Testa tentativa de alteração parcial quando o objeto não existe.
     */
    @Test
    void deveRetornarNotFoundAoAlterarParcialInexistente() {
        when(produtoRepositorio.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<ProdutoServico> resposta = produtoServico.alterarParcial(id, produto);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    /**
     * Testa a remoção de um objeto existente.
     */
    @Test
    void deveRemoverExistente() {
        when(produtoRepositorio.existsById(id)).thenReturn(true);
        ResponseEntity<Void> resposta = produtoServico.remover(id);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(produtoRepositorio).deleteById(id);
    }

    /**
     * Testa a resposta à tentativa de remoção de objeto inexistente.
     */
    @Test
    void deveRemoverInexistente() {
        when(produtoRepositorio.existsById(id)).thenReturn(false);
        ResponseEntity<Void> resposta = produtoServico.remover(id);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
