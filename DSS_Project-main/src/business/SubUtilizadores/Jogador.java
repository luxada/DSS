package business.SubUtilizadores;

public class Jogador extends  Utilizador{
    // Indica se o jogador é ou não premium
    private boolean premium;

    public Jogador(String n, String m, int c, int premium){
        super(n,m,c);
        this.premium = premium == 1;
    }

    public boolean isAdmin(){
        return false;
    }

    public boolean isJogador(){
        return true;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
