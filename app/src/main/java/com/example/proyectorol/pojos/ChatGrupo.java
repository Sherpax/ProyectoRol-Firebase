package com.example.proyectorol.pojos;

public class ChatGrupo {
    String mensaje;
    String nombre;
    String key;
    String uid;
    boolean mensajeDados;

    public ChatGrupo() {
    }

    public ChatGrupo(String mensaje, String nombre,String uid) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.uid=uid;
    }

    public ChatGrupo(String mensaje, String nombre,String uid,boolean mensajeDados) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.uid=uid;
        this.mensajeDados=mensajeDados;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isMensajeDados() {
        return mensajeDados;
    }

    public void setMensajeDados(boolean mensajeDados) {
        this.mensajeDados = mensajeDados;
    }
}
