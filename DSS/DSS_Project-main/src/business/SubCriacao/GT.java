package business.SubCriacao;
import business.SubCriacao.Carro;

public class GT extends Carro{

    public GT(String marca, String modelo, float pac, int potMotorCombs, int cilindrada) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
		super.fiabilidade = 0.75f;
	}

	public void atualizaFiabilidade(int nrVoltas)
	{
		float maximoAtirar = 0.10f + ((nrVoltas>4) ?(nrVoltas-4) :0);
		float valorAtirar  = maximoAtirar / nrVoltas;
		super.fiabilidade -= valorAtirar;
	}

	public int comparaTipo(Carro c)
	{
		if (c instanceof SC)
			return 1;
		else if (c instanceof GT)
			return 0;
		else return -1;
	}
}
