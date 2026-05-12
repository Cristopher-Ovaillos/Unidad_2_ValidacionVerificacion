package com.ejercicio_2;

//segun el ejercicio 1
public class Auto extends Vehiculo {
    private int numPuertas;

    public Auto(String patente, String marca, int anio, double kmActuales, int numPuertas) {
        super(patente, kmActuales);
        this.numPuertas = numPuertas;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        // Precondiciones del contrato de Vehiculo 
        if (estado != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalStateException("Vehiculo no disponible"); // 
        }
        if (kmEstimados <= 0 || kmEstimados > 500) {
            throw new IllegalArgumentException("km estimados fuera de rango"); 
        }
        
        this.estado = EstadoVehiculo.EN_USO; 
        return "Asignado a " + legajo + " por " + kmEstimados + " km"; 
    }

    @Override
    public void liberar(double kmRecorridos) {
        // Precondiciones del contrato 
        if (estado != EstadoVehiculo.EN_USO) {
            throw new IllegalStateException("El vehiculo no está en uso"); 
        }
        if (kmRecorridos < 0) {
            throw new IllegalArgumentException("km no puede ser negativo"); 
        }
        
        this.kmActuales += kmRecorridos; 
        this.estado = EstadoVehiculo.DISPONIBLE; 
    }
}