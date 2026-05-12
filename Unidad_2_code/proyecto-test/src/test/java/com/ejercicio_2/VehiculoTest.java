package com.ejercicio_2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.ejercicio_2.Vehiculo;

//suite heredad 

public abstract class VehiculoTest {

    protected abstract Vehiculo crearVehiculo(double kmIniciales); // nuevo metodo que luego podran usar,

    // @test caso de prueba individual
    // AAA
    @Test
    void testAsignarCE1CE4() {
        Vehiculo v = crearVehiculo(0.0); // disponible y 0.0, para instancia un aso inicial.
        String confirmacion = v.asignar("legajo01", 400.0); // ACAAAAAA es valido en vehiculo, pero invalido para moto porque moto la precondicion es mas fuerte, entonces ahi se rompe el LSP.
        //assertEquals(EstadoVehiculo.EN_USO, v.getEstado()); debido a que camion tiene un limite de carga.
        assertAll(
            () -> assertEquals(EstadoVehiculo.EN_USO, v.getEstado(), "El estado no cambio"),
            () -> assertNotNull(confirmacion, "La confirmacion es nula")
        );
    }

    // tc2 va a ser lo mismo, solo que el caso <0, asi que no lo hice
    @Test
    void testAsignarCE2CE4() {

        Vehiculo v = crearVehiculo(0.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> v.asignar("Leg-001", 0.0));
    }

    @Test
    void testAsignarCE3CE4() {// ejemplo pag 10/29
        // TC3: km > 500
        Vehiculo v = crearVehiculo(0.0);
        assertThrows(IllegalArgumentException.class, () -> v.asignar("Leg-001", 501.0));
    }

    @Test
    void testAsignarCE1CE5() {
        // Intentar asignar cuando ya esta EN_USO
        Vehiculo v = crearVehiculo(0.0);
        v.asignar("Leg-001", 100.0); // Primero lo ponemos en EN_USO
        // ahora intentamos asignar de nuevo (Estado no disponible)
        assertThrows(IllegalStateException.class, () -> v.asignar("Leg-002", 50.0));
    }

    // liberar metodo
    @Test
    void testLiberarCE6CE8() {
        // TC5: Estado correcto y km positivos
        Vehiculo v = crearVehiculo(100.0);
        v.asignar("Leg-001", 50.0); // dejarlo en EN_USO
        v.liberar(150.0); // CE8
        assertEquals(EstadoVehiculo.DISPONIBLE, v.getEstado());
    }

    @Test
    void testLiberarCE7CE8() {
        // TC6: Intentar liberar un auto que ya está DISPONIBLE
        Vehiculo v = crearVehiculo(0.0); // Inicia en DISPONIBLE
        assertThrows(IllegalStateException.class, () -> v.liberar(100.0));
    }

    @Test
    void testLiberarCE6CE9() {

        Vehiculo v = crearVehiculo(100.0);

        v.asignar("Leg-001", 50.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> v.liberar(-10.0));
    }
}