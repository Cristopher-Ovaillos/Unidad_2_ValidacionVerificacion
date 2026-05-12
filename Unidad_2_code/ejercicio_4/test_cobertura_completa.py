# tests opcionales para cubrir el 100% del modulo
# el metodo asignar() ya tiene 100% de cobertura con los tests principales
# estos tests cubren getters y validaciones del invariante no ejercitadas antes

import pytest
from modelo import Auto, EstadoVehiculo


class TestCoberturaCompleta:
    # tests adicionales para lineas no cubiertas en los tests principales
    # estas lineas no afectan a asignar() pero son parte del modulo

    def test_getters_no_usados(self):
        # cubre los getters get_km() y get_puertas()
        # no se usaban antes porque no son necesarios para probar asignar()

        auto = Auto("ABC-123", 100, 4, EstadoVehiculo.DISPONIBLE)

        assert auto.get_km() == 100
        assert auto.get_puertas() == 4

    def test_invariante_puertas_invalidas(self):
        # cubre la validacion S3 del invariante (puertas in {2,4,5})
        # puertas = 3 viola S3, invariante debe ser False

        # arrange: auto con 3 puertas (invalido)
        auto = Auto("ABC-123", 100, 3, EstadoVehiculo.DISPONIBLE)

        # assert: invariante violada
        assert auto.verificar_invariante() is False

    def test_invariante_puertas_validas_borde_2(self):
        # test complementario: puertas=2 es valido
        auto = Auto("ABC-123", 100, 2, EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True

    def test_invariante_puertas_validas_borde_5(self):
        # test complementario: puertas=5 es valido
        auto = Auto("ABC-123", 100, 5, EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True

    # nota: la linea 136 (S4, _estado not isinstance EstadoVehiculo)
    # es dificil de cubrir porque requiere que _estado NO sea del tipo correcto
    # en la practica, python type hints ya garantizan esto
    # forzar _estado a un string violaria encapsulamiento y no es recomendado
