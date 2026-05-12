package com.enunciado_3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * =====================================================================
 * ITEM 13 — Analisis LSP de Moto y Camion (Propiedad 2.3 del apunte)
 * =====================================================================
 *
 * Propiedad 2.3 (Verificacion LSP):
 *   Una subclase D cumple LSP respecto de su superclase C si y solo si:
 *
 *     (1) Pre_D ⟹ Pre_C    (la precondicion de D es DEBIL o igual que la de C)
 *     (2) Post_C ⟹ Post_D  (la postcondicion de D es FUERTE o igual que la de C)
 *
 * Equivalentemente en terminos de conjuntos:
 *     (1) Pre_C ⊆ Pre_D   (todo input valido para C tambien es valido para D)
 *     (2) Post_D ⊆ Post_C (todo output de D satisface el output de C)
 * =====================================================================
 */
@DisplayName("ITEM 13 — Analisis formal LSP (Propiedad 2.3)")
public class LspAnalysisTest {

    // =================================================================
    // CONTRATO DE LA SUPERCLASE VEHICULO (referencia)
    // =================================================================

    /**
     * Metodo asignar(legajo, kmEstimados):
     *   Pre_C:  kmEstimados > 0  &&  kmEstimados <= 500  &&  estado == DISPONIBLE
     *   Post_C: estado == EN_USO  &&  retorno != null
     *
     * Metodo liberar(kmRecorridos):
     *   Pre_C:  estado == EN_USO  &&  kmRecorridos >= 0
     *   Post_C: estado == DISPONIBLE  &&  kmActuales += kmRecorridos
     */

    // =================================================================
    // ANALISIS DE Moto
    // =================================================================

    @Nested
    @DisplayName("Moto — analisis de LSP")
    class AnalisisMoto {

        /**
         * Pre_Moto:  kmEstimados > 0  &&  kmEstimados <= 300  &&  estado == DISPONIBLE
         * Post_Moto: estado == EN_USO  &&  retorno != null
         *
         * Comparacion de Precondiciones:
         *   Pre_C = { (legajo, km) | km ∈ (0, 500] }
         *   Pre_D = { (legajo, km) | km ∈ (0, 300] }
         *
         *   Pre_D ⊂ Pre_C  (es un SUBCONJUNTO estricto)
         *   Esto significa que Pre_Moto es MAS FUERTE que Pre_Vehiculo.
         *
         *   Para cumplir LSP se necesita: Pre_C ⊆ Pre_D
         *   Pero aqui: (0, 500] NO está contenido en (0, 300]
         *   Contraejemplo: km = 400 → Pre_C es verdadera, Pre_Moto es falsa.
         *
         * CONCLUSION: Moto VIOLA LSP porque fortalece la precondicion.
         */
        @Test
        @DisplayName("Moto viola LSP: km=400 es valido en Vehiculo pero rechazado en Moto")
        void moto_viola_lsp_precondicion_mas_fuerte() {
            // ARRANGE — Vehiculo acepta km=400 (dentro de (0,500])
            Vehiculo auto = new Auto("ABC", 0, 4);
            auto.asignar("LEG", 400.0);
            assertEquals(EstadoVehiculo.EN_USO, auto.getEstado());
            // Auto acepta km=400 sin problema → Pre_Vehiculo se cumple

            // ACT — Moto rechaza km=400 (fuera de (0,300])
            Vehiculo moto = new Moto("MOTO", 0, false);
            // km=400 esta dentro de (0,500] pero NO dentro de (0,300]

            // ASSERT — Pre_Moto rechaza lo que Pre_Vehiculo acepta
            assertThrows(IllegalArgumentException.class, () ->
                moto.asignar("LEG", 400.0),
                "Moto rechaza km=400, pero Vehiculo lo acepta: VIOLACION de LSP");
        }

        @Test
        @DisplayName("Moto: Post_Moto es equivalente a Post_Vehiculo (cumple)")
        void moto_postcondicion_equivalente() {
            // Post_Moto: estado == EN_USO && retorno != null
            // Post_C:    estado == EN_USO && retorno != null
            // Son IDENTICAS → Post_C == Post_Moto → Post_C ⟹ Post_Moto ✓
            Moto m = new Moto("MOTO2", 0, false);
            String res = m.asignar("LEG", 200.0); // dentro de (0,300]
            assertNotNull(res);
            assertEquals(EstadoVehiculo.EN_USO, m.getEstado());
            // La postcondicion se cumple correctamente
        }

        /**
         * Resumen Moto:
         *   Pre_D ⟹ Pre_C ?  SI (si km ∈ (0,300] entonces km ∈ (0,500])
         *   Pre_C ⟹ Pre_D ?  NO (km=400 ∈ (0,500] pero no ∈ (0,300])
         *   → Moto VIOLA LSP porque la direccion de la implicacion es la INVERSA.
         *   La regla LSP exige: Pre_C ⊆ Pre_D (la subclase acepta TODO lo que acepta la superclase).
         *   Aqui: Pre_D ⊂ Pre_C (la subclase acepta MENOS).
         */
    }

    // =================================================================
    // ANALISIS DE Camion
    // =================================================================

    @Nested
    @DisplayName("Camion — analisis de LSP")
    class AnalisisCamion {

