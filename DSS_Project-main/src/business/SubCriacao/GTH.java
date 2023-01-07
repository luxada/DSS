package business.SubCriacao;
import business.SubCriacao.Carro;

public class GTH extends Carro{
    private int potMotorEle;

    public GTH(String marca, String modelo, float pac, int potMotorCombs, int cilindrada, int potMotorEle) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
        this.potMotorEle = potMotorEle;
		super.fiabilidade = 0.7f;
    }

    public void setPotMotorEle(int potMotorEle) {
        this.potMotorEle = potMotorEle;
    }

    public int getPotMotorEle() {
        return potMotorEle;
    }

	public void atualizaFiabilidade(int nrVoltas)
	{
		float maximoAtirar = 0.10f + ((nrVoltas>4) ?(nrVoltas-4) :0);
		float valorAtirar  = maximoAtirar / nrVoltas;
		super.fiabilidade -= valorAtirar;
	}

	public int comparaTipo(Carro c)
	{
		if ((c instanceof SC) || (c instanceof GT))
			return 1;
		else if (c instanceof GTH)
			return 0;
		else return -1;
	}
}
