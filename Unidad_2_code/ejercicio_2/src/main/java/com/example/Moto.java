package com.example;

public class Moto extends Vehiculo {
    private boolean requiereLicenciaA;

    public Moto(String patente) {
        this.patente = patente;
        this.kmActuales = 0;
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.requiereLicenciaA = false;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException("Vehiculo no disponible");
        if (kmEstimados <= 0 || kmEstimados > 300)
            throw new IllegalArgumentException("Motos: max 300 km");
        estado = EstadoVehiculo.EN_USO;
        return "Moto asignada";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException("El vehiculo no esta en uso");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }
}
