package com.example.proyectorol.pojos;

public class ChatCon {

    private String remitenteID;
    private String remitenteNick;
    private String ultimoMensaje;

    public ChatCon() {
    }

    public ChatCon(String remitenteID, String remitenteNick, String ultimoMensaje) {
        this.remitenteID = remitenteID;
        this.remitenteNick = remitenteNick;
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getRemitenteID() {
        return remitenteID;
    }

    public void setRemitenteID(String remitenteID) {
        this.remitenteID = remitenteID;
    }

    public String getRemitenteNick() {
        return remitenteNick;
    }

    public void setRemitenteNick(String remitenteNick) {
        this.remitenteNick = remitenteNick;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}
