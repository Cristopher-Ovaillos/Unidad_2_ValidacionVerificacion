package com.enunciado_3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * =====================================================================
 * ITEMS 14 y 15 — Documentacion formal y verificacion de Propiedad 2.3
 * =====================================================================
 *
 * Item 14: Documentar formalmente la extension (Pre_D, Post_D, invariante_D)
 *          de la subclase que extiende el contrato de forma valida.
 *
 * Item 15: Verificar Pre_D ⟹ Pre_C y Post_C ⟹ Post_D para cada subclase.
 *          Si alguna viola la regla, indicar la correccion de diseño.
 * =====================================================================
 */
@DisplayName("Items 14-15 — Documentacion formal y correcciones")
public class FormalContractTest {

    // =================================================================
    // ITEM 14 — Documentacion formal del contrato de cada subclase
    // =================================================================

    @Nested
    @DisplayName("Item 14 — Documentacion formal de contratos")
    class Item14_DocumentacionFormal {

        // -----------------------------------------------------------------
        // AUTO: Cumple el contrato sin modificacion (caso ideal de LSP)
        // -----------------------------------------------------------------

        /**
         * CONTRATO DE AUTO (cumple LSP):
         *
         * Pre_Auto:  kmEstimados ∈ (0, 500]  ∧  estado == DISPONIBLE
         *            ∧ legajo != null  ∧  legajo no vacio
         *
         * Post_Auto: estado == EN_USO  ∧  retorno != null
         *            ∧ conductor == legajo   (extension documentada)
         *
         * invariante_Auto:
         *   estado ∈ {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
         *   ∧ kmActuales >= 0
         *   ∧ numPuertas ∈ {2, 4, 5}
         *   ∧ (conductor == null) == (estado != EN_USO)
         *
         * Comparacion con Vehiculo:
         *   Pre_Auto  == Pre_Vehiculo     (identica)
         *   Post_Auto => Post_Vehiculo    (mas fuerte: agrega conductor == legajo)
         *   invariante_Auto => invariante_Vehiculo  (mas fuerte: agrega restricciones)
         *
         * Auto CUMPLE LSP:
         *   - No debilita la postcondicion
         *   - No fortalece la precondicion
         *   - Fortalece el invariante (permitido por LSP)
         */
        @Test
        @DisplayName("Auto: Post_Auto implica Post_Vehiculo (cumple)")
        void auto_postcondicion_implica_superclase() {
            Auto a = new Auto("ABC123", 100.0, 4);
            a.asignar("LEG-TEST", 250.0);
            // Post_Vehiculo: estado == EN_USO && retorno != null
            assertEquals(EstadoVehiculo.EN_USO, a.getEstado());
            // Post_Auto (mas fuerte): ademas conductor == legajo
            assertEquals("LEG-TEST", a.getConductor());
        }

        @Test
        @DisplayName("Auto: invariante_Auto implica invariante_Vehiculo")
        void auto_invariante_implica_superclase() {
            Auto a = new Auto("ABC123", 100.0, 4);
            a.asignar("LEG", 50.0);
            a.liberar(50.0);
            // Invariante Vehiculo: estado valido
            assertTrue(esEstadoValido(a.getEstado()));
            // Invariante Auto (mas fuerte): conductor null cuando no EN_USO
            assertNull(a.getConductor());
            // Invariante Auto: numPuertas en {2,4,5}
            assertTrue(a.getNumPuertas() == 2 || a.getNumPuertas() == 4
                       || a.getNumPuertas() == 5);
        }

        // -----------------------------------------------------------------
        // MOTO: VIOLA LSP — fortalece la precondicion
        // -----------------------------------------------------------------

