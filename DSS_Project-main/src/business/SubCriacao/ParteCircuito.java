package business.SubCriacao;

import data.CircuitoDAO;

enum GDU{
    Possivel, Dificil, Impossivel;
}

public abstract class ParteCircuito {
    private static int nextID = CircuitoDAO.getInstance().nrDePartesCircuito();
    private int id;
    private GDU gdu;

	public ParteCircuito(int gdu)
	{
		this.id = nextID++;
        if(gdu == 0){
            this.gdu = GDU.Possivel;
        }else if(gdu == 1){
            this.gdu = GDU.Dificil;
        }else{
            this.gdu = GDU.Impossivel;
        }
	}

    public ParteCircuito(int id, int gdu){
        this.id = id;

        if(gdu == 0){
            this.gdu = GDU.Possivel;
        }else if(gdu == 1){
            this.gdu = GDU.Dificil;
        }else{
            this.gdu = GDU.Impossivel;
        }
    }

    public ParteCircuito(GDU gdu){
        this.id = nextID++;
        this.gdu = gdu;
    }

    public int getGDU(){
        if(this.gdu == GDU.Possivel){
            return 0;
        }
        if(this.gdu == GDU.Dificil){
            return 1;
        }

        return 2; // GDU é impossível
    }

    public void setGDU(int gdu){

        if(gdu == 0){
            this.gdu = GDU.Possivel;
        } else if(gdu == 1){
            this.gdu = GDU.Dificil;
        }else if (gdu == 2){
            this.gdu = GDU.Impossivel;
        }
    }

	public int getID() { return this.id; }
}
