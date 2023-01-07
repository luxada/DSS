package business;
import business.SubCampeonatos.Campeonato;
import business.SubCampeonatos.Corrida;
import business.SubCampeonatos.GestorCampeonatos;
import business.SubCampeonatos.ICampeonatos;
import business.SubCriacao.*;
import business.SubUtilizadores.GestorUtilizadores;
import business.SubUtilizadores.IUtilizadores;
import business.SubUtilizadores.Utilizador;

import java.util.ArrayList;
import java.util.List;

// Classe principal do LN do trabalho que vai receber as interfaces de cada subssistema (SubCampeonatos, SubCriacao, SubUtilizadores)
public class F1ManagerLN {
    private IUtilizadores iUtilizadores;
    private ICampeonatos iCampeonatos;
    private ICriacao iCriacao;

    public F1ManagerLN(){
        this.iCampeonatos = new GestorCampeonatos();
        this.iUtilizadores = new GestorUtilizadores();
        this.iCriacao = new GestorCriacao();
    }

    public boolean signUpJogador(String nome, String password, boolean premium){
        return this.iUtilizadores.signUpJogador(nome, password, premium);
    }
    public boolean loginAdmin(String nome, String pass){
        return this.iUtilizadores.loginAdmin(nome, pass);
    }

    public boolean loginJogador(String nome, String pass){
        return this.iUtilizadores.loginJogador(nome, pass);
    }

    // Esta função pode criar um carro C1 ou C1H
    public void criaCarroC1_C1H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        this.iCriacao.criaCarroC1_C1H(marca, modelo, cilindrada, potMotCombs, potMotEele, pac);
    }

    // Esta função pode criar um carro C2 ou C2H
    public void criaCarroC2_C2H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        this.iCriacao.criaCarroC2_C2H(marca, modelo, cilindrada, potMotCombs, potMotEele, pac);
    }
    // Esta função pode criar um carro GT ou GTH
    public void criaCarroGT_GTH(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        this.iCriacao.criaCarroGT_GTH(marca, modelo, cilindrada, potMotCombs, potMotEele, pac);
    }
    // Esta função pode criar um carro SC
    public void criaCarroSC(String marca, String modelo, int cilindrada, int potMotCombs, float pac){
        this.iCriacao.criaCarroSC(marca, modelo, cilindrada, potMotCombs, pac);
    }

    public void criaPiloto(String nome, float cts, float sva){
        this.iCriacao.criaPiloto(nome,cts,sva);
    }

    // Método que gera um circuito e devolve a lista de partes de circuito para que o administrador depois indique os GDU's
    public List<ParteCircuito> geraPartesCircuito(int nrRetas, int nrCurvas, int nrChicanes){
        return this.iCriacao.geraPartesCircuito(nrRetas,nrCurvas,nrChicanes);
    }

    public void criaCircuito(String nome, float dist ,List<ParteCircuito> pcs, int nrVoltas){
        this.iCriacao.criaCircuito(nome,dist,pcs,nrVoltas);
    }

    public void criaCampeonato(String nomeCamp, List<String> nomeCircs){
        this.iCampeonatos.criarCampeonato(nomeCamp, nomeCircs);
    }

    public void registaParticipantesCamp(String nomeCamp, List<String> nomes, List<String> marcas, List<String> modelos, List<String> nomesPilotos){
        this.iCampeonatos.comecaCampeonato(nomeCamp);
        for(int i=0; i<nomes.size(); i++){
            Utilizador u = this.iUtilizadores.getUtilizador(nomes.get(i));
            Carro car = this.iCriacao.getCarro(marcas.get(i), modelos.get(i));
            Piloto p = this.iCriacao.getPiloto(nomesPilotos.get(i));
            this.iCampeonatos.registaParticipante(u,car,p);
        }
        this.iCampeonatos.participantesCorridas();
    }

    public List<Corrida> getCorridas(String nomeCamp){
        return this.iCampeonatos.getCorridas(nomeCamp);
    }

    public List<Utilizador> atualizaPontuacoes(){
        return this.iCampeonatos.atualizaClassificacaoGlobal();
    }
    public List<String> getCircuitosDisponiveis(){
        return this.iCriacao.getNomesCirc();
    }
    public boolean jaExisteCarro(String marca, String modelo){
        return this.iCriacao.jaExisteCarro(marca, modelo);
    }
    public boolean jaExisteCircuito(String nome){
        return  this.iCriacao.jaExisteCircuito(nome);
    }
    public boolean jaExistePiloto(String nome){
        return this.iCriacao.jaExistePiloto(nome);
    }

    public boolean jaExisteCampeonato(String nome){
        return this.iCampeonatos.jaExisteCampeonato(nome);
    }
    public boolean haCarros(){
        return this.iCriacao.haCarros();
    }

    public boolean haPilotos(){
        return this.iCriacao.haPilotos();
    }

    public boolean haCircuitos(){
        return this.iCriacao.haCircuitos();
    }

    public boolean haCampeonatos(){
        return this.iCampeonatos.haCampeonatos();
    }

    public List<String> getNomeCamps(){
        return this.iCampeonatos.getNomeCamps();
    }

    public Campeonato getCamp(String nomeCamp){
        return this.iCampeonatos.getCamp(nomeCamp);
    }

    public Campeonato getCampAtivo(){
        return this.iCampeonatos.getCampAtivo();
    }

    public void setiUtilizadores(IUtilizadores iUtilizadores) {
        this.iUtilizadores = iUtilizadores;
    }

    public void setiCampeonatos(ICampeonatos iCampeonatos) {
        this.iCampeonatos = iCampeonatos;
    }

    public void setiCriacao(ICriacao iCriacao) {
        this.iCriacao = iCriacao;
    }

    public IUtilizadores getiUtilizadores() {
        return iUtilizadores;
    }

    public ICampeonatos getiCampeonatos() {
        return iCampeonatos;
    }

    public ICriacao getiCriacao() {
        return iCriacao;
    }
}
