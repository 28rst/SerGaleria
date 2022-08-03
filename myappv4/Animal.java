package com.example.rafae.myappv4;

/**
 * Created by rafae on 04/04/2018.
 */

public class Animal {

    private int id;
    private String especie, nomeComum, sexo, ordem, familia, classe, genero, descri;

    public Animal(int id, String especie, String nomeComum, String sexo, String ordem,
                  String familia, String classe, String genero, String descri) {

        this.id         = id;
        this.especie    = especie;
        this.nomeComum  = nomeComum;
        this.sexo       = sexo;
        this.ordem      = ordem;
        this.familia    = familia;
        this.classe     = classe;
        this.genero     = genero;
        this.descri     = descri;
    }

    public int      getId() {return id;}
    public String   getEspecie() {
        return especie;
    }
    public String   getNomeComum() {
        return nomeComum;
    }
    public String   getSexo() {
        return sexo;
    }
    public String   getOrdem() {
        return ordem;
    }
    public String   getFamilia() {
        return familia;
    }
    public String   getClasse() {
        return classe;
    }
    public String   getGenero() {
        return genero;
    }
    public String   getDescri() {
        return descri;
    }
}
