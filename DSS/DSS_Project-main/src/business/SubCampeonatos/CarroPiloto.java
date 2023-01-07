package business.SubCampeonatos;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import business.SubCriacao.Carro;
import business.SubCriacao.GT;
import business.SubCriacao.GTH;
import business.SubCriacao.SC;
import business.SubCriacao.Piloto;

public class CarroPiloto {
	private boolean SCatualizado = false;
    private Carro carro;
    private Piloto piloto;
    private enum TipoPneu {Macio, Duro, Chuva};
    private enum ModoMotor {Conservador, Normal, Agressivo};
    private TipoPneu tp; // O tipo de pneu só é escolhido na configuração da corrida
    private ModoMotor mm; // O modo do motor só é escolhido na configuração da corrida

    public CarroPiloto(Carro c, Piloto p){
        this.carro = c;
        this.piloto = p;
    }

    public void setTp(int tp) {
        if(tp == 1){
            this.tp = TipoPneu.Macio;
        }
        if(tp == 2){
            this.tp = TipoPneu.Duro;
        }
        if(tp == 3){
            this.tp = TipoPneu.Chuva;
        }
    }

    public void setMm(int mm) {
        if(mm == 1){
            this.mm = ModoMotor.Conservador;
        }
        if(mm == 2){
            this.mm = ModoMotor.Normal;
        }
        if(mm == 3){
            this.mm = ModoMotor.Agressivo;
        }
    }

    public TipoPneu getTp() {
        return tp;
    }

    public ModoMotor getMm() {
        return mm;
    }

    public void setC(Carro c) {
        this.carro = c;
    }

    public void setP(Piloto p) {
        this.piloto = p;
    }

    public Carro getC() {
        return carro;
    }

    public Piloto getP() {
        return piloto;
    }

	public void atualizaFiabilidade(int nrVoltas, boolean chuva)
	{

		if (this.carro instanceof SC && !this.SCatualizado)
		{
			this.SCatualizado = true;
			float SVA = this.piloto.getSVA();
			float CTS = this.piloto.getCTS() - 0.5f;
			float fiabilidade = this.carro.getFiabilidade();
			float newFiabilidade = fiabilidade*0.25f + 0.6f - (SVA-0.5f)*5 + ((chuva) ?((CTS<0) ?CTS*5 :CTS*(-5)) :((CTS>0) ?CTS*5 :CTS*(-5))); 
			this.carro.setFiabilidade(newFiabilidade);
		}
		else if (this.carro instanceof GT || this.carro instanceof GTH)
			this.carro.atualizaFiabilidade(nrVoltas);
	}

	public boolean calculaAvaria()
	{
		float probAvaria;
		if (this.mm == ModoMotor.Conservador)
			probAvaria = 0.05f;
		else if (this.mm == ModoMotor.Normal)
			probAvaria = 0.075f;
		else
			probAvaria = 0.10f;

		
		return ThreadLocalRandom.current().nextFloat() < probAvaria;
	}

	public boolean calculaAcidente(int gdu)
	{
		float fiabilidade =  this.carro.getFiabilidade();
		float fatorAgressividade = 0.0f;
		if (this.piloto.getSVA() > 0.5)
		{
			if (gdu == 0)
				fatorAgressividade -= 0.01;
			else if (gdu == 1)
				fatorAgressividade -= 0.04f;
			else
				fatorAgressividade -= 0.075;
		}
		else
		{
			if (gdu == 0)
				fatorAgressividade += 0.075;
			else if (gdu == 1)
				fatorAgressividade += 0.5f;
			else
				fatorAgressividade += 0.025;
		}
		return fiabilidade + fatorAgressividade > ThreadLocalRandom.current().nextFloat();
	}

    public String toString(){
        return "Carro: " + this.carro + ", Piloto: " + this.piloto + ", Tipo Pneu " + this.tp + ", Tipo Motor " + this.mm;
    }
}
