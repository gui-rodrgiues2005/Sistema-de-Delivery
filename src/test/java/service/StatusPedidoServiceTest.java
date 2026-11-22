package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import exception.RegraNegocioException;
import model.Pedido;
import model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StatusPedidoServiceTest {

    private NotificacaoFake notificacao;
    private StatusPedidoService service;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        notificacao = new NotificacaoFake();
        service = new StatusPedidoService(notificacao);
        pedido = new Pedido();
        pedido.fechar();
    }

    @Test
    @DisplayName("Deve avançar o status de Recebido para Em preparo")
    void deveAvancarDeRecebidoParaEmPreparo() {
        service.alterarStatus(pedido, StatusPedido.EM_PREPARO);

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.EM_PREPARO);
    }

    @Test
    @DisplayName("Não deve permitir voltar de Entregue para Em preparo")
    void naoDeveVoltarDeEntregueParaEmPreparo() {
        pedido.setStatus(StatusPedido.ENTREGUE);

        assertThatThrownBy(() -> service.alterarStatus(pedido, StatusPedido.EM_PREPARO))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Um pedido entregue não pode mudar de status.");

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.ENTREGUE);
    }

    @Test
    @DisplayName("Não deve permitir cancelar um pedido que já saiu para entrega")
    void naoDeveCancelarAposSairParaEntrega() {
        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);

        assertThatThrownBy(() -> service.alterarStatus(pedido, StatusPedido.CANCELADO))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Não é possível cancelar um pedido que já saiu para entrega.");
    }

    @Test
    @DisplayName("Deve enviar uma notificação a cada mudança de status")
    void deveNotificarACadaMudanca() {
        service.alterarStatus(pedido, StatusPedido.EM_PREPARO);
        service.alterarStatus(pedido, StatusPedido.SAIU_PARA_ENTREGA);
        service.alterarStatus(pedido, StatusPedido.ENTREGUE);

        assertThat(notificacao.getMensagens()).containsExactly(
                "Seu pedido agora está: Em preparo.",
                "Seu pedido agora está: Saiu para entrega.",
                "Seu pedido agora está: Entregue.");
    }

    private static class NotificacaoFake implements NotificacaoService {

        private final List<String> mensagens = new ArrayList<>();

        @Override
        public void notificar(Pedido pedido, String mensagem) {
            mensagens.add(mensagem);
        }

        List<String> getMensagens() {
            return mensagens;
        }
    }
}