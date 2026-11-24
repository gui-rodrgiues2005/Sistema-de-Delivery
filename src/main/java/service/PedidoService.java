package service;

import exception.RegraNegocioException;
import model.ItemCardapio;
import model.ItemPedido;
import model.Pedido;

public class PedidoService {

    public void adicionarItem(Pedido pedido, ItemCardapio item, int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }
        if (!item.isAtivo()) {
            throw new RegraNegocioException("O item " + item.getNome() + " não está disponível.");
        }
        pedido.adicionarItem(new ItemPedido(item, quantidade));
    }

    public void removerItem(Pedido pedido, String nomeItem) {
        ItemPedido encontrado = null;
        for (ItemPedido itemPedido : pedido.getItens()) {
            if (itemPedido.getItem().getNome().equalsIgnoreCase(nomeItem)) {
                encontrado = itemPedido;
                break;
            }
        }
        if (encontrado == null) {
            throw new RegraNegocioException("O item " + nomeItem + " não está no pedido.");
        }
        pedido.removerItem(encontrado);
    }

    public void fecharPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new RegraNegocioException("Não é possível fechar um pedido sem itens.");
        }
        pedido.fechar();
    }
}


