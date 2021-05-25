package com.example.proyectorol.pojos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String nombre;
    private String nick; //El que verán otros usuarios
    private String email;
    private String foto;
    private String fecha;
    private String hora;
    private String estado; //Conectado, Desconectado
    private int solicitudAmistad;
    private int nuevosMensajes;

    public Usuario() {
    }

    public Usuario(String uid,String nick) {
        this.id=uid;
        this.nick=nick;
    }

    public Usuario(String id, String nombre, String nick, String email, String foto, String fecha, String hora, String estado, int solicitudAmistad, int nuevosMensajes) {
        this.id = id;
        this.nombre = nombre;
        this.nick = nick;
        this.email = email;
        this.foto = foto;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.solicitudAmistad = solicitudAmistad;
        this.nuevosMensajes = nuevosMensajes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getSolicitudMensajes() {
        return solicitudAmistad;
    }

    public void setSolicitudMensajes(int solicitudMensajes) {
        this.solicitudAmistad = solicitudMensajes;
    }

    public int getNuevosMensajes() {
        return nuevosMensajes;
    }

    public void setNuevosMensajes(int nuevosMensajes) {
        this.nuevosMensajes = nuevosMensajes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
