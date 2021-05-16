package com.example.proyectorol.Ficha.Virtudes;

public class Virtudes {

    private byte conciencia;
    private byte autocontrol;
    private byte coraje;

    public Virtudes() {
    }

    public Virtudes(byte conciencia, byte autocontrol, byte coraje) {
        this.conciencia = conciencia;
        this.autocontrol = autocontrol;
        this.coraje = coraje;
    }

    public byte getConciencia() {
        return conciencia;
    }

    public void setConciencia(byte conciencia) {
        this.conciencia = conciencia;
    }

    public byte getAutocontrol() {
        return autocontrol;
    }

    public void setAutocontrol(byte autocontrol) {
        this.autocontrol = autocontrol;
    }

    public byte getCoraje() {
        return coraje;
    }

    public void setCoraje(byte coraje) {
        this.coraje = coraje;
    }
}
