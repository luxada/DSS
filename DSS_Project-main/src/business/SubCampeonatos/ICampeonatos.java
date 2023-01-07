package business.SubCampeonatos;
import business.SubCriacao.Carro;
import business.SubCriacao.Piloto;
import business.SubUtilizadores.Utilizador;
import java.util.List;

public interface ICampeonatos {

    public void registaParticipante(Utilizador u, Carro c, Piloto p);
    public List<Utilizador> atualizaClassificacaoGlobal();
    public void criarCampeonato(String nomeCamp, List<String> nomeCircs);
    public boolean haCampeonatos();
    public List<String> getNomeCamps();
    public Campeonato getCamp(String nomeCamp);
    public void comecaCampeonato(String nomeCamp); // Coloca o campeonato C na variável campAtivo
    public boolean jaExisteCampeonato(String nome);
    public List<Corrida> getCorridas(String nomeCamp);
    public void participantesCorridas();
    public Campeonato getCampAtivo();
}
