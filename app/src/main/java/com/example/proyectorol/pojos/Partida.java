package com.example.proyectorol.pojos;

import java.io.Serializable;

public class Partida implements Serializable {

    private String idPartida;
    private String nombre;
    private int nJugadores;
    private boolean publica;
    private String pass;

    public Partida() {
    }

    public Partida(String idPartida, String nombre, int nJugadores, boolean publica) {
        this.idPartida = idPartida;
        this.nombre = nombre;
        this.nJugadores = nJugadores;
        this.publica = publica;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getnJugadores() {
        return nJugadores;
    }

    public void setnJugadores(int nJugadores) {
        this.nJugadores = nJugadores;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
