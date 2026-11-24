package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Cupom {

    private String codigo;
    private TipoCupom tipo;
    private BigDecimal valor;
    private BigDecimal valorMinimo;
    private LocalDate validade;

    public Cupom(String codigo, TipoCupom tipo, BigDecimal valor, BigDecimal valorMinimo, LocalDate validade) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.valor = valor;
        this.valorMinimo = valorMinimo;
        this.validade = validade;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoCupom getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public BigDecimal calcularDesconto(BigDecimal subtotal) {
        BigDecimal desconto;
        if (tipo == TipoCupom.PERCENTUAL) {
            desconto = subtotal.multiply(valor).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else {
            desconto = valor;
        }
        return desconto.min(subtotal);
    }
}