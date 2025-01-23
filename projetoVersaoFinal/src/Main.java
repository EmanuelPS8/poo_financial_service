import java.io.File;

/**
 *Classe principal do sistema financeiro "POO Financial Services".
 *
 * Esta classe é o ponto de entrada do programa. Ela inicializa o sistema,
 * verifica os arquivos de dados existentes (objeto ou texto) para carregar informações,
 * e exibe o menu principal para a interação com o usuário.
 *
 * @author Emanuel Pereira Schlickmann
 * @author Fábio Campos Fernandes
 * @version 1.0
 */
public class Main {

    public static void main(String[] args){

    PooFinancialServices sistemaCentral=new PooFinancialServices();

    String fileObjetos = "ficheiro.obj";
    String fileTexto = "dados.txt";
    File file= new File(fileObjetos);

    if (file.exists() && file.length() > 0){
        sistemaCentral.lerDadosObjetos(fileObjetos);
    } else {
        sistemaCentral.lerDados(fileTexto);
    }

    sistemaCentral.exibirMenu();

    sistemaCentral.escreverDadosObjetos(fileObjetos);
    sistemaCentral.escreverDados(fileTexto);
    }
}