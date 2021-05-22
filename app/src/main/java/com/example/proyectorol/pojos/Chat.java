package com.example.proyectorol.pojos;

public class Chat {
    private String id;
    private String envia, recibe, mensaje, fecha, hora;
    private boolean visto;

    public Chat() {
    }


    public Chat(String id, String envia, String recibe, String mensaje, String fecha, String hora, boolean visto) {
        this.id = id;
        this.envia = envia;
        this.recibe = recibe;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.hora = hora;
        this.visto = visto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvia() {
        return envia;
    }

    public void setEnvia(String envia) {
        this.envia = envia;
    }

    public String getRecibe() {
        return recibe;
    }

    public void setRecibe(String recibe) {
        this.recibe = recibe;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }
}
