import pytest 
from flota.gestor_flota import GestorFlota

from unittest.mock import MagicMock # unidad 2 herramietnas
#vehiculo para pruebas

vehiculo_mock=MagicMock()
vehiculo_mock.get_patente.return_value="ABC123" 


# el test B falal porque el test A mdofiico el esado global de

def test_A_agrega_vehiculo():
    #deja test singleton sucio
    gestor=GestorFlota.get_instancia()
    gestor.registrar(vehiculo_mock)
    #vehiculo con una entrada
    assert len(gestor._vehiculos)==1

def test_B_gestor_vacio():
    gestor= GestorFlota.get_instancia() # fallara porque el test A ya agrego un vehículo, entonces el gestor no esta vacio ()
    #arrange: El test asume que el gestor empieza vacio
    # ACT y ASSERT: Al intentar registrar, lanzara ValueError porque la patente 
    # ya existe del test anterior, aunque este test no la agregó.
    gestor.registrar(vehiculo_mock)
