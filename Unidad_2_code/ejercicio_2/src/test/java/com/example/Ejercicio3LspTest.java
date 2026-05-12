package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Ejercicio3LspTest {

    @Test
    @DisplayName("LSP: Auto acepta km=400 (dentro de precondicion padre <=500)")
    void lspAutoAcepta400km() {
        Auto auto = new Auto("ABC123", 4);
        String r = auto.asignar("L001", 400);
        assertNotNull(r);
        assertEquals(EstadoVehiculo.EN_USO, auto.getEstado());
    }

    @Test
    @DisplayName("LSP Violada: Moto rechaza km=400 (restringe a 300, padre permite <=500)")
    void lspMotoRechaza400km() {
        Vehiculo moto = new Moto("MOTO01");
        assertThrows(IllegalArgumentException.class, () -> moto.asignar("L001", 400));
    }

    @Test
    @DisplayName("LSP Violada: Camion tiene precondicion adicional (carga) no presente en padre")
    void lspCamionPrecondicionAdicional() {
        Camion camion = new Camion("CAM001", 1000);
        camion.cargar(900);
        camion.asignar("L001", 100);
        assertEquals(EstadoVehiculo.EN_USO, camion.getEstado());
    }

    @Test
    @DisplayName("Polimorfismo seguro: Auto cumple contrato del padre")
    void polimorfismoSeguroAuto() {
        Vehiculo v = new Auto("ABC123", 4);
        assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
        v.asignar("L001", 200);
        assertEquals(EstadoVehiculo.EN_USO, v.getEstado());
        v.liberar(150);
        assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
        assertEquals(150, v.getKm());
    }
}
