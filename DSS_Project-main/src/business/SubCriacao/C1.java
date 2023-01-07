package business.SubCriacao;
import business.SubCriacao.Carro;

public class C1 extends Carro{

    public C1(String marca, String modelo, float pac, int potMotorCombs, int cilindrada){
        super(marca, modelo,pac, potMotorCombs, cilindrada);
		super.fiabilidade = 0.95f;
    }

	public void atualizaFiabilidade(int nrVoltas)
	{
		super.fiabilidade = super.fiabilidade;
	}

	public int comparaTipo(Carro c)
	{
		if (c instanceof C1)
			return 0;
		else if (c instanceof C1H)
			return -1;
		else 
			return 1;
	}
}
