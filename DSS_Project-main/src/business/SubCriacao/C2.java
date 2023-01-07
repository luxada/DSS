package business.SubCriacao;
import business.SubCriacao.Carro;

public class C2 extends Carro {

    public C2(String marca, String modelo, float pac, int potMotorCombs, int cilindrada) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
		super.fiabilidade = 0.80f;
	}

	public void atualizaFiabilidade(int nrVoltas)
	{
		super.fiabilidade = super.fiabilidade;
	}

	public int comparaTipo(Carro c)
	{
		if ((c instanceof C1) || (c instanceof C1H) || (c instanceof C2H))
			return -1;
		else if ((c instanceof C2))
			return 0;
		else
			return 1;
	}
}
