package com.example.proyectorol.Ficha.Habilidades;

public class HabilidadesTecnicas {
    private byte sigilo;
    private byte armasFuego;
    private byte armasMelee;

    public HabilidadesTecnicas(byte sigilo, byte armasFuego, byte armasMelee) {
        this.sigilo = sigilo;
        this.armasFuego = armasFuego;
        this.armasMelee = armasMelee;
    }

    public HabilidadesTecnicas() {

    }

    public byte getSigilo() {
        return sigilo;
    }

    public void setSigilo(byte sigilo) {
        this.sigilo = sigilo;
    }

    public byte getArmasFuego() {
        return armasFuego;
    }

    public void setArmasFuego(byte armasFuego) {
        this.armasFuego = armasFuego;
    }

    public byte getArmasMelee() {
        return armasMelee;
    }

    public void setArmasMelee(byte armasMelee) {
        this.armasMelee = armasMelee;
    }
}
