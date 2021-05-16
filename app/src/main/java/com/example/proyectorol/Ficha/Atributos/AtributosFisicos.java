package com.example.proyectorol.ficha.Atributos;

import java.io.Serializable;

//Autor: Manuel Vázquez del Pino
public class AtributosFisicos implements Serializable {
    private byte fuerza;
    private byte destreza;
    private byte resistencia;

    public AtributosFisicos() {
    }

    public AtributosFisicos(byte fuerza, byte destreza, byte resistencia) {
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.resistencia = resistencia;
    }

    public byte getFuerza() {
        return fuerza;
    }

    public void setFuerza(byte fuerza) {
        this.fuerza = fuerza;
    }

    public byte getDestreza() {
        return destreza;
    }

    public void setDestreza(byte destreza) {
        this.destreza = destreza;
    }

    public byte getResistencia() {
        return resistencia;
    }

    public void setResistencia(byte resistencia) {
        this.resistencia = resistencia;
    }
}
