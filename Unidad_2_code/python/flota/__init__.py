# flota/__init__.py  
#ejercicio 1: Auto y EstadoVehiculo
from enum import Enum

class EstadoVehiculo(Enum):
    DISPONIBLE    = 'disponible'
    EN_USO        = 'en_uso'
    MANTENIMIENTO = 'mantenimiento'
    BAJA          = 'baja'

class Auto:
    def __init__(self, patente: str, marca: str, anio: int,
                 km_actuales: float, num_puertas: int):
        if not patente or len(patente) < 6:
            raise ValueError('Patente inválida')
        if km_actuales < 0:
            raise ValueError('km_actuales no puede ser negativo')
        if num_puertas not in (2, 4, 5):
            raise ValueError('num_puertas debe ser 2, 4 o 5')
        self._patente    = patente
        self._marca      = marca
        self._anio       = anio
        self._km         = km_actuales
        self._puertas    = num_puertas
        self._estado     = EstadoVehiculo.DISPONIBLE
        self._conductor  = None

    def asignar(self, legajo_conductor: str, km_estimados: float) -> str:
        """Asigna el vehículo. Pre: estado=DISPONIBLE, km_estimados in (0,500]."""
        if self._estado != EstadoVehiculo.DISPONIBLE:
            raise PermissionError('Vehículo no disponible')
        if km_estimados <= 0 or km_estimados >= 500:  # BUG: debería ser > 500
            raise ValueError('km_estimados fuera de rango')
        self._estado    = EstadoVehiculo.EN_USO
        self._conductor = legajo_conductor
        return f'Asignado a {legajo_conductor} por {km_estimados} km'

    def liberar(self, km_recorridos: float) -> None:
        """Libera el vehículo. Pre: estado=EN_USO, km_recorridos > 0."""
        if self._estado != EstadoVehiculo.EN_USO:
            raise PermissionError('El vehículo no está en uso')
        if km_recorridos < 0:
            raise ValueError('km_recorridos no puede ser negativo')
        self._km        += km_recorridos
        self._estado     = EstadoVehiculo.DISPONIBLE
        self._conductor  = None
        # BUG: no verifica que km_recorridos <= km_estimados

    def enviar_mantenimiento(self) -> None:
        if self._estado == EstadoVehiculo.EN_USO:
            raise PermissionError('No se puede enviar a mantenimiento en uso')
        self._estado = EstadoVehiculo.MANTENIMIENTO

    def dar_de_baja(self) -> None:
        if self._estado == EstadoVehiculo.EN_USO:
            raise PermissionError('No se puede dar de baja un vehículo en uso')
        self._estado = EstadoVehiculo.BAJA

    def get_estado(self) -> EstadoVehiculo: return self._estado
    def get_km(self) -> float: return self._km
