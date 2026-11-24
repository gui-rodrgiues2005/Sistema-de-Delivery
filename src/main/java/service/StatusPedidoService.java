package service;

import exception.RegraNegocioException;
import model.Pedido;
import model.StatusPedido;

public class StatusPedidoService {

    private final NotificacaoService notificacaoService;

    public StatusPedidoService(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    public void alterarStatus(Pedido pedido, StatusPedido novoStatus) {
        StatusPedido atual = pedido.getStatus();

        if (atual == null) {
            throw new RegraNegocioException("O pedido ainda não foi fechado.");
        }
        if (atual == StatusPedido.ENTREGUE || atual == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Um pedido " + atual.getDescricao().toLowerCase()
                    + " não pode mudar de status.");
        }
        if (novoStatus == StatusPedido.CANCELADO && atual == StatusPedido.SAIU_PARA_ENTREGA) {
            throw new RegraNegocioException("Não é possível cancelar um pedido que já saiu para entrega.");
        }
        if (novoStatus != StatusPedido.CANCELADO && novoStatus != proximoStatus(atual)) {
            throw new RegraNegocioException("Não é possível mudar o status de "
                    + atual.getDescricao() + " para " + novoStatus.getDescricao() + ".");
        }

        pedido.setStatus(novoStatus);
        notificacaoService.notificar(pedido, "Seu pedido agora está: " + novoStatus.getDescricao() + ".");
    }

    private StatusPedido proximoStatus(StatusPedido atual) {
        return switch (atual) {
            case RECEBIDO -> StatusPedido.EM_PREPARO;
            case EM_PREPARO -> StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> StatusPedido.ENTREGUE;
            default -> null;
        };
    }
}