package business.SubCriacao;

import java.util.zip.CheckedInputStream;

public class Chicane extends ParteCircuito{

    public Chicane(int id, int gdu){
        super(id, gdu);
    }

    public Chicane(){
        super(0);
    }

    public Chicane(int gdu){
        super(gdu);
    }

    public String toString(){
        return "chicane";
    }
}
