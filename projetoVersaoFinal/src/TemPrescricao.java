/**
 * Clsse para realizar as operações com os Produtos de farmácia com prescrição
 */
public class TemPrescricao extends ProdutosFarmacia{
    private String medicoPrescritor;

    public TemPrescricao(String nome, String codigo, double valorUnitario, int quantidade, String medicoPrescritor) {
        super(nome, codigo, valorUnitario, quantidade);
        this.medicoPrescritor=medicoPrescritor;
    }

    @Override
    public String getDetalhesFicheiro() {
        return "farmaciaCom/"+getNome() + "," + getCodigo() + "," + getValorUnitario() + "," + getQuantidade() + ";" +
                medicoPrescritor;
    }

    /**
     * Implementação do cálculo de taxa, herdado da classe Produto
     * @param localizacao localização do Cliente
     * @return valor do IVA
     */
    @Override
    public double calculaTaxa(Localizacao localizacao) {
        double taxa=0;

        if(localizacao.equals(Localizacao.CONTINENTE)) taxa=6;
        if(localizacao.equals(Localizacao.MADEIRA)) taxa=5;
        if(localizacao.equals(Localizacao.ACORES)) taxa=4;

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
            System.out.println("\n===Editar Produto Farmácia Com Prescrição===");
            System.out.println("1. Alterar Médico Prescritor");
            System.out.println("2. Sair");
            System.out.println("Escolha uma opção:");

            int opcao = central.lerInteiroValido();
            switch (opcao) {
                case 1:
                    this.setMedicoPrescritor(central.pedirMedicoPrescritor());
                    System.out.println("Médico prescritor modificado com sucesso!");
                    break;
                case 2:
                    continuar=false;
                    break;
                default:
                    System.out.println("Opção inválida.Tente novamente.");
            }
        }
    }

    //getters
    public String getMedicoPrescritor(){
        return this.medicoPrescritor;
    }

    //setters
    public void setMedicoPrescritor(String medicoPrescritor){
        this.medicoPrescritor=medicoPrescritor;
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central)+", Tem prescrição: true, Médico prescritor: "+this.medicoPrescritor;
    }

}
