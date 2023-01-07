package business.SubCriacao;

public class Piloto {
    private String nome;
	private float SVA, CTS;

    public Piloto(String nome, float SVA, float CTS)
	{
        this.nome = nome;
		this.SVA = SVA;
		this.CTS = CTS;
	}

    public void setNome(String nome) {
        this.nome = nome;
    }

	public void setSVA(float SVA)
	{
		this.SVA = SVA;
	}

	public void setCTS(float CTS)
	{
		this.CTS = CTS;
	}

    public String getNome() {
        return nome;
    }

	public float getSVA() {
		return this.SVA;
	}

	public float getCTS() {
		return this.CTS;
	}

	public String toString(){
		return "Nome: " + this.nome + ", SVA: " + this.SVA + ", CTS: " + this.CTS;
	}
}
