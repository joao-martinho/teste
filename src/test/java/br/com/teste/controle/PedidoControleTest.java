package br.com.teste.controle;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.teste.modelo.Pedido;
import br.com.teste.servico.PedidoServico;

/**
 * Classe de testes dedicada ao controlador {@link PedidoControle},
 * garantindo que suas rotas HTTP deleguem corretamente ao serviço
 * e retornem respostas adequadas às situações testadas.
 */
@WebMvcTest(PedidoControle.class)
public class PedidoControleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoServico pedidoServico;

    private Pedido pedido;
    private UUID id;

    /**
     * Cria objetos de teste antes de cada execução.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        pedido = new Pedido();
        pedido.setId(id);
    }

    /**
     * Testa a descrição de todos os pedidos armazenados.
     */
    @Test
    void deveListarTodos() throws Exception {
        when(pedidoServico.listarTodos())
            .thenReturn(org.springframework.http.ResponseEntity.ok(java.util.List.of(pedido)));

        mockMvc.perform(get("/pedidos"))
            .andExpect(status().isOk());
    }

    /**
     * Testa a busca de um pedido pelo seu ID.
     */
    @Test
    void deveBuscarPorId() throws Exception {
        when(pedidoServico.buscarPorId(id))
            .thenReturn(org.springframework.http.ResponseEntity.ok(pedido));

        mockMvc.perform(get("/pedidos/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Testa a substituição integral de um pedido.
     */
    @Test
    void deveAlterarTotal() throws Exception {
        when(pedidoServico.alterarTotal(eq(id), any()))
            .thenReturn(org.springframework.http.ResponseEntity.ok(pedido));

        mockMvc.perform(
            put("/pedidos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a alteração parcial de um pedido.
     */
    @Test
    void deveAlterarParcial() throws Exception {
        when(pedidoServico.alterarParcial(eq(id), any()))
            .thenReturn(org.springframework.http.ResponseEntity.ok(pedido));

        mockMvc.perform(
            patch("/pedidos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a remoção de um pedido pelo ID.
     */
    @Test
    void deveRemover() throws Exception {
        when(pedidoServico.remover(id))
            .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(delete("/pedidos/" + id))
            .andExpect(status().isOk());
    }
}
