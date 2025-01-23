import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *Classe principal para o gerenciamento de clientes e faturas no sistema financeiro
 *A classe implementa as funcionalidades para criar, editar e gerenciar clientes,
 *assim como criar e manipular as faturas desses clientes
 *
 * @author Emanuel Pereira Schlickmann
 * @author Fábio Campos Fernandes
 * @version 1.0
 */

public class PooFinancialServices {
    private Scanner scanner = new Scanner(System.in);
    /**
     * Lista de clientes gerenciadas pelo sistema
     */
    private ArrayList<Cliente> listaClientes;

    /**
     * Contrutor da classe
     * Inicializa a lista de clientes
     */
    public PooFinancialServices() {
        listaClientes = new ArrayList<>();
    }

    /**
     * Cria um novo clientes no sistema
     * Solicita dados ao utilizador, seu nome, NIF e localização
     */
    public void criaCliente() {
        System.out.println("\n=== Criar Cliente ===");

        String nome = pedirNome();
        String frase="Introduza o NIF do cliente: ";
        String nif = pedirNIF(frase);

        if (buscaClienteNIF(nif) == null) {
            Localizacao localizacao = escolherLocalizacao();
            System.out.println("Cliente criado com sucesso!");
            adicionarCliente(new Cliente(nome, nif, localizacao));
        } else {
            System.out.println("Falha ao criar cliente! Esse NIF já se encontra associado a um cliente.");
        }
    }

    /**
     * Permite editar as informaçãoes de um cliente
     * Solicita o NIF do cliente, e pode editar seu nome, NIF ou localização
     */
    public void editarCliente() {

        String frase="Introduza o NIF do cliente que quer editar: ";
        String nif = pedirNIF(frase);
        Cliente clienteParaEditar = buscaClienteNIF(nif);

        if (clienteParaEditar == null) {
            System.out.println("O cliente com o NIF " + nif + " não foi encontrado!");
        } else {
            System.out.println("Dados atuais: " + clienteParaEditar.toString());
            boolean continuar = true;

            while (continuar) {
                System.out.println("\n--- Editar Cliente ---");
                System.out.println("1. Alterar Nome");
                System.out.println("2. Alterar NIF");
                System.out.println("3. Alterar Localização");
                System.out.println("4. Sair do menu de edição");
                System.out.print("Escolha uma opção: ");

                int opcao = lerInteiroValido();

                switch (opcao) {
                    case 1:
                        String novoNome = pedirNome();
                        clienteParaEditar.setNome(novoNome);
                        System.out.println("Nome atualizado com sucesso.");
                        break;

                    case 2:
                        frase="Introduza o novo NIF: ";
                        String novoNif = pedirNIF(frase);
                        if(buscaClienteNIF(novoNif) ==null) {
                            clienteParaEditar.setNif(novoNif);
                            System.out.println("NIF atualizado com sucesso.");

                        }else System.out.println("O NIF já se encontra associado a um cliente!");
                        break;

                    case 3:
                        Localizacao novaLocalizacao = escolherLocalizacao();
                        clienteParaEditar.setLocalizacao(novaLocalizacao);
                        System.out.println("Localização atualizada com sucesso.");
                        break;
                    case 4:
                        System.out.println("Saindo do menu de edição...");
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opção inválida. Por favor, selecione uma das opções disponíveis!");
                }
            }
            System.out.println("Dados atualizados: " + clienteParaEditar);
        }
    }

    /**
     * Adiciona um cliente a lista
     * @param cliente Objeto cliente para ser adicionado
     *
     */
    public void adicionarCliente(Cliente cliente) {
        listaClientes.add(cliente);
    }

    /**
     * Remove um cliente da lista
     */
    public void removerCliente() {
        Cliente clienteParaRemover;

        String frase="Introduza o NIF do cliente a ser removido: ";
        String nif = pedirNIF(frase);

        clienteParaRemover = buscaClienteNIF(nif);

        if (clienteParaRemover == null)
            System.out.println("Falha ao remover! O NIF não está associado a nenhum cliente.");
        else {
            System.out.println("Remoção efetuada com sucesso!");
            listaClientes.remove(clienteParaRemover);
        }
    }

    /**
     *
     * @param nif NIF do cliente
     * @return O cliente correspondente ou  null se não encontrado
     */
    public Cliente buscaClienteNIF(String nif) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getNif().equals(nif)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Lista todos os clientes
     */
    public void listarClientes() {
        if (listaClientes.isEmpty()) System.out.println("Nenhum cliente existente!");
        else {
            for (Cliente cliente : listaClientes) {
                System.out.println(cliente.toString());
            }
        }
    }

