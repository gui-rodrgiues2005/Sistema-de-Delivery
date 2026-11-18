package model;

import java.math.BigDecimal;

public class ItemCardapio {

    private String nome;
    private BigDecimal preco;
    private String categoria;
    private boolean ativo;

    public ItemCardapio(String nome, BigDecimal preco, String categoria) {
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.ativo = true;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}