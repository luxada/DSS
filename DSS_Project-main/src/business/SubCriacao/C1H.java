package business.SubCriacao;
import business.SubCriacao.Carro;

public class C1H extends Carro {
    private int potMotorEle;

    public C1H(String marca, String modelo, float pac, int potMotorCombs, int cilindrada, int potMotorEle) {
        super(marca, modelo, pac, potMotorCombs, cilindrada);
		super.fiabilidade = 0.9f;
        this.potMotorEle = potMotorEle;
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
		if (c instanceof C1H)
			return 0;
		else 
			return 1;
	}
}
