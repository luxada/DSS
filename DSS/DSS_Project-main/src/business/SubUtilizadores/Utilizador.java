package business.SubUtilizadores;

public abstract class Utilizador 
{
	private String nome;
	private String password;
	private int classificacao_global;

    public Utilizador()
    {
        this.nome = "";
        this.password = "";
        this.classificacao_global = 0;
    }

    public Utilizador(String n, String m, int c)
    {
        this();
        this.nome = n;
        this.password = m;
        this.classificacao_global = c;
	}

    public Utilizador(Utilizador u)
    {
        this.nome = u.getNome();
        this.password = u.getPassword();
        this.classificacao_global = u.getClassificacaoGlobal();
	}

    public abstract boolean isAdmin();

    public abstract boolean isJogador();
    
    public String getNome()
    {
        return this.nome;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    public int getClassificacaoGlobal()
    {
        return this.classificacao_global;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClassificacao_global(int classificacao_global) {
        this.classificacao_global = classificacao_global;
    }

    public String toString()
    {
        return "nome: " + this.nome + " password: " + this.password + " classificação geral: " + this.classificacao_global;
    }
    public boolean equals(Object o)
    {
        if(this==o)
        return true;
        
        if(o==null || this.getClass()!=o.getClass())
        return false;
        
        Utilizador u = (Utilizador) o;
        return	this.nome.equals(u.getNome()) &&
                this.password.equals(u.getPassword()) &&
                this.classificacao_global == u.getClassificacaoGlobal();
    }

}
