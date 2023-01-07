package business.SubCampeonatos;
import business.SubCriacao.Circuito;
import business.SubCriacao.Piloto;
import business.SubCriacao.Carro;
import business.SubUtilizadores.Utilizador;
import data.*;

import java.util.*;

public class GestorCampeonatos implements ICampeonatos {
	private Campeonato campAtivo;
    private Map<String, Utilizador> utlizadores;
	private Map<String, Circuito> circuitos;
    private Map<String, Campeonato> campeonatos;

    public GestorCampeonatos(){
        this.campeonatos = CampeonatoDAO.getInstance();
		this.utlizadores = UtilizadorDAO.getInstance();
        this.circuitos = CircuitoDAO.getInstance();

    }

    // Para criar um campeonato o gestor precisa de receber o nome do campeonato e a lista de corridas do campeonato
    public void criarCampeonato(String nomeCamp, List<String> nomeCircs){
        List<Corrida> corridas = new ArrayList<>();

        for(String nomeCirc : nomeCircs){
            Circuito c = this.circuitos.get(nomeCirc);
            corridas.add(new Corrida(c));
        }
        Campeonato camp = new Campeonato(nomeCamp, corridas);
        this.campeonatos.put(nomeCamp, camp);
    }

    public void registaParticipante(Utilizador u, Carro c, Piloto p){
        this.campAtivo.registaParticipanteCampeonato(u,c,p);
    }

    public void participantesCorridas(){ // Método que avisa o campeonato que este vai começar e que deve dar setup às corridas
        this.campAtivo.setupCorridas();
    }

    public List<Utilizador> atualizaClassificacaoGlobal(){
        List<Utilizador> participantes = this.campAtivo.getUtilizadores();
        Map<Utilizador, Integer> clas = this.campAtivo.getClassificacao();

        for(Utilizador u: participantes){
            // Soma a classificação do utilizador no campeonato à classificação global atual do utilizador
            u.setClassificacao_global(clas.get(u) + u.getClassificacaoGlobal());
            this.utlizadores.put(u.getNome(), u);
        }

        participantes.sort((u1,u2)->{
            return clas.get(u2) - clas.get(u1);
        });
        return participantes;
    }

    public List<Corrida> getCorridas(String nomeCamp){
        return this.campeonatos.get(nomeCamp).getCorridas();
    }

    public void comecaCampeonato(String nomeCamp){
        this.campAtivo = this.campeonatos.get(nomeCamp);
    }

    public boolean jaExisteCampeonato(String nome){
        return this.campeonatos.containsKey(nome);
    }

    public List<String> getNomeCamps(){
        return new ArrayList<>(this.campeonatos.keySet());
    }

    public Campeonato getCamp(String nomeCamp){
        return this.campeonatos.get(nomeCamp);
    }

    public boolean haCampeonatos(){
        return !this.campeonatos.isEmpty();
    }

    public void setCampAtivo(Campeonato campAtivo) {
        this.campAtivo = campAtivo;
    }

    public void setUtlizadores(Map<String, Utilizador> utlizadores) {
        this.utlizadores = utlizadores;
    }

    public void setCircuitos(Map<String, Circuito> circuitos) {
        this.circuitos = circuitos;
    }

    public void setCampeonatos(Map<String, Campeonato> campeonatos) {
        this.campeonatos = campeonatos;
    }

    public Campeonato getCampAtivo() {
        return campAtivo;
    }

    public Map<String, Utilizador> getUtlizadores() {
        return utlizadores;
    }

    public Map<String, Circuito> getCircuitos() {
        return circuitos;
    }

    public Map<String, Campeonato> getCampeonatos() {
        return campeonatos;
    }
}
