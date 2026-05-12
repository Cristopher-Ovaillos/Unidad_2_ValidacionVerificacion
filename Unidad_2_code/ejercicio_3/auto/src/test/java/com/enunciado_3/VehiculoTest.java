package com.enunciado_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * =====================================================================
 * ITEM 11 — Suite Heredada (Definición 2.11 del apunte)
 * =====================================================================
 * Clase abstracta de test que contiene un caso de prueba por cada
 * combinacion metodo x clase de equivalencia del contrato de la
 * superclase Vehiculo.
 *
 * CONTRATO DE VEHICULO (del enunciado):
 *
 * Metodo asignar(legajo, kmEstimados):
 *   Pre:  kmEstimados > 0 && kmEstimados <= 500 && estado == DISPONIBLE
 *   Post: estado == EN_USO && retorna confirmacion no nula
 *
 * Metodo liberar(kmRecorridos):
 *   Pre:  estado == EN_USO && kmRecorridos >= 0
 *   Post: estado == DISPONIBLE && kmActuales += kmRecorridos
 *
 * Invariante: estado en {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
 * =====================================================================
 *
 * Subclases concretas (AutoTest, MotoTest, CamionTest) deben extender
 * esta clase e implementar crearVehiculo().
 *
 * Al aplicar el Algoritmo 2.4 (Verificacion LSP), cada subclase
 * heredara TODOS estos tests automaticamente.
 */
@DisplayName("Suite Heredada — Contrato de Vehiculo")
public abstract class VehiculoTest {

    // Factory method: cada subclase concreta provee su implementacion
    protected abstract Vehiculo crearVehiculo();

    // Nombre de la subclase para mensajes de error
    protected abstract String nombreSubclase();

    // Tolerancia para comparacion de doubles
    protected static final double DELTA = 0.001;

    // =================================================================
    // SECCION A: Tests del metodo asignar()
    // =================================================================

    @Nested
    @DisplayName("asignar() — precondicion valida")
    class AsignarPreValida {

        @Test
        @DisplayName("TC-A1: asignar con km en (0,500] cambia estado a EN_USO y retorna no null")
        void asignar_valido_cambia_estado() {
            // ARRANGE
            Vehiculo v = crearVehiculo();
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado(),
                "Pre: el vehiculo debe iniciar en DISPONIBLE");

            // ACT
            String resultado = v.asignar("LEG-001", 100.0);

            // ASSERT — Postcondicion del contrato
            assertNotNull(resultado,
                "Post: el retorno no debe ser null");
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado(),
                "Post: estado debe ser EN_USO");

            // Invariante
            assertTrue(esEstadoValido(v.getEstado()),
                "Invariante: estado debe ser valido");
        }

        @Test
        @DisplayName("TC-A2: asignar con km = 1 (limite inferior) es valido")
        void asignar_limite_inferior() {
            Vehiculo v = crearVehiculo();
            String resultado = v.asignar("LEG-002", 1.0);
            assertNotNull(resultado);
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado());
        }

        @Test
        @DisplayName("TC-A3: asignar con km = 500 (limite superior) es valido")
        void asignar_limite_superior() {
            Vehiculo v = crearVehiculo();
            String resultado = v.asignar("LEG-003", 500.0);
            assertNotNull(resultado);
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado());
        }
    }

    @Nested
    @DisplayName("asignar() — precondicion invalida (km fuera de rango)")
    class AsignarKmInvalido {

        @Test
        @DisplayName("TC-A4: asignar con km = 0 viola precondicion -> IllegalArgumentException")
        void asignar_km_cero_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            EstadoVehiculo estadoInicial = v.getEstado();

            assertThrows(IllegalArgumentException.class, () ->
                v.asignar("LEG-004", 0.0),
                "Pre: km = 0 debe lanzar IllegalArgumentException");

            // Verificar que el estado no cambio (invariante tras excepcion)
            assertEquals(estadoInicial, v.getEstado(),
                "Invariante tras excepcion: estado no debe cambiar");
        }

        @Test
        @DisplayName("TC-A5: asignar con km = -5 viola precondicion -> IllegalArgumentException")
        void asignar_km_negativo_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            EstadoVehiculo estadoInicial = v.getEstado();

            assertThrows(IllegalArgumentException.class, () ->
                v.asignar("LEG-005", -5.0),
                "Pre: km < 0 debe lanzar IllegalArgumentException");

            assertEquals(estadoInicial, v.getEstado(),
                "Invariante tras excepcion: estado no debe cambiar");
        }

        @Test
        @DisplayName("TC-A6: asignar con km = 501 viola precondicion -> IllegalArgumentException")
        void asignar_km_mayor_500_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            EstadoVehiculo estadoInicial = v.getEstado();

            assertThrows(IllegalArgumentException.class, () ->
                v.asignar("LEG-006", 501.0),
                "Pre: km > 500 debe lanzar IllegalArgumentException");

            assertEquals(estadoInicial, v.getEstado(),
                "Invariante tras excepcion: estado no debe cambiar");
        }
    }

    @Nested
    @DisplayName("asignar() — precondicion invalida (estado incorrecto)")
    class AsignarEstadoInvalido {

        @Test
        @DisplayName("TC-A7: asignar cuando estado = EN_USO -> IllegalStateException")
        void asignar_cuando_en_uso_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            v.asignar("LEG-007", 100.0);
            // Ahora estado = EN_USO

            assertThrows(IllegalStateException.class, () ->
                v.asignar("LEG-008", 50.0),
                "Pre: estado != DISPONIBLE debe lanzar IllegalStateException");
        }
    }

    // =================================================================
    // SECCION B: Tests del metodo liberar()
    // =================================================================

    @Nested
    @DisplayName("liberar() — precondicion valida")
    class LiberarPreValida {

        @Test
        @DisplayName("TC-L1: liberar con km >= 0 cambia estado a DISPONIBLE y acumula km")
        void liberar_valido_cambia_estado_y_acumula_km() {
            // ARRANGE
            Vehiculo v = crearVehiculo();
            double kmIniciales = v.getKm();
            v.asignar("LEG-010", 100.0);
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado());

            // ACT
            double kmRecorridos = 75.0;
            v.liberar(kmRecorridos);

            // ASSERT — Postcondicion del contrato
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado(),
                "Post: estado debe ser DISPONIBLE");
            assertEquals(kmIniciales + kmRecorridos, v.getKm(), DELTA,
                "Post: kmActuales += kmRecorridos");

            // Invariante
            assertTrue(esEstadoValido(v.getEstado()),
                "Invariante: estado debe ser valido");
        }

        @Test
        @DisplayName("TC-L2: liberar con kmRecorridos = 0 (limite inferior) es valido")
        void liberar_km_cero_es_valido() {
            Vehiculo v = crearVehiculo();
            double kmIniciales = v.getKm();
            v.asignar("LEG-011", 100.0);

            v.liberar(0.0);

            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
            assertEquals(kmIniciales, v.getKm(), DELTA,
                "Post: km no cambia cuando kmRecorridos = 0");
        }
    }

    @Nested
    @DisplayName("liberar() — precondicion invalida (estado incorrecto)")
    class LiberarEstadoInvalido {

        @Test
        @DisplayName("TC-L3: liberar cuando estado = DISPONIBLE -> IllegalStateException")
        void liberar_cuando_disponible_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            double kmIniciales = v.getKm();
            // Estado inicial = DISPONIBLE

            assertThrows(IllegalStateException.class, () ->
                v.liberar(50.0),
                "Pre: estado != EN_USO debe lanzar IllegalStateException");

            assertEquals(kmIniciales, v.getKm(), DELTA,
                "Invariante tras excepcion: km no deben cambiar");
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado(),
                "Invariante tras excepcion: estado no debe cambiar");
        }
    }

    // =================================================================
    // METODO AUXILIAR: verificacion del invariante de estado
    // =================================================================

    /**
     * Verifica el invariante de clase sobre el atributo estado:
       estado in {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
     */
    protected boolean esEstadoValido(EstadoVehiculo estado) {
        return estado == EstadoVehiculo.DISPONIBLE
            || estado == EstadoVehiculo.EN_USO
            || estado == EstadoVehiculo.MANTENIMIENTO
            || estado == EstadoVehiculo.BAJA;
    }
}
