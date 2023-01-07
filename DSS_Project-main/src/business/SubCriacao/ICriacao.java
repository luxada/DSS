package business.SubCriacao;

import java.util.List;

public interface ICriacao {
    public void criaCarroC1_C1H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac);

    public void criaCarroC2_C2H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac);

    public void criaCarroGT_GTH(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac);

    public void criaCarroSC(String marca, String modelo, int cilindrada, int potMotCombs, float pac);

    public void criaPiloto(String nome, float cts, float sva);
    public List<ParteCircuito> geraPartesCircuito(int nrRetas, int nrCurvas, int nrChicanes);
    public void criaCircuito(String nome, float dist , List<ParteCircuito> pcs, int nrVoltas);
    public List<String> getNomesCirc();
    public boolean haCarros();
    public boolean haPilotos();
    public boolean haCircuitos();
    public Piloto getPiloto(String nome);
    public Carro getCarro(String marca, String modelo);
    public boolean jaExistePiloto(String nome);
    public boolean jaExisteCarro(String marca, String modelo);
    public boolean jaExisteCircuito(String nome);
}
