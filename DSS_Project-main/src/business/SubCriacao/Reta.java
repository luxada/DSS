package business.SubCriacao;

public class Reta extends ParteCircuito{

    public Reta(int id, int gdu){
        super(id, gdu);
    }

    public Reta(){
        super(0); // Cria uma parte de circuito com gdu 0 (mais tarde é alterado)
    }

    public Reta(int gdu){
        super(gdu);
    }

    public String toString(){
        return "reta";
    }
}
