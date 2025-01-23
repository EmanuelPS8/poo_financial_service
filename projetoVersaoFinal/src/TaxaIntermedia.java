/**
 * Classe para realizar as operações com os Produtos de taxa intermédia
 */
public class TaxaIntermedia extends ProdutosAlimentares{
    private CategoriasAlimentos categoria;

    public TaxaIntermedia(String nome, String codigo, double valorUnitario, int quantidade, boolean Biologico, CategoriasAlimentos categoria) {
        super(nome, codigo, valorUnitario, quantidade, Biologico);
        this.categoria=categoria;
    }

    /**
     * Implementação do cálculo de taxa, herdado da classe Produto
     * @param localizacao localização do Cliente
     * @return valor do IVA
     */
    @Override
    public double calculaTaxa(Localizacao localizacao){
        double taxa=0;

        if(localizacao.equals(Localizacao.CONTINENTE)) taxa=13;
        if(localizacao.equals(Localizacao.MADEIRA)) taxa=12;
        if(localizacao.equals(Localizacao.ACORES)) taxa=9;

        if (categoria.equals(CategoriasAlimentos.VINHO)) taxa+=1;
        if(this.getBiologico()) taxa*=0.9;

        return taxa;
    }

    @Override
    public String getDetalhesFicheiro() {
        return "taxaIntermedia/"+ getNome() + "," + getCodigo() + "," + getValorUnitario() + "," + getQuantidade() + "," +
                getBiologico() + ";" + categoria;
    }

    @Override
    public double getPrecoComIva(Localizacao localizacao) {
        return this.getPrecoSemIva() + this.getPrecoSemIva()*(calculaTaxa(localizacao)/100);
    }

    @Override
    public void editarAtributosEspecificos(PooFinancialServices central){
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Editar Produto De Taxa Intermédia===");
            System.out.println("1. Alterar Campo\"Biológico\"");
            System.out.println("2. Alterar Categoria do ALimento");
            System.out.println("3. Sair");
            System.out.println("Escolha uma opção:");

            int opcao = central.lerInteiroValido();
            switch (opcao) {
                case 1:
                    this.setBiologico((central.pedirBiologico() ==1));
                    System.out.println("Campo\"Biológico\" modificado com sucesso!");
                    break;
                case 2:
                    this.setCategoria(central.pedirCategoriaAlimento());
                    System.out.println("Categoria modificada com sucesso!");
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
    public CategoriasAlimentos getCategoria(){
        return this.categoria;
    }

    //setter
    public void setCategoria(CategoriasAlimentos categoria){
        this.categoria=categoria;
    }

    @Override
    public String toString(Localizacao localizacao,PooFinancialServices central){
        return super.toString(localizacao,central) + ", Categoria do alimento: " + this.categoria;
    }
}

