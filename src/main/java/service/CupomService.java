package service;

import exception.RegraNegocioException;
import model.Cupom;
import model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CupomService {

    private final Map<String, Cupom> cupons = new HashMap<>();

    public void cadastrar(Cupom cupom) {
        cupons.put(cupom.getCodigo().toUpperCase(), cupom);
    }

    public BigDecimal aplicarCupom(Pedido pedido, String codigo, LocalDate dataAtual) {
        Cupom cupom = cupons.get(codigo.toUpperCase());

        if (cupom == null) {
            throw new RegraNegocioException("O cupom " + codigo + " não existe.");
        }
        if (pedido.getCupom() != null) {
            throw new RegraNegocioException("Este pedido já possui um cupom aplicado.");
        }
        if (dataAtual.isAfter(cupom.getValidade())) {
            throw new RegraNegocioException("O cupom " + codigo + " está vencido.");
        }
        if (pedido.getSubtotal().compareTo(cupom.getValorMinimo()) < 0) {
            throw new RegraNegocioException("O pedido não atinge o valor mínimo de R$ "
                    + cupom.getValorMinimo() + " para este cupom.");
        }

        pedido.setCupom(cupom);
        return pedido.getTotal();
    }
}