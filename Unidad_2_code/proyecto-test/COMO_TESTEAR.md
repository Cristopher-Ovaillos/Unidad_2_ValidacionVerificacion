#ejercicio 3: 


```powershell
cd C:\Users\jason\Downloads\Verificacion-Validacion-sw\Unidad_2_code\proyecto-test
```
```
mvn test
```

#### Limpiar y ejecutar tests
```powershell
mvn clean test
```

#### Compilar sin ejecutar tests
```powershell
mvn compile
```

#### Ejecutar un test específico
```powershell
mvn test -Dtest=TestAutoHeredado
mvn test -Dtest=TestCamionHeredado
mvn test -Dtest=TestMotoHeredadi
```

#### Ejecutar un método específico
```powershell
mvn test -Dtest=TestAutoHeredado#testAsignarCE1
```

## 📊 Tests Disponibles en el Proyecto

### 1. **TestAutoHeredado** (7 tests)
Prueba la clase `Auto` que hereda de `Vehiculo`:
- ✅ `testAsignarCE1` - Asignación válida con estado DISPONIBLE
- ✅ `testAsignarCE2` - Asignación con km estimados = 0 (debe fallar)
- ✅ `testAsignarCE3` - Asignación con estado EN_USO (debe fallar)
- ✅ `testAsignarCE4CE5` - Asignación con km > 500 (debe fallar)
- ✅ `testLiberarCE6` - Liberación válida con estado EN_USO
- ✅ `testLiberarCE7CE8` - Liberar cuando ya está DISPONIBLE (debe fallar)
- ✅ `testLiberarCE6CE9` - Liberar con km negativos (debe fallar)

### 2. **TestCamionHeredado** (7 tests)
Prueba la clase `Camion` con las mismas validaciones de vehículo.

### 3. **TestMotoHeredadi** (7 tests)
Prueba la clase `Moto` con restricción adicional de máximo 300 km.

## 📈 Interpretar Resultados

### Éxito completo
```
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Fallos en tests
```
[ERROR] Tests run: 21, Failures: 2, Errors: 0, Skipped: 0
[ERROR] BUILD FAILURE
```
- **Failures**: Test falló (assertion no se cumplió)
- **Errors**: Excepción inesperada durante el test
- **Skipped**: Tests omitidos (marcados con `@Disabled`)

## 🔍 Ver Reportes Detallados

Después de ejecutar `mvn test`, los reportes se generan en:
```
target/surefire-reports/
```

Para ver un reporte específico:
```powershell
cat target/surefire-reports/com.ejercicio_2.TestAutoHeredado.txt
```

## 🛠️ Testear desde VS Code

### Opción 1: Test Runner de Java
1. Abre cualquier archivo de test (ej: `TestAutoHeredado.java`)
2. Verás iconos ▶️ al lado de cada método `@Test`
3. Click en ▶️ para ejecutar ese test específico
4. Click derecho → "Run Tests" para ejecutar todos

### Opción 2: Maven desde VS Code
1. Abre la terminal integrada (Ctrl + `)
2. Ejecuta los comandos Maven mencionados arriba

## 📝 Estructura de los Tests

Los tests siguen el patrón **AAA** (Arrange-Act-Assert):

```java
@Test
void testAsignarCE1() {
    // Arrange: Preparar el objeto
    Vehiculo v = crearVehiculo(100.0);
    
    // Act: Ejecutar la acción
    String resultado = v.asignar("Leg-001", 50.0);
    
    // Assert: Verificar el resultado
    assertNotNull(resultado);
    assertEquals(EstadoVehiculo.EN_USO, v.getEstado());
}
```

### Validaciones comunes:
- `assertEquals(esperado, actual)` - Valores iguales
- `assertThrows(Exception.class, () -> code)` - Debe lanzar excepción
- `assertNotNull(objeto)` - No debe ser null
- `assertTrue(condicion)` - Condición debe ser verdadera

## 🐛 Depuración de Tests

### Ver más detalles en caso de fallo
```powershell
mvn test -X
```

### Ejecutar sin colorear la salida
```powershell
mvn test --batch-mode
```

### Continuar aunque fallen algunos tests
```powershell
mvn test -Dmaven.test.failure.ignore=true
```

## 📦 Comandos Maven Útiles

```powershell
# Ver dependencias del proyecto
mvn dependency:tree

# Limpiar compilaciones anteriores
mvn clean

# Compilar + testear + empaquetar
mvn package

# Instalar en repositorio local
mvn install

# Ver versión de Maven
mvn -version
```

## ✅ Estado Actual del Proyecto

**Todos los tests pasan correctamente:**
- ✅ 7 tests de Auto
- ✅ 7 tests de Camion  
- ✅ 7 tests de Moto
- **Total: 21/21 tests OK**

## 🎯 Próximos Pasos

1. **Agregar más tests** para casos extremos
2. **Medir cobertura de código** con JaCoCo
3. **Tests de integración** entre vehículos
4. **Tests parametrizados** para reducir duplicación

---

💡 **Tip**: Ejecuta `mvn clean test` antes de hacer commits para asegurarte de que todo funciona.
