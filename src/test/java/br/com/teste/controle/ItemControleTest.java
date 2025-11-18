package br.com.teste.controle;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.teste.modelo.Item;
import br.com.teste.servico.ItemServico;

/**
 * Classe de testes responsável pela verificação do comportamento do
 * controlador {@link ItemControle}, assegurando que as rotas HTTP
 * respondam adequadamente e que chamadas ao serviço sejam corretamente delegadas.
 */
@WebMvcTest(ItemControle.class)
public class ItemControleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServico itemServico;

    private Item item;
    private UUID id;

    /**
     * Prepara objetos utilizados pelos testes antes de cada método.
     */
    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        item = new Item();
        item.setId(id);
        item.setNome("Exemplo");
    }

    /**
     * Testa a listagem de todos os itens cadastrados.
     */
    @Test
    void deveListarTodos() throws Exception {
        when(itemServico.listarTodos()).thenReturn(
            org.springframework.http.ResponseEntity.ok(java.util.List.of(item))
        );

        mockMvc.perform(get("/itens"))
            .andExpect(status().isOk());
    }

    /**
     * Testa a busca de um item por ID.
     */
    @Test
    void deveBuscarPorId() throws Exception {
        when(itemServico.buscarPorId(id)).thenReturn(
            org.springframework.http.ResponseEntity.ok(item)
        );

        mockMvc.perform(get("/itens/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Testa a substituição completa de um item existente.
     */
    @Test
    void deveAlterarTotal() throws Exception {
        when(itemServico.alterarTotal(eq(id), any())).thenReturn(
            org.springframework.http.ResponseEntity.ok(item)
        );

        mockMvc.perform(
            put("/itens/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Novo\"}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a alteração parcial de um item existente.
     */
    @Test
    void deveAlterarParcial() throws Exception {
        when(itemServico.alterarParcial(eq(id), any())).thenReturn(
            org.springframework.http.ResponseEntity.ok(item)
        );

        mockMvc.perform(
            patch("/itens/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Outro\"}")
        ).andExpect(status().isOk());
    }

    /**
     * Testa a remoção de um item a partir do ID.
     */
    @Test
    void deveRemover() throws Exception {
        when(itemServico.remover(id)).thenReturn(
            org.springframework.http.ResponseEntity.ok().build()
        );

        mockMvc.perform(delete("/itens/" + id))
            .andExpect(status().isOk());
    }
}
