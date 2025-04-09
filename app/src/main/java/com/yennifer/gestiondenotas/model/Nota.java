package com.yennifer.gestiondenotas.model;

public class Nota {
    private int id;
    private int estudianteId;
    private double valor;

    public Nota(int id, int estudianteId, double valor) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public double getValor() {
        return valor;
    }
}
