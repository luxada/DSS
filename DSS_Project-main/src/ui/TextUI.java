package ui;
import business.F1ManagerLN;
import business.SubCampeonatos.Campeonato;
import business.SubCampeonatos.CarroPiloto;
import business.SubCampeonatos.Corrida;
import business.SubCriacao.*;
import business.SubUtilizadores.Administrador;
import business.SubUtilizadores.Jogador;
import business.SubUtilizadores.Utilizador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.concurrent.ThreadLocalRandom;

// A camada UI segue um padrão Model-Delegate
// O model aqui vai ser a classe F1ManagerLN quem tem todas as interfaces de todos os subssistemas da LN
// O delegate vai ser implementado pelo TextUI com o apoio da classe Menu

public class TextUI {
    private F1ManagerLN model;
    private Menu menu;
    private Menu menuAdmin;
    private Menu menuJogador;
    private Scanner sc;

    public TextUI(){
        this.model = new F1ManagerLN();
        this.sc = new Scanner(System.in);

        this.menu = new Menu(new String[]{
                "Efetuar signUp como Jogador",
                "Efetuar login como Jogador",
                "Efetuar login como Administrador",
        });

        this.menu.setHandler(1,this :: trataSignUpJogador);
        this.menu.setHandler(2,this :: trataLoginJogador);
        this.menu.setHandler(3,this :: trataLoginAdmin);


        this.menuAdmin = new Menu(new String[]{
                "Criar carro",
                "Criar piloto",
                "Criar circuito",
                "Criar campeonato",
                "Jogar"
        });
        this.menuAdmin.setHandler(1,this :: trataCriarCarro);
        this.menuAdmin.setHandler(2,this :: trataCriarPiloto);
        this.menuAdmin.setHandler(3,this :: trataCriarCircuito);
        this.menuAdmin.setHandler(4,this :: trataCriarCampeonato);
        this.menuAdmin.setHandler(5,this :: trataJogar);
        // Apenas é possível jogar se houverem Carros, Pilotos, Circuitos e Campeonatos.
        this.menuAdmin.setPreCondition(5, () -> this.model.haCarros() && this.model.haPilotos() && this.model.haCircuitos() && this.model.haCampeonatos());


        this.menuJogador = new Menu(new String[]{
                "Jogar"
        });
        this.menuJogador.setHandler(1,this :: trataJogar);
        // Apenas é possível jogar se houverem Carros, Pilotos, Circuitos e Campeonatos.
        this.menuJogador.setPreCondition(1, () -> this.model.haCarros() && this.model.haPilotos() && this.model.haCircuitos() && this.model.haCampeonatos());
    }

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        this.menu.run();
        System.out.println("Até breve!...");
    }

    public void trataSignUpJogador(){
        try {
            System.out.println("Nome de utilizador: ");
            String nome = sc.nextLine();
            System.out.println("Password: ");
            String pass = sc.nextLine();
            System.out.println("A conta de jogador é premium? s-> Sim; n-> Não");
            String line = sc.nextLine();
            boolean premium =  false;
            if(line.equals("s"))
                premium = true;
            boolean sucesso = this.model.signUpJogador(nome,pass,premium);
            if(sucesso)
                this.menuJogador.run(); // Executa o menu do jogador após o SignUp
            System.out.println("Já existe um utilizador com esse nome!");
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void trataLoginAdmin() {
        try {
            System.out.println("Nome de utilizador: ");
            String nome = sc.nextLine();
            System.out.println("Password: ");
            String pass = sc.nextLine();
            boolean sucesso = this.model.loginAdmin(nome, pass);
            if(sucesso)// Login efetuado com sucesso
                this.menuAdmin.run();
            System.out.println("Falha no login como Administrador");
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void trataLoginJogador() {
        try {
            System.out.println("Nome de utilizador: ");
            String nome = sc.nextLine();
            System.out.println("Password: ");
            String pass = sc.nextLine();
            boolean sucesso = this.model.loginJogador(nome, pass);
            if(sucesso)// Login efetuado com sucesso
                this.menuJogador.run();
            System.out.println("Falha no login como Jogador");
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void trataCriarCarro(){
        try {
            System.out.println("Categorias de carros disponíveis:");
            System.out.println("1: C1, que são protótipos feitos especialmente para este campeonato - cilindrada de 6000cm3");
            System.out.println("2: C2, que são veículos de alta performance que podem entrar noutros campeonatos — cilindradas entre 3000cm3 e 5000cm3");
            System.out.println("3: GT, que são desportivos de produção em massa — entre 2000cm3 e 4000cm3");
            System.out.println("4: SC, que são carros derivados dos automóveis quotidianos — 2500cm3 de cilindrada");
            String line = sc.nextLine();
            int classe = Integer.parseInt(line);
            String marca = null, modelo = null;
            boolean existe = true;
            while(existe){
                System.out.println("Indique a marca do carro: ");
                marca = sc.nextLine();
                System.out.println("Indique o modelo do carro: ");
                modelo = sc.nextLine();
                existe = this.model.jaExisteCarro(marca, modelo);
                if (existe) System.out.println("Já existe um carro com essa marca e modelo!");
            }

            int cilindrada = 0; // cilindrada c1 é sempre 6000
            boolean clIncorreta = true;
            while(classe != 1 && classe != 4 && clIncorreta){ // Se o carro não for C1 nem SC tem que definir a cilindrada que depois é verificada
                System.out.println("Indique a cilindrada do carro (inteiro): ");
                line = sc.nextLine();
                cilindrada = Integer.parseInt(line);

                if(classe == 2 && cilindrada <= 3000 || cilindrada >= 5000){
                    System.out.println("Cilindrada inválida!");
                }else if(classe == 2){
                    clIncorreta = false;
                }

                if(classe == 3 && cilindrada <= 2000 || cilindrada >= 4000){
                    System.out.println("Cilindrada inválida!");
                }else if(classe == 3){
                    clIncorreta = false;
                }
            }

            if(classe == 1){
                cilindrada = 6000;
            }
            if(classe == 4){
                cilindrada = 2500;
            }

            System.out.println("Indique a potência do motor de combustão (inteiro): ");
            line = sc.nextLine();
            int potMotCombs = Integer.parseInt(line);
            int potMotEle = 0;
            if (classe == 1 || classe == 2 || classe == 3){
                System.out.println("Indique a potência do motor elétrico (0 significa que o carro não é híbrido): ");
                line = sc.nextLine();
                potMotEle = Integer.parseInt(line);
            }
            System.out.println("Indique o perfil aerodinâmico do carro (PAC):");
            line = sc.nextLine();
            float pac = Float.parseFloat(line);

            if (classe == 1){
                this.model.criaCarroC1_C1H(marca, modelo, cilindrada, potMotCombs, potMotEle, pac);
            }else if(classe == 2){
                this.model.criaCarroC2_C2H(marca, modelo, cilindrada, potMotCombs, potMotEle, pac);
            }else if(classe == 3){
                this.model.criaCarroGT_GTH(marca, modelo, cilindrada, potMotCombs, potMotEle, pac);
            }else{
                this.model.criaCarroSC(marca, modelo, cilindrada, potMotCombs, pac);
            }
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void trataCriarPiloto(){
        try {
            String nome = null;
            boolean existe = true;
            while(existe){
                System.out.println("Indique o nome do piloto: ");
                nome = sc.nextLine();
                existe = this.model.jaExistePiloto(nome);
                if (existe) System.out.println("Já existe um piloto com esse nome!");
            }

            System.out.println("Indique o valor do critério Chuva vs. Tempo Seco (CTS):");
            String line = sc.nextLine();
            float cts = Float.parseFloat(line);
            System.out.println("Indique o valor do critério Segurança vs. Agressividade (SVA):");
            line = sc.nextLine();
            float sva = Float.parseFloat(line);
            this.model.criaPiloto(nome, cts, sva);
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void trataCriarCircuito(){
        try {
            String nome = null;
            boolean existe = true;
            while(existe){
                System.out.println("Indique o nome do novo circuito: ");
                nome = sc.nextLine();
                existe = this.model.jaExisteCircuito(nome);
                if (existe) System.out.println("Já existe um circuito com esse nome!");
            }

            System.out.println("Distância do circuito: ");
            String line = sc.nextLine();
            float dist = Float.parseFloat(line);
            System.out.println("Indique o número de curvas do circuito: ");
            line = sc.nextLine();
            int nrCurvas = Integer.parseInt(line);
            System.out.println("Indique o número de chicanes do circuito: ");
            line = sc.nextLine();
            int nrChicanes = Integer.parseInt(line);
            int nrRetas = nrChicanes + nrCurvas;

            List<ParteCircuito> pcs = this.model.geraPartesCircuito(nrRetas, nrCurvas, nrChicanes);

            int i = 0;
            for (ParteCircuito pc : pcs){
                if(!(pc instanceof Chicane)){
                    System.out.println("O troço " + i + " do circuito é um " + pc);
                    System.out.println("Indique o GDU respetivo (0-> Possível, 1-> Difícil, 2-> Impossível): ");
                    line = sc.nextLine();
                    pc.setGDU(Integer.parseInt(line));
                    i += 1;
                }else{
                    pc.setGDU(1); // Chicanes têm sempre um GDU difícil
                }
            }
            System.out.println("Indique o número de voltas do circuito: ");
            line = sc.nextLine();
            int nrVoltas = Integer.parseInt(line);
            this.model.criaCircuito(nome, dist, pcs, nrVoltas);
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void trataCriarCampeonato(){
        try {
            String nome = null;
            boolean existe = true;
            while(existe){
                System.out.println("Indique o nome do novo campeonato: ");
                nome = sc.nextLine();
                existe = this.model.jaExisteCampeonato(nome);
				if (existe) System.out.println("Já existe um campeonato com esse nome!");
            }

            List<String> circNomes = this.model.getCircuitosDisponiveis();

            for(int i = 0; i < circNomes.size(); i++){
                System.out.println("Circuito " + i + ": " + circNomes.get(i));
            }
            System.out.println("Escolha os circuitos que quer adicionar ao campeonato (digite break para terminar):");
            List<String> circNomesAdicionar = new ArrayList<>();
            String line;
            while(!(line = sc.nextLine()).equals("break")){
                int op = Integer.parseInt(line);
                String circNome = circNomes.get(op);
                circNomesAdicionar.add(circNome);
            }
            this.model.criaCampeonato(nome, circNomesAdicionar);
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void trataJogar(){
        try {
            this.configurarCamp();
            System.out.println("Vamos começar com a configuração das corridas e respetiva simulação!");
            this.configurarCorridas();
            this.resultadoFinal();
        }
        catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void configurarCamp() {
        System.out.println("\n---------------------------------------- Configurar Campeonato ----------------------------------------\n");
        String line, nomeCamp = null;
        boolean prosseguir = false;
        System.out.println("Lista de campeonatos disponíveis: ");
        List<String> nomesCamps = this.model.getNomeCamps();

        while (!prosseguir) {
            for(int i = 0; i < nomesCamps.size(); i++){
                System.out.println(i + ": " + nomesCamps.get(i));
            }
            System.out.println("Escolha um campeonato para examinar: ");
            line = sc.nextLine();
            int op = Integer.parseInt(line);
            nomeCamp = nomesCamps.get(op);
            List<Corrida> corridas = this.model.getCorridas(nomeCamp);
            for(Corrida c : corridas)
                System.out.println(c);
            System.out.println("Quer prosseguir com este campeonato? (s-> sim; n-> não)");
            line = sc.nextLine();
            if (line.equals("s")) {
                prosseguir = true;
            }
        }

        System.out.println("Vamos prosseguir à inscrição dos participantes no campeonato!");
        List<String> nomes = new ArrayList<>();
        List<String> marcas = new ArrayList<>();
        List<String> modelos = new ArrayList<>();
        List<String> nomesPilotos = new ArrayList<>();
        boolean stop = false;

        while(!stop) {
            System.out.println("Pretende parar com a inscrição de novos participantes? (digite break para parar; digite qualquer coisa para continuar)");
            line = sc.nextLine();
            if (!line.equals("break")) {
                System.out.println("Indique o nome de utilizador:");
                nomes.add(sc.nextLine());
                System.out.println("Indique a marca e modelo do carro com o qual quer participar! (um de cada vez)");
                marcas.add(sc.nextLine());
                modelos.add(sc.nextLine());
                System.out.println("Indique o nome do piloto com o qual quer participar!");
                nomesPilotos.add(sc.nextLine());
            } else {
                stop = true;
            }
        }
        this.model.registaParticipantesCamp(nomeCamp, nomes, marcas, modelos, nomesPilotos);
    }

    public void configurarCorridas(){
        System.out.println("\n---------------------------------------- Configurar Corridas ------------------------------------------\n");
        boolean chuva = true;
        Campeonato c = this.model.getCampAtivo();
        List<Corrida> corridas = c.getCorridas();
        Map<Utilizador, CarroPiloto> participantes = c.getParticipantes();

        int i = 0;
        for(Corrida cor : corridas){
            int choice = ThreadLocalRandom.current().nextInt(0, 2);// Dá 0 ou 1
            if(choice == 0){
                chuva = false;
            }
            cor.setChuva(chuva);
            System.out.println(cor);
            for(Utilizador u : participantes.keySet()){
                System.out.println("Utilizador " + u.getNome() + ", pretende afinar o seu carro? (s-> sim, n-> não)");
                String line = sc.nextLine();
                // Afinação do carro
                if(line.equals("s")){
                    if(participantes.get(u).getC() instanceof C1 || participantes.get(u).getC() instanceof C1H || participantes.get(u).getC() instanceof C2 || participantes.get(u).getC() instanceof C2H){
                        System.out.println("Insira o novo valor do PAC: (valor entre 0 e 1)");
                        line = sc.nextLine();
                        float pac = Float.parseFloat(line);
                        participantes.get(u).getC().setPac(pac);
                    }else{
                        System.out.println("Não é possível afinar o seu carro");
                    }
                }
                System.out.println("Escolha o tipo de pneu que quer utilizar");
                System.out.println("1-> macio, 2-> duro, 3-> chuva");
                line = sc.nextLine();
                int pneu = Integer.parseInt(line);
                participantes.get(u).setTp(pneu);
                System.out.println("Escolha o tipo de modo de motor que quer utilizar");
                System.out.println("1-> conservador, 2-> normal, 3-> agressivo");
                line = sc.nextLine();
                int mm = Integer.parseInt(line);
                participantes.get(u).setMm(mm);
            }
			boolean premium = false;
			Utilizador utilizadorPrincipal = c.getUtilizadores().get(0);
			if (utilizadorPrincipal instanceof Administrador)
				premium = true;
			else
				premium = ((Jogador)utilizadorPrincipal).isPremium();
            this.simulaCorrida(c, i, premium);
            i += 1;
        }
    }

    public void simulaCorrida(Campeonato camp, int nrCorrida, boolean premium){
        System.out.println("\n---------------------------------------- Simulação de corrida -------------------------------------------\n");
        List<SortedMap<Utilizador,CarroPiloto>> classificacoes = camp.simulaCorrida(nrCorrida,premium);

        // Para cada volta o sistema mostra o resultado intermédio
        for(int i = 0; i < classificacoes.size(); i++){
            int j = 1;
            System.out.println("O resultado intermédio para a volta " + (i+1) + " no formato carros/pilotos/jogadores foi:");
            for(Map.Entry<Utilizador,CarroPiloto> e : classificacoes.get(i).entrySet()){
                System.out.println("Posição " + j + ": Carro-> " + e.getValue().getC().strResultados() + "/Piloto-> " + e.getValue().getP().getNome() + "/Jogador: " + e.getKey().getNome());
                j += 1;
            }
            System.out.println("\n\n");
        }
        System.out.println("Os resultados finais da corrida foram:");
        SortedMap<Utilizador, CarroPiloto> f = classificacoes.get(classificacoes.size()-1);
        int c = f.keySet().size();
        int i = 1;
        for(Utilizador u : f.keySet())
            System.out.println("Posição " + i++ + ": " + u.getNome() + ", com " + c-- + " pontos.");
        System.out.println("\n");
    }

    public void resultadoFinal(){
        System.out.println("\n---------------------------------------- Resultado Final do Campeonato -------------------------------------------\n");
        List<Utilizador> classFinal = this.model.atualizaPontuacoes();
        Map<Utilizador,Integer> classificacoes = this.model.getCampAtivo().getClassificacao();
        System.out.println("No final do campeonato as posições foram:");
        int i = 1;
        for(Utilizador u : classFinal){
            System.out.println("Posição " + i + ": " + u.getNome() + ", pontos: " + classificacoes.get(u));
            i += 1;
        }
    }
}
