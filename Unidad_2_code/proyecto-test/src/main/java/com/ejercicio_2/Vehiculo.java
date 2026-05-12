package com.ejercicio_2;

import com.ejercicio_2.EstadoVehiculo;

//vehiculo es una clase abstracta
public abstract class Vehiculo {
    protected String patente;
    protected double kmActuales;
    protected EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    public Vehiculo(String patente, double kmActuales) {
        this.patente = patente;
        this.kmActuales = kmActuales;

    }
    /*
     * Contrato superclase:
     * - pre: kmestimados>0 && kmestimados<=500 && estado == disponible
     */

    // metodos
    public abstract String asignar(String legajo, double kmestimados);

    // Pre: estado == EN_USO && kmRecorridos >= 0
    public abstract void liberar(double kmRecorridos);

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public double getKm() {
        return kmActuales;
    }

    public String getPatente() {
        return patente;
    }

}
