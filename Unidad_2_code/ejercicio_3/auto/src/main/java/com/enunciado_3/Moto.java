package com.enunciado_3;

/**
 * Moto: subclase que RESTRINGE más el contrato de Vehiculo.
 * Solo permite asignacion por maximo 300 km (la superclase permite hasta 500).
 * Esto viola LSP porque fortalece la precondicion.
 */
public class Moto extends Vehiculo {
    private boolean requiereLicenciaEspecial;

    public Moto(String patente, double kmActuales, boolean requiereLicenciaEspecial) {
        this.patente = patente;
        this.kmActuales = kmActuales;
        this.requiereLicenciaEspecial = requiereLicenciaEspecial;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    @Override
    public String asignar(String legajo, double kmEstimados) {
        if (estado != EstadoVehiculo.DISPONIBLE)
            throw new IllegalStateException("Moto no disponible");
        if (kmEstimados <= 0 || kmEstimados > 300)
            throw new IllegalArgumentException("Motos: max 300 km");
        estado = EstadoVehiculo.EN_USO;
        return "Moto asignada";
    }

    @Override
    public void liberar(double kmRecorridos) {
        if (estado != EstadoVehiculo.EN_USO)
            throw new IllegalStateException("La moto no esta en uso");
        if (kmRecorridos < 0)
            throw new IllegalArgumentException("kmRecorridos no puede ser negativo");
        kmActuales += kmRecorridos;
        estado = EstadoVehiculo.DISPONIBLE;
    }

    public boolean isRequiereLicenciaEspecial() {
        return requiereLicenciaEspecial;
    }
}
