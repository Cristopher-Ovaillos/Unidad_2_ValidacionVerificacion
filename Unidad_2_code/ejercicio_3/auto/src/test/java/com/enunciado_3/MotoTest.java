package com.enunciado_3;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ITEM 12 — Ejecucion de la Suite Heredada sobre Moto.
 *
 * Moto RESTRINGE el limite de km de 500 a 300 en el metodo asignar().
 * Se espera que TC-A3 (asignar con km=500) FALLE porque Moto lanza
 * IllegalArgumentException para km > 300.
 *
 * Esto es una VIOLACION de LSP (Propiedad 2.3): Pre_Moto es MAS
 * FUERTE que Pre_Vehiculo, ya que excluye valores de km en (300, 500]
 * que la superclase acepta.
 */
@DisplayName("Test Moto — Suite Heredada de Vehiculo")
public class MotoTest extends VehiculoTest {

    @Override
    protected Vehiculo crearVehiculo() {
        return new Moto("MOTO01", 0.0, false);
    }

    @Override
    protected String nombreSubclase() {
        return "Moto";
    }

    // ================================================================
    // Tests especificos de Moto (no estan en la Suite Heredada)
    // ================================================================

    @org.junit.jupiter.api.Nested
    @DisplayName("Tests especificos de Moto")
    class TestsEspecificosMoto {

        @org.junit.jupiter.api.Test
        @DisplayName("TE-M1: Moto acepta km = 300 (su limite real)")
        void moto_acepta_300km() {
            Moto m = new Moto("MOTO02", 0.0, true);
            String resultado = m.asignar("LEG-200", 300.0);
            assertNotNull(resultado);
            assertEquals(EstadoVehiculo.EN_USO, m.getEstado());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-M2: Moto rechaza km = 301 (fuera de su limite)")
        void moto_rechaza_301km() {
            Moto m = new Moto("MOTO03", 0.0, false);
            assertThrows(IllegalArgumentException.class, () ->
                m.asignar("LEG-201", 301.0));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-M3: Verificar atributo requiereLicenciaEspecial")
        void moto_verifica_licencia() {
            Moto m1 = new Moto("MOTO04", 0.0, true);
            Moto m2 = new Moto("MOTO05", 0.0, false);
            assertTrue(m1.isRequiereLicenciaEspecial());
            assertFalse(m2.isRequiereLicenciaEspecial());
        }
    }
}
