package service;

import model.Pedido;

public interface NotificacaoService {

    void notificar(Pedido pedido, String mensagem);
}