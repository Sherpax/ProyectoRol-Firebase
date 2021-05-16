package com.example.proyectorol.Ficha.Habilidades;

public class HabilidadesConocimientos {
    private byte informatica;
    private byte medicina;
    private byte tecnologia;

    public HabilidadesConocimientos(byte informatica, byte medicina, byte tecnologia) {
        this.informatica = informatica;
        this.medicina = medicina;
        this.tecnologia = tecnologia;
    }

    public HabilidadesConocimientos() {

    }

    public byte getInformatica() {
        return informatica;
    }

    public void setInformatica(byte informatica) {
        this.informatica = informatica;
    }

    public byte getMedicina() {
        return medicina;
    }

    public void setMedicina(byte medicina) {
        this.medicina = medicina;
    }

    public byte getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(byte tecnologia) {
        this.tecnologia = tecnologia;
    }
}
