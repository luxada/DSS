package business.SubCampeonatos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import business.SubUtilizadores.Utilizador;
import business.SubCriacao.Circuito;
import business.SubCriacao.ParteCircuito;
import data.CampeonatoDAO;

public class Corrida {
	private static int nextID = CampeonatoDAO.getInstance().nrOfCorridas(); 
	private int ID;
    private boolean chuva;
    private Map<Utilizador, Integer> classificacao;
    private Map<Utilizador, CarroPiloto> participantes;
	private Circuito circuito;

	/**
	 * Construtor usado quando uma corrida não está registada na base de dados
	 * de forma a atribuir-lhe um ID
	 *
	 * @param  c  {@link Circuito} que dita qual circuito é usado nesta corrida
	 * */
    public Corrida(Circuito c){
        this.ID = nextID++;
		// Random r = new Random(ProcessHandle.current().pid());
		this.chuva = true;
        this.classificacao = new HashMap<>();
        this.participantes = new HashMap<>();
		this.circuito = c;
    }

	/**
	 * Construtor usado quando uma corrida está registada na base de dados
	 * de forma a atribuir-lhe o ID que está na mesma
	 *
	 * @param  ID  {@code int} que dita o ID da corrida na base de dados
	 * @param  c  {@link Circuito} que dita qual circuito é usado nesta corrida
	 * */
    public Corrida(int ID, Circuito c){
        this.ID = ID;
		// Random r = new Random(ProcessHandle.current().pid());
		this.chuva = true;
        this.classificacao = new HashMap<>();
        this.participantes = new HashMap<>();
		this.circuito = c;
    }

     /* T-Bag!!!
     *  De notar que ambas as simulações têm que enviar as classificações intermédias para a UI (as classificações por volta)
     */
    public List<SortedMap<Utilizador, CarroPiloto>> simulaCorrida(){
		List<Utilizador> posicoes = this.participantes.keySet().stream().collect(Collectors.toList());
		List<SortedMap<Utilizador, CarroPiloto>> classificacoes = new ArrayList<>();
		
		for (int i=0 ; i<this.circuito.getNrVoltas() ; i++)
		{
			for (ParteCircuito pc : this.circuito.getPartesCircuito())
			{
				for (int j=0 ; j<posicoes.size()-1 ; j++)
				{
					int gdu = pc.getGDU();
					boolean ultrapassou = this.calculaMudancaStandard(posicoes, j, j+1, gdu);

					if (ultrapassou)
						j++;
				}
			}
			SortedMap<Utilizador, CarroPiloto> classificacaoVoltaAtual = new TreeMap<>((u1,u2)->{
				return posicoes.indexOf(u2) - posicoes.indexOf(u1);
			});
			for (Utilizador u : posicoes)
				classificacaoVoltaAtual.put(u,this.participantes.get(u));
			classificacoes.add(classificacaoVoltaAtual);
		}

		int pontuacao = posicoes.size();
		for (Utilizador u : classificacoes.get(classificacoes.size()-1).keySet())
			this.classificacao.put(u, pontuacao--);

		return classificacoes;
    }

	private boolean calculaMudancaStandard(List<Utilizador> posicoes, int i1, int i2, int gdu)
	{
		boolean ult = false;
		Utilizador u1 = posicoes.get(i1);
		CarroPiloto carroPiloto1 = this.participantes.get(u1);
		boolean avaria = carroPiloto1.calculaAvaria(), 
				acidente = carroPiloto1.calculaAcidente(gdu);

		if (avaria || acidente)
		{
			posicoes.remove(u1);
			posicoes.add(u1);
		}
		else
		{
			Utilizador u2 = posicoes.get(i2);
			ult = this.ultrapassa(u1,u2,gdu);
			if (ult)
			{
				posicoes.set(i2,u1);
				posicoes.set(i1,u2);
			}
		}
		return ult;
	}

	private boolean ultrapassa(Utilizador u1, Utilizador u2, int gdu)
	{
		float dist = this.circuito.getDistancia() / this.circuito.getPartesCircuito().size();
		float posU1 = ThreadLocalRandom.current().nextFloat()*dist, posU2 = ThreadLocalRandom.current().nextFloat()*(dist-posU1)+posU1;
		float dif = posU1-posU2, percentagemDist = dist/dif;
		
		float probUltrapassar;
		if (gdu == 2)
			probUltrapassar = 0.075f;
		else if (gdu == 1)
			probUltrapassar = 0.15f;
		else
			probUltrapassar = 0.3f;
		probUltrapassar *= ((percentagemDist<=0.25) ?(2*(1-percentagemDist)) :((percentagemDist>=0.75) ?(4*(1-percentagemDist)) :1));
		return ThreadLocalRandom.current().nextFloat() < probUltrapassar;
	}

