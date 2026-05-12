package com.enunciado_3;

public abstract class Vehiculo {
    protected String patente;
    protected double kmActuales;
    protected EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;


    // Contrato:
    // Pre: kmEstimados > 0 && kmEstimados <= 500 && estado == DISPONIBLE
    // Post: estado == EN_USO && retorna confirmación no nula
    public abstract String asignar(String legajo, double kmEstimados);


    // Pre: estado == EN_USO && kmRecorridos >= 0
    // Post: estado == DISPONIBLE && kmActuales += kmRecorridos
    public abstract void liberar(double kmRecorridos);


    public EstadoVehiculo getEstado() { return estado; }
    public double getKm() { return kmActuales; }
}
