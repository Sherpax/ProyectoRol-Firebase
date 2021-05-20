package com.example.proyectorol.ficha.Atributos;

import java.io.Serializable;

public class AtributosSociales implements Serializable {
    private int carisma;
    private int manipulacion;
    private int apariencia;

    public AtributosSociales() {
    }

    public AtributosSociales(int carisma, int manipulacion, int apariencia) {
        this.carisma = carisma;
        this.manipulacion = manipulacion;
        this.apariencia = apariencia;
    }

    public int getCarisma() {
        return carisma;
    }

    public void setCarisma(int carisma) {
        this.carisma = carisma;
    }

    public int getManipulacion() {
        return manipulacion;
    }

    public void setManipulacion(int manipulacion) {
        this.manipulacion = manipulacion;
    }

    public int getApariencia() {
        return apariencia;
    }

    public void setApariencia(int apariencia) {
        this.apariencia = apariencia;
    }
}