    /**
     * Cria uma nova fatura
     */
    public void criarFatura() {

        System.out.println("\n=== Criar Fatura ===");

        String frase="Introduza o NIF do cliente ao qual pretende adicionar a fatura: ";
        String nif = pedirNIF(frase);
        Cliente cliente = buscaClienteNIF(nif);

        if (cliente == null) System.out.println("O NIF não está associado a nenhum cliente! ");
        else {
            System.out.println("Introduza o número da fatura: ");
            String numFatura = scanner.nextLine();

            Fatura fatura = buscaFatura(numFatura);

            if (fatura == null) {
                LocalDate date = escolherData();
                cliente.adicionarFatura(new Fatura(numFatura,cliente,date,pedirProdutos()));
                System.out.println("Fatura criada com sucesso!");

            } else System.out.println("O número de fatura dado, já se encontra associado a outra fatura.");
        }
    }

    /**
     * Permite editar uma fatura. Tanto seus dados, como os seus produtos
     */
    public void editarFatura() {

        String frase="Introduza o NIF do cliente a quem pertence a fatura: ";
        String nif = pedirNIF(frase);

        Cliente clienteFatura = buscaClienteNIF(nif);

        if (clienteFatura != null) {

            if (clienteFatura.getQuantidadeFaturas() != 0) {
                System.out.println("Lista de faturas do cliente: ");
                for (int i = 0; i < clienteFatura.getQuantidadeFaturas(); i++) {
                    System.out.println("\nFatura " + (i + 1) + " - " );
                    clienteFatura.getListaDeFaturas().get(i).apresentarFatura(this);
                }

                frase = "\nPor favor, escolha uma fatura válida: ";
                int numValido = metodoWhile(frase, 1, clienteFatura.getQuantidadeFaturas()) - 1;

                Fatura faturaParaEditar = clienteFatura.getListaDeFaturas().get(numValido);

                boolean continuar = true;
                while (continuar) {
                    System.out.println("\n=== Editar Fatura ===");
                    System.out.println("1- Editar número da fatura.");
                    System.out.println("2- Editar data de emissão da fatura.");
                    System.out.println("3- Editar produtos.");
                    System.out.println("4- Sair.");
                    System.out.print("Escolha uma opção: ");

                    int opcao = lerInteiroValido();

                    switch (opcao) {
                        case 1:
                            System.out.println("Introduza o novo número da fatura: ");
                            String novoNumero = scanner.nextLine();

                            if(buscaFatura(novoNumero) ==null) {
                                faturaParaEditar.setNumFatura(novoNumero);
                                System.out.println("Número modificado com sucesso!");
                            }
                            else System.out.println("Não foi possivel modificar o número!");
                            break;

                        case 2:
                            System.out.println("Introduza a nova data de emissão da fatura.");
                            LocalDate novaData = escolherData();
                            faturaParaEditar.setData(novaData);
                            System.out.println("Data modificada com sucesso!");
                            break;

                        case 3:
                            editarProduto(faturaParaEditar);
                            break;

                        case 4:
                            System.out.println("Saindo do menu de edição...");
                            continuar = false;
                            break;
                        default:
                            System.out.println("Por favor, introduza uma opção válida: ");
                    }
                }
            }
            else System.out.println("O cliente com NIF " + nif + " não tem nenhuma fatura registada!");
        }
        else System.out.println("O NIF " + nif +" não está associado a nenhum cliente!");
    }

    /**
     *
     * @param numeroFatura Numero da fatura
     * @return a fatura, e null se não existir
     */
    public Fatura buscaFatura(String numeroFatura) {
        for (Cliente cliente : listaClientes) {
            for (Fatura fatura : cliente.getListaDeFaturas()) {
                if (fatura.getNumFatura().equals(numeroFatura)) return fatura;
            }
        }
        return null;
    }

    /**
     * Lista todas as faturas
     */
    public void listarFaturas() {
        boolean encontrouFatura=false;

        for (Cliente cliente : listaClientes) {
            for (Fatura fatura : cliente.getListaDeFaturas()) {
                encontrouFatura=true;
                System.out.println("Número fatura: " + fatura.getNumFatura() + "," + cliente.toString() + ", Quantidade de produtos: " + fatura.quantidadeProdutos() + ", Total sem IVA: " + fatura.calculaValorTotalSemIva(this) + ", Total com IVA: " + fatura.calculaValorTotalComIva(cliente.getLocalizacao(),this));
            }
        }
        if(!encontrouFatura) System.out.println("Nenhuma fatura encontrada para os clientes!");
    }

    /**
     * Introduzir um numero de fatura, se existir, mostra a fatura completa
     */
    public void visualizarFatura() {
        System.out.println("Introduza o número da fatura que deseja visualizar: ");
        String numeroFatura = scanner.nextLine();

        Fatura faturaEncontrada = buscaFatura(numeroFatura);
        if (faturaEncontrada == null) System.out.println("Fatura com número " + numeroFatura + " não encontrada.");
        else {
            System.out.println("\n=== Detalhes da Fatura ===");
            faturaEncontrada.apresentarFatura(this);
        }
    }

