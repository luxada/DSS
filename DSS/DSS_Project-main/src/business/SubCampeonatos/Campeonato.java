package business.SubCampeonatos;
import business.SubUtilizadores.Utilizador;
import business.SubCriacao.*;
import java.util.*;

public class Campeonato {
    private String nome;
    private List<Utilizador> utilizadores;
    private Map<Utilizador, CarroPiloto> participantes;
    private List<Corrida> corridas;
    private Map<Utilizador, Integer> classificacao;

    public Campeonato(String nome, List<Corrida> corridas){
        this.nome = nome;
        this.utilizadores = new ArrayList<>();
        this.participantes = new HashMap<>();
        this.corridas = corridas;
        this.classificacao = new HashMap<>();
    }

    public void registaParticipanteCampeonato(Utilizador u, Carro c, Piloto p){
        CarroPiloto cp = new CarroPiloto(c,p);
        this.participantes.put(u,cp);
        this.utilizadores.add(u);
        this.classificacao.put(u,0);
    }

    // Coloca os participantes em cada corrida do campeonato
    public void setupCorridas(){
        for(Corrida c : this.corridas)
            c.setParticipantes(this.participantes);
    }

    public List<SortedMap<Utilizador,CarroPiloto>> simulaCorrida(int nrCorrida, boolean premium){
        List<SortedMap<Utilizador,CarroPiloto>> res;
        Corrida c = this.corridas.get(nrCorrida);

        if(premium)
            res = c.simulaCorridaPremium();
        else
            res = c.simulaCorrida();

        Map<Utilizador, Integer> classi = c.getClassificacao();
        for(Utilizador u: classi.keySet()){
            this.classificacao.put(u, classi.get(u) + this.classificacao.get(u));
        }
        return res;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setUtilizadores(List<Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public void setParticipantes(Map<Utilizador, CarroPiloto> participantes) {
        this.participantes = participantes;
    }

    public void setCorridas(List<Corrida> corridas) {
        this.corridas = corridas;
    }

    public void setClassificacao(Map<Utilizador, Integer> classificacao) {
        this.classificacao = classificacao;
    }

    public List<Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public Map<Utilizador, CarroPiloto> getParticipantes() {
        return participantes;
    }

    public List<Corrida> getCorridas() {
        return corridas;
    }

    public Map<Utilizador, Integer> getClassificacao() {
        return classificacao;
    }

    public String toString(){
        String str = "O campeonato " + this.nome + " tem os seguintes circuitos:\n";
        for(Corrida c: this.corridas){
            Circuito circ = c.getCircuito();
            str += circ.getNomeCirc() + "\n";
        }
        return str;
    }
}
