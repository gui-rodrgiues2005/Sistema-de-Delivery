package model;

import java.math.BigDecimal;

public class ItemPedido {

    private ItemCardapio item;
    private int quantidade;

    public ItemPedido(ItemCardapio item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public ItemCardapio getItem() {
        return item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSubtotal() {
        return item.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }
}