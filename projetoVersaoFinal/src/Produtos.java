import java.io.Serializable;

/**
 * Representa um produto do sistema
 */
public abstract class Produtos implements Serializable {
    private String nome;
    private String codigo;
    private double valorUnitario;
    private int quantidade;

    /**
     * Contrutor da classe Produto
     * @param nome nome do produto
     * @param codigo codigo do produto
     * @param valorUnitario valor unitário do produto
     * @param quantidade quantidade de um produto
     */
    public Produtos(String nome, String codigo, double valorUnitario, int quantidade) {
        this.nome = nome;
        this.codigo = codigo;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    /**
     * Preço do produto sem IVA
     * @return preço sem IVA
     */
    public double getPrecoSemIva(){
        return quantidade*valorUnitario;
    }

    /**
     * Valor do IVA
     * @param localizacao localização do cliente para fins de calculo
     * @return valor do IVA
     */
    public double getValorIva(Localizacao localizacao){
        return (this.getPrecoComIva(localizacao) - this.getPrecoSemIva());
    }

    /**
     * Método abstrato para calcular preço com IVA
     * @param localizacao localização do Cliente
     * @return valor com IVA
     */
    public abstract double getPrecoComIva(Localizacao localizacao);

    /**
     * calcula a o IVA
     * @param localizacao localização do Cliente
     * @return preço do IVA
     */
    public abstract double calculaTaxa(Localizacao localizacao);

    /**
     *Detalhes para adicionar no ficheiro
     * @return
     */
    public abstract String getDetalhesFicheiro();

    //getters
    public String getNome() {
        return nome;
    }
    public String getCodigo() {
        return codigo;
    }
    public double getValorUnitario() {
        return valorUnitario;
    }
    public int getQuantidade() {
        return quantidade;
    }

    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Edição do produto
     * @param central Instância da classe principal
     * @param fatura Fatura a qual o produto pertence
     */
    public  void editarProduto(PooFinancialServices central,Fatura fatura){
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Editar Produto===");
            System.out.println("1. Alterar Nome");
            System.out.println("2. Alterar Valor Unitário");
            System.out.println("3. Alterar Quantidade");
            System.out.println("4. Alterar Código");
            System.out.println("5. Outro Atributos Específico");
            System.out.println("6. Sair");
            System.out.println("Escolha uma opção: ");

            int opcao = central.lerInteiroValido();

            switch (opcao) {
                case 1:
                    this.setNome(central.pedirNome());
                    System.out.println("Nome modificado com sucesso!");
                    break;
                case 2:
                    this.setValorUnitario(central.pedirValorUnitario());
                    System.out.println("Valor modificado com sucesso!");
                    break;
                case 3:
                    this.setQuantidade(central.pedirQuantidade());
                    System.out.println("Quantidade modificada com sucesso!");
                    break;
                case 4:
                    this.setCodigo(central.pedirCodigoProduto(fatura.getListaProdutos()));
                    System.out.println("Código do Produto modificado com sucesso!");
                    break;
                case 5:
                    editarAtributosEspecificos(central);
                    break;
                case 6:
                    continuar = false;
                    System.out.println("Saindo do menu de edição");
                    break;
                default:
                    System.out.println("Opção inválida.Tente novamente.");
            }
        }
    }
    public abstract void editarAtributosEspecificos(PooFinancialServices central);

    /**
     * Imprime os dados do produtos
     * @param localizacao localização do cliente
     * @param central Intância da classe principal
     * @return dados do produto
     */
    public String toString(Localizacao localizacao,PooFinancialServices central) {
        return "Nome do produto: " + this.nome +
                ", Valor unitário: " + this.valorUnitario +
                ", Quantidade: " + this.quantidade  +
                ", Código do produto: " +this.codigo +
                ", Valor total sem IVA: " + central.arredondarNumero(getPrecoSemIva()) +
                ", Taxa do IVA: " + central.arredondarNumero(calculaTaxa(localizacao)) + "% " +
                ", Valor IVA: " + central.arredondarNumero(getValorIva(localizacao)) +
                ", Valor total com IVA: "+ central.arredondarNumero(getPrecoComIva(localizacao));
    }
}
