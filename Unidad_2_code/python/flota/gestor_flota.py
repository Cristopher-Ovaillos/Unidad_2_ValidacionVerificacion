# flota/gestor_flota.py
import threading


class GestorFlota:
    _instancia = None
    _lock = threading.Lock()


    def __init__(self):
        self._vehiculos = {}   # patente -> Vehiculo
        self._asignaciones = 0


    @classmethod
    def get_instancia(cls):
        if cls._instancia is None:
            with cls._lock:
                if cls._instancia is None:   # double-checked locking
                    cls._instancia = cls()
        return cls._instancia


    def registrar(self, vehiculo) -> None:
        if vehiculo.get_patente() in self._vehiculos:
            raise ValueError('Patente ya registrada')
        self._vehiculos[vehiculo.get_patente()] = vehiculo


    def asignar(self, patente: str, legajo: str, km: float) -> str:
        if patente not in self._vehiculos:
            raise KeyError('Vehículo no encontrado')
        resultado = self._vehiculos[patente].asignar(legajo, km)
        self._asignaciones += 1
        return resultado


    def get_total_asignaciones(self) -> int:
        return self._asignaciones


    # Solo para tests — no usar en producción
    @classmethod
    def _reset_para_tests(cls):
        cls._instancia = None
