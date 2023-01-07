package business.SubCriacao;

public abstract class Carro {
    private String marca;
    private String modelo;
    private float pac;
    private int potMotorCombs;
    private int cilindrada;
	protected float fiabilidade;

    public Carro(String marca, String modelo, float pac, int potMotorCombs, int cilindrada){
        this.marca = marca;
        this.modelo = modelo;
        this.pac = pac;
        this.potMotorCombs = potMotorCombs;
        this.cilindrada = cilindrada;
    }

    public void setPotMotorCombs(int potMotorCombs) {
        this.potMotorCombs = potMotorCombs;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public void setPac(float pac) {
        this.pac = pac;
    }

    public float getPac() {
        return pac;
    }

    public int getPotMotorCombs() {
        return potMotorCombs;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public String toString()
    {
        return "pac: " + this.pac + "Cilindrada: " + this.cilindrada + "Potência dos motores combiados: " + this.potMotorCombs;
    }

    public String strResultados(){
        return this.marca + " " + this.modelo;
    }

    public boolean equals(Object o)
    {
        if(this==o)
        return true;
        
        if(o==null || this.getClass()!=o.getClass())
        return false;
        
        Carro c = (Carro) o;
        return	this.pac == (c.getPac()) &&
                this.cilindrada == (c.getCilindrada()) &&
                this.potMotorCombs == c.getPotMotorCombs(); 
				
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

	public MarcaModelo getMarcaModelo()
	{
		return new MarcaModelo(marca, modelo);
	}

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

	public float getFiabilidade()
	{
		return this.fiabilidade;
	}

	public abstract void atualizaFiabilidade(int nrVoltas);

	public void setFiabilidade(float fiabilidade)
	{
		this.fiabilidade = fiabilidade;
	}

	public abstract int comparaTipo(Carro c);
}
