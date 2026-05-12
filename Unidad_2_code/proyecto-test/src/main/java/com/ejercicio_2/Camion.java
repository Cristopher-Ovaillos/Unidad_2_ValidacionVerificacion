package com.ejercicio_2;

public class Camion extends Vehiculo {
    private double capacidadCargaKg;
    private double cargaActualKg = 0;

    public Camion(String patente, double km, double capacidad) {
        super(patente, km);
        this.capacidadCargaKg = capacidad;
    }



    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE) throw new IllegalStateException();
        if (kmEstimados <= 0 || kmEstimados > 500) throw new IllegalArgumentException();
        // PRECONDICIÓN ADICIONAL (Violación potencial de LSP) 
        if (cargaActualKg > capacidadCargaKg) throw new RuntimeException("Sobrecarga");
        
        estado = EstadoVehiculo.EN_USO;
        return "Camión asignado";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException();
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }


    public void cargar(double kg) {
        if (cargaActualKg + kg > capacidadCargaKg)
            throw new SobrecargaException("Sobrecarga");
        cargaActualKg += kg;
    }

}