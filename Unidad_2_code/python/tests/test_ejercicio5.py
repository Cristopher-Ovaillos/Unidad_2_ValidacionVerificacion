import pytest
from flota.gestor_flota import GestorFlota 
#prueba: estar en python  y python -m pytest .\tests\test_ejercicio5.py

def test_unicidad_sing():

    #arrange y el act por el get
    instancia1 = GestorFlota.get_instancia()
    instancia2 = GestorFlota.get_instancia()
    #assert verificamos la identidiad, es decir que ambas variables apuntan al mismo objeto
    assert instancia1 is instancia2