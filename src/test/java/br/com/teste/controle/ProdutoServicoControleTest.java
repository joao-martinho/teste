package br.com.teste.controle;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.teste.modelo.ProdutoServico;
import br.com.teste.servico.ProdutoServicoServico;

/**
 * Classe de testes destinada ao controlador {@link ProdutoServicoControle}.
 * Avalia o correto funcionamento das rotas HTTP, bem como a delegação
 * de responsabilidade para o serviço de produto/serviço.
 */
@WebMvcTest(ProdutoServicoControle.class)
public class ProdutoServicoControleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoServicoServico produtoServicoServico;

    private ProdutoServico produto;
    private UUID id;

    /**
     * Inicializa objetos usados nos testes antes de cada método.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        produto = new ProdutoServico();
        produto.setId(id);
        produto.setNome("Exemplo");
    }

    /**
     * Testa a listagem de todos os produtos/serviços cadastrados.
     */
    @Test
    void deveListarTodos() throws Exception {
        when(produtoServicoServico.listarTodos())
            .thenReturn(org.springframework.http.ResponseEntity.ok(java.util.List.of(produto)));

        mockMvc.perform(get("/produtos-servicos"))
            .andExpect(status().isOk());
    }

    /**
     * Testa a busca pelo ID do objeto.
     */
    @Test
    void deveBuscarPorId() throws Exception {
        when(produtoServicoServico.buscarPorId(id))
            .thenReturn(org.springframework.http.ResponseEntity.ok(produto));

        mockMvc.perform(get("/produtos-servicos/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Testa a substituição integral do objeto por novo conteúdo.
     */
    @Test
    void deveAlterarTotal() throws Exception {
        when(produtoServicoServico.alterarTotal(eq(id), any()))
            .thenReturn(org.springframework.http.ResponseEntity.ok(produto));

        mockMvc.perform(
            put("/produtos-servicos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Novo\"}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a alteração parcial de um objeto existente.
     */
    @Test
    void deveAlterarParcial() throws Exception {
        when(produtoServicoServico.alterarParcial(eq(id), any()))
            .thenReturn(org.springframework.http.ResponseEntity.ok(produto));

        mockMvc.perform(
            patch("/produtos-servicos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Alterado\"}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a remoção de um objeto pelo identificador.
     */
    @Test
    void deveRemover() throws Exception {
        when(produtoServicoServico.remover(id))
            .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(delete("/produtos-servicos/" + id))
            .andExpect(status().isOk());
    }
}