        /**
         * CONTRATO DE MOTO (VIOLA LSP):
         *
         * Pre_Moto:  kmEstimados ∈ (0, 300]  ∧  estado == DISPONIBLE
         *
         * Post_Moto: estado == EN_USO  ∧  retorno != null
         *
         * invariante_Moto:
         *   estado ∈ {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
         *   ∧ kmActuales >= 0
         *
         * Problema:
         *   Pre_Moto = { km ∈ (0, 300] }  ⊂  Pre_Vehiculo = { km ∈ (0, 500] }
         *   Pre_Moto es MAS FUERTE → VIOLA LSP
         *
         * CORRECCION DE DISEÑO propuesta (Item 15):
         *   Opcion A: Cambiar el contrato de Vehiculo para km ∈ (0, 300]
         *             (todos los vehiculos comparten el mismo limite).
         *   Opcion B: Moto debe aceptar km hasta 500 pero internamente
         *             limitar la duracion del viaje (no los km).
         *   Opcion C: Moto no debe ser subclase de Vehiculo; ambas deben
         *             implementar una interfaz comun con contratos distintos.
         */
        @Test
        @DisplayName("Moto: documentacion de violacion LSP en precondicion")
        void moto_documentar_violacion_lsp() {
            // Demostramos que Pre_Vehiculo NO implica Pre_Moto:
            // km = 400 es valido para Vehiculo pero NO para Moto.
            Vehiculo auto = new Auto("X", 0, 4);
            assertDoesNotThrow(() -> auto.asignar("L", 400.0));
            // Pre_Vehiculo acepta km=400

            Vehiculo moto = new Moto("Y", 0, false);
            assertThrows(IllegalArgumentException.class,
                () -> moto.asignar("L", 400.0));
            // Pre_Moto rechaza km=400 → VIOLACION
        }

        // -----------------------------------------------------------------
        // CAMION: VIOLA LSP — agrega precondicion no declarada
        // -----------------------------------------------------------------

        /**
         * CONTRATO DE CAMION (VIOLA LSP):
         *
         * Pre_Camion: kmEstimados ∈ (0, 500]  ∧  estado == DISPONIBLE
         *             ∧ cargaActualKg <= capacidadCargaKg   // EXTRA
         *
         * Post_Camion: estado == EN_USO  ∧  retorno != null
         *
         * invariante_Camion:
         *   estado ∈ {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
         *   ∧ kmActuales >= 0
         *   ∧ cargaActualKg >= 0
         *   ∧ cargaActualKg <= capacidadCargaKg   (cuando estado != EN_USO)
         *
         * Problema:
         *   Pre_Camion tiene una condicion que Pre_Vehiculo no tiene.
         *   Un cliente que programa contra Vehiculo no puede garantizar
         *   la condicion de carga.
         *
         * CORRECCION DE DISEÑO propuesta (Item 15):
         *   Opcion A: Mover la verificacion de carga al metodo cargar(),
         *             y permitir asignar() siempre que estado == DISPONIBLE.
         *             La carga es una preocupacion ortogonal a la asignacion.
         *   Opcion B: Documentar que Vehiculo puede tener subclases con
         *             restricciones adicionales (debilitar el contrato base).
         *   Opcion C: Crear una interfaz VehiculoAsignable con un metodo
         *             puedeAsignarse() que cada subclase implemente.
         */
        @Test
        @DisplayName("Camion: documentacion de violacion LSP en precondicion adicional")
        void camion_documentar_violacion_lsp() {
            // Demostramos que Camion.asignar() puede fallar por una
            // condicion que no existe en el contrato de Vehiculo:
            CamionTest.CamionForTest c = new CamionTest.CamionForTest("Z", 0, 5000.0);
            c.forzarCarga(6000.0);

            // Si programaramos contra Vehiculo, no esperariamos esto:
            assertThrows(SobrecargaException.class,
                () -> c.asignar("L", 100.0),
                "Excepcion no declarada en contrato de Vehiculo");
        }
    }

    // =================================================================
    // ITEM 15 — Verificacion formal de Propiedad 2.3
    // =================================================================

    @Nested
    @DisplayName("Item 15 — Verificacion formal Propiedad 2.3")
    class Item15_VerificacionFormal {

        /**
         * Propiedad 2.3 (LSP):
         *   Para que D sea subclase valida de C se debe cumplir:
         *     (1) Pre_D ⟹ Pre_C    (Pre de D es debil o igual que Pre de C)
         *     (2) Post_C ⟹ Post_D  (Post de D es fuerte o igual que Post de C)
         *
         * En terminos de conjuntos de inputs aceptables:
         *     (1) Pre_C ⊆ Pre_D
         *     (2) Post_D ⊆ Post_C
         */

        // -----------------------------------------------------------------
        // AUTO: Verificacion
        // -----------------------------------------------------------------

