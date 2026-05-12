package com.enunciado_3;

/**
 * Camion: subclase que AGREGA una precondicion adicional
 * (cargaActualKg <= capacidadCargaKg) al metodo asignar().
 * Esta precondicion adicional fortalece el contrato respecto
 * a Vehiculo, lo cual viola LSP.
 */
public class Camion extends Vehiculo {
    private double capacidadCargaKg;
    private double cargaActualKg = 0;

    public Camion(String patente, double kmActuales, double capacidadCargaKg) {
        this.patente = patente;
        this.kmActuales = kmActuales;
        this.capacidadCargaKg = capacidadCargaKg;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    // Pre: kmEstimados > 0 && kmEstimados <= 500   // MISMA precondicion
    //      && estado == DISPONIBLE
    //      && cargaActualKg <= capacidadCargaKg   // PRECONDICION ADICIONAL
    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException();
        if (kmEstimados <= 0 || kmEstimados > 500)
            throw new IllegalArgumentException();
        if (cargaActualKg > capacidadCargaKg)
            throw new SobrecargaException("Carga supera capacidad");
        estado = EstadoVehiculo.EN_USO;
        return "Camion asignado — carga: " + cargaActualKg + " kg";
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

    public double getCargaActualKg() {
        return cargaActualKg;
    }

    public double getCapacidadCargaKg() {
        return capacidadCargaKg;
    }

    public void descargar() {
        cargaActualKg = 0;
    }
}
