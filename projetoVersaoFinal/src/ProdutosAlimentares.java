/**
 * Classe específica para produtos alimentares, herdada de Produtos
 */
public abstract class ProdutosAlimentares extends Produtos  {
    private boolean Biologico;


    public ProdutosAlimentares(String nome,String codigo, double valorUnitario, int quantidade, boolean Biologico) {
        super(nome, codigo, valorUnitario, quantidade);
        this.Biologico = Biologico;
    }

    //getters
    public boolean getBiologico(){
        return this.Biologico;
    }

    //setters
    public void setBiologico(boolean biologico) {
        this.Biologico = biologico;
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central) + ", Biológico: " + this.Biologico;
    }

}
