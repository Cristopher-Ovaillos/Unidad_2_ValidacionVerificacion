package com.enunciado_3;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ITEM 12 — Ejecucion de la Suite Heredada sobre Auto.
 *
 * Auto implementa el contrato de Vehiculo sin agregar ni quitar
 * restricciones. Se espera que TODOS los tests de la Suite Heredada
 * pasen (LSP cumplido).
 *
 * Algoritmo 2.4 (Verificacion LSP), Paso: ejecutar la Suite Heredada
 * sustituyendo instancias de la subclase.
 */
@DisplayName("Test Auto — Suite Heredada de Vehiculo")
public class AutoTest extends VehiculoTest {

    @Override
    protected Vehiculo crearVehiculo() {
        return new Auto("ABC123", 0.0, 4);
    }

    @Override
    protected String nombreSubclase() {
        return "Auto";
    }

    // ================================================================
    // Tests especificos de Auto (no estan en la Suite Heredada)
    // ================================================================

    @org.junit.jupiter.api.Nested
    @DisplayName("Tests especificos de Auto")
    class TestsEspecificosAuto {

        @org.junit.jupiter.api.Test
        @DisplayName("TE-A1: Auto registra conductor tras asignar")
        void auto_registra_conductor() {
            Auto a = new Auto("XYZ789", 10.0, 4);
            assertNull(a.getConductor());
            a.asignar("LEG-100", 50.0);
            assertEquals("LEG-100", a.getConductor());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-A2: Auto libera conductor tras liberar")
        void auto_libera_conductor() {
            Auto a = new Auto("XYZ789", 10.0, 4);
            a.asignar("LEG-101", 50.0);
            a.liberar(50.0);
            assertNull(a.getConductor());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-A3: Invariante de Auto — puertas en {2,4,5}")
        void invariante_puertas() {
            Auto a2 = new Auto("AAA", 0, 2);
            Auto a4 = new Auto("BBB", 0, 4);
            Auto a5 = new Auto("CCC", 0, 5);
            assertTrue(a2.getNumPuertas() == 2);
            assertTrue(a4.getNumPuertas() == 4);
            assertTrue(a5.getNumPuertas() == 5);
        }
    }
}
