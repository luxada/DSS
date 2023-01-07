package business.SubCriacao;

public class Curva extends ParteCircuito{

    public Curva(int id, int gdu){
        super(id, gdu);
    }

    public Curva(int gdu){
        super(gdu);
    }

    public Curva(){
        super(0);
    }

    public String toString(){
        return "curva";
    }
}
