/**
 * Classe para realizar as operações relacionadas o Produtos Alimentares de taxa normal
 */
public class TaxaNormal extends ProdutosAlimentares{


    public TaxaNormal(String nome, String codigo, double valorUnitario, int quantidade, boolean Biologico) {
        super(nome, codigo, valorUnitario, quantidade, Biologico);
    }

    @Override
    public String getDetalhesFicheiro() {
        return "taxaNormal/"+getNome() + "," + getCodigo() + "," + getValorUnitario() + "," + getQuantidade() + "," +
                getBiologico();
    }

    /**
     * Implementação do cálculo de taxa, herdado da classe Produto
     * @param localizacao localização do Cliente
     * @return valor do IVA
     */
    @Override
    public double calculaTaxa(Localizacao localizacao){
        double taxa=0;

        if(localizacao.equals(Localizacao.CONTINENTE)) taxa=23;
        if(localizacao.equals(Localizacao.MADEIRA)) taxa=22;
        if(localizacao.equals(Localizacao.ACORES)) taxa=16;

        if(this.getBiologico()) taxa*=0.9;

        return taxa;
    }

    @Override
    public void editarAtributosEspecificos(PooFinancialServices central){
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Editar Produto De Taxa Normal===");
            System.out.println("1. Alterar Campo\"Biológico\"");
            System.out.println("2. Sair");
            System.out.println("Escolha uma opção:");

            int opcao = central.lerInteiroValido();
            switch (opcao) {
                case 1:
                    this.setBiologico((central.pedirBiologico() ==1));
                    System.out.println("Campo\"Biológico\" modificado com sucesso!");
                    break;
                case 2:
                    continuar=false;
                    break;
                default:
                    System.out.println("Opção inválida.Tente novamente.");
            }
        }
    }

    @Override
    public double getPrecoComIva(Localizacao localizacao) {
        return this.getPrecoSemIva() + this.getPrecoSemIva()*(calculaTaxa(localizacao)/100.0);
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central);
    }
}
