package business.SubCriacao;

public class MarcaModelo 
{
	private String marca, modelo;

	public MarcaModelo(String marca, String modelo) 
	{
		this.marca = marca;
		this.modelo = modelo;
	}

	public String getMarca() { return this.marca; }

	public String getModelo() { return this.modelo; }

	public void setMarca(String marca) { this.marca = marca; }

	public void setModelo(String modelo) { this.modelo = modelo; }	
}
