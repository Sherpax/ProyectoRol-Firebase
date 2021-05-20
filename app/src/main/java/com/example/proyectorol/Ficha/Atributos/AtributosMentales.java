package com.example.proyectorol.ficha.Atributos;

import java.io.Serializable;

public class AtributosMentales implements Serializable {
    private int percepcion;
    private int inteligencia;
    private int astucia;

    public AtributosMentales() {
    }

    public AtributosMentales(int percepcion, int inteligencia, int astucia) {
        this.percepcion = percepcion;
        this.inteligencia = inteligencia;
        this.astucia = astucia;
    }

    public int getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public int getAstucia() {
        return astucia;
    }

    public void setAstucia(int astucia) {
        this.astucia = astucia;
    }
}

