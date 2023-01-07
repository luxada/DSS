package business.SubCriacao;
import business.SubCriacao.Carro;

public class SC extends Carro{

    public SC(String marca, String modelo, float pac, int potMotorCombs, int cilindrada) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
		super.fiabilidade = 0.7f; // valor default
    }

	public void atualizaFiabilidade(int nrVoltas)
	{
		super.fiabilidade = super.fiabilidade;
	}

	public int comparaTipo(Carro c)
	{
		if (c instanceof SC)
			return 0;
		else 
			return -1;
	}
}
