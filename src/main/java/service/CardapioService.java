package service;
import exception.RegraNegocioException;
import model.ItemCardapio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardapioService {

    private final List<ItemCardapio> itens = new ArrayList<>();

    public ItemCardapio cadastrar(String nome, BigDecimal preco, String categoria) {
        validarTexto(nome, "O nome do item é obrigatório.");
        validarTexto(categoria, "A categoria do item é obrigatória.");
        validarPreco(preco);

        if (buscarPorNome(nome) != null) {
            throw new RegraNegocioException("Já existe um item com o nome " + nome + ".");
        }

        ItemCardapio item = new ItemCardapio(nome.trim(), preco, categoria.trim());
        itens.add(item);
        return item;
    }

    public void editarPreco(String nome, BigDecimal novoPreco) {
        validarPreco(novoPreco);
        buscarExistente(nome).setPreco(novoPreco);
    }

    public void desativar(String nome) {
        buscarExistente(nome).setAtivo(false);
    }

    public List<ItemCardapio> listarAtivos() {
        return itens.stream()
                .filter(ItemCardapio::isAtivo)
                .toList();
    }

    private ItemCardapio buscarPorNome(String nome) {
        for (ItemCardapio item : itens) {
            if (item.getNome().equalsIgnoreCase(nome.trim())) {
                return item;
            }
        }
        return null;
    }

    private ItemCardapio buscarExistente(String nome) {
        ItemCardapio item = buscarPorNome(nome);
        if (item == null) {
            throw new RegraNegocioException("Item não encontrado: " + nome + ".");
        }
        return item;
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O preço deve ser maior que zero.");
        }
    }

    private void validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException(mensagem);
        }
    }
}