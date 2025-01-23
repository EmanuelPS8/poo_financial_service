import java.util.ArrayList;

/**
 * Classe para realizar as operações relacionadas o Produtos Alimentares de taxa reduzida
 */
public class TaxaReduzida extends ProdutosAlimentares{
    private ArrayList<Certificacoes> listaCertificacoes;

    public TaxaReduzida(String nome, String codigo, double valorUnitario, int quantidade, boolean biologico, ArrayList<Certificacoes> listaCertificacoes){
        super(nome, codigo, valorUnitario, quantidade,biologico);
        this.listaCertificacoes=listaCertificacoes;
    }

    @Override
    public String getDetalhesFicheiro() {

        String[] certificacoes = new String[4];
        for (int i = 0; i < listaCertificacoes.size(); i++) {
            certificacoes[i] = listaCertificacoes.get(i).toString();
        }

        String certificadosFinal = String.join(",", certificacoes);

        return "taxaReduzida/"+getNome() + "," + getCodigo() + "," + getValorUnitario() + "," + getQuantidade() + "," +
                getBiologico() +";"+ certificadosFinal;
    }

    /**
     * Implementação do cálculo de taxa, herdado da classe Produto
     * @param localizacao localização do Cliente
     * @return valor do IVA
     */
    @Override
    public double calculaTaxa(Localizacao localizacao){
        double taxa=0;

        if(localizacao.equals(Localizacao.CONTINENTE)) taxa=6;
        if(localizacao.equals(Localizacao.MADEIRA)) taxa=5;
        if(localizacao.equals(Localizacao.ACORES)) taxa=4;

        if(this.listaCertificacoes.size() ==4) taxa-=1;

        if(this.getBiologico()) taxa*=0.9;

        return taxa;
    }

    @Override
    public double getPrecoComIva(Localizacao localizacao) {
        return this.getPrecoSemIva() + this.getPrecoSemIva()*(calculaTaxa(localizacao)/100.0);
    }

    @Override
    public void editarAtributosEspecificos(PooFinancialServices central){
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Editar Produto De Taxa Reduzida===");
            System.out.println("1. Alterar Campo \"Biológico\"");
            System.out.println("2. Alterar Especificações");
            System.out.println("3. Sair");
            System.out.println("Escolha uma opção:");

            int opcao = central.lerInteiroValido();
            switch (opcao) {
                case 1:
                    this.setBiologico((central.pedirBiologico() ==1));
                    System.out.println("Campo   \"Biológico\" modificado com sucesso!");
                    break;
                case 2:
                    this.setListaCertificacoes(central.pedirCertificacoes());
                    System.out.println("Especificações modificadas com sucesso!");
                    break;
                case 3:
                    continuar=false;
                    break;
                default:
                    System.out.println("Opção inválida.Tente novamente.");
            }
        }
    }

    //getters
    public ArrayList<Certificacoes> getListaCertificacoes(){
        return this.listaCertificacoes;
    }

    //setters
    public void setListaCertificacoes(ArrayList<Certificacoes> listaCertificacoes){
        this.listaCertificacoes=listaCertificacoes;
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central) + ", Especificações: " + listaCertificacoes;
    }
}
