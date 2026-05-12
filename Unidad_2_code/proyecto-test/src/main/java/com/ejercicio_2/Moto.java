package com.ejercicio_2;

public class Moto extends Vehiculo {
    public Moto(String patente, double kmActuales) {
        super(patente, kmActuales);
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE) throw new IllegalStateException();
        // esta es una restriccion mas fuerte (de la precondicion no puede suceder
        if (kmEstimados <= 0 || kmEstimados > 300) throw new IllegalArgumentException("Motos: máx 300 km");
        
        estado = EstadoVehiculo.EN_USO;
        return "Moto asignada";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO) throw new IllegalStateException();
        if (kmRecorridos < 0) throw new IllegalArgumentException("km no puede ser negativo");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }
}
