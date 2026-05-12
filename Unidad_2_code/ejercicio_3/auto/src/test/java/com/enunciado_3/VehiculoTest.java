package com.enunciado_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * consigna 11: suite heredada (definicion 2.11 unidad ii)
 * 
 * clase abstracta de test que implementa el algoritmo 2.4 (verificacion lsp)
 * contiene un caso de prueba por cada combinacion metodo x clase de equivalencia
 * 
 * contrato de vehiculo:
 *   asignar(legajo, kmEstimados):
 *     pre:  kmEstimados > 0 && kmEstimados <= 500 && estado == DISPONIBLE
 *     post: estado == EN_USO && retorno != null
 * 
 *   liberar(kmRecorridos):
 *     pre:  estado == EN_USO && kmRecorridos >= 0
 *     post: estado == DISPONIBLE && kmActuales += kmRecorridos
 * 
 *   invariante: estado in {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
 * 
 * subclases (autotest, mototest, camiontest) heredan estos tests automaticamente
 */
@DisplayName("Suite Heredada — Contrato de Vehiculo")
public abstract class VehiculoTest {

    // factory method: cada subclase implementa este metodo
    protected abstract Vehiculo crearVehiculo();

    protected abstract String nombreSubclase();

    protected static final double DELTA = 0.001;

    @Nested
    @DisplayName("asignar() — precondicion valida")
    class AsignarPreValida {

        @Test
        @DisplayName("TC-A1: asignar con km en (0,500] cambia estado a EN_USO y retorna no null")
        void asignar_valido_cambia_estado() {
            // arrange
            Vehiculo v = crearVehiculo();
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());

            // act
            String resultado = v.asignar("LEG-001", 100.0);

            // assert: postcondicion
            assertNotNull(resultado);
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado());
            assertTrue(esEstadoValido(v.getEstado()));
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
                v.asignar("LEG-004", 0.0));

            // invariante: estado no cambia tras excepcion
            assertEquals(estadoInicial, v.getEstado());
        }

        @Test
        @DisplayName("TC-A5: asignar con km = -5 viola precondicion -> IllegalArgumentException")
        void asignar_km_negativo_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            EstadoVehiculo estadoInicial = v.getEstado();

            assertThrows(IllegalArgumentException.class, () ->
                v.asignar("LEG-005", -5.0));

            assertEquals(estadoInicial, v.getEstado());
        }

        @Test
        @DisplayName("TC-A6: asignar con km = 501 viola precondicion -> IllegalArgumentException")
        void asignar_km_mayor_500_lanza_excepcion() {
            Vehiculo v = crearVehiculo();
            EstadoVehiculo estadoInicial = v.getEstado();

            assertThrows(IllegalArgumentException.class, () ->
                v.asignar("LEG-006", 501.0));

            assertEquals(estadoInicial, v.getEstado());
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

            assertThrows(IllegalStateException.class, () ->
                v.asignar("LEG-008", 50.0));
        }
    }

    @Nested
    @DisplayName("liberar() — precondicion valida")
    class LiberarPreValida {

        @Test
        @DisplayName("TC-L1: liberar con km >= 0 cambia estado a DISPONIBLE y acumula km")
        void liberar_valido_cambia_estado_y_acumula_km() {
            // arrange
            Vehiculo v = crearVehiculo();
            double kmIniciales = v.getKm();
            v.asignar("LEG-010", 100.0);
            assertEquals(EstadoVehiculo.EN_USO, v.getEstado());

            // act
            double kmRecorridos = 75.0;
            v.liberar(kmRecorridos);

            // assert: postcondicion
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
            assertEquals(kmIniciales + kmRecorridos, v.getKm(), DELTA);
            assertTrue(esEstadoValido(v.getEstado()));
        }

        @Test
        @DisplayName("TC-L2: liberar con kmRecorridos = 0 (limite inferior) es valido")
        void liberar_km_cero_es_valido() {
            Vehiculo v = crearVehiculo();
            double kmIniciales = v.getKm();
            v.asignar("LEG-011", 100.0);

            v.liberar(0.0);

            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
            assertEquals(kmIniciales, v.getKm(), DELTA);
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

            assertThrows(IllegalStateException.class, () ->
                v.liberar(50.0));

            assertEquals(kmIniciales, v.getKm(), DELTA);
            assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
        }
    }

    // verifica invariante: estado in {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
    protected boolean esEstadoValido(EstadoVehiculo estado) {
        return estado == EstadoVehiculo.DISPONIBLE
            || estado == EstadoVehiculo.EN_USO
            || estado == EstadoVehiculo.MANTENIMIENTO
            || estado == EstadoVehiculo.BAJA;
    }
}