    /**
     * Método para relizar a edição de um produto a fatura
     * @return lista de produtos
     */
    public ArrayList<Produtos> pedirProdutos() {
        ArrayList<Produtos> listaProdutos = new ArrayList<>();

        String nome = null;
        int quantidade = 0;
        double valorUnitario = 0;
        String codigo = null;

        boolean continuar=true;

        while (continuar) {
            System.out.println("\n===============================");
            System.out.println("Menu de Adição de Produtos");
            System.out.println("1. Adicionar Produto Alimentar");
            System.out.println("2. Adicionar Produto Farmacêutico");
            System.out.println("3. Sair do menu de adição de produtos");
            System.out.println("===============================\n");

            String frase="Por favor, selecione uma opção válida: ";
            int escolha = metodoWhile(frase,1,3);

            if (escolha == 1 || escolha == 2) {
                nome = pedirNome();
                quantidade = pedirQuantidade();
                valorUnitario = pedirValorUnitario();
                codigo=pedirCodigoProduto(listaProdutos);
            }

            if (escolha == 1) {

                boolean biologio = (pedirBiologico() == 1);
                int opcao = pedirCategoriaIva();

                if(opcao==1) {
                    ArrayList<Certificacoes> listaCertificacoes = pedirCertificacoes();
                    listaProdutos.add(new TaxaReduzida(nome, codigo, valorUnitario, quantidade, biologio, listaCertificacoes));
                    System.out.println("Produto adicionado com sucesso!");

                } else if (opcao==2) {
                    CategoriasAlimentos categoriaAlimento= pedirCategoriaAlimento();
                    listaProdutos.add(new TaxaIntermedia(nome, codigo, valorUnitario, quantidade, biologio, categoriaAlimento));
                    System.out.println("Produto adicionado com sucesso!");

                } else if (opcao==3) {
                    listaProdutos.add(new TaxaNormal(nome, codigo, valorUnitario, quantidade, biologio));
                    System.out.println("Produto adicionado com sucesso!");
                }

            } else if (escolha == 2) {

                int opcao=pedirPrescricao();

                if(opcao==1) {
                    String medico = pedirMedicoPrescritor();
                    listaProdutos.add(new TemPrescricao(nome, codigo, valorUnitario, quantidade, medico));
                    System.out.println("Produto adicionado com sucesso!");

                } else if (opcao==2) {
                    CategoriasFarmacia categoria = pedirCategoriaFarmacia();
                    listaProdutos.add(new SemPrescricao(nome, codigo, valorUnitario, quantidade, categoria));
                    System.out.println("Produto adicionado com sucesso!");
                }

            } else if (escolha ==3) {
                if(listaProdutos.isEmpty()){
                    System.out.println("ERRO! Ainda não adicionou um produto. A criação " +
                            "de uma fatura requer que sejam adicionados um ou mais produtos. " +
                            "Por favor, adicione produtos antes de prosseguir.");
                }
                else continuar=false;
            }
        }
        return listaProdutos;
    }

