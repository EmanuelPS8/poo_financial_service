import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Representa um cliente do sistema, contendo informações pessoais e uma lista de fatura
 */
public class Cliente implements Serializable {
    private String nome;
    private String nif;
    private Localizacao localizacao;
    /**
     * Lista de faturas do cliente
     */
    private ArrayList<Fatura> listaDeFaturas;

    /**
     * Construtos da classe Cliente
     * @param nome nome do cliente
     * @param nif NIF do cliente
     * @param localizacao localização cliente
     */
    public Cliente(String nome, String nif, Localizacao localizacao) {
        this.nome = nome;
        this.nif = nif;
        this.localizacao = localizacao;
        this.listaDeFaturas=new ArrayList<>();
    }

    /**
     * Adiciona uma fatura a lista
     * @param fatura Objeto do tipo fatura
     */
    public void adicionarFatura(Fatura fatura){
        listaDeFaturas.add(fatura);
    }

    //getters
    public String getNome() {
        return this.nome;
    }
    public Localizacao getLocalizacao() {
        return this.localizacao;
    }
    public String getNif(){
        return this.nif;
    }
    public ArrayList<Fatura> getListaDeFaturas() {
        return listaDeFaturas;
    }
    public int getQuantidadeFaturas(){ return listaDeFaturas.size(); }

    //Setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
    public void setNif(String nif) {
        this.nif = nif;
    }
    public void setListaDeFaturas(ArrayList<Fatura> listaDeFaturas) {
        this.listaDeFaturas = listaDeFaturas;
    }

    /**
     * Imprime as informações de um cliente
     * @return informações do cliente
     */
    @Override
    public String toString() {
        return " Cliente: " + nome + ", NIF: " + nif + ", Localização: " + localizacao;
    }
}
