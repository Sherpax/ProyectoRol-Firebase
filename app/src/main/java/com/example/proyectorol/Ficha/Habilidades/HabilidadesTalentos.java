package com.example.proyectorol.ficha.Habilidades;

import java.io.Serializable;

//Autor: Manuel Vázquez del Pino
public class HabilidadesTalentos implements Serializable {

    private int alerta;
    private int atletismo;
    private int intimidacion;

    public HabilidadesTalentos(int alerta, int atletismo, int intimidacion) {
        this.alerta = alerta;
        this.atletismo = atletismo;
        this.intimidacion = intimidacion;
    }

    public HabilidadesTalentos() {

    }

    public int getAlerta() {
        return alerta;
    }

    public void setAlerta(int alerta) {
        this.alerta = alerta;
    }

    public int getAtletismo() {
        return atletismo;
    }

    public void setAtletismo(int atletismo) {
        this.atletismo = atletismo;
    }

    public int getIntimidacion() {
        return intimidacion;
    }

    public void setIntimidacion(int intimidacion) {
        this.intimidacion = intimidacion;
    }
}
