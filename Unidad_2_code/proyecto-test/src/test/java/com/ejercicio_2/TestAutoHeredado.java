package com.ejercicio_2;

//supondre que lo de auto sera igual al ejercicio 1
public class TestAutoHeredado extends VehiculoTest {
    @Override
    
    protected Vehiculo crearVehiculo(double km){
        return new Auto("AAA-111", "Fiat", 2023, km, 5);
    }
    
}
