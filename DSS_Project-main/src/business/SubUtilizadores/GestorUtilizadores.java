package business.SubUtilizadores;

import data.UtilizadorDAO;

import java.util.Map;

public class GestorUtilizadores implements IUtilizadores {
    private Map<String, Utilizador> utilizadores;

    public GestorUtilizadores(){
        this.utilizadores = UtilizadorDAO.getInstance();
    }

    public void criaAdmin(String username, String password, int c){
        Administrador admin = new Administrador(username, password, c);
        this.utilizadores.put(username, admin);
    }

    public void criaJogador(String username, String password, int c, int premium){
        Jogador jogador = new Jogador(username, password, c, premium);
        this.utilizadores.put(username, jogador);
    }

    // Esta função verifica se o utilizador já existe na base de dados (UserDAO), se ainda não existir cria um, se já existir apenas autentica-o na plataforma
    public boolean signUpJogador(String username, String password, boolean premium){
        if(!this.utilizadores.containsKey(username)){ // Se não existir o utilizador na base de dados
            Utilizador u;
            if(premium){
                u = new Jogador(username, password, 0, 1);
            }else{
                u = new Jogador(username, password, 0, 0);
            }
            this.utilizadores.put(username, u);
            return true;
        }
        return false;
    }

    public boolean loginAdmin(String nome, String pass){
        if(this.utilizadores.containsKey(nome)){
            Utilizador u = this.utilizadores.get(nome);
            if(u.isAdmin()){
                return true;
            }
        }
        return false;
    }

    public boolean loginJogador(String nome, String pass){
        if(this.utilizadores.containsKey(nome)){
            Utilizador u = this.utilizadores.get(nome);
            if(u.isJogador()){
                return true;
            }
        }
        return false;
    }

    public Utilizador getUtilizador(String nome){
        return this.utilizadores.get(nome);
    }

    public Map<String, Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public void setUtilizadores(Map<String, Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }
}


