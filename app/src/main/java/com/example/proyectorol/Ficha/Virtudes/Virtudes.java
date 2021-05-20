package com.example.proyectorol.ficha.Virtudes;

import java.io.Serializable;

public class Virtudes implements Serializable {

    private int conciencia;
    private int autocontrol;
    private int coraje;

    public Virtudes() {
    }

    public Virtudes(int conciencia, int autocontrol, int coraje) {
        this.conciencia = conciencia;
        this.autocontrol = autocontrol;
        this.coraje = coraje;
    }

    public int getConciencia() {
        return conciencia;
    }

    public void setConciencia(int conciencia) {
        this.conciencia = conciencia;
    }

    public int getAutocontrol() {
        return autocontrol;
    }

    public void setAutocontrol(int autocontrol) {
        this.autocontrol = autocontrol;
    }

    public int getCoraje() {
        return coraje;
    }

    public void setCoraje(int coraje) {
        this.coraje = coraje;
    }
}
