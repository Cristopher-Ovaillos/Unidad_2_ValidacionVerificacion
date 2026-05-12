# tests de particion por categoria (alg 2.5)
# catalogo reducido de las consignas 16 y 17
# sigue el alg 2.1: arrange -> act -> assert

import pytest
from modelo import Auto, EstadoVehiculo


# fixture para estado consistente (def 2.5)
@pytest.fixture
def auto_consistente():
    # auto en estado valido segun invariante
    # patente valida (len >= 6), km valido (>= 0), puertas 4, estado DISPONIBLE
    return Auto(patente="ABC-123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)


class TestParticionCategoria:
    # catalogo reducido: de 18 combinaciones posibles -> 6 casos

    def test_asignar_exito_P1_P4_P7(self, auto_consistente):
        # caso exitoso: legajo valido x km valido x estado disponible
        # categorias: P1 (len>=4), P4 (0<km<=500), P7 (DISPONIBLE)
        # resultado esperado: EN_USO con conductor asignado

        # arrange
        legajo, kms = "A123", 200

        # act
        retorno = auto_consistente.asignar(legajo, kms)

        # assert
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.get_conductor() == legajo
        assert str(kms) in retorno  # confirmacion contiene los km
        assert auto_consistente.verificar_invariante() is True

    def test_asignar_error_estado_no_disponible_P1_P4_P8(self):
        # error por estado no disponible
        # categorias: P1 (valido), P4 (valido), P8 (MANTENIMIENTO)
        # resultado esperado: PermissionError sin cambios

        # arrange: estado invalido
        auto = Auto("ABC-123", 100, 4, EstadoVehiculo.MANTENIMIENTO)
        estado_original = auto.get_estado()

        # act & assert
        with pytest.raises(PermissionError):
            auto.asignar("A123", 100)

        # verificar que el estado no cambio
        assert auto.get_estado() == estado_original
        assert auto.verificar_invariante() is True

    def test_asignar_error_legajo_vacio_P2_P4_P7(self, auto_consistente):
        # error por legajo vacio
        # categorias: P2 (vacio), P4 (valido), P7 (DISPONIBLE)
        # resultado esperado: ValueError sin cambios

        # arrange
        estado_original = auto_consistente.get_estado()

        # act & assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("", 100)

        # verificar que nada cambio
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True

    def test_asignar_error_legajo_corto_P3_P4_P7(self, auto_consistente):
        # error por legajo muy corto
        # categorias: P3 (len<4), P4 (valido), P7 (DISPONIBLE)
        # resultado esperado: ValueError sin cambios

        # arrange
        estado_original = auto_consistente.get_estado()

        # act & assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("ABC", 100)  # solo 3 caracteres

        # verificar que nada cambio
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True

    def test_asignar_error_km_no_positivo_P1_P5_P7(self, auto_consistente):
        # error por km <= 0
        # categorias: P1 (valido), P5 (km<=0), P7 (DISPONIBLE)
        # resultado esperado: ValueError sin cambios

        # arrange
        estado_original = auto_consistente.get_estado()

        # act & assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("A123", 0)

        # verificar que nada cambio
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True

    def test_asignar_error_km_excede_maximo_P1_P6_P7(self, auto_consistente):
        # error por km > 500
        # categorias: P1 (valido), P6 (km>500), P7 (DISPONIBLE)
        # resultado esperado: ValueError sin cambios

        # arrange
        estado_original = auto_consistente.get_estado()

        # act & assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("A123", 501)

        # verificar que nada cambio
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True
