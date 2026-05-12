#version corregida

import pytest
from flota.gestor_flota import GestorFlota
from unittest.mock import MagicMock # unidad 2 herramietnas
#vehiculo para pruebas

vehiculo_mock=MagicMock()
vehiculo_mock.get_patente.return_value="ABC123" 

@pytest.fixture(scope='function')
def gestor_limpio():
    #setup: obtenemos la instancia del singleton
    yield GestorFlota.get_instancia() #yield como sabemos es una forma de devolver un valor desde una fixture, y luego ejecutar codigo de limpieza despues del test
    # tearDown significa desmontar el estado del singleton para evitar contaminación entre tests osea que el estado del singleton no afecte a otros tests
    GestorFlota._reset_para_tests()

def test_1_limpio(gestor_limpio):
    gestor_limpio.registrar(vehiculo_mock)
    assert len(gestor_limpio._vehiculos) == 1

def test_2_limpio(gestor_limpio):
    #gracia al reset del singleton, este test empieza con un gestor limpio, sin vehiculos registrados
    gestor_limpio.registrar(vehiculo_mock)
    assert len(gestor_limpio._vehiculos) == 1