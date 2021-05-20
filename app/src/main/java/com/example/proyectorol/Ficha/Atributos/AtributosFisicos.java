package com.example.proyectorol.ficha.Atributos;

import java.io.Serializable;

//Autor: Manuel Vázquez del Pino
public class AtributosFisicos implements Serializable {
    private int fuerza;
    private int destreza;
    private int resistencia;

    public AtributosFisicos() {
    }

    public AtributosFisicos(int fuerza, int destreza, int resistencia) {
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.resistencia = resistencia;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getDestreza() {
        return destreza;
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }
}