    /**
     * Edição de um produto
     * @param faturaEditar dados da fatura para editar
     */
    public void editarProduto(Fatura faturaEditar) {

        Produtos produto = null;

        System.out.println("\n===Menu de edição de Produtos===");
        System.out.println("1- Adicionar um novo produto.");
        System.out.println("2- Excluir um produto.");
        System.out.println("3- Editar um produto.");
        System.out.println("4- Sair.");

        String frase = "Por favor, selecione uma opção";
        int opcao = metodoWhile(frase, 1, 4);

        if (opcao == 1) {

            System.out.println("Qual o tipo de produto? ");
            System.out.println("1- Produto Alimentar ");
            System.out.println("2- Produto de Farmacia ");

            int op = metodoWhile(frase, 1, 2);

            String nome = pedirNome();              //caracteristicas comuns
            String codigo = pedirCodigoProduto(faturaEditar.getListaProdutos());
            double valorUnitario = pedirValorUnitario();
            int quantidade = pedirQuantidade();

            if (op == 1) {
                boolean isBiologico = (pedirBiologico() == 1);
                int categoria = pedirCategoriaIva();

                if (categoria == 1) {
                    ArrayList<Certificacoes> certificacoes = pedirCertificacoes();
                    produto = new TaxaReduzida(nome, codigo, valorUnitario, quantidade, isBiologico, certificacoes);

                } else if (categoria == 2) {
                    CategoriasAlimentos categoriaAlimento = pedirCategoriaAlimento();
                    produto = new TaxaIntermedia(nome, codigo, valorUnitario, quantidade, isBiologico, categoriaAlimento);

                } else if (categoria == 3) {
                    produto = new TaxaNormal(nome, codigo, valorUnitario, quantidade, isBiologico);
                }
                faturaEditar.adicionaProduto(produto);
                System.out.println("Produto adicionado com sucesso!");
            }
            if (op == 2) {
                int terPrescricao = pedirPrescricao();

                if (terPrescricao == 1) {
                    String medico = pedirMedicoPrescritor();
                    produto = new TemPrescricao(nome, codigo, valorUnitario, quantidade, medico);
                }
                if (terPrescricao == 2) {
                    CategoriasFarmacia categoriaFarmacia = pedirCategoriaFarmacia();
                    produto = new SemPrescricao(nome, codigo, valorUnitario, quantidade, categoriaFarmacia);
                }
                faturaEditar.adicionaProduto(produto);
                System.out.println("Produto adicionado com sucesso!");
            }
        }
        if (opcao == 2) {
            System.out.println("Introduza o código do produto que quer remover");
            String codigo = scanner.nextLine();

            for (Produtos produtoRemover : faturaEditar.getListaProdutos()) {
                if (produtoRemover.getCodigo().equalsIgnoreCase(codigo)) {
                    faturaEditar.getListaProdutos().remove(produtoRemover);
                    System.out.println("O produto foi removido com sucesso!");
                    break;
                }
            }
        }
        if (opcao == 3) {

            Produtos produtoEditar=null;
            System.out.println("Introduza o código do produto que quer editar: ");
            String codigo = scanner.nextLine();

            for (Produtos produto1 : faturaEditar.getListaProdutos()) {
                if (produto1.getCodigo().equalsIgnoreCase(codigo)) {
                    produtoEditar = produto1;
                }
            }

            if(produtoEditar!=null) produtoEditar.editarProduto(this,faturaEditar);
            else System.out.println("Produto não encontrado!");
        }
    }

    /**
     * Validar a entrada numerica de um usuárioa
     * @param frase mensagem que será exibida ao usuario pedindo um entrada
     * @param opcao1 mínimo
     * @param opcao2 máximo
     * @return o valor válido
     */
    public int metodoWhile(String frase,int opcao1,int opcao2) {
        int opcao;
        do {
            System.out.println(frase);
            opcao = lerInteiroValido();
        } while (opcao < opcao1 || opcao > opcao2);
        return opcao;
    }

    public int lerInteiroValido(){
        int numero=-1;
        boolean entradaValida=false;

        while(!entradaValida){
            try{
                numero=Integer.parseInt(scanner.nextLine());
                entradaValida=true;
            } catch (NumberFormatException e){
                System.out.println("Erro: Por favor, insira uma opção válida.");
            }
        }
        return numero;
    }
    public double lerDoubleValido(){
        double numero=-1.0;
        boolean entradaValida=false;

        while(!entradaValida){
            try{
                numero=Double.parseDouble(scanner.nextLine());
                entradaValida=true;
            } catch (NumberFormatException e){
                System.out.println("Erro: Por favor, insira uma opção válida.");
            }
        }
        return numero;
    }

    public String pedirNome(){
        System.out.println("Nome: ");
        String nome = scanner.nextLine();

        return nome;
    }
    private String pedirNIF(String frase){
        String nif;

        while(true) {
            System.out.println(frase);
            nif = scanner.nextLine();

            if (validacaoNIF(nif)) {
                return nif;
            } else {
                System.out.println("NIF com formato inválido! Tente novamente. ");
            }
        }
    }

    public int pedirQuantidade(){
        System.out.println("Quantidade: ");
        int quantidade = lerInteiroValido();

        return quantidade;
    }
    public double pedirValorUnitario(){
        System.out.println("Valor unitário: ");
        Double valorUnitario = lerDoubleValido();

        return valorUnitario;
    }
    public String pedirCodigoProduto(ArrayList<Produtos> listaProdutos){
        Boolean continua=true;
        String codigo="";

        do {
            System.out.println("Por favor, Introduza o Código do Produto (deve ser diferente dos já existentes na fatura): ");
            codigo = scanner.nextLine();
            continua=CodigoJaExiste(codigo,listaProdutos);
        }while(continua);

        return codigo;
    }

    private int pedirCategoriaIva() {
        System.out.println("Categoria de IVA:");
        System.out.println("1 - Taxa reduzida");
        System.out.println("2 - Taxa Intermédia");
        System.out.println("3 - Taxa Normal");
        String frase = "Por favor, escolha uma das categorias de IVA: ";
        int opcao=metodoWhile(frase, 1, 3);

        return opcao;
    }

