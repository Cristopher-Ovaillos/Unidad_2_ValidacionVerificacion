"""
test_ejercicio1.py

coman
    cd unidad_2/ejercicio_1
    python -m pytest tests/test_ejercicio1.py -v --tb=short --cov=flota --cov-branch --cov-report=term-missing
"""

import pytest
import sys
import os
#esto lo saque de ia porq tenia un problema de imporantacion, que al final se solucionaba facil pero lo dejo asi
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from flota import Auto, EstadoVehiculo


@pytest.fixture(scope="function")
def auto_valido():
    # Fixture con scope function: cada test recibe instancia nueva e independiente.
    # Los metodos de Auto son mutables; scope=function garantiza que
    # cada caso parte de un estado conocido y reproducible (Algoritmo 2.1, Paso 2).
    return Auto(patente="ABC123", marca="Toyota",
                anio=2020, km_actuales=200.0, num_puertas=4)


class TestEjercicio1:

    # 3.a) asignar con km=1.0 (limite inferior valido)
    def test_3a_asignar_km_limite_inferior(self, auto_valido):
        legajo = "L-001"
        km = 1.0
        resultado = auto_valido.asignar(legajo, km)
        assert auto_valido.get_estado() == EstadoVehiculo.EN_USO
        assert auto_valido._conductor == legajo
        assert auto_valido.get_km() >= 0  # invariante

    # 3.b) asignar con km=500.0 (limite superior) aca esta el  BUG
    def test_3b_asignar_km_limite_superior_500(self, auto_valido):
        legajo = "L-002"
        resultado = auto_valido.asignar(legajo, 500.0)
        assert auto_valido.get_estado() == EstadoVehiculo.EN_USO
        assert auto_valido._conductor == legajo

    # 3.c) asignar con km=0.0 (borde invalido)
    def test_3c_asignar_km_cero(self, auto_valido):
        estado_inicial = auto_valido.get_estado()
        km_inicial = auto_valido.get_km()
        with pytest.raises(ValueError):
            auto_valido.asignar("L-003", 0.0)
        assert auto_valido.get_estado() == estado_inicial
        assert auto_valido.get_km() == km_inicial

    # 3.d) asignar cuando estado=EN_USO
    def test_3d_asignar_desde_en_uso(self, auto_valido):
        auto_valido.asignar("L-004", 100.0)
        estado_inicial = auto_valido.get_estado()
        assert estado_inicial == EstadoVehiculo.EN_USO
        with pytest.raises(PermissionError):
            auto_valido.asignar("L-005", 50.0)
        assert auto_valido.get_estado() == estado_inicial

    # 3.e) liberar con km=150.0
    def test_3e_liberar_km_150(self, auto_valido):
        auto_valido.asignar("L-006", 200.0)
        km_inicial = auto_valido.get_km()
        auto_valido.liberar(150.0)
        assert auto_valido.get_estado() == EstadoVehiculo.DISPONIBLE
        assert auto_valido._conductor is None
        assert auto_valido.get_km() == km_inicial + 150.0

    # 3.f) liberar cuando estado=DISPONIBLE
    def test_3f_liberar_desde_disponible(self, auto_valido):
        estado_inicial = auto_valido.get_estado()
        km_inicial = auto_valido.get_km()
        with pytest.raises(PermissionError):
            auto_valido.liberar(100.0)
        assert auto_valido.get_estado() == estado_inicial
        assert auto_valido.get_km() == km_inicial

    # 3.g) enviar_mantenimiento desde DISPONIBLE
    def test_3g_enviar_mantenimiento(self, auto_valido):
        auto_valido.enviar_mantenimiento()
        assert auto_valido.get_estado() == EstadoVehiculo.MANTENIMIENTO

    # 3.h) dar_de_baja desde MANTENIMIENTO
    def test_3h_dar_de_baja(self, auto_valido):
        auto_valido.enviar_mantenimiento()
        auto_valido.dar_de_baja()
        assert auto_valido.get_estado() == EstadoVehiculo.BAJA
