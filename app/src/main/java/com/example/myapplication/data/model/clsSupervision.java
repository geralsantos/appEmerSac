package com.example.myapplication.data.model;

public class clsSupervision {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public int getGeolocalizacion_estado_id() {
        return geolocalizacion_estado_id;
    }

    public void setGeolocalizacion_estado_id(int geolocalizacion_estado_id) {
        this.geolocalizacion_estado_id = geolocalizacion_estado_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLogitud() {
        return logitud;
    }

    public void setLogitud(String logitud) {
        this.logitud = logitud;
    }

    private int id;
    private int proyecto_id;
    private int geolocalizacion_estado_id;
    private String descripcion;
    private int porcentaje;
    private String latitud;
    private String logitud;
}
