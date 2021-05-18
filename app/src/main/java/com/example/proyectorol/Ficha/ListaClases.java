package com.example.proyectorol.ficha;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.List;

//Autor: Manuel Vázquez del Pino
public class ListaClases implements Serializable {
    private String nombre;
    private String naturaleza;
    private String clan;
    private String uid;
    private List<Object> listaAtributos = new LinkedList();
    private List<Object> listaHabilidades = new LinkedList();
    private List<Object> listaVirtudes = new LinkedList();

    public ListaClases(){
    }

    public ListaClases(String nombre, String naturaleza, String clan, String uid, List<Object> listaAtributos, List<Object> listaHabilidades, List<Object> listaVirtudes) {
        this.nombre = nombre;
        this.naturaleza = naturaleza;
        this.clan = clan;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Object> getListaAtributos() {
        return listaAtributos;
    }

    public void setListaAtributos(List<Object> listaAtributos) {
        this.listaAtributos = listaAtributos;
    }

    public List<Object> getListaHabilidades() {
        return listaHabilidades;
    }

    public void setListaHabilidades(List<Object> listaHabilidades) {
        this.listaHabilidades = listaHabilidades;
    }

    public List<Object> getListaVirtudes() {
        return listaVirtudes;
    }

    public void setListaVirtudes(List<Object> listaVirtudes) {
        this.listaVirtudes = listaVirtudes;
    }
}
