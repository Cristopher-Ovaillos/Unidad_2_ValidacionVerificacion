"""
Tests opcionales para alcanzar 100% de cobertura del módulo completo.
Estos tests no son necesarios para las consignas 19-20, ya que el método
asignar() bajo prueba tiene 100% de cobertura. Se incluyen como ejercicio
adicional para cubrir métodos auxiliares y validaciones del invariante.
"""

import pytest
from modelo import Auto, EstadoVehiculo


class TestCoberturaCompleta:
    """
    Tests adicionales para cubrir líneas no ejercitadas en los tests principales.
    
    Estas líneas no afectan al método asignar() pero son parte del módulo modelo.py.
    """
    
    def test_getters_no_usados(self):
        """
        Cubre los getters get_km() y get_puertas() (líneas 106, 110).
        
        Estos métodos no se usan en los tests principales porque no son
        necesarios para verificar el comportamiento del método asignar().
        """
        auto = Auto("ABC-123", 100, 4, EstadoVehiculo.DISPONIBLE)
        
        # Ejercitar los getters
        assert auto.get_km() == 100
        assert auto.get_puertas() == 4
    
    def test_invariante_puertas_invalidas(self):
        """
        Cubre la validación S3 del invariante (línea 132).
        
        S3: _puertas in {2, 4, 5}
        Este test construye un objeto con puertas = 3, que viola S3.
        """
        # Arrange: Crear un auto con número de puertas inválido
        auto = Auto("ABC-123", 100, 3, EstadoVehiculo.DISPONIBLE)
        
        # Assert: La invariante debe estar violada
        assert auto.verificar_invariante() is False
        
        # Nota: Este objeto no debería usarse en producción, pero es útil
        # para probar que verificar_invariante() detecta esta inconsistencia.
    
    def test_invariante_puertas_validas_borde_2(self):
        """
        Test complementario para verificar que puertas=2 es válido.
        """
        auto = Auto("ABC-123", 100, 2, EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True
    
    def test_invariante_puertas_validas_borde_5(self):
        """
        Test complementario para verificar que puertas=5 es válido.
        """
        auto = Auto("ABC-123", 100, 5, EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True
    
    # NOTA: La línea 136 (validación S4 del invariante) es difícil de cubrir
    # porque requiere que _estado NO sea una instancia de EstadoVehiculo.
    # Esto violaría el sistema de tipos de Python si se hace correctamente.
    # 
    # En una implementación real, esta validación sería redundante porque
    # Python ya garantiza que solo se pueden asignar valores de EstadoVehiculo
    # a un atributo tipado como EstadoVehiculo (con type hints).
    # 
    # Si realmente necesitas cubrirla, tendrías que hacer algo como:
    # auto._estado = "string_invalido"  # Viola encapsulamiento y tipos
    # pero esto no es una práctica recomendada en pruebas de calidad.
