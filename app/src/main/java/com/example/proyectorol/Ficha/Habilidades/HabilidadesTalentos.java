package com.example.proyectorol.Ficha.Habilidades;

//Autor: Manuel Vázquez del Pino
public class HabilidadesTalentos {

    private byte alerta;
    private byte atletismo;
    private byte intimidacion;

    public HabilidadesTalentos(byte alerta, byte atletismo, byte intimidacion) {
        this.alerta = alerta;
        this.atletismo = atletismo;
        this.intimidacion = intimidacion;
    }

    public HabilidadesTalentos() {

    }

    public byte getAlerta() {
        return alerta;
    }

    public void setAlerta(byte alerta) {
        this.alerta = alerta;
    }

    public byte getAtletismo() {
        return atletismo;
    }

    public void setAtletismo(byte atletismo) {
        this.atletismo = atletismo;
    }

    public byte getIntimidacion() {
        return intimidacion;
    }

    public void setIntimidacion(byte intimidacion) {
        this.intimidacion = intimidacion;
    }
}
