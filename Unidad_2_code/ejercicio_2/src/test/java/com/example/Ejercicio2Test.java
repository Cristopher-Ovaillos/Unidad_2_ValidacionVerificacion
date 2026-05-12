package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Ejercicio2Test {

    private Auto auto;

    @BeforeEach
    void setUp() {
        auto = new Auto("ABC123", 4);
    }

    @Test
    @DisplayName("Estado inicial: DISPONIBLE")
    void estadoInicial() {
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
    }

    @Test
    @DisplayName("C-Trans: DISPONIBLE -> EN_USO mediante asignar()")
    void transicionDisponibleEnUso() {
        String resultado = auto.asignar("L001", 100);
        assertNotNull(resultado);
        assertEquals(EstadoVehiculo.EN_USO, auto.getEstado());
    }

    @Test
    @DisplayName("C-Trans: EN_USO -> DISPONIBLE mediante liberar()")
    void transicionEnUsoDisponible() {
        auto.asignar("L001", 100);
        auto.liberar(80);
        assertEquals(EstadoVehiculo.DISPONIBLE, auto.getEstado());
        assertEquals(80, auto.getKm());
    }

    @Test
    @DisplayName("C-Trans: DISPONIBLE -> MANTENIMIENTO")
    void transicionDisponibleMantenimiento() {
        auto.enviarMantenimiento();
        assertEquals(EstadoVehiculo.MANTENIMIENTO, auto.getEstado());
    }

    @Test
    @DisplayName("C-Trans: DISPONIBLE -> BAJA")
    void transicionDisponibleBaja() {
        auto.darDeBaja();
        assertEquals(EstadoVehiculo.BAJA, auto.getEstado());
    }

    @Test
    @DisplayName("Transicion invalida: EN_USO -> MANTENIMIENTO")
    void transicionInvalidaEnUsoMantenimiento() {
        auto.asignar("L001", 100);
        assertThrows(IllegalStateException.class, () -> auto.enviarMantenimiento());
    }

    @Test
    @DisplayName("Transicion invalida: EN_USO -> BAJA")
    void transicionInvalidaEnUsoBaja() {
        auto.asignar("L001", 100);
        assertThrows(IllegalStateException.class, () -> auto.darDeBaja());
    }

    @Test
    @DisplayName("Transicion invalida: asignar cuando no esta DISPONIBLE")
    void asignarCuandoNoDisponible() {
        auto.asignar("L001", 100);
        assertThrows(IllegalStateException.class, () -> auto.asignar("L002", 50));
    }

    @Test
    @DisplayName("Transicion invalida: liberar cuando no esta EN_USO")
    void liberarCuandoNoEnUso() {
        assertThrows(IllegalStateException.class, () -> auto.liberar(50));
    }

    @Test
    @DisplayName("BUG: km=500 rechazado por >= en lugar de >")
    void bugKm500Rechazado() {
        assertThrows(IllegalArgumentException.class, () -> auto.asignar("L001", 500));
    }
}
