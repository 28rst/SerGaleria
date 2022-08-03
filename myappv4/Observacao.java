package com.example.rafae.myappv4;

/**
 * Created by rafae on 14/04/2018.
 */

public class Observacao {

    private int id, idAnimal, utilizadorId;
    Double latitude, longitude;
    private String nome, descricao, ficheiro;

    public Observacao(int id, String nome, int idAnimal,
                      String descricao, String ficheiro,
                      Double longitude, Double latitude,
                      int utilizadorId) {

        this.id         = id;
        this.nome    = nome;
        this.ficheiro  = ficheiro;
        this.descricao     = descricao;
    }

    public int      getId() {return id;}
    public String   getNome() {
        return nome;
    }
    public int      getIdAnimal() {
        return idAnimal;
    }
    public String   getDescricao() { return descricao; }
    public String   getFicheiro() {
        return ficheiro;
    }
    public Double   getLongitude() { return longitude; }
    public Double   getLatitude() { return latitude; }
    public int      getUtilizadorId() { return utilizadorId; }
}
