package com.enunciado_3;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ITEM 12 — Ejecucion de la Suite Heredada sobre Camion.
 *
 * Camion AGREGA una precondicion adicional al metodo asignar():
 *   cargaActualKg <= capacidadCargaKg
 * Esta precondicion no existe en el contrato de Vehiculo.
 *
 * Los tests de la Suite Heredada pasaran si el Camion se crea con
 * cargaActualKg = 0 (estado inicial normal). Pero la precondicion
 * adicional constituye una VIOLACION de LSP (Propiedad 2.3): un
 * cliente que usa un Vehiculo no espera que asignar() pueda fallar
 * por una condicion de carga que no conoce.
 */
@DisplayName("Test Camion — Suite Heredada de Vehiculo")
public class CamionTest extends VehiculoTest {

    @Override
    protected Vehiculo crearVehiculo() {
        // Capacidad de carga de 5000 kg, carga inicial 0
        return new Camion("CAM001", 0.0, 5000.0);
    }

    @Override
    protected String nombreSubclase() {
        return "Camion";
    }

    // ================================================================
    // Tests especificos de Camion (no estan en la Suite Heredada)
    // ================================================================

    @org.junit.jupiter.api.Nested
    @DisplayName("Tests especificos de Camion")
    class TestsEspecificosCamion {

        @org.junit.jupiter.api.Test
        @DisplayName("TE-C1: Camion permite cargar dentro de la capacidad")
        void camion_cargar_valido() {
            Camion c = new Camion("CAM002", 0.0, 5000.0);
            c.cargar(2000.0);
            assertEquals(2000.0, c.getCargaActualKg(), 0.001);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-C2: Camion lanza SobrecargaException al superar capacidad")
        void camion_sobrecarga() {
            Camion c = new Camion("CAM003", 0.0, 5000.0);
            c.cargar(4000.0);
            assertThrows(SobrecargaException.class, () ->
                c.cargar(1500.0));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-C3: Camion no puede asignar si esta sobrecargado (LSP violation demo)")
        void camion_no_asigna_sobrecargado() {
            // Usamos un CamionForTest que permite forzar el estado
            // sobrecargado para demostrar la violacion de LSP:
            // asignar() puede lanzar SobrecargaException, una excepcion
            // que NO existe en el contrato de Vehiculo.
            CamionForTest c = new CamionForTest("CAM004", 0.0, 5000.0);
            c.forzarCarga(6000.0); // cargaActualKg = 6000 > capacidadCargaKg = 5000
            assertThrows(SobrecargaException.class, () ->
                c.asignar("LEG-300", 100.0),
                "LSP violation: asignar() lanza SobrecargaException, excepcion no declarada en Vehiculo");
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-C4: Camion puede asignar y liberar normalmente sin sobrecarga")
        void camion_asignar_liberar_normal() {
            Camion c = new Camion("CAM005", 1000.0, 5000.0);
            double kmIniciales = c.getKm();
            c.asignar("LEG-301", 200.0);
            assertEquals(EstadoVehiculo.EN_USO, c.getEstado());
            c.liberar(150.0);
            assertEquals(EstadoVehiculo.DISPONIBLE, c.getEstado());
            assertEquals(kmIniciales + 150.0, c.getKm(), 0.001);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("TE-C5: Camion puede descargar para quedar sin carga")
        void camion_descargar() {
            Camion c = new Camion("CAM006", 0.0, 5000.0);
            c.cargar(3000.0);
            assertEquals(3000.0, c.getCargaActualKg(), 0.001);
            c.descargar();
            assertEquals(0.0, c.getCargaActualKg(), 0.001);
        }
    }

    // =================================================================
    // Doble de prueba: permite forzar cargaActualKg para demostrar
    // la violacion de LSP en Camion (precondicion adicional)
    // =================================================================
    static class CamionForTest extends Camion {

        public CamionForTest(String patente, double kmActuales, double capacidadCargaKg) {
            super(patente, kmActuales, capacidadCargaKg);
        }

        void forzarCarga(double kg) {
            // Usamos un hack: cargar a capacidad y luego restar capacidad
            // para quedar por encima del limite. Como no se puede via API
            // publica, forzamos el atributo via reflection.
            try {
                java.lang.reflect.Field field = Camion.class.getDeclaredField("cargaActualKg");
                field.setAccessible(true);
                field.setDouble(this, kg);
            } catch (Exception e) {
                throw new RuntimeException("No se pudo forzar cargaActualKg", e);
            }
        }
    }
}
