package com.domdev.pc.pogodne;

public class Game {
    int _id;
    String zabawa;
    String kategoria;
    String tekst;
    String lastUpdate;

    public Game(){}

    public Game(int _id, String zabawa, String kategoria, String tekst, String lastUpdate) {
        this._id = _id;
        this.zabawa = zabawa;
        this.kategoria = kategoria;
        this.tekst = tekst;
        this.lastUpdate = lastUpdate;
    }

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

    public String getLastUpdate() {
        return lastUpdate;
    }
}
