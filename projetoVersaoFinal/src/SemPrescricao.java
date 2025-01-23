/**
 * Classe para realizar as operações com os Produtos de farmácia sem prescrição
 */
public class SemPrescricao extends ProdutosFarmacia{

    private CategoriasFarmacia categoria;

    public SemPrescricao(String nome, String codigo, double valorUnitario, int quantidade, CategoriasFarmacia categoria) {
        super(nome, codigo, valorUnitario, quantidade);
        this.categoria=categoria;
    }

    @Override
    public String getDetalhesFicheiro() {
        return "farmaciaSem/"+getNome() + "," + getCodigo() + "," + getValorUnitario() + "," + getQuantidade() + ";" +
                categoria;
    }

    /**
     * Implementação do cálculo de taxa, herdado da classe Produto
     * @param localizacao localização do Cliente
     * @return valor do IVA
     */
    @Override
    public double calculaTaxa(Localizacao localizacao) {
        double taxa=0;

        if(localizacao.equals(Localizacao.CONTINENTE)) taxa=23;
        if(localizacao.equals(Localizacao.MADEIRA)) taxa=23;
        if(localizacao.equals(Localizacao.ACORES)) taxa=23;

        if (categoria.equals(CategoriasFarmacia.ANIMAIS)) taxa-=1;

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
            System.out.println("\n===Editar Produto Farmácia Sem Prescrição===");
            System.out.println("1. Alterar Categoria do Produto");
            System.out.println("2. Sair");
            System.out.println("Escolha uma opção:");

            int opcao = central.lerInteiroValido();
            switch (opcao) {
                case 1:
                    this.setCategoria(central.pedirCategoriaFarmacia());
                    System.out.println("Categoria do Produto modificada com sucesso!");
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
    public CategoriasFarmacia getCategoria(){
        return this.categoria;
    }

    //setter
    public void setCategoria(CategoriasFarmacia categoria){
        this.categoria=categoria;
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central)+", Tem prescrição: false" + ", Categoria do Produto de Farmácia: " + this.categoria;
    }

}
