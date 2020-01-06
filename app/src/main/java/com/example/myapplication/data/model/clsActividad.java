package com.example.myapplication.data.model;

public class clsActividad {
    private int id_actividad;
    private String nombreTrabajador;
    private String horainicio;
    private String horafin;
    private String horasdiurnas;
    private String renddiurno;
    private String horasnocturas;
    private String rendnocturno;

    public clsActividad(int id_actividad, String nombreTrabajador, String horainicio, String horafin, String horasdiurnas, String renddiurno, String horasnocturas, String rendnocturno) {
        this.id_actividad = id_actividad;
        this.nombreTrabajador = nombreTrabajador;
        this.horainicio = horainicio;
        this.horafin = horafin;
        this.horasdiurnas = horasdiurnas;
        this.renddiurno = renddiurno;
        this.horasnocturas = horasnocturas;
        this.rendnocturno = rendnocturno;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public String getHorasdiurnas() {
        return horasdiurnas;
    }

    public void setHorasdiurnas(String horasdiurnas) {
        this.horasdiurnas = horasdiurnas;
    }

    public String getRenddiurno() {
        return renddiurno;
    }

    public void setRenddiurno(String renddiurno) {
        this.renddiurno = renddiurno;
    }

    public String getHorasnocturas() {
        return horasnocturas;
    }

    public void setHorasnocturas(String horasnocturas) {
        this.horasnocturas = horasnocturas;
    }

    public String getRendnocturno() {
        return rendnocturno;
    }

    public void setRendnocturno(String rendnocturno) {
        this.rendnocturno = rendnocturno;
    }
}
