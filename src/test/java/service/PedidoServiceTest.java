package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import exception.RegraNegocioException;
import model.ItemCardapio;
import model.Pedido;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PedidoServiceTest {

    private PedidoService service;
    private Pedido pedido;
    private ItemCardapio pizza;
    private ItemCardapio refrigerante;

    @BeforeEach
    void setUp() {
        service = new PedidoService();
        pedido = new Pedido();
        pizza = new ItemCardapio("Pizza Calabresa", new BigDecimal("45.90"), "Pizzas");
        refrigerante = new ItemCardapio("Refrigerante", new BigDecimal("8.00"), "Bebidas");
    }

    @Test
    @DisplayName("Deve calcular o subtotal com itens e quantidades variadas")
    void deveCalcularSubtotal() {
        service.adicionarItem(pedido, pizza, 2);
        service.adicionarItem(pedido, refrigerante, 3);

        assertThat(pedido.getSubtotal()).isEqualByComparingTo("115.80");
    }

    @Test
    @DisplayName("Deve recusar a adição de um item desativado")
    void deveRecusarItemDesativado() {
        refrigerante.setAtivo(false);

        assertThatThrownBy(() -> service.adicionarItem(pedido, refrigerante, 1))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("não está disponível");

        assertThat(pedido.getItens()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Deve recusar quantidade zero ou negativa")
    void deveRecusarQuantidadeInvalida(int quantidade) {
        assertThatThrownBy(() -> service.adicionarItem(pedido, pizza, quantidade))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("A quantidade deve ser maior que zero.");
    }

    @Test
    @DisplayName("Deve recusar o fechamento de um pedido sem itens")
    void deveRecusarFecharPedidoVazio() {
        assertThatThrownBy(() -> service.fecharPedido(pedido))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Não é possível fechar um pedido sem itens.");

        assertThat(pedido.isFechado()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar o subtotal ao remover um item")
    void deveAtualizarSubtotalAoRemoverItem() {
        service.adicionarItem(pedido, pizza, 2);
        service.adicionarItem(pedido, refrigerante, 3);

        service.removerItem(pedido, "Refrigerante");

        assertThat(pedido.getSubtotal()).isEqualByComparingTo("91.80");
    }
}