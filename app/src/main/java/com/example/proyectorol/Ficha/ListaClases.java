package com.example.proyectorol.ficha;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

//Autor: Manuel Vázquez del Pino
public class ListaClases implements Serializable {
    private String nombre;
    private String naturaleza;
    private String clan;
    private String emailJugador;
    private LinkedList<Object> listaAtributos = new LinkedList();
    private LinkedList<Object> listaHabilidades = new LinkedList();
    private LinkedList<Object> listaVirtudes = new LinkedList();

    public ListaClases(){
    }

    public ListaClases(String nombre, String naturaleza, String clan, String emailJugador, LinkedList<Object> listaAtributos, LinkedList<Object> listaHabilidades, LinkedList<Object> listaVirtudes) {
        this.nombre = nombre;
        this.naturaleza = naturaleza;
        this.clan = clan;
        this.emailJugador = emailJugador;
        this.listaAtributos = listaAtributos;
        this.listaHabilidades = listaHabilidades;
        this.listaVirtudes = listaVirtudes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public String getEmailJugador() {
        return emailJugador;
    }

    public void setEmailJugador(String emailJugador) {
        this.emailJugador = emailJugador;
    }

    public LinkedList<Object> getListaAtributos() {
        return listaAtributos;
    }

    public void setListaAtributos(LinkedList<Object> listaAtributos) {
        this.listaAtributos = listaAtributos;
    }

    public LinkedList<Object> getListaHabilidades() {
        return listaHabilidades;
    }

    public void setListaHabilidades(LinkedList<Object> listaHabilidades) {
        this.listaHabilidades = listaHabilidades;
    }

    public LinkedList<Object> getListaVirtudes() {
        return listaVirtudes;
    }

    public void setListaVirtudes(LinkedList<Object> listaVirtudes) {
        this.listaVirtudes = listaVirtudes;
    }
}
