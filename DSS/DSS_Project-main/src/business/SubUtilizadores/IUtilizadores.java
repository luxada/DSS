package business.SubUtilizadores;

public interface IUtilizadores {
    public void criaAdmin(String username, String password, int c);
    public void criaJogador(String username, String password, int c, int premium);
    public boolean signUpJogador(String username, String password, boolean premium);
    public boolean loginAdmin(String nome, String pass);
    public boolean loginJogador(String nome, String pass);
    public Utilizador getUtilizador(String nome);
}
