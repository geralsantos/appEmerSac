package com.example.myapplication.data.model;

public class clsActividad {



    private int id;


    private String nombre_proyecto;
    private String estado_proyecto;
    private String hito;
    private int cantidad_adjuntos;

    public clsActividad(int id, String proyecto_nombre, String geolocalizacion_estado, String supervision_procentaje, int supervision_adjuntos) {
        this.id = id;
        this.nombre_proyecto = proyecto_nombre;
        this.estado_proyecto = geolocalizacion_estado;
        this.hito = supervision_procentaje;
        this.cantidad_adjuntos = supervision_adjuntos;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre_proyecto() {
        return nombre_proyecto;
    }
    public String getEstado_proyecto() {
        return estado_proyecto;
    }
    public String getHito() {
        return hito;
    }
    public int getCantidad_adjuntos() {
        return cantidad_adjuntos;
    }

   /* public void setNombre_proyecto(String nombre_proyecto) {
        this.nombre_proyecto = nombre_proyecto;
    }*/


   /* public void setEstado_proyecto(String estado_proyecto) {
        this.estado_proyecto = estado_proyecto;
    }*/

/*
    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }*/


   /* public void setCantidad_adjuntos(int cantidad_adjuntos) {
        this.cantidad_adjuntos = cantidad_adjuntos;
    }*/


}
