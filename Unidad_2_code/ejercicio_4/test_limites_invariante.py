# tests de limites de invariante (alg 2.6) y parametros (alg 1.7)
# analiza fronteras del estado interno del objeto y de los parametros de entrada

import pytest
from modelo import Auto, EstadoVehiculo


@pytest.fixture
def auto_consistente():
    # auto en estado valido segun invariante
    return Auto(patente="ABC-123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)


class TestLimitesInvariante:

    # === S1: limite de patente (len >= 6) ===

    def test_invariante_patente_limite_inconsistente_L_menos_epsilon(self):
        # S1: L - epsilon = 5 -> violacion del invariante
        # patente con len=5 viola S1, invariante debe ser False
        # no llamar a asignar() porque el objeto es inconsistente

        # arrange: patente con 5 caracteres
        auto = Auto(patente="ABC12", km=0, puertas=2, estado=EstadoVehiculo.DISPONIBLE)

        # assert: invariante violada
        assert auto.verificar_invariante() is False

    def test_invariante_patente_limite_L(self, auto_consistente):
        # S1: L = 6 -> en el limite, valido
        # patente con exactamente 6 caracteres, invariante debe ser True

        # arrange: patente de 6 caracteres
        auto = Auto(patente="ABC123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True

        # act
        retorno = auto.asignar("A123", 200)

        # assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.get_conductor() == "A123"
        assert auto.verificar_invariante() is True

    def test_invariante_patente_limite_L_mas_epsilon(self, auto_consistente):
        # S1: L + epsilon = 7 -> sobre el limite, valido
        # la fixture ya tiene ABC-123 (len=7)

        # arrange
        assert len(auto_consistente.get_patente()) == 7
        assert auto_consistente.verificar_invariante() is True

        # act
        retorno = auto_consistente.asignar("A123", 200)

        # assert
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.verificar_invariante() is True

    # === S2: limite de kilometraje (_km >= 0) ===

    def test_invariante_km_limite_inconsistente_L_menos_epsilon(self):
        # S2: L - epsilon = -1 -> violacion del invariante
        # km negativo viola S2, invariante debe ser False

        # arrange: km negativo
        auto = Auto(patente="ABC-123", km=-1, puertas=4, estado=EstadoVehiculo.DISPONIBLE)

        # assert: invariante violada
        assert auto.verificar_invariante() is False

    def test_invariante_km_limite_L(self):
        # S2: L = 0 -> en el limite, valido (auto nuevo)
        # km = 0 es valido, invariante debe ser True

        # arrange: auto nuevo con km = 0
        auto = Auto(patente="ABC-123", km=0, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True

        # act
        retorno = auto.asignar("A123", 100)

        # assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.verificar_invariante() is True

    def test_invariante_km_limite_L_mas_epsilon(self):
        # S2: L + epsilon = 1 -> sobre el limite, valido
        # km = 1 es valido

        # arrange: auto con km = 1
        auto = Auto(patente="ABC-123", km=1, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True

        # act
        retorno = auto.asignar("A123", 100)

        # assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.verificar_invariante() is True

    # === S4: consistencia conductor/estado (cualitativo) ===

    def test_invariante_consistencia_estado_conductor_punto_consistente(self, auto_consistente):
        # S4: punto consistente -> conductor=None y estado=DISPONIBLE
        # invariante debe cumplirse, metodo funciona normalmente

        # arrange: fixture ya esta en estado consistente
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
        assert auto_consistente.verificar_invariante() is True

        # act
        retorno = auto_consistente.asignar("A123", 100)

        # assert: despues de asignar, S4 debe seguir cumpliendose
        assert auto_consistente.get_conductor() == "A123"
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.verificar_invariante() is True

    def test_invariante_consistencia_estado_conductor_punto_inconsistente(self):
        # S4: punto inconsistente -> conductor="A123" y estado=DISPONIBLE
        # esto viola S4, ya que si estado != EN_USO el conductor debe ser None
        # invariante debe ser False

        # arrange: forzar estado inconsistente
        auto = Auto(patente="ABC-123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        auto._conductor = "A123"  # asignacion directa (solo para test)

        # assert: invariante violada
        assert auto.verificar_invariante() is False

    # === limites de parametros de entrada (alg 1.7) ===

    @pytest.mark.parametrize("kms, esperado", [
        (0, ValueError),   # L_inf: excluido por precondicion
        (1, "exito"),      # L_inf + eps: minimo valido
        (500, "exito"),    # L_sup: maximo valido
        (501, ValueError)  # L_sup + eps: excede el maximo
    ])
    def test_limites_km_estimados_parametro(self, auto_consistente, kms, esperado):
        # analisis de valores limite sobre km_estimados
        # dominio: (0, 500]
        # complementa el analisis de invariante (alg 2.6) con el de parametros (alg 1.7)

        if esperado == ValueError:
            with pytest.raises(ValueError):
                auto_consistente.asignar("A123", kms)
            assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
            assert auto_consistente.get_conductor() is None
        else:
            retorno = auto_consistente.asignar("A123", kms)
            assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
            assert auto_consistente.get_conductor() == "A123"

        # en todos los casos el invariante debe mantenerse
        assert auto_consistente.verificar_invariante() is True

    @pytest.mark.parametrize("legajo, esperado", [
        ("ABC", ValueError),   # len=3 < 4: invalido
        ("A123", "exito"),     # len=4: minimo valido
        ("A1234", "exito"),    # len=5: valido
    ])
    def test_limites_legajo_conductor_parametro(self, auto_consistente, legajo, esperado):
        # analisis de valores limite sobre legajo_conductor
        # dominio: len >= 4

        if esperado == ValueError:
            with pytest.raises(ValueError):
                auto_consistente.asignar(legajo, 100)
            assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
            assert auto_consistente.get_conductor() is None
        else:
            retorno = auto_consistente.asignar(legajo, 100)
            assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
            assert auto_consistente.get_conductor() == legajo

        assert auto_consistente.verificar_invariante() is True
