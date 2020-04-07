package com.example.myapplication.data.model;

public class clsSolid {
    private int id;
    private int proyecto_id;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getActualizado() {
        return actualizado;
    }

    public void setActualizado(int actualizado) {
        this.actualizado = actualizado;
    }

    public int getVerificado() {
        return verificado;
    }

    public void setVerificado(int verificado) {
        this.verificado = verificado;
    }

    private String nombre;
    private String descripcion;
    private String version;
    private int actualizado;
    private int verificado;
}
