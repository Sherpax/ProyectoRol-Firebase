package com.example.proyectorol.ficha.Atributos;

import java.io.Serializable;

public class AtributosSociales implements Serializable {
    private byte carisma;
    private byte manipulacion;
    private byte apariencia;

    public AtributosSociales() {
    }

    public AtributosSociales(byte carisma, byte manipulacion, byte apariencia) {
        this.carisma = carisma;
        this.manipulacion = manipulacion;
        this.apariencia = apariencia;
    }

    public byte getCarisma() {
        return carisma;
    }

    public void setCarisma(byte carisma) {
        this.carisma = carisma;
    }

    public byte getManipulacion() {
        return manipulacion;
    }

    public void setManipulacion(byte manipulacion) {
        this.manipulacion = manipulacion;
    }

    public byte getApariencia() {
        return apariencia;
    }

    public void setApariencia(byte apariencia) {
        this.apariencia = apariencia;
    }
}
