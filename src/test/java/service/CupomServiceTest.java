package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import exception.RegraNegocioException;
import model.Cupom;
import model.ItemCardapio;
import model.ItemPedido;
import model.Pedido;
import model.TipoCupom;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CupomServiceTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 11, 20);

    private CupomService service;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        service = new CupomService();

        pedido = new Pedido();
        ItemCardapio combo = new ItemCardapio("Combo Família", new BigDecimal("100.00"), "Combos");
        pedido.adicionarItem(new ItemPedido(combo, 1));

        service.cadastrar(new Cupom("DEZOFF", TipoCupom.PERCENTUAL,
                new BigDecimal("10"), BigDecimal.ZERO, LocalDate.of(2026, 11, 30)));
    }

    @Test
    @DisplayName("Deve aplicar 10% de desconto em um pedido de R$ 100,00")
    void deveAplicarDescontoPercentual() {
        BigDecimal total = service.aplicarCupom(pedido, "DEZOFF", HOJE);

        assertThat(total).isEqualByComparingTo("90.00");
    }

    @Test
    @DisplayName("Deve recusar um cupom vencido")
    void deveRecusarCupomVencido() {
        service.cadastrar(new Cupom("VENCIDO", TipoCupom.PERCENTUAL,
                new BigDecimal("10"), BigDecimal.ZERO, LocalDate.of(2026, 11, 10)));

        assertThatThrownBy(() -> service.aplicarCupom(pedido, "VENCIDO", HOJE))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("O cupom VENCIDO está vencido.");

        assertThat(pedido.getTotal()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Deve recusar o cupom quando o pedido não atinge o valor mínimo")
    void deveRecusarPedidoAbaixoDoMinimo() {
        service.cadastrar(new Cupom("PEDIDOGRANDE", TipoCupom.PERCENTUAL,
                new BigDecimal("15"), new BigDecimal("150.00"), LocalDate.of(2026, 11, 30)));

        assertThatThrownBy(() -> service.aplicarCupom(pedido, "PEDIDOGRANDE", HOJE))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("valor mínimo");
    }

    @Test
    @DisplayName("Cupom de valor fixo maior que o pedido deve resultar em total R$ 0,00")
    void deveZerarTotalComCupomMaiorQueOPedido() {
        service.cadastrar(new Cupom("CENTOECINQUENTA", TipoCupom.VALOR_FIXO,
                new BigDecimal("150.00"), BigDecimal.ZERO, LocalDate.of(2026, 11, 30)));

        BigDecimal total = service.aplicarCupom(pedido, "CENTOECINQUENTA", HOJE);

        assertThat(total).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Deve recusar um segundo cupom no mesmo pedido")
    void deveRecusarSegundoCupom() {
        service.aplicarCupom(pedido, "DEZOFF", HOJE);

        assertThatThrownBy(() -> service.aplicarCupom(pedido, "DEZOFF", HOJE))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Este pedido já possui um cupom aplicado.");
    }
}