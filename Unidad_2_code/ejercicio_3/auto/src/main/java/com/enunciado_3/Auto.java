package com.enunciado_3;

/**
 * Auto: subclase que respeta el contrato de Vehiculo sin agregar
 * ni quitar restricciones. Es el caso "correcto" de herencia LSP.
 */
public class Auto extends Vehiculo {
    private int numPuertas;
    private String conductor;

    public Auto(String patente, double kmActuales, int numPuertas) {
        this.patente = patente;
        this.kmActuales = kmActuales;
        this.numPuertas = numPuertas;
        this.conductor = null;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException("Vehiculo no disponible");
        if (kmEstimados <= 0 || kmEstimados > 500)
            throw new IllegalArgumentException("kmEstimados fuera de rango (0, 500]");
        estado = EstadoVehiculo.EN_USO;
        conductor = legajo;
        return "Auto asignado a " + legajo + " por " + kmEstimados + " km";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException("El vehiculo no esta en uso");
        if (kmRecorridos < 0)
            throw new IllegalArgumentException("kmRecorridos no puede ser negativo");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
        conductor = null;
    }

    public String getConductor() {
        return conductor;
    }

    public int getNumPuertas() {
        return numPuertas;
    }

    @Override
    public String toString() {
        return "Auto{patente='" + patente + "', estado=" + estado +
               ", km=" + kmActuales + ", puertas=" + numPuertas + "}";
    }
}
