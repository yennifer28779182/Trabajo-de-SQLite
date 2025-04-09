package com.yennifer.gestiondenotas.model;

public class Estudiante {
    private int id;
    private String nombre;
    private String codigo;

    public Estudiante(
            int id,
            String nombre,
            String codigo
    ){
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }
}
