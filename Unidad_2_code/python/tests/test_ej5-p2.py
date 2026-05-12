
import pytest
from flota.gestor_flota import GestorFlota


@pytest.fixture(scope="function")
def gestor_reset():
    # fixtura que garantiza el aislamiento entre tests, este scope asegura que se ejecute para cada test indivudal.

    #arrange | setup: obtenemos la instancia del singleton
    gestor = GestorFlota.get_instancia()
    #uso de yield (ayuda de gemini: yield permite devolver un valor desde un fixture y luego ejecutar código de limpieza después del test)
    yield gestor 
    #teardown: desmontar el estado del singleton para evitar contaminación entre tests
    GestorFlota._reset_para_tests()

#ejecutar con python -m pytest .\tests\test_ej5-p2.py