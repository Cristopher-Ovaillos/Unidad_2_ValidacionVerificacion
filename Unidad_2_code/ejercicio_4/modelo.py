# clase Auto con invariante de estado
# basado en la def 2.2 y el alg 2.1

from enum import Enum


class EstadoVehiculo(Enum):
    # estados posibles de un vehiculo en la flota
    DISPONIBLE = "disponible"
    EN_USO = "en_uso"
    MANTENIMIENTO = "mantenimiento"


class Auto:
    # invariantes:
    #   len(_patente) >= 6
    #   _km >= 0
    #   _puertas in {2, 4, 5}
    #   _estado in EstadoVehiculo
    #   (_conductor is None) == (_estado != EN_USO)

    def __init__(self, patente: str, km: int, puertas: int, estado: EstadoVehiculo):
        self._patente = patente
        self._km = km
        self._puertas = puertas
        self._estado = estado
        self._conductor = None  # arranca sin conductor

    def asignar(self, legajo_conductor: str, km_estimados: int) -> str:
        # pre: estado == DISPONIBLE
        #      legajo_conductor != '' AND len(legajo_conductor) >= 4
        #      0 < km_estimados <= 500
        # post: estado == EN_USO
        #       _conductor == legajo_conductor
        # exc: PermissionError si estado != DISPONIBLE
        #      ValueError si km_estimados fuera de (0, 500]
        #      ValueError si legajo_conductor invalido

        # B1: validacion de estado
        if self._estado != EstadoVehiculo.DISPONIBLE:
            raise PermissionError(f"El vehiculo no esta disponible. Estado: {self._estado.value}")

        # B3: validacion de km
        if km_estimados <= 0 or km_estimados > 500:
            raise ValueError(f"km estimados deben estar en (0, 500]. Recibido: {km_estimados}")

        # B5: validacion de legajo
        if legajo_conductor == '' or len(legajo_conductor) < 4:
            raise ValueError(f"legajo del conductor debe tener al menos 4 caracteres. Recibido: '{legajo_conductor}'")

        # B7: postcondicion - transicion de estado
        self._estado = EstadoVehiculo.EN_USO
        self._conductor = legajo_conductor

        return f"Vehiculo {self._patente} asignado a {legajo_conductor} por {km_estimados} km"

    def get_estado(self) -> EstadoVehiculo:
        return self._estado

    def get_conductor(self) -> str:
        return self._conductor

    def get_patente(self) -> str:
        return self._patente

    def get_km(self) -> int:
        return self._km

    def get_puertas(self) -> int:
        return self._puertas

    def verificar_invariante(self) -> bool:
        # verifica que el objeto cumple el invariante de clase
        # segun alg 2.1 paso 7, llamar en el assert de cada test

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
