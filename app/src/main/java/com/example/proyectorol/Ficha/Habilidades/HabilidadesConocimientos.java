package com.example.proyectorol.ficha.Habilidades;

import java.io.Serializable;

public class HabilidadesConocimientos implements Serializable {
    private int informatica;
    private int medicina;
    private int tecnologia;

    public HabilidadesConocimientos(int informatica, int medicina, int tecnologia) {
        this.informatica = informatica;
        this.medicina = medicina;
        this.tecnologia = tecnologia;
    }

    public HabilidadesConocimientos() {

    }

    public int getInformatica() {
        return informatica;
    }

    public void setInformatica(int informatica) {
        this.informatica = informatica;
    }

    public int getMedicina() {
        return medicina;
    }

    public void setMedicina(int medicina) {
        this.medicina = medicina;
    }

    public int getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(int tecnologia) {
        this.tecnologia = tecnologia;
    }
}