    /**
     * Pedir os certificados de taxa Reduzida
     * @return lista de certificados
     */
    public ArrayList<Certificacoes> pedirCertificacoes(){
        ArrayList<Certificacoes> listaCertificacoes=new ArrayList<>();
        Certificacoes certificacao=null;
        boolean continuar=true;
        int opcao;

        while(listaCertificacoes.size() < 4 && continuar){
            System.out.println("Certificações: 1.ISO22000 2.FSSC22000 3.HACCP 4.GMP");
            System.out.println("Introduza o número correspondente à certificação que pretende adicionar ao produto");

            opcao=lerInteiroValido();

            switch (opcao){
                case 1:
                    certificacao=Certificacoes.ISO22000;
                    break;
                case 2:
                    certificacao=Certificacoes.FSSC22000;
                    break;
                case 3:
                    certificacao=Certificacoes.HACCP;
                    break;
                case 4:
                    certificacao=Certificacoes.GMP;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            if(certificacao !=null){
                if(listaCertificacoes.contains(certificacao)) System.out.println("Erro ao adicionar Certificação! A certificação já se encontra presente.");
                else{
                    listaCertificacoes.add(certificacao);
                    System.out.println("Certificação adiciona com sucesso!");
                }
            }

            System.out.println("Continuar adição de Certificações ?");

            String frase="Digite 1 para continuar, ou 0 para sair.";
            opcao=metodoWhile(frase,0,1);
            continuar= (opcao == 1);
        }
        return listaCertificacoes;
    }

    /**
     * Perguntar a categoria do alimento
     * @return categoria do alimento
     */
    public CategoriasAlimentos pedirCategoriaAlimento(){

        System.out.println("Categoria de alimentos: ");
        System.out.println("1 - Congelados");
        System.out.println("2 - Enlatados");
        System.out.println("3 - Vinho");

        String frase = "Por favor, escolha a categoria a que pertence o produto ";
        int categoriaEscolha = metodoWhile(frase, 1, 3);

        CategoriasAlimentos categoriaAlimentos=(categoriaEscolha==1) ? CategoriasAlimentos.CONGELADOS : (categoriaEscolha==2) ? CategoriasAlimentos.ENLATADOS: CategoriasAlimentos.VINHO;

        return categoriaAlimentos;
    }

    public int pedirBiologico(){
        String frase = "O produto é biológico? (1-sim 0-não). Por favor, selecione uma opção válida";
        int opcao = metodoWhile(frase, 0, 1);

        return opcao;
    }

    public CategoriasFarmacia pedirCategoriaFarmacia(){

        System.out.println("Categorias de produtos de farmácia:");
        System.out.println("1. Beleza");
        System.out.println("2. Bem-estar");
        System.out.println("3. Bebés");
        System.out.println("4. Animais");
        System.out.println("5. Outros");

        String frase = "Por favor, escolha a categoria a que pertence o produto: ";
        int categoriaEscolha = metodoWhile(frase, 1, 5);

        CategoriasFarmacia categoria = (categoriaEscolha == 1) ? CategoriasFarmacia.BELEZA : (categoriaEscolha == 2) ? CategoriasFarmacia.BEMESTAR : (categoriaEscolha == 3) ? CategoriasFarmacia.BEBES
                : (categoriaEscolha == 4) ? CategoriasFarmacia.ANIMAIS : CategoriasFarmacia.OUTROS;

        return categoria;
    }

    private int pedirPrescricao(){

        System.out.println("O produto de farmácia tem prescrição médica? ");
        System.out.println("1 - Com Prescrição");
        System.out.println("2 - Sem Prescrição");
        String frase = "Por favor, escolha uma das categorias disponíveis: ";
        int opcao = metodoWhile(frase, 1, 2);

        return opcao;
    }

    /**
     * Perguntar o médico prescritor
     * @return nome do medico
     */
    public String pedirMedicoPrescritor(){
        System.out.println("Por favor, introduza o médico que preescreveu o medicamento: ");
        String medico = scanner.nextLine();

        return medico;
    }

    private Localizacao escolherLocalizacao(){
        System.out.println("\nEscolha a localização do cliente:");
        System.out.println("1. Continente");
        System.out.println("2. Madeira");
        System.out.println("3. Açores");
        int escolha=0;
        Localizacao localizacao;

        while(escolha < 1 || escolha > 3){
                escolha=lerInteiroValido();
                if(escolha < 1 || escolha >3){
                    System.out.println("Erro: Por favor, escolha uma opção válida");
                }
        }
        localizacao=(escolha==1) ? Localizacao.CONTINENTE : (escolha==2) ? Localizacao.MADEIRA : Localizacao.ACORES;

        return localizacao;
    }

    private LocalDate escolherData(){
        LocalDate date=null;
        boolean continuar=true;

        while(continuar){
            System.out.println("Introduza a data(formato yyyy-mm-dd): ");
            try{
                date=LocalDate.parse(scanner.nextLine());
                continuar=false;
            }catch (DateTimeParseException e){
                System.out.println("Formato inválido. Por favor, tente novamente. ");
                continuar=true;
            }
        }
        return date;
    }

    public Double arredondarNumero(Double valor){
        return  (Math.round(valor*100.0)/100.0);
    }

    /**
     * Imprime as estatísticas de um cliente
     */
    public void estatisticas(){

        String frase="Digite o NIF do cliente: ";
        String nif=pedirNIF(frase);

        Cliente clienteFatura = buscaClienteNIF(nif);
        if (clienteFatura == null){
            System.out.println("Cliente com o nif " + nif + " não encontrado!" );
            return;
        }
        int numeroDeFaturas = clienteFatura.getQuantidadeFaturas();
        int totalProdutos = 0;
        double totalSemIva = 0.0;
        double totalComIva = 0.0;
        double totalIva = 0.0;

         for (Fatura fatura : clienteFatura.getListaDeFaturas() ){
             int produtosFatura = fatura.quantidadeProdutos();
             totalProdutos += produtosFatura;

             double semIva = fatura.calculaValorTotalSemIva(this);
             totalSemIva += semIva;

             double comIva = fatura.calculaValorTotalComIva(clienteFatura.getLocalizacao(),this);
             totalComIva += comIva;

             double iva = fatura.getTotalIva(clienteFatura.getLocalizacao(),this);
             totalIva += iva;
         }

         System.out.println("\n===Estatísticas===");
         System.out.println("Número de faturas: " + numeroDeFaturas);
         System.out.println("Total de produtos: " + totalProdutos);
         System.out.println("Valor total sem o IVA: " + arredondarNumero(totalSemIva));
         System.out.println("Valor total do IVA: " + arredondarNumero(totalIva));
         System.out.println("Valor total com o IVA: " + arredondarNumero(totalComIva));
    }

    /**
     * Valida se um NIF é válido
     * @param nif nif do cliente
     * @return true ou false
     */
    public boolean validacaoNIF(String nif){

        try{
            int numNif = Integer.parseInt(nif);
            if (nif.length() != 9){
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean CodigoJaExiste(String codigo,ArrayList<Produtos> listaProdutos){

        if(listaProdutos==null){
            return false;
        }
        else {
            for (Produtos produto : listaProdutos) {
                if (produto.getCodigo().equalsIgnoreCase(codigo)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Escreve os dados em um ficheiro de texto
     * @param file nome do ficheiro de texto
     */
    public void escreverDados(String file) {
        File f = new File(file);

            try{
                FileWriter fw = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(fw);
                // Escrevendo clientes
                bw.write("CLIENTES");
                bw.newLine();
                for (Cliente cliente : getListaClientes()) {
                    bw.write(cliente.getNome()+'/'+cliente.getNif()+'/'+cliente.getLocalizacao());
                    bw.newLine();
                }
                // Escrevendo faturas
                bw.write("FATURAS");
                bw.newLine();
                for (Cliente cliente : getListaClientes()) {
                    for (Fatura fatura : cliente.getListaDeFaturas()) {
                        for (Produtos produto : fatura.getListaProdutos()) {
                            bw.write(fatura.getNumFatura()+ "/" +  cliente.getNif() + "/" + fatura.getData() + "/" + produto.getDetalhesFicheiro());
                            bw.newLine();
                        }
                    }
                }
                bw.close();
            } catch (IOException e) {
                System.out.println("Erro ao escrever no ficheiro: " + e.getMessage());
            }
    }

    /**
     * Lê os dados no ficheiro de texto e adiciona no sistema
     * @param file nome do ficheiro de texto
     */
    public void lerDados(String file) {
        File f = new File(file);

        if (f.exists() && f.isFile()) {
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line;
                boolean leituraCliente = true;

                while ((line = br.readLine()) != null) {
                    if (line.startsWith("CLIENTES")) {
                        leituraCliente = true;
                        continue;
                    } else if (line.startsWith("FATURAS")) {
                        leituraCliente = false;
                        continue;
                    }

                    if (leituraCliente) {
                        // CLIENTES
                        String[] clientes = line.split("/");
                        if (clientes.length < 3) continue;

                        String nome = clientes[0];
                        String nif = clientes[1];
                        Localizacao localizacao;
                        try {
                            localizacao = Localizacao.valueOf(clientes[2].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Localização inválida para o cliente: " + line);
                            continue;
                        }

                        if (buscaClienteNIF(nif) == null) {
                            adicionarCliente(new Cliente(nome, nif, localizacao));
                        }
                    } else {

                        // FATURAS
                        String[] faturas = line.split("/");

                        String codigoFatura = faturas[0];
                        String nifCliente = faturas[1];
                        LocalDate dataFatura;

                        Fatura fatu=buscaFatura(codigoFatura);

                        try {
                            dataFatura = LocalDate.parse(faturas[2]);
                        } catch (DateTimeParseException e) {
                            System.out.println("Data inválida para a fatura: " + line);
                            continue;
                        }

                        String tipoFatura = faturas[3];
                        String detalhesProdutos = faturas[4];

                        Cliente cliente = buscaClienteNIF(nifCliente);
                        if (cliente != null) {

                            ArrayList<Produtos> listaProdutos = new ArrayList<>();
                            String[] produtos = detalhesProdutos.split(";");

                            String[] caracteristicasProduto = produtos[0].split(",");
                            String[] detalhesExtra=null;

                            if(produtos.length > 1)
                                detalhesExtra = produtos[1].split(",");

                            String nomeProduto = caracteristicasProduto[0];
                            String codigoProduto = caracteristicasProduto[1];
                            double valorProduto = Double.parseDouble(caracteristicasProduto[2]);
                            int quantidadeProduto = Integer.parseInt(caracteristicasProduto[3]);

                            // TIPO DO PRODUTO
                            if (tipoFatura.equalsIgnoreCase("taxaNormal")) {
                                boolean biologico = Boolean.parseBoolean(caracteristicasProduto[4]);

                                if (fatu == null)
                                    listaProdutos.add(new TaxaNormal(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico));
                                else
                                    fatu.adicionaProduto(new TaxaNormal(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico));

                            } else if (tipoFatura.equalsIgnoreCase("taxaIntermedia")) {
                                boolean biologico = Boolean.parseBoolean(caracteristicasProduto[4]);
                                CategoriasAlimentos categoria = parseCategoriaAlimentos(detalhesExtra);
                                if (categoria != null) {
                                    if (fatu == null)
                                        listaProdutos.add(new TaxaIntermedia(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico, categoria));
                                    else
                                        fatu.adicionaProduto(new TaxaIntermedia(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico, categoria));
                                }

                            } else if (tipoFatura.equalsIgnoreCase("taxaReduzida")) {
                                boolean biologico = Boolean.parseBoolean(caracteristicasProduto[4]);
                                ArrayList<Certificacoes> listaCertificacoes = parseCertificacoes(detalhesExtra);
                                if (listaCertificacoes != null) {
                                    if (fatu == null)
                                        listaProdutos.add(new TaxaReduzida(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico, listaCertificacoes));
                                    else
                                        fatu.adicionaProduto(new TaxaReduzida(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, biologico, listaCertificacoes));
                                }

                            } else if (tipoFatura.equalsIgnoreCase("farmaciaCom")) {
                                String medicoPrescritor = detalhesExtra[0];
                                if (fatu == null)
                                    listaProdutos.add(new TemPrescricao(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, medicoPrescritor));
                                else
                                    fatu.adicionaProduto(new TemPrescricao(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, medicoPrescritor));

                            } else if (tipoFatura.equalsIgnoreCase("farmaciaSem")) {
                                CategoriasFarmacia categoriaFarmacia = parseCategoriaFarmacia(detalhesExtra);
                                if (categoriaFarmacia != null) {
                                    if (fatu == null)
                                        listaProdutos.add(new SemPrescricao(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, categoriaFarmacia));
                                    else
                                        fatu.adicionaProduto(new SemPrescricao(nomeProduto, codigoProduto, valorProduto, quantidadeProduto, categoriaFarmacia));
                                }
                            } else {
                                System.out.println("Tipo de fatura inválido: " + tipoFatura);
                            }
                            if (fatu == null && !listaProdutos.isEmpty()) {
                                cliente.adicionarFatura(new Fatura(codigoFatura, cliente, dataFatura, listaProdutos));
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        }else System.out.println("O ficheiro não existe!" + file);
    }

    /**
     * Converter informações em texto para objetos do tipo ENUM CategoriasAlimentos
     * @param detalhes valor a ser convertido
     * @return valores convertidos
     */
    private CategoriasAlimentos parseCategoriaAlimentos(String[] detalhes) {
        try{
            return CategoriasAlimentos.valueOf(detalhes[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Converter informações em texto para objetos do tipo ENUM CategoriasFarmacia
     * @param detalhes valor a ser convertido
     * @return valores convertidos
     */
    private CategoriasFarmacia parseCategoriaFarmacia(String[] detalhes) {
        try{
            return CategoriasFarmacia.valueOf(detalhes[0].toUpperCase());
        } catch (IllegalArgumentException e) {
        }
        return null;
    }

    /**
     * Converter informações de texto para objetos do tipo ENUM
     * @param detalhes valor a ser convertido
     * @return lista de certificações
     */
    private ArrayList<Certificacoes> parseCertificacoes(String[] detalhes) {
        ArrayList<Certificacoes> listaCertificacoes = new ArrayList<>();

        for (String certificado : detalhes) {
            try {
                listaCertificacoes.add(Certificacoes.valueOf(certificado.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }
        return listaCertificacoes;
    }

    /**
     * Escreve os dados no ficheiro de objeto
     * @param ficheiroObjetos nome do ficheiro de objetos
     */
    public void escreverDadosObjetos(String ficheiroObjetos) {
        File file = new File(ficheiroObjetos);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Grave todos os clientes
            for (Cliente cliente : listaClientes) {
                oos.writeUTF("CLIENTE");
                oos.writeObject(cliente);

                // Grave as faturas do cliente
                for (Fatura fatura : cliente.getListaDeFaturas()) {
                    oos.writeUTF("FATURA");
                    oos.writeObject(fatura);
                }
            }
            oos.close();
        }catch (FileNotFoundException ex) {
            System.out.println("Erro ao abrir o ficheiro!");
        }catch (IOException e) {
            System.out.println("Erro ao salvar os dados no ficheiro de objetos: " + e.getMessage());
        }
    }

    /**
     * Lê o ficheiro de objetos
     * @param ficheiroObjetos nome do ficheiro de objetos
     */
    public void lerDadosObjetos(String ficheiroObjetos) {
        File file = new File(ficheiroObjetos);

        if (file.exists() && file.isFile()) {
            try{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listaClientes = new ArrayList<>(); // Inicializar lista de clientes

                while (true) {
                    try {
                        String tipo = ois.readUTF(); // Ler o marcador de tipo

                        if ("CLIENTE".equals(tipo)) {
                            Cliente cliente = (Cliente) ois.readObject();
                            listaClientes.add(cliente);
                        } else if ("FATURA".equals(tipo)) {
                            Fatura fatura = (Fatura) ois.readObject();
                            Cliente cliente = buscaClienteNIF(fatura.getCliente().getNif());
                            if (cliente != null && !cliente.getListaDeFaturas().contains(fatura)) {
                                cliente.adicionarFatura(fatura);
                            }
                        }
                    } catch (EOFException e) {
                        break; // Fim do ficheiro
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar os dados do ficheiro de objetos: " + e.getMessage());
            }
        }
    }

    /**
     * exporta os dados para um ficheiro de texto
     */
    public void exportarFaturas(){
        escreverDados("faturas.txt");
        System.out.println("Exportando faturas...");
    }

    /**
     * Importa as faturas para o sistema
     */
    public void importarFaturas(){
        System.out.println("Digite o nome do ficheiro que deseja importar:");
        String ficheiroTexto = scanner.nextLine();

        System.out.println("A importar faturas...");
        lerDados(ficheiroTexto);
    }

    /**
     * Menu inicial do sistema
     */
    public void exibirMenu() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Gerir Clientes");
            System.out.println("2. Gerir Faturas");
            System.out.println("3. Estatísticas do utilizador");
            System.out.println("4. Importar Faturas");
            System.out.println("5. Exportar Faturas");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção de entre as disponíveis: ");

            int opcao = lerInteiroValido();

            switch (opcao) {
                case 1:
                    exibirMenuClientes();
                    break;
                case 2:
                    exibirMenuFaturas();
                    break;
                case 3:
                    estatisticas();
                    break;
                case 4:
                    importarFaturas();
                    break;
                case 5:
                    exportarFaturas();
                    break;
                case 6:
                    System.out.println("Saíndo do programa...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Menu para informações de cliente
     */
    public void exibirMenuClientes() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Gerir Clientes===");
            System.out.println("1. Criar Cliente");
            System.out.println("2. Editar Cliente");
            System.out.println("3. Listar Clientes");
            System.out.println("4. Remover cliente");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiroValido();

            switch (opcao) {
                case 1:
                    criaCliente();
                    break;
                case 2:
                    editarCliente();
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    removerCliente();
                    break;
                case 5:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Menu para informação de faturas
     */
    public void exibirMenuFaturas() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===Gerir Faturas===");
            System.out.println("1. Criar Fatura");
            System.out.println("2. Editar Fatura");
            System.out.println("3. Listar Faturas");
            System.out.println("4. Visualizar Fatura");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiroValido();

            switch (opcao) {
                case 1:
                    criarFatura();
                    break;
                case 2:
                    editarFatura();
                    break;
                case 3:
                    listarFaturas();
                    break;
                case 4:
                    visualizarFatura();
                    break;
                case 5:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


    //getters
    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    //setters
    public void setListaClientes(ArrayList<Cliente> listaClientes){
        this.listaClientes=listaClientes;
    }

}
