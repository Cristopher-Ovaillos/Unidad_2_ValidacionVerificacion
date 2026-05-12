package com.ejercicio_2;

public class TestMotoHeredadi extends VehiculoTest{
    @Override
    protected Vehiculo crearVehiculo(double km) {
        // Sustitución: Se inyecta una Moto
        return new Moto("MOT-222", km);
    }
}
