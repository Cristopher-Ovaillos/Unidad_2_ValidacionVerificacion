"""
Tests de Límites de Invariante (Algoritmo 2.6) y Parámetros (Algoritmo 1.7)
Implementa el análisis de valores límite sobre el invariante de clase y los parámetros de entrada.

El Algoritmo 2.6 analiza las fronteras del estado interno del objeto,
mientras que el Algoritmo 1.7 analiza las fronteras de los parámetros de entrada.
Ambos buscan defectos off-by-one en dimensiones complementarias.
"""

import pytest
from modelo import Auto, EstadoVehiculo


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


class TestLimitesInvariante:
    """
    Implementa los límites del Invariante (Alg. 2.6) y Parámetros (Alg. 1.7).
    
    Analiza las fronteras críticas identificadas en la Consigna 18.
    """
    
    # =========================================================================
    # S1: Límite de Invariante - Patente (len >= 6)
    # =========================================================================
    
    def test_invariante_patente_limite_inconsistente_L_menos_epsilon(self):
        """
        S1: L - epsilon = 5 (Violación del invariante).
        
        Límite: len(_patente) >= 6
        Punto: L - ε = 5
        
        Resultado esperado: Objeto inconsistente, invariante falso.
        Según Def. 2.2, el test debe detectar la violación en Arrange.
        """
        # Arrange: Patente con longitud 5 (viola S1)
        auto = Auto(patente="ABC12", km=0, puertas=2, estado=EstadoVehiculo.DISPONIBLE)
        
        # Assert: La invariante debe estar violada
        assert auto.verificar_invariante() is False
        # No se debe llamar a asignar() porque el objeto está en estado inconsistente
    
    def test_invariante_patente_limite_L(self, auto_consistente):
        """
        S1: L = 6 (En el límite, válido).
        
        Límite: len(_patente) >= 6
        Punto: L = 6
        
        Resultado esperado: Invariante cumplida, método tiene éxito.
        """
        # Arrange: La fixture ya tiene patente "ABC-123" (len = 7)
        # Crear un auto con patente de exactamente 6 caracteres
        auto = Auto(patente="ABC123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True
        
        # Act
        retorno = auto.asignar("A123", 200)
        
        # Assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.get_conductor() == "A123"
        assert auto.verificar_invariante() is True
    
    def test_invariante_patente_limite_L_mas_epsilon(self, auto_consistente):
        """
        S1: L + epsilon = 7 (Sobre el límite, válido).
        
        Límite: len(_patente) >= 6
        Punto: L + ε = 7
        
        Resultado esperado: Invariante cumplida, método tiene éxito.
        """
        # Arrange: auto_consistente ya tiene patente "ABC-123" (len = 7)
        assert len(auto_consistente.get_patente()) == 7
        assert auto_consistente.verificar_invariante() is True
        
        # Act
        retorno = auto_consistente.asignar("A123", 200)
        
        # Assert
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.verificar_invariante() is True
    
    # =========================================================================
    # S2: Límite de Invariante - Kilometraje (_km >= 0)
    # =========================================================================
    
    def test_invariante_km_limite_inconsistente_L_menos_epsilon(self):
        """
        S2: L - epsilon = -1 (Violación del invariante).
        
        Límite: _km >= 0
        Punto: L - ε = -1
        
        Resultado esperado: Objeto inconsistente, invariante falso.
        """
        # Arrange: Kilometraje negativo (viola S2)
        auto = Auto(patente="ABC-123", km=-1, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        
        # Assert: La invariante debe estar violada
        assert auto.verificar_invariante() is False
    
    def test_invariante_km_limite_L(self):
        """
        S2: L = 0 (En el límite, válido).
        
        Límite: _km >= 0
        Punto: L = 0
        
        Resultado esperado: Invariante cumplida (auto nuevo). Método tiene éxito.
        """
        # Arrange: Auto nuevo con km = 0
        auto = Auto(patente="ABC-123", km=0, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True
        
        # Act
        retorno = auto.asignar("A123", 100)
        
        # Assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.verificar_invariante() is True
    
    def test_invariante_km_limite_L_mas_epsilon(self):
        """
        S2: L + epsilon = 1 (Sobre el límite, válido).
        
        Límite: _km >= 0
        Punto: L + ε = 1
        
        Resultado esperado: Invariante cumplida. Método tiene éxito.
        """
        # Arrange: Auto con km = 1
        auto = Auto(patente="ABC-123", km=1, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        assert auto.verificar_invariante() is True
        
        # Act
        retorno = auto.asignar("A123", 100)
        
        # Assert
        assert auto.get_estado() == EstadoVehiculo.EN_USO
        assert auto.verificar_invariante() is True
    
    # =========================================================================
    # S4: Límite de Invariante - Consistencia conductor/estado (cualitativo)
    # =========================================================================
    
    def test_invariante_consistencia_estado_conductor_punto_consistente(self, auto_consistente):
        """
        S4: Punto consistente - conductor=None y estado=DISPONIBLE.
        
        Límite cualitativo: (_conductor is None) == (_estado != EN_USO)
        Punto: _conductor = None AND _estado = DISPONIBLE
        
        Resultado esperado: Invariante cumplida. Método transiciona correctamente.
        """
        # Arrange: auto_consistente ya está en este estado
        assert auto_consistente.get_conductor() is None
        assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
        assert auto_consistente.verificar_invariante() is True
        
        # Act
        retorno = auto_consistente.asignar("A123", 100)
        
        # Assert: Después de asignar, debe cumplir S4 con el nuevo estado
        assert auto_consistente.get_conductor() == "A123"
        assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
        assert auto_consistente.verificar_invariante() is True
    
    def test_invariante_consistencia_estado_conductor_punto_inconsistente(self):
        """
        S4: Punto inconsistente - conductor='A123' y estado=DISPONIBLE.
        
        Límite cualitativo: (_conductor is None) == (_estado != EN_USO)
        Punto: _conductor = 'A123' AND _estado = DISPONIBLE (viola S4)
        
        Resultado esperado: Invariante violada (error de estado interno).
        """
        # Arrange: Crear objeto con estado interno inconsistente
        # (esto simula un error de implementación o corrupción de estado)
        auto = Auto(patente="ABC-123", km=100, puertas=4, estado=EstadoVehiculo.DISPONIBLE)
        auto._conductor = "A123"  # Asignación directa (viola encapsulamiento para el test)
        
        # Assert: La invariante debe estar violada
        assert auto.verificar_invariante() is False
    
    # =========================================================================
    # Límites de Parámetros de Entrada (Algoritmo 1.7)
    # =========================================================================
    
    @pytest.mark.parametrize("kms, esperado", [
        (0, ValueError),   # L_inf: Excluido por precondición (0 < km)
        (1, "exito"),      # L_inf + eps: Mínimo válido aceptado
        (500, "exito"),    # L_sup: Máximo válido aceptado
        (501, ValueError)  # L_sup + eps: Excede el máximo
    ])
    def test_limites_km_estimados_parametro(self, auto_consistente, kms, esperado):
        """
        Análisis de Valores Límite (AVL) sobre el parámetro km_estimados.
        
        Dominio: (0, 500]
        Límites:
            - L_inf = 0 (excluido): debe lanzar ValueError
            - L_inf + ε = 1: mínimo aceptado
            - L_sup = 500: máximo aceptado
            - L_sup + ε = 501: debe lanzar ValueError
        
        Complementa el análisis de invariante (Alg. 2.6) con el análisis
        de parámetros (Alg. 1.7) según la nota de la Consigna 18.
        """
        if esperado == ValueError:
            # Caso inválido: debe lanzar excepción
            with pytest.raises(ValueError):
                auto_consistente.asignar("A123", kms)
            
            # Verificar que el estado no cambió
            assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
            assert auto_consistente.get_conductor() is None
        else:
            # Caso válido: debe tener éxito
            retorno = auto_consistente.asignar("A123", kms)
            
            assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
            assert auto_consistente.get_conductor() == "A123"
        
        # En todos los casos, el invariante debe mantenerse
        assert auto_consistente.verificar_invariante() is True
    
    @pytest.mark.parametrize("legajo, esperado", [
        ("ABC", ValueError),   # length = 3 < 4: inválido
        ("A123", "exito"),     # length = 4: mínimo válido (en el límite)
        ("A1234", "exito"),    # length = 5: válido (sobre el límite)
    ])
    def test_limites_legajo_conductor_parametro(self, auto_consistente, legajo, esperado):
        """
        Análisis de Valores Límite (AVL) sobre el parámetro legajo_conductor.
        
        Dominio: len >= 4
        Límites:
            - L - ε = 3: debe lanzar ValueError
            - L = 4: mínimo aceptado
            - L + ε = 5: válido
        """
        if esperado == ValueError:
            with pytest.raises(ValueError):
                auto_consistente.asignar(legajo, 100)
            
            # Verificar que el estado no cambió
            assert auto_consistente.get_estado() == EstadoVehiculo.DISPONIBLE
            assert auto_consistente.get_conductor() is None
        else:
            retorno = auto_consistente.asignar(legajo, 100)
            
            assert auto_consistente.get_estado() == EstadoVehiculo.EN_USO
            assert auto_consistente.get_conductor() == legajo
        
        assert auto_consistente.verificar_invariante() is True
