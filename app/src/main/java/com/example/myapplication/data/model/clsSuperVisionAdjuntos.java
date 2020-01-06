package com.example.myapplication.data.model;

public class clsSuperVisionAdjuntos {

    private int id;
    private int supervision_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupervision_id() {
        return supervision_id;
    }

    public void setSupervision_id(int supervision_id) {
        this.supervision_id = supervision_id;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    private String adjunto;
    private int tipo;

}
