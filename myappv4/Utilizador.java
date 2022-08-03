package com.example.rafae.myappv4;

/**
 * Created by rafae on 14/04/2018.
 */

public class Utilizador {

    private int id, nivelAcesso, nivelPremium;
    private String nomeUtilizador, avatar, email, imagemBackground;
    private String password;
    public Utilizador(int id, String nomeUtilizador, String avatar,
                      int nivelAcesso, int nivelPremium,
                      String email, String imagemBackground, String password) {

        /*
        'id'=>$id,
			 'nomeUtilizador'=>$username,
			 'email'=>$email,
			 'avatar'=>$avatar,
			 'nivelAcesso'=>$nivelAcesso,
			 'nivelPremium'=>$nivelPremium,
			 'imagemBackground'=>$imagemBackground

         */
        this.id                 = id;
        this.nomeUtilizador     = nomeUtilizador;
        this.password           = password;
        this.avatar             = avatar;
        this.email              = email;
        this.nivelAcesso        = nivelAcesso;
        this.nivelPremium       = nivelPremium;
        this.imagemBackground   = imagemBackground;
    }

    public int      getId() {return id;}
    public String   getNomeUtilizador() {
        return nomeUtilizador;
    }
    public String   getPassword() {return password;}
    public String   getAvatar() {
        return avatar;
    }
    public String   getEmail() {
        return email;
    }
    public int      getNivelAcesso() {
        return nivelAcesso;
    }
    public int      getNivelPremium(){ return nivelPremium; }
    public String   getImagemBackground() {
        return imagemBackground;
    }


}