    public List<SortedMap<Utilizador, CarroPiloto>> simulaCorridaPremium(){
		List<Utilizador> posicoes = this.participantes.keySet().stream().collect(Collectors.toList());
		Map<Utilizador,Float> tempos = new HashMap<>();
		this.participantes.keySet().forEach((u) -> tempos.put(u,0.0f));
		List<SortedMap<Utilizador, CarroPiloto>> classificacoes = new ArrayList<>();
		
		for (int i=0 ; i<this.circuito.getNrVoltas() ; i++)
		{
			this.participantes.keySet().forEach((u) -> tempos.put(u,0.0f));
			for (ParteCircuito pc : this.circuito.getPartesCircuito())
			{
				for (int p=posicoes.size()-1 ; p>=0 ; p--)
					this.tempo(posicoes.get(p), tempos.get(posicoes.get(p)), (p==posicoes.size()-1) ?-1 :tempos.get(posicoes.get(p+1)));

				for (int j=0 ; j<posicoes.size()-1 ; j++)
				{
					int gdu = pc.getGDU();
					boolean ultrapassou = this.calculaMudancaPremium(posicoes, tempos, j, j+1, gdu);

					if (ultrapassou)
						j++;
				}
			}
			SortedMap<Utilizador, CarroPiloto> classificacaoVoltaAtual = new TreeMap<>((u1,u2)->{
				return posicoes.indexOf(u2) - posicoes.indexOf(u1);
			});
			for (Utilizador u : posicoes)
				classificacaoVoltaAtual.put(u,this.participantes.get(u));
			classificacoes.add(classificacaoVoltaAtual);
		}

		int pontuacao = posicoes.size();
		for (Utilizador u : classificacoes.get(classificacoes.size()-1).keySet())
			this.classificacao.put(u, pontuacao--);

		return classificacoes;
    }

	private float tempo(Utilizador u, float tempoAtual, float melhorTempoPossivel)
	{
		// calcular +/- a distancia de um troço do circuito (admitindo que tem todos a mesma distancia)
		float dist = this.circuito.getDistancia() / this.circuito.getPartesCircuito().size();
		float vel, tempo;
		do
		{
			// obter velocidade entre os 320 e os 400 km/h
			vel = ThreadLocalRandom.current().nextFloat()*80.0f+320.0f; 
			tempo = dist / vel;
		} while(tempo + tempoAtual < melhorTempoPossivel);

		return tempo;
	}

	private boolean calculaMudancaPremium(List<Utilizador> posicoes, Map<Utilizador,Float> tempos, int i1, int i2, int gdu)
	{
		boolean ult = false;
		Utilizador u1 = posicoes.get(i1);
		CarroPiloto carroPiloto1 = this.participantes.get(u1);
		boolean avaria = carroPiloto1.calculaAvaria(), 
				acidente = carroPiloto1.calculaAcidente(gdu);

		if (avaria || acidente)
		{
			posicoes.remove(u1);
			posicoes.add(u1);
		}
		else
		{
			Utilizador u2 = posicoes.get(i2);
			ult = this.ultrapassaPremium(u1,u2,gdu, tempos);
			if (ult)
			{
				posicoes.set(i2,u1);
				posicoes.set(i1,u2);
				float temp = tempos.get(u1);
				tempos.put(u1, tempos.get(u2));
				tempos.put(u2, temp);;
			}
		}
		return ult;
	}

	private boolean ultrapassaPremium(Utilizador u1, Utilizador u2, int gdu, Map<Utilizador,Float> tempos)
	{
		CarroPiloto cp1 = this.participantes.get(u1),
					cp2 = this.participantes.get(u2);
		// variavel para guardar a diferença de tempo limite para ultrapassar
		float limDif = 0.0f;
		float probUltrapassar = 0.0f;
		switch(cp1.getC().comparaTipo(cp2.getC()))
		{
			case 0:
				// se forem do mesmo tipo será 1 segundo
				limDif = 1.0f;
				probUltrapassar = 0.50f;
				break;
			case 1:
				// se o carro de u1 for melhor que o de u2 será 2 segundos
				limDif = 2.0f;
				probUltrapassar = 0.65f;
				break;
			case -1:
				// se o carro de u2 for melhor que o de u1 será 0.5 segundos
				limDif = 0.5f;
				probUltrapassar = 0.35f;
				break;
		}

		if (tempos.get(u1)-tempos.get(u2) >= limDif)
			return  false;
		
		// fator que tem em conta o SVA do piloto em relação ao GDU
		float fatorHabilidade = 0.0f;
		if (cp1.getP().getSVA() > 0.5)
		{
			if (gdu == 0)
				fatorHabilidade += 0.05;
			else if (gdu == 1)
				fatorHabilidade += 0.02f;
			else
				fatorHabilidade += 0.01;
		}
		else
		{
			if (gdu == 0)
				fatorHabilidade -= 0.05;
			else if (gdu == 1)
				fatorHabilidade -= 0.02f;
			else
				fatorHabilidade -= 0.05;
		}
		return probUltrapassar + fatorHabilidade < ThreadLocalRandom.current().nextFloat();
	}

    public void setChuva(boolean chuva) {
        this.chuva = chuva;
    }

	public String toString(){
		return this.circuito.getNomeCirc();
	}

    public void setClassificacao(Map<Utilizador, Integer> classificacao) {
        this.classificacao = classificacao;
    }

    public void setParticipantes(Map<Utilizador, CarroPiloto> participantes) {
        this.participantes = participantes;
    }

	public void setCircuito(Circuito c) {
        this.circuito = c;
    }

    public boolean isChuva() {
        return chuva;
    }

	public int getID() {
        return this.ID;
    }

    public Map<Utilizador, Integer> getClassificacao() {
        return classificacao;
    }

    public Map<Utilizador, CarroPiloto> getParticipantes() {
        return participantes;
    }

	public Circuito getCircuito() { return this.circuito; }

	// public void printClassificacao()
	// {
		// List<Utilizador> utilizadores = this.classificacao.keySet().stream().collect(Collectors.toList());
		// utilizadores.sort((u1, u2) -> { 
			// return this.classificacao.get(u2) - this.classificacao.get(u1); 
		// });
// 
		// for (Utilizador u : utilizadores)
		// {
				// 
		// }
	// }
}
