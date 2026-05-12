package com.example;

public abstract class Vehiculo {
    protected String patente;
    protected double kmActuales;
    protected EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    public abstract String asignar(String legajo, double kmEstimados);

    public abstract void liberar(double kmRecorridos);

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public double getKm() {
        return kmActuales;
    }
}
