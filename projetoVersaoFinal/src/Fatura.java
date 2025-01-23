import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Representa uma fatura do sistema
 * Contendo uma lista de produtos
 */
public class Fatura implements Serializable {
    private String numFatura;
    private Cliente cliente;
    private LocalDate data;
    private ArrayList<Produtos> listaProdutos = new ArrayList<>();

    /**
     * Contrutor da classe Fatura
     * @param numFatura numero da fatura
     * @param cliente Cliente associado
     * @param data data de emissão
     * @param listaProdutos lista de produtos
     */
    public Fatura(String numFatura,Cliente cliente,LocalDate data,ArrayList<Produtos> listaProdutos){
        this.numFatura=numFatura;
        this.cliente=cliente;
        this.data=data;
        this.listaProdutos=listaProdutos;
    }

    /**
     * Adiciona um produto a fatura
     * @param produto Produto a ser adicionado
     */
    public void adicionaProduto(Produtos produto){
        listaProdutos.add(produto);
    }

    /**
     * Calcula o valor total da fatura sem o IVA
     * @param central Instancia central para arredondamento
     * @return valor total sem IVA
     */
    public double calculaValorTotalSemIva(PooFinancialServices central){
        double total = 0;
        for (Produtos p: listaProdutos){
            total += p.getPrecoSemIva();
        }
        return central.arredondarNumero(total);
    }

    /**
     *
     * @param localizacao localização do cliente para calcular IVA
     * @param central Instância central para arredondamento
     * @return valor total com IVA
     */
    public double calculaValorTotalComIva(Localizacao localizacao,PooFinancialServices central){
        double total = 0;
        for (Produtos p: listaProdutos){
            total += p.getPrecoComIva(localizacao);
        }
        return central.arredondarNumero(total);
    }

    /**
     *
     * @param localizacao localização do cliente para calcular IVA
     * @param central Instância central para arredondamento
     * @return valor total do IVA
     */
    public double getTotalIva(Localizacao localizacao,PooFinancialServices central){
        double total = 0;
        for (Produtos p: listaProdutos){
            total += p.getPrecoComIva(localizacao) - p.getPrecoSemIva();
        }
        return central.arredondarNumero(total);
    }

    public int quantidadeProdutos(){
        int total=0;
        for(Produtos produto: this.listaProdutos){
            total+=produto.getQuantidade();
        }
        return total;
    }

    //getters
    public String getNumFatura(){
        return this.numFatura;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public LocalDate getData() {
        return data;
    }
    public ArrayList<Produtos> getListaProdutos() {
        return listaProdutos;
    }

    //setters
    public void setNumFatura(String numFatura) {
        this.numFatura = numFatura;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public void setListaProdutos(ArrayList<Produtos> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    /**
     * Apresenta os produtos
     * @param central Instância da classe centrar
     */
    public void apresentarProdutos(PooFinancialServices central) {
        for (Produtos produto : this.listaProdutos) {
            System.out.println(produto.toString(this.cliente.getLocalizacao(),central));
        }
    }

    /**
     * Apresenta a fatura
     * @param central Instância da classe centrar
     */
    public void apresentarFatura(PooFinancialServices central){
        System.out.println(toString() + this.cliente.toString());
        apresentarProdutos(central);
        System.out.println("Total da Fatura sem IVA: "+calculaValorTotalSemIva(central)+", Total do IVA: " + getTotalIva(this.cliente.getLocalizacao(),central) + ", Total da Fatura com IVA: "+ calculaValorTotalComIva(this.cliente.getLocalizacao(),central));
    }

    /**
     * Imprime os dados da fatura
     * @return dados da fatura
     */
    public String toString(){
        return "Número fatura: "+ numFatura + ", Data emissão Fatura: "+ data;
    }
}
