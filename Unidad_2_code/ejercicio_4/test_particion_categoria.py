"""
Tests de Partición por Categoría (Algoritmo 2.5)
Implementa el catálogo reducido derivado de las consignas 16 y 17.

Siguiendo el Algoritmo 2.1 (Diseño de Caso de Prueba Unitario OO):
- Paso 2 y 4: Arrange - Establecer estado inicial consistente
- Paso 6: Act - Invocar el método bajo prueba
- Paso 6 y 7: Assert - Verificar postcondición e invariante
"""

import pytest
from modelo import Auto, EstadoVehiculo


# Fixture para el estado consistente (Definición 2.5)
@pytest.fixture
def auto_consistente():
    """
    Crea un Auto en estado consistente según el invariante.
    
    Estado inicial:
        - Patente válida (len >= 6)
        - Kilometraje válido (>= 0)
        - Puertas válidas (4 está en {2, 4, 5})
        - Estado DISPONIBLE con conductor None (cumple S5)
    """
    return Auto(patente="ABC-123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)


class TestParticionCategoria:
    """
    Implementa el catálogo reducido del Algoritmo 2.5.
    
    De las 18 combinaciones del producto cartesiano completo,
    se seleccionan 6 casos representativos según las restricciones
    identificadas en la Consigna 17.
    """
    
    def test_asignar_exito_P1_P4_P7(self, auto_consistente):
        """
        Caso de éxito: legajo válido × km válidos × estado disponible.
        
        Categorías:
            - legajo_conductor: P1 (length >= 4)
            - km_estimados: P4 (0 < km <= 500)
            - estado: P7 (DISPONIBLE)
        
        Resultado esperado: Transición a EN_USO con conductor asignado.
        """
        # Arrange (Paso 2 y 4 Alg. 2.1)
        legajo, kms = "A123", 200
        
        # Act (Paso 6 Alg. 2.1)
        retorno = auto_consistente.asignar(legajo, kms)
        
        # Assert (Paso 6 y 7 Alg. 2.1)
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.get_conductor() == legajo
        assert str(kms) in retorno  # Verificar que la confirmación contiene los km
        assert auto_consistente.verificar_invariante() is True
    
    def test_asignar_error_estado_no_disponible_P1_P4_P8(self):
        """
        Error de estado: vehículo no disponible.
        
        Categorías:
            - legajo_conductor: P1 (válido)
            - km_estimados: P4 (válido)
            - estado: P8 (MANTENIMIENTO != DISPONIBLE)
        
        Resultado esperado: PermissionError sin cambios de estado.
        """
        # Arrange: Estado inválido para el método
        auto = Auto("ABC-123", 100, 4, EstadoVehiculo.MANTENIMIENTO)
        estado_original = auto.get_estado()
        
        # Act & Assert
        with pytest.raises(PermissionError):
            auto.asignar("A123", 100)
        
        # Verificar que el estado no cambió (postcondición negativa)
        assert auto.get_estado() == estado_original
        assert auto.verificar_invariante() is True
    
    def test_asignar_error_legajo_vacio_P2_P4_P7(self, auto_consistente):
        """
        Error de legajo: cadena vacía.
        
        Categorías:
            - legajo_conductor: P2 (cadena vacía)
            - km_estimados: P4 (válido)
            - estado: P7 (DISPONIBLE)
        
        Resultado esperado: ValueError sin cambios de estado.
        """
        # Arrange
        estado_original = auto_consistente.get_estado()
        
        # Act & Assert
        with pytest.raises(ValueError, match="legajo.*4 caracteres"):
            auto_consistente.asignar("", 100)
        
        # Verificar que el estado no cambió
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True
    
    def test_asignar_error_legajo_corto_P3_P4_P7(self, auto_consistente):
        """
        Error de legajo: longitud < 4.
        
        Categorías:
            - legajo_conductor: P3 (length < 4)
            - km_estimados: P4 (válido)
            - estado: P7 (DISPONIBLE)
        
        Resultado esperado: ValueError sin cambios de estado.
        """
        # Arrange
        estado_original = auto_consistente.get_estado()
        
        # Act & Assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("ABC", 100)  # Solo 3 caracteres
        
        # Verificar que el estado no cambió
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True
    
    def test_asignar_error_km_no_positivo_P1_P5_P7(self, auto_consistente):
        """
        Error de kilómetros: km <= 0.
        
        Categorías:
            - legajo_conductor: P1 (válido)
            - km_estimados: P5 (km <= 0)
            - estado: P7 (DISPONIBLE)
        
        Resultado esperado: ValueError sin cambios de estado.
        """
        # Arrange
        estado_original = auto_consistente.get_estado()
        
        # Act & Assert
        with pytest.raises(ValueError, match="kilómetros.*rango"):
            auto_consistente.asignar("A123", 0)
        
        # Verificar que el estado no cambió
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True
    
    def test_asignar_error_km_excede_maximo_P1_P6_P7(self, auto_consistente):
        """
        Error de kilómetros: km > 500.
        
        Categorías:
            - legajo_conductor: P1 (válido)
            - km_estimados: P6 (km > 500)
            - estado: P7 (DISPONIBLE)
        
        Resultado esperado: ValueError sin cambios de estado.
        """
        # Arrange
        estado_original = auto_consistente.get_estado()
        
        # Act & Assert
        with pytest.raises(ValueError):
            auto_consistente.asignar("A123", 501)
        
        # Verificar que el estado no cambió
        assert auto_consistente.get_estado() == estado_original
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.verificar_invariante() is True
