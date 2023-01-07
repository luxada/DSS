package business.SubCriacao;
import data.CarroDAO;
import data.CircuitoDAO;
import data.PilotoDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GestorCriacao implements ICriacao {
    private Map<MarcaModelo,Carro> carros;
    private Map<String, Piloto> pilotos;
    private Map<String, Circuito> circuitos;

    public GestorCriacao(){
        this.carros = CarroDAO.getInstance();
        this.pilotos = PilotoDAO.getInstance();
        this.circuitos = CircuitoDAO.getInstance();
    }

    public void criaCarroC1_C1H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        Carro c;

        if(potMotEele == 0){// Não é híbrido
            c = new C1(marca,modelo,pac,potMotCombs,cilindrada);
        }else{
            c = new C1H(marca,modelo,pac,potMotCombs,cilindrada,potMotEele);
        }
        this.carros.put(new MarcaModelo(marca, modelo), c);
    }

    public void criaCarroC2_C2H(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        Carro c;

        if(potMotEele == 0){// Não é híbrido
            c = new C2(marca,modelo,pac,potMotCombs,cilindrada);
        }else{
            c = new C2H(marca,modelo,pac,potMotCombs,cilindrada,potMotEele);
        }
        this.carros.put(new MarcaModelo(marca,modelo), c);
    }

    public void criaCarroGT_GTH(String marca, String modelo, int cilindrada, int potMotCombs, int potMotEele, float pac){
        Carro c;

        if(potMotEele == 0){// Não é híbrido
            c = new GT(marca,modelo,pac,potMotCombs,cilindrada);
        }else{
            c = new GTH(marca,modelo,pac,potMotCombs,cilindrada,potMotEele);
        }
        this.carros.put(new MarcaModelo(marca, modelo), c);
    }

    public void criaCarroSC(String marca, String modelo, int cilindrada, int potMotCombs, float pac){
        Carro c = new SC(marca,modelo,pac,potMotCombs,cilindrada);
        this.carros.put(new MarcaModelo(marca, modelo), c);
    }

    public void criaPiloto(String nome, float cts, float sva){
        Piloto p = new Piloto(nome,cts,sva);
        this.pilotos.put(nome, p);
    }

    // Método que gera aleatóriamente um circuito (as partes de circuito que constituem o circuito)
    public List<ParteCircuito> geraPartesCircuito(int nrRetas, int nrCurvas, int nrChicanes){
        List<ParteCircuito> pcs = new ArrayList<>();
        int nrPcs = nrRetas + nrCurvas + nrChicanes;
        int choice; boolean reta = true;
        ParteCircuito pc;

        for(int i = 0; i < nrPcs; i++){

            if(reta && nrRetas > 0){
                pc = new Reta();
                pcs.add(pc);
                reta = false;
                nrRetas--;
            }else{
                if(nrCurvas > 0 && nrChicanes > 0){
                    choice = ThreadLocalRandom.current().nextInt(0, 1 + 1); // gera aleatoriamente um 0 ou 1
                    if(choice == 0){
                        pc = new Curva();
                        pcs.add(pc);
                        nrCurvas--;
                    }else{
                        pc = new Chicane();
                        pcs.add(pc);
                        nrChicanes--;
                    }
                }else if(nrCurvas > 0){
                    pc = new Curva();
                    pcs.add(pc);
                    nrCurvas--;
                }else{
                    pc = new Chicane();
                    pcs.add(pc);
                    nrChicanes--;
                }
                reta = true;
            }
        }
        return pcs;
    }

    public void criaCircuito(String nome, float dist , List<ParteCircuito> pcs, int nrVoltas){
        Circuito c = new Circuito(nome, nrVoltas, dist, pcs);
        this.circuitos.put(nome, c);
    }

    public boolean jaExistePiloto(String nome){
        return this.pilotos.containsKey(nome);
    }

    public boolean jaExisteCarro(String marca, String modelo){
        MarcaModelo mm = new MarcaModelo(marca, modelo);
        return this.carros.containsKey(mm);
    }

    public boolean jaExisteCircuito(String nome){
        return this.circuitos.containsKey(nome);
    }

    public List<String> getNomesCirc(){
        return new ArrayList<>(this.circuitos.keySet());
    }

    public Piloto getPiloto(String nome){
        return this.pilotos.get(nome);
    }
    public Carro getCarro(String marca, String modelo){
        MarcaModelo mm = new MarcaModelo(marca, modelo);
        return this.carros.get(mm);
    }

    public boolean haCarros(){
        return !this.carros.isEmpty();
    }
    public boolean haPilotos(){
        return !this.pilotos.isEmpty();
    }
    public boolean haCircuitos(){
        return !this.circuitos.isEmpty();
    }

    public void setCarros(Map<MarcaModelo, Carro> carros) {
        this.carros = carros;
    }

    public void setPilotos(Map<String, Piloto> pilotos) {
        this.pilotos = pilotos;
    }

    public void setCircuitos(Map<String, Circuito> circuitos) {
        this.circuitos = circuitos;
    }

    public Map<MarcaModelo, Carro> getCarros() {
        return carros;
    }

    public Map<String, Piloto> getPilotos() {
        return pilotos;
    }

    public Map<String, Circuito> getCircuitos() {
        return circuitos;
    }
}
