/**
 * Classe específica para produtos farmacêuticos, herdada de Produtos
 */
public abstract class ProdutosFarmacia extends Produtos {

    public ProdutosFarmacia(String nome, String codigo,double valorUnitario, int quantidade) {
        super(nome, codigo, valorUnitario, quantidade);
    }

}
