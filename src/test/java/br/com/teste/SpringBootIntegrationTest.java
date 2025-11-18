package br.com.teste;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Classe de testes de integração da aplicação Spring Boot.
 * Executa requisições reais aos controladores, utilizando o contexto
 * completo da aplicação, banco em memória e mapeamentos REST verdadeiros.
 *  
 * Este arquivo valida o funcionamento integrado das camadas de controle,
 * serviço e repositório, garantindo que todas as rotas respondam
 * adequadamente e que o fluxo completo dos dados ocorra conforme esperado.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testa o fluxo de criação, busca, alteração e remoção de um item.
     */
    @Test
    void integrarItemCompleto() throws Exception {
        String jsonCriacao = "{\"nome\":\"Item A\",\"preco\":10.0,\"quantidade\":2}";

        String respostaCriacao = mockMvc.perform(
            post("/itens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonCriacao)
        ).andExpect(status().isCreated())
         .andReturn().getResponse().getContentAsString();

        UUID id = extrairId(respostaCriacao);

        mockMvc.perform(get("/itens/" + id))
            .andExpect(status().isOk());

        mockMvc.perform(
            put("/itens/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Item Alterado\",\"preco\":20.0,\"quantidade\":5}")
        ).andExpect(status().isOk());

        mockMvc.perform(
            patch("/itens/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Item Parcial\"}")
        ).andExpect(status().isOk());

        mockMvc.perform(delete("/itens/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Testa o fluxo completo para objetos de produto/serviço.
     */
    @Test
    void integrarProdutoServicoCompleto() throws Exception {
        String jsonCriacao = "{\"nome\":\"Produto X\",\"preco\":50.0,\"ehProduto\":true,\"estahDesativado\":false}";

        String respostaCriacao = mockMvc.perform(
            post("/produtos-servicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonCriacao)
        ).andExpect(status().isCreated())
         .andReturn().getResponse().getContentAsString();

        UUID id = extrairId(respostaCriacao);

        mockMvc.perform(get("/produtos-servicos/" + id))
            .andExpect(status().isOk());

        mockMvc.perform(
            put("/produtos-servicos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Produto Y\",\"preco\":60.0}")
        ).andExpect(status().isOk());

        mockMvc.perform(
            patch("/produtos-servicos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Produto Parcial\"}")
        ).andExpect(status().isOk());

        mockMvc.perform(delete("/produtos-servicos/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Testa o fluxo de integração completo da entidade pedido.
     */
    @Test
    void integrarPedidoCompleto() throws Exception {
        String jsonCriacao = """
        {
            "cliente":"Fulano",
            "precoBase":100.0,
            "desconto":10.0,
            "precoFinal":90.0,
            "estahAberto":true
        }
        """;

        String respostaCriacao = mockMvc.perform(
            post("/pedidos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonCriacao)
        ).andExpect(status().isCreated())
         .andReturn().getResponse().getContentAsString();

        UUID id = extrairId(respostaCriacao);

        mockMvc.perform(get("/pedidos/" + id))
            .andExpect(status().isOk());

        mockMvc.perform(
            put("/pedidos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
                "cliente":"Cliente Alterado",
                "precoBase":200.0,
                "desconto":20.0,
                "precoFinal":160.0,
                "estahAberto":false
            }
            """)
        ).andExpect(status().isOk());

        mockMvc.perform(
            patch("/pedidos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"cliente\":\"Cliente Parcial\"}")
        ).andExpect(status().isOk());

        mockMvc.perform(delete("/pedidos/" + id))
            .andExpect(status().isOk());
    }

    /**
     * Extrai o campo "id" de uma resposta JSON retornada pelos controladores.
     *
     * @param json a resposta JSON completa
     * @return o valor UUID do campo id
     */
    private UUID extrairId(String json) {
        String campo = "\"id\":\"";
        int inicio = json.indexOf(campo) + campo.length();
        int fim = json.indexOf("\"", inicio);
        return UUID.fromString(json.substring(inicio, fim));
    }
}
