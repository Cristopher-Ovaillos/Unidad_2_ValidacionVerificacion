# Tests del Ejercicio 4 - Partición por Categoría y Límites de Invariante

## Estructura de archivos

- `modelo.py`: Implementación de la clase `Auto` con invariante de estado
- `test_particion_categoria.py`: Tests del Algoritmo 2.5 (Partición por Categoría)
- `test_limites_invariante.py`: Tests del Algoritmo 2.6 (Límites de Invariante) y Algoritmo 1.7 (Límites de Parámetros)

## Instalación de dependencias

```powershell
pip install pytest pytest-cov
```

## Ejecución de los tests

### Para la Consigna 19 - Ejecutar todos los tests:

```powershell
cd "c:\Users\jason\Downloads\Verificacion-Validacion-sw\Unidad_2\ejercicio_4\codigo"
pytest -v
```

**Copia la salida completa de este comando y pégala en el informe LaTeX en la sección indicada de la Consigna 19.**

### Para la Consigna 20 - Generar reporte de cobertura:

```powershell
cd "c:\Users\jason\Downloads\Verificacion-Validacion-sw\Unidad_2\ejercicio_4\codigo"
pytest --cov=modelo --cov-report=term-missing --cov-branch -v
```

**Copia la salida completa de este comando (especialmente la tabla de cobertura) y pégala en el informe LaTeX en la sección indicada de la Consigna 20.**

### Generar reporte de cobertura HTML (opcional):

```powershell
pytest --cov=modelo --cov-report=html --cov-branch
```

Esto generará un reporte HTML en el directorio `htmlcov/`. Abre `htmlcov/index.html` en tu navegador para ver un reporte visual detallado.

## Interpretación del reporte de cobertura

El reporte mostrará:

- **Stmts**: Número total de declaraciones (líneas de código ejecutables)
- **Miss**: Número de líneas no ejecutadas
- **Branch**: Número total de ramas (decisiones if/else)
- **BrPart**: Ramas parcialmente cubiertas (solo se ejecutó True o False, no ambos)
- **Cover**: Porcentaje de cobertura total
- **Missing**: Números de línea específicos no cubiertos

### Objetivo

Deberías obtener **100% de cobertura** tanto en líneas como en ramas, ya que los tests están diseñados para cubrir:

1. Todos los caminos de éxito del método `asignar()`
2. Todas las excepciones (PermissionError, ValueError por km, ValueError por legajo)
3. Todos los límites del invariante (patente, km, consistencia estado/conductor)
4. Todos los límites de los parámetros de entrada

## Tests incluidos

### TestParticionCategoria (6 tests):
- ✓ Éxito con parámetros válidos
- ✓ Error por estado no disponible
- ✓ Error por legajo vacío
- ✓ Error por legajo corto (< 4 caracteres)
- ✓ Error por km no positivo (≤ 0)
- ✓ Error por km que excede máximo (> 500)

### TestLimitesInvariante (11 tests):
- ✓ Límites de patente: L-ε=5, L=6, L+ε=7
- ✓ Límites de km: L-ε=-1, L=0, L+ε=1
- ✓ Consistencia conductor/estado: consistente e inconsistente
- ✓ Límites parametrizados de km_estimados: 0, 1, 500, 501
- ✓ Límites parametrizados de legajo: longitudes 3, 4, 5

**Total: 17 tests**

## Notas para el informe

Al completar el informe LaTeX, busca los comentarios que dicen:

```latex
% ============================================================================
% INSTRUCCIONES PARA COMPLETAR EL INFORME:
% ============================================================================
```

Y sigue las instrucciones para pegar los resultados de los comandos de pytest en esas secciones.
