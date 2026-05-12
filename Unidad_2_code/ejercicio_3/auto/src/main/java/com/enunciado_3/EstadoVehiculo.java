package com.enunciado_3;

public enum EstadoVehiculo {
    DISPONIBLE("disponible"),
    EN_USO("en_uso"),
    MANTENIMIENTO("mantenimiento"),
    BAJA("baja");

    private final String valor;

    EstadoVehiculo(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
