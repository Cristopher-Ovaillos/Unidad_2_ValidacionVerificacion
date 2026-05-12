import threading 
import pytest
from flota.gestor_flota import GestorFlota

# PS C:\Users\jason\Downloads\Verificacion-Validacion-sw\Unidad_2_code\python> python -m pytest tests/test_ej5p4.py  

def test_singleton_ts(): #el ts es para thread safe
    #arrange: creamos una lista para almacenar las instancias obtenidas por cada hilo
    instancias_obtenidas =[]
    barrera = threading.Barrier(50) #creamos una barrera para sincronizar los hilos, en este caso 5 hilos

    def obtener_instancia():
        barrera.wait() #esperamos a que todos los hilos estén listos antes de obtener la instancia
        instancia = GestorFlota.get_instancia()
        instancias_obtenidas.append(instancia)

    #act: lanzamos los hilos
    hilos = [threading.Thread(target=obtener_instancia) for _ in range(50)]
    for hilo in hilos:
        hilo.start()
    for hilo in hilos:
        hilo.join()

    #assert: verificamos que todas las instancias obtenidas sean la misma

    primera_instancia = instancias_obtenidas[0]
    
    for i in range(1, len(instancias_obtenidas)):
        assert instancias_obtenidas[i] is primera_instancia, f"Instancia {i} no es la misma que la primera instancia"