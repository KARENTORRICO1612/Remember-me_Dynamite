package com.cdp.agenda.entidades;

public class Contactos {
    /*
    set y get de mis contactos
     */
    private int id;
    private String titulo;
    private String hora;
    private String fecha;
    private String direccion;
    private String descripcion;
    private String adulto_r;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitulo() {

        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = titulo;
    }


    public String getHora(){

        return hora;
    }
    public void setHora(String hora) {
        this.hora =hora;
    }

    public String getFecha(){

        return fecha;
    }
    public void setFecha(String fecha){

        this.fecha = fecha;
    }

    public String getDireccion(){

        return direccion;
    }
    public void setDireccion(String direccion) {

        this.direccion = direccion;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public String getAdulto_r() {
        return adulto_r;
    }

    public void setAdulto_r(String adulto_r) {
        this.adulto_r = adulto_r;
    }
}
