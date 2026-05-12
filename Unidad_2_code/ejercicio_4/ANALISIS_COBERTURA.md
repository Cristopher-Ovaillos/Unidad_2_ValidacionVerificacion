# Análisis de Cobertura - Ejercicio 4

## Resultados de la ejecución

### Cobertura obtenida:
- **Total**: 90% (46 líneas, 4 no cubiertas)
- **Ramas**: 87.5% (16 ramas, 2 parcialmente cubiertas)

### Líneas no cubiertas:
- **Línea 106**: `return self._km` - Método getter `get_km()`
- **Línea 110**: `return self._puertas` - Método getter `get_puertas()`
- **Línea 132**: `return False` - Validación S3 del invariante (puertas no en {2, 4, 5})
- **Línea 136**: `return False` - Validación S4 del invariante (estado no es EstadoVehiculo)

## Interpretación

### ¿Por qué estas líneas no están cubiertas?

1. **Getters `get_km()` y `get_puertas()`**: Estos métodos no se invocan en ningún test porque:
   - No son necesarios para verificar el comportamiento del método `asignar()`
   - Los tests se enfocan en `get_estado()`, `get_conductor()` y `verificar_invariante()`

2. **Validaciones S3 y S4 del invariante**: Estas ramas del método `verificar_invariante()` retornan `False` solo cuando:
   - S3: Las puertas no están en {2, 4, 5}
   - S4: El estado no es una instancia de `EstadoVehiculo`
   
   Ningún test crea objetos con estos estados inválidos porque:
   - Los tests existentes se enfocan en validar el método `asignar()` específicamente
   - Las validaciones S1, S2 y S5 del invariante ya están cubiertas

### ¿El método `asignar()` tiene 100% de cobertura?

**SÍ**. Todas las líneas y ramas del método `asignar()` están completamente cubiertas:
- ✓ B1: Validación de estado != DISPONIBLE (líneas 69-70)
- ✓ B3: Validación de km fuera de rango (líneas 73-74)
- ✓ B5: Validación de legajo inválido (líneas 77-78)
- ✓ B7: Procesamiento de postcondición (líneas 81-84)

La cobertura parcial se debe a que el análisis incluye TODO el módulo `modelo.py`, no solo el método `asignar()`.

## Recomendación para el informe

### Opción 1: Reportar la cobertura del método específico

En el informe, puedes mencionar que:
> "El método `asignar()` alcanzó el **100% de cobertura** en todas sus ramas de decisión (B1, B3, B5, B7). La cobertura total del módulo es del 90% debido a métodos auxiliares (getters) y validaciones del invariante que no son invocadas directamente en estos tests."

### Opción 2: Agregar tests adicionales para 100% total

Si deseas alcanzar el 100% de cobertura del módulo completo, puedes agregar tests que:

1. **Para los getters**: Simplemente llamarlos en algún test existente
2. **Para S3 (puertas inválidas)**: Crear un test que construya un Auto con puertas=3
3. **Para S4 (estado inválido)**: Este caso es difícil de probar sin violar el tipo de Python

**Archivo de ejemplo**: `test_cobertura_completa.py` (opcional)

```python
def test_getters_cobertura():
    """Test adicional para cubrir los getters no usados."""
    auto = Auto("ABC-123", 100, 4, EstadoVehiculo.DISPONIBLE)
    assert auto.get_km() == 100
    assert auto.get_puertas() == 4

def test_invariante_puertas_invalidas():
    """Test para cubrir la validación S3 del invariante."""
    auto = Auto("ABC-123", 100, 3, EstadoVehiculo.DISPONIBLE)  # 3 puertas no está en {2,4,5}
    assert auto.verificar_invariante() is False
```

## Conclusión para el informe LaTeX

**Para la Consigna 20**, puedes incluir el análisis así:

> Al ejecutar `pytest --cov=modelo --cov-branch`, se obtuvo una **cobertura del 90%** del módulo completo. Las líneas no cubiertas (106, 110, 132, 136) corresponden a:
>
> - Métodos getter (`get_km()`, `get_puertas()`) que no son necesarios para verificar el comportamiento de `asignar()`
> - Ramas del método `verificar_invariante()` que validan condiciones que no se presentan en los tests actuales (puertas inválidas, tipo de estado inválido)
>
> **El método `asignar()` bajo prueba alcanzó el 100% de cobertura**, ejecutando todas sus ramas de decisión según el GFC del Algoritmo 1.2:
> - B1 → B2: Estado no disponible (PermissionError)
> - B1 → B3 → B4: Kilómetros fuera de rango (ValueError)
> - B1 → B3 → B5 → B6: Legajo inválido (ValueError)
> - B1 → B3 → B5 → B7 → S: Caso de éxito (transición a EN_USO)
>
> Los 21 tests diseñados cubren exhaustivamente el catálogo reducido del Algoritmo 2.5 (Partición por Categoría), los límites del Algoritmo 2.6 (Límites de Invariante) y del Algoritmo 1.7 (Límites de Parámetros), garantizando la detección de defectos en las fronteras críticas del método.
