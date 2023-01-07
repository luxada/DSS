package business.SubCriacao;
import java.util.List;

import business.SubCriacao.ParteCircuito;

public class Circuito
{
    private String nomeCirc;
    private int nrVoltas;
    private float distancia;
    private List<ParteCircuito> partesCircuito;

    public Circuito(String nomeCirc, int nrVoltas, float distancia, List<ParteCircuito> pc)
    {
        this.nomeCirc = nomeCirc;
        this.nrVoltas = nrVoltas;
        this.distancia = distancia;
        this.partesCircuito = pc;
    }

    public String getNomeCirc() {
        return nomeCirc;
    }

    public int getNrVoltas() {
        return nrVoltas;
    }

    public float getDistancia() {
        return distancia;
    }

    public List<ParteCircuito> getPartesCircuito() {
        return this.partesCircuito;
    }

    public void setPartesCircuito(List<ParteCircuito> pc) {
        this.partesCircuito = pc;
    }

    public void setNomeCirc(String nomeCirc) {
        this.nomeCirc = nomeCirc;
    }

    public void setNrVoltas(int nrVoltas) {
        this.nrVoltas = nrVoltas;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

}
