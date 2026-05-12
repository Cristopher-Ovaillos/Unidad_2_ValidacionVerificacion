package com.example;

public class Camion extends Vehiculo {
    private double capacidadCargaKg;
    private double cargaActualKg = 0;

    public Camion(String patente, double capacidadCargaKg) {
        this.patente = patente;
        this.capacidadCargaKg = capacidadCargaKg;
        this.kmActuales = 0;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException("Vehiculo no disponible");
        if (kmEstimados <= 0 || kmEstimados > 500)
            throw new IllegalArgumentException("km fuera de rango");
        if (cargaActualKg > capacidadCargaKg)
            throw new SobrecargaException("Carga supera capacidad");
        estado = EstadoVehiculo.EN_USO;
        return "Camion asignado - carga: " + cargaActualKg + " kg";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException("El vehiculo no esta en uso");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }

    public void cargar(double kg) {
        if (cargaActualKg + kg > capacidadCargaKg)
            throw new SobrecargaException("Sobrecarga");
        cargaActualKg += kg;
    }
}
