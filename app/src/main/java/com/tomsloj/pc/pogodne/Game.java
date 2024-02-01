package com.tomsloj.pc.pogodne;

public class Game {
    int _id;
    String zabawa;
    String kategoria;
    String tekst;

    public Game(){}

//    public Game(int _id, String zabawa, String kategoria, String tekst) {
//        this._id = _id;
//        this.zabawa = zabawa;
//        this.kategoria = kategoria;
//        this.tekst = tekst;
//    }

    public int get_id() {
        return _id;
    }

    public String getZabawa() {
        return zabawa;
    }

    public String getKategoria() {
        return kategoria;
    }

    public String getTekst() {
        return tekst;
    }
}
