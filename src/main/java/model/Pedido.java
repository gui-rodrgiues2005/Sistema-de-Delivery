package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private List<ItemPedido> itens = new ArrayList<>();
    private boolean fechado;

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void adicionarItem(ItemPedido itemPedido) {
        itens.add(itemPedido);
    }

    public void removerItem(ItemPedido itemPedido) {
        itens.remove(itemPedido);
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemPedido itemPedido : itens) {
            subtotal = subtotal.add(itemPedido.getSubtotal());
        }
        return subtotal;
    }

    public boolean isFechado() {
        return fechado;
    }

    public void fechar() {
        this.fechado = true;
    }
}