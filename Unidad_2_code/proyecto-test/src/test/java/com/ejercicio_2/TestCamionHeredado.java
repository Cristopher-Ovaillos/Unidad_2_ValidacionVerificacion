package com.ejercicio_2;

public class TestCamionHeredado extends VehiculoTest {
    @Override
    protected Vehiculo crearVehiculo(double km) {
        Camion c = new Camion("CAM-333", km, 1000.0); // capacidad 1000

        c.cargar(1500.0); // Lo sobrecargamos a proposito

        return c;
    }
}