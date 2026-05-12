package com.example;

public class Auto extends Vehiculo {
    private int numPuertas;

    public Auto(String patente, int numPuertas) {
        this.patente = patente;
        this.numPuertas = numPuertas;
        this.kmActuales = 0;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException("Vehiculo no disponible");
        if (kmEstimados <= 0 || kmEstimados >= 500)
            throw new IllegalArgumentException("km_estimados fuera de rango");
        estado = EstadoVehiculo.EN_USO;
        return "Asignado a " + legajo + " por " + kmEstimados + " km";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException("El vehiculo no esta en uso");
        if (kmRecorridos < 0)
            throw new IllegalArgumentException("km_recorridos no puede ser negativo");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }

    public void enviarMantenimiento() {
        if (estado == EstadoVehiculo.EN_USO)
            throw new IllegalStateException("No se puede enviar a mantenimiento en uso");
        estado = EstadoVehiculo.MANTENIMIENTO;
    }

    public void darDeBaja() {
        if (estado == EstadoVehiculo.EN_USO)
            throw new IllegalStateException("No se puede dar de baja un vehiculo en uso");
        estado = EstadoVehiculo.BAJA;
    }
}