        /**
         * TABLA DE VERIFICACION — AUTO
         *
         * | Propiedad        | Expresion                          | Resultado |
         * |-----------------|-------------------------------------|----------|
         * | Pre_Auto        | km ∈ (0,500] ∧ estado = DISP       |          |
         * | Pre_Vehiculo    | km ∈ (0,500] ∧ estado = DISP       |          |
         * | (1) Pre_A ⟹ Pre_V | (0,500] ⊆ (0,500]               | SI ✓     |
         * | Post_Vehiculo   | estado = EN_USO ∧ retorno != null  |          |
         * | Post_Auto       | estado = EN_USO ∧ retorno != null  |          |
         * |                 | ∧ conductor = legajo               |          |
         * | (2) Post_V ⟹ Post_A| Post_V ∧ conductor = legajo     | SI ✓     |
         * |                 |   (Post_A es mas fuerte que Post_V)|          |
         * | CONCLUSION      | Auto CUMPLE LSP                   | ✓        |
         */
        @Test
        @DisplayName("Auto: Pre_Auto ⟹ Pre_Vehiculo (SI — identicas)")
        void auto_precondicion_implica() {
            // Pre_Auto = Pre_Vehiculo = {km ∈ (0,500] ∧ estado = DISP}
            // (0,500] ⊆ (0,500] → TRUE (conjuntos iguales)
            // Cada llamada usa un Auto nuevo para evitar conflicto de estado
            assertDoesNotThrow(() -> new Auto("A1", 0, 4).asignar("L", 1.0));
            assertDoesNotThrow(() -> new Auto("A2", 0, 4).asignar("L", 250.0));
            assertDoesNotThrow(() -> new Auto("A3", 0, 4).asignar("L", 500.0));
        }

        @Test
        @DisplayName("Auto: Post_Vehiculo ⟹ Post_Auto (SI — Post_Auto mas fuerte)")
        void auto_postcondicion_implicada() {
            // Post_Vehiculo: estado = EN_USO ∧ retorno != null
            // Post_Auto:     estado = EN_USO ∧ retorno != null ∧ conductor = legajo
            // Post_Auto satisface Post_Vehiculo Y agrega conductor = legajo
            // Por tanto Post_Vehiculo ⟹ Post_Auto ✓
            Auto a = new Auto("A", 0, 4);
            String res = a.asignar("LEG-X", 100.0);
            assertNotNull(res);       // parte de Post_Vehiculo
            assertEquals(EstadoVehiculo.EN_USO, a.getEstado()); // parte de Post_Vehiculo
            assertEquals("LEG-X", a.getConductor()); // parte extra de Post_Auto
        }

        // -----------------------------------------------------------------
        // MOTO: Verificacion
        // -----------------------------------------------------------------

        /**
         * TABLA DE VERIFICACION — MOTO
         *
         * | Propiedad        | Expresion                          | Resultado |
         * |-----------------|-------------------------------------|----------|
         * | Pre_Moto        | km ∈ (0,300] ∧ estado = DISP       |          |
         * | Pre_Vehiculo    | km ∈ (0,500] ∧ estado = DISP       |          |
         * | (1) Pre_M ⟹ Pre_V | (0,300] ⊆ (0,500]               | SI ✓     |
         * |                 | PERO la regla LSP exige Pre_V ⊆ Pre_M|         |
         * | Pre_V ⊆ Pre_M?  | (0,500] ⊆ (0,300]?               | NO ✗     |
         * | Contraejemplo:  | km=400 ∈ (0,500] pero ∉ (0,300]   |          |
         * | (2) Post_V ⟹ Post_M| mismas postcondiciones          | SI ✓     |
         * | CONCLUSION      | Moto VIOLA LSP (Pre_M mas fuerte)  | ✗        |
         */
        @Test
        @DisplayName("Moto: Pre_Vehiculo ⊆ Pre_Moto? NO — contraejemplo km=400")
        void moto_precondicion_no_implica() {
            // Contraejemplo: km = 400
            // Pre_Vehiculo(400) = TRUE  (400 ∈ (0,500])
            // Pre_Moto(400)     = FALSE (400 ∉ (0,300])
            // Pre_Vehiculo ⊄ Pre_Moto → VIOLACION
            Moto m = new Moto("M", 0, false);
            assertThrows(IllegalArgumentException.class,
                () -> m.asignar("L", 400.0),
                "km=400 es valido para Vehiculo pero NO para Moto");
        }

        // -----------------------------------------------------------------
        // CAMION: Verificacion
        // -----------------------------------------------------------------

