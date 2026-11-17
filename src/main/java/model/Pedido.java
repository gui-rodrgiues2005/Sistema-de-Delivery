package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private List<ItemPedido> itens = new ArrayList<>();

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void adicionarItem(ItemPedido itemPedido) {
        itens.add(itemPedido);
    }
}