        /**
         * Pre_Camion: kmEstimados > 0 && kmEstimados <= 500
         *             && estado == DISPONIBLE
         *             && cargaActualKg <= capacidadCargaKg   // ← PRECONDICION ADICIONAL
         *
         * Post_Camion: estado == EN_USO && retorno != null
         *              (ademas: el camion queda asignado con su carga actual)
         *
         * Comparacion de Precondiciones:
         *   Pre_C = { (legajo, km) | km ∈ (0,500] ∧ estado == DISPONIBLE }
         *   Pre_D = { (legajo, km, carga) | km ∈ (0,500] ∧ estado == DISPONIBLE
         *                                   ∧ cargaActualKg <= capacidadCargaKg }
         *
         *   Pre_D tiene una condicion EXTRA que no existe en Pre_C.
         *   Un cliente que usa un Vehiculo no conoce ni puede garantizar
         *   la condicion de carga. Por lo tanto, Pre_C NO implica Pre_D.
         *
         * CONCLUSION: Camion VIOLA LSP porque agrega una precondicion
         * que no forma parte del contrato de Vehiculo.
         */
        @Test
        @DisplayName("Camion viola LSP: asignar() lanza SobrecargaException no declarada en Vehiculo")
        void camion_viola_lsp_precondicion_adicional() {
            // Demostramos que Camion.asignar() puede lanzar una excepcion
            // que NO esta en el contrato de Vehiculo:
            //   - Vehiculo solo declara: IllegalStateException, IllegalArgumentException
            //   - Camion agrega: SobrecargaException (por cargaActualKg > capacidadCargaKg)

            CamionTest.CamionForTest c = new CamionTest.CamionForTest("CAM", 0.0, 5000.0);
            c.forzarCarga(6000.0); // forzamos estado ilegal: carga > capacidad

            // Un cliente que usa Vehiculo no espera SobrecargaException
            assertThrows(SobrecargaException.class, () ->
                c.asignar("LEG", 100.0),
                "SobrecargaException no existe en el contrato de Vehiculo: VIOLACION de LSP");
        }

        @Test
        @DisplayName("Camion: Post_Camion es equivalente a Post_Vehiculo (cumple)")
        void camion_postcondicion_equivalente() {
            // Post_Camion: estado == EN_USO && retorno != null
            // Post_C:      estado == EN_USO && retorno != null
            // Son IDENTICAS → Post_C == Post_Camion → Post_C ⟹ Post_Camion ✓
            Camion c = new Camion("CAM2", 1000.0, 5000.0);
            String res = c.asignar("LEG", 200.0);
            assertNotNull(res);
            assertEquals(EstadoVehiculo.EN_USO, c.getEstado());
            c.liberar(150.0);
            assertEquals(EstadoVehiculo.DISPONIBLE, c.getEstado());
        }

        /**
         * Resumen Camion:
         *   La precondicion adicional (cargaActualKg <= capacidadCargaKg)
         *   no puede ser garantizada por un cliente que programa contra
         *   la interfaz Vehiculo.
         *
         *   Pre_C ⊆ Pre_D ?  NO — Pre_D exige algo que Pre_C no menciona.
         *   → Camion VIOLA LSP.
         *
         *   Nota: los tests heredados PASAN sobre Camion porque en el
         *   estado inicial cargaActualKg = 0 <= capacidadCargaKg.
         *   La violacion se manifiesta solo cuando el camion esta sobrecargado.
         */
    }

    // =================================================================
    // ANALISIS DE Auto (caso correcto)
    // =================================================================

    @Nested
    @DisplayName("Auto — caso correcto de LSP")
    class AnalisisAuto {

        /**
         * Pre_Auto:  kmEstimados > 0  &&  kmEstimados <= 500  &&  estado == DISPONIBLE
         * Post_Auto: estado == EN_USO  &&  retorno != null
         *
         * Comparacion con Vehiculo:
         *   Pre_Auto  == Pre_Vehiculo   (exactamente la misma)
         *   Post_Auto == Post_Vehiculo  (exactamente la misma)
         *
         *   Pre_C ⊆ Pre_D  ✓  (son iguales)
         *   Post_D ⊆ Post_C  ✓  (son iguales)
         *
         * CONCLUSION: Auto CUMPLE LSP perfectamente.
         */
        @Test
        @DisplayName("Auto cumple LSP: acepta km=500 como Vehiculo")
        void auto_cumple_lsp_acesta_limite_superior() {
            Auto a = new Auto("ABC", 0, 4);
            String res = a.asignar("LEG", 500.0);
            assertNotNull(res);
            assertEquals(EstadoVehiculo.EN_USO, a.getEstado());
            // km=500 es valido tanto en Vehiculo como en Auto
        }

        @Test
        @DisplayName("Auto cumple LSP: no lanza excepciones no declaradas")
        void auto_no_lanza_excepciones_extra() {
            Auto a = new Auto("ABC", 0, 4);
            a.asignar("LEG", 100.0);
            // Las unicas excepciones posibles son las del contrato de Vehiculo:
            // - IllegalStateException (si estado != DISPONIBLE)
            // - IllegalArgumentException (si km fuera de rango)
            // No hay excepciones adicionales como SobrecargaException
        }
    }
}
