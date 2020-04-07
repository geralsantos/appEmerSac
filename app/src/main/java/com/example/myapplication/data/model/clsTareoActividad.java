package com.example.myapplication.data.model;

public class clsTareoActividad {


    private Integer id;

    public clsTareoActividad(Integer id, String hora_inicio, String proyecto_nombre, String solid_nombre, String tarea_nombre) {
        this.id = id;
        this.hora_inicio = hora_inicio;
        this.proyecto_nombre = proyecto_nombre;
        this.solid_nombre = solid_nombre;
        this.tarea_nombre = tarea_nombre;
    }

    private String hora_inicio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getProyecto_nombre() {
        return proyecto_nombre;
    }

    public void setProyecto_nombre(String proyecto_nombre) {
        this.proyecto_nombre = proyecto_nombre;
    }

    public String getSolid_nombre() {
        return solid_nombre;
    }

    public void setSolid_nombre(String solid_nombre) {
        this.solid_nombre = solid_nombre;
    }

    public String getTarea_nombre() {
        return tarea_nombre;
    }

    public void setTarea_nombre(String tarea_nombre) {
        this.tarea_nombre = tarea_nombre;
    }

    private String proyecto_nombre;
    private String solid_nombre;
    private String tarea_nombre;


}
