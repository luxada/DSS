package business.SubCriacao;
import business.SubCriacao.Carro;

public class C2H extends Carro {
    private int potMotorEle;

    public C2H(String marca, String modelo, float pac, int potMotorCombs, int cilindrada, int potMotorEle) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
        this.potMotorEle = potMotorEle;
		super.fiabilidade = 0.725f;
    }

    public void setPotMotorEle(int potMotorEle) {
        this.potMotorEle = potMotorEle;
    }

    public int getPotMotorEle() {
        return potMotorEle;
    }

	public void atualizaFiabilidade(int nrVoltas)
	{
		super.fiabilidade = super.fiabilidade;
	}

	public int comparaTipo(Carro c)
	{
		if ((c instanceof C1) || (c instanceof C1H))
			return -1;
		else if (c instanceof C2H)
			return 0;
		else
			return 1;
	}
}
