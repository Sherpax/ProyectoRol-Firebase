package com.example.proyectorol.Ficha.Atributos;

import java.io.Serializable;

public class AtributosMentales implements Serializable {
    private byte percepcion;
    private byte inteligencia;
    private byte astucia;

    public AtributosMentales() {
    }

    public AtributosMentales(byte percepcion, byte inteligencia, byte astucia) {
        this.percepcion = percepcion;
        this.inteligencia = inteligencia;
        this.astucia = astucia;
    }

    public byte getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(byte percepcion) {
        this.percepcion = percepcion;
    }

    public byte getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(byte inteligencia) {
        this.inteligencia = inteligencia;
    }

    public byte getAstucia() {
        return astucia;
    }

    public void setAstucia(byte astucia) {
        this.astucia = astucia;
    }
}

