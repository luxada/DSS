package business.SubUtilizadores;

public class Administrador extends Utilizador{
    // Um administrador é sempre premium e admin logo os valores vão ser ambos 1
    public Administrador(String n, String m, int c){
        super(n,m,c);
    }

    public boolean isAdmin(){
        return true;
    }

    public boolean isJogador(){
        return false;
    }
}
