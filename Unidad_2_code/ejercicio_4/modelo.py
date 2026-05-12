"""
Implementación de la clase Auto con invariante de estado.
Siguiendo la Definición 2.2 (Invariante de Clase) y el Algoritmo 2.1 (Diseño de Caso de Prueba Unitario OO).
"""

from enum import Enum


class EstadoVehiculo(Enum):
    """Estados posibles de un vehículo en la flota."""
    DISPONIBLE = "disponible"
    EN_USO = "en_uso"
    MANTENIMIENTO = "mantenimiento"


class Auto:
    """
    Clase Auto con invariante de estado.
    
    Invariante:
        - len(_patente) >= 6
        - _km >= 0
        - _puertas in {2, 4, 5}
        - _estado in EstadoVehiculo
        - (_conductor is None) == (_estado != EN_USO)
    """
    
    def __init__(self, patente: str, km: int, puertas: int, estado: EstadoVehiculo):
        """
        Constructor que establece el estado inicial del objeto.
        
        Args:
            patente: Identificación del vehículo (mín 6 caracteres)
            km: Kilometraje actual (>= 0)
            puertas: Cantidad de puertas (2, 4 o 5)
            estado: Estado inicial del vehículo
        """
        self._patente = patente
        self._km = km
        self._puertas = puertas
        self._estado = estado
        self._conductor = None  # Inicialmente sin conductor asignado
    
    def asignar(self, legajo_conductor: str, km_estimados: int) -> str:
        """
        Asigna un conductor al vehículo para un viaje estimado.
        
        Precondiciones:
            - estado == DISPONIBLE
            - legajo_conductor != '' AND len(legajo_conductor) >= 4
            - 0 < km_estimados <= 500
        
        Postcondiciones:
            - estado == EN_USO
            - _conductor == legajo_conductor
            - retorno contiene confirmación con km_estimados
        
        Excepciones:
            - PermissionError si estado != DISPONIBLE
            - ValueError si km_estimados fuera de (0, 500]
            - ValueError si legajo_conductor inválido
        
        Args:
            legajo_conductor: Identificador del conductor (mín 4 caracteres)
            km_estimados: Kilómetros estimados del viaje (0 < km <= 500)
        
        Returns:
            Mensaje de confirmación de asignación
        
        Raises:
            PermissionError: Si el vehículo no está disponible
            ValueError: Si los parámetros son inválidos
        """
        # B1: Validación de estado del receptor
        if self._estado != EstadoVehiculo.DISPONIBLE:
            raise PermissionError(f"El vehículo no está disponible. Estado actual: {self._estado.value}")
        
        # B3: Validación de rango de kilómetros
        if km_estimados <= 0 or km_estimados > 500:
            raise ValueError(f"Los kilómetros estimados deben estar en el rango (0, 500]. Recibido: {km_estimados}")
        
        # B5: Validación de formato de legajo
        if legajo_conductor == '' or len(legajo_conductor) < 4:
            raise ValueError(f"El legajo del conductor debe tener al menos 4 caracteres. Recibido: '{legajo_conductor}'")
        
        # B7: Procesamiento de postcondición (transición de estado)
        self._estado = EstadoVehiculo.EN_USO
        self._conductor = legajo_conductor
        
        return f"Vehículo {self._patente} asignado al conductor {legajo_conductor} para {km_estimados} km"
    
    def get_estado(self) -> EstadoVehiculo:
        """Retorna el estado actual del vehículo."""
        return self._estado
    
    def get_conductor(self) -> str:
        """Retorna el conductor asignado (None si no hay)."""
        return self._conductor
    
    def get_patente(self) -> str:
        """Retorna la patente del vehículo."""
        return self._patente
    
    def get_km(self) -> int:
        """Retorna el kilometraje actual."""
        return self._km
    
    def get_puertas(self) -> int:
        """Retorna la cantidad de puertas."""
        return self._puertas
    
    def verificar_invariante(self) -> bool:
        """
        Verifica que el objeto cumple con su invariante de clase.
        
        Según Algoritmo 2.1 (Paso 7), este método debe invocarse en el Assert
        de cada caso de prueba para garantizar la integridad del objeto.
        
        Returns:
            True si el invariante se cumple, False en caso contrario
        """
        # S1: len(_patente) >= 6
        if len(self._patente) < 6:
            return False
        
        # S2: _km >= 0
        if self._km < 0:
            return False
        
        # S3: _puertas in {2, 4, 5}
        if self._puertas not in {2, 4, 5}:
            return False
        
        # S4: _estado in EstadoVehiculo
        if not isinstance(self._estado, EstadoVehiculo):
            return False
        
        # S5: (_conductor is None) == (_estado != EN_USO)
        conductor_nulo = (self._conductor is None)
        estado_no_en_uso = (self._estado != EstadoVehiculo.EN_USO)
        if conductor_nulo != estado_no_en_uso:
            return False
        
        return True
