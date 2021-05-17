package com.example.proyectorol.ficha.Habilidades;

import java.io.Serializable;

public class HabilidadesTecnicas implements Serializable {
    private int sigilo;
    private int armasFuego;
    private int armasMelee;

    public HabilidadesTecnicas(int sigilo, int armasFuego, int armasMelee) {
        this.sigilo = sigilo;
        this.armasFuego = armasFuego;
        this.armasMelee = armasMelee;
    }

    public HabilidadesTecnicas() {

    }

    public int getSigilo() {
        return sigilo;
    }

    public void setSigilo(int sigilo) {
        this.sigilo = sigilo;
    }

    public int getArmasFuego() {
        return armasFuego;
    }

    public void setArmasFuego(int armasFuego) {
        this.armasFuego = armasFuego;
    }

    public int getArmasMelee() {
        return armasMelee;
    }

    public void setArmasMelee(int armasMelee) {
        this.armasMelee = armasMelee;
    }
}
