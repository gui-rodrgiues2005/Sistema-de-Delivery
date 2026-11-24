package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import exception.RegraNegocioException;
import model.ItemCardapio;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CardapioServiceTest {

    private CardapioService service;

    @BeforeEach //um teste não interfere o outro
    void setUp() {
        service = new CardapioService();
    }

    @Test
    @DisplayName("Deve cadastrar um item válido e exibi-lo na listagem")
    void deveCadastrarItemValido() {
        service.cadastrar("Pizza Calabresa", new BigDecimal("45.90"), "Pizzas");

        assertThat(service.listarAtivos())
                .hasSize(1)
                .extracting(ItemCardapio::getNome)
                .containsExactly("Pizza Calabresa");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-10.00"})
    @DisplayName("Deve recusar o cadastro com preço zero ou negativo")
    void deveRecusarPrecoInvalido(String preco) {
        assertThatThrownBy(() -> service.cadastrar("Pizza Calabresa", new BigDecimal(preco), "Pizzas"))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("O preço deve ser maior que zero.");

        assertThat(service.listarAtivos()).isEmpty();
    }

    @Test
    @DisplayName("Deve recusar o cadastro de item com nome duplicado")
    void deveRecusarNomeDuplicado() {
        service.cadastrar("Pizza Calabresa", new BigDecimal("45.90"), "Pizzas");

        assertThatThrownBy(() -> service.cadastrar("pizza calabresa", new BigDecimal("40.00"), "Pizzas"))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Já existe um item");
    }

    @Test
    @DisplayName("Não deve exibir item desativado na listagem do cliente")
    void naoDeveExibirItemDesativado() {
        service.cadastrar("Pizza Calabresa", new BigDecimal("45.90"), "Pizzas");
        service.cadastrar("Refrigerante", new BigDecimal("8.00"), "Bebidas");

        service.desativar("Refrigerante");

        assertThat(service.listarAtivos())
                .extracting(ItemCardapio::getNome)
                .containsExactly("Pizza Calabresa");
    }
}