        /**
         * TABLA DE VERIFICACION — CAMION
         *
         * | Propiedad        | Expresion                                | Resultado |
         * |-----------------|-------------------------------------------|----------|
         * | Pre_Camion      | km ∈ (0,500] ∧ estado = DISP              |          |
         * |                 | ∧ cargaActualKg <= capacidadCargaKg       |          |
         * | Pre_Vehiculo    | km ∈ (0,500] ∧ estado = DISP              |          |
         * | (1) Pre_C ⟹ Pre_V | km ∈ (0,500] ∧ estado = DISP            | SI ✓     |
         * |                 | (la parte comun implica la de Vehiculo)   |          |
         * | PERO Pre_V ⊆ Pre_C?                                       |          |
         * | Pre_C tiene condicion EXTRA (carga) que Pre_V no tiene     |          |
         * | Contraejemplo:  | km=100, estado=DISP, carga=6000>5000     |          |
         * |                 | Pre_V = TRUE, Pre_C = FALSE               |          |
         * | (2) Post_V ⟹ Post_C| mismas postcondiciones                | SI ✓     |
         * | CONCLUSION      | Camion VIOLA LSP (precondicion adicional) | ✗        |
         */
        @Test
        @DisplayName("Camion: Pre_Vehiculo ⊆ Pre_Camion? NO — condicion de carga extra")
        void camion_precondicion_no_implica() {
            // Contraejemplo: Camion sobrecargado
            CamionTest.CamionForTest c = new CamionTest.CamionForTest("C", 0, 5000.0);
            c.forzarCarga(6000.0);
            // Pre_Vehiculo se cumple: km=100 ∈ (0,500], estado=DISP
            // Pre_Camion NO se cumple: cargaActualKg=6000 > capacidadCargaKg=5000
            // Pre_Vehiculo ⊄ Pre_Camion → VIOLACION
            assertThrows(SobrecargaException.class,
                () -> c.asignar("L", 100.0));
        }

        // -----------------------------------------------------------------
        // RESUMEN FINAL
        // -----------------------------------------------------------------

        /**
         * RESUMEN DE VERIFICACION LSP (Propiedad 2.3):
         *
         * | Subclase | Pre_D ⟹ Pre_C | Pre_C ⊆ Pre_D | Post_C ⟹ Post_D | LSP  |
         * |----------|--------------|---------------|-----------------|------|
         * | Auto     | SI           | SI (iguales)  | SI              | CUMPLE|
         * | Moto     | SI           | NO            | SI              | VIOLA |
         * | Camion   | SI           | NO (extra)    | SI              | VIOLA |
         *
         * CORRECCIONES PROPUESTAS:
         *
         * Moto:
         *   - Opcion A: Unificar el limite de km a 300 en TODA la jerarquia
         *     (cambiar Pre_Vehiculo a km ∈ (0,300]).
         *   - Opcion B: Moto no debe ser subclase de Vehiculo. Ambas deben
         *     heredar de una clase base con un contrato mas generico.
         *
         * Camion:
         *   - Opcion A: Eliminar la verificacion de carga de asignar().
         *     La carga es responsabilidad de cargar(), no de asignar().
         *     asignar() solo debe verificar estado == DISPONIBLE.
         *   - Opcion B: Agregar un metodo puedeAsignarse() a Vehiculo que
         *     cada subclase implemente segun sus propias restricciones.
         *   - Opcion C: Documentar que asignar() en Vehiculo puede fallar
         *     por condiciones especificas de la subclase (debilitar contrato).
         */
        @Test
        @DisplayName("Resumen final: solo Auto cumple LSP")
        void resumen_final_lsp() {
            // Auto: CUMPLE LSP
            Auto a = new Auto("A", 0, 4);
            a.asignar("L", 500.0);  // limite superior de Vehiculo
            assertEquals(EstadoVehiculo.EN_USO, a.getEstado());

            // Moto: VIOLA LSP (rechaza km=500)
            Moto m = new Moto("M", 0, false);
            assertThrows(IllegalArgumentException.class,
                () -> m.asignar("L", 500.0));

            // Camion: VIOLA LSP (puede lanzar SobrecargaException)
            // Los tests heredados pasan porque carga inicial = 0,
            // pero la violacion existe en el diseño del contrato.
        }
    }

    // Auxiliar
    private boolean esEstadoValido(EstadoVehiculo estado) {
        return estado == EstadoVehiculo.DISPONIBLE
            || estado == EstadoVehiculo.EN_USO
            || estado == EstadoVehiculo.MANTENIMIENTO
            || estado == EstadoVehiculo.BAJA;
    }
}
