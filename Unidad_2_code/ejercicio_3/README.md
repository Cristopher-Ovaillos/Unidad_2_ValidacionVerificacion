# Ejercicio 3 - Testing Polimórfico y LSP

## Estructura del Ejercicio

Este ejercicio implementa el **Algoritmo 2.4 (Verificación LSP)** mediante la técnica de **Suite Heredada** (Definición 2.11) sobre la jerarquía `Vehiculo` → {`Auto`, `Moto`, `Camion`}.

---

## Consignas y Archivos

### **Consigna 11**: Suite Heredada

**Objetivo:** Implementar una clase abstracta de test que contenga un caso por cada combinación `método × clase de equivalencia` del contrato de `Vehiculo`.

**Archivo:** `VehiculoTest.java`

**Contrato de Vehiculo:**
```java
// metodo asignar(legajo, kmEstimados)
Pre:  kmEstimados > 0 && kmEstimados <= 500 && estado == DISPONIBLE
Post: estado == EN_USO && retorno != null

// metodo liberar(kmRecorridos)
Pre:  estado == EN_USO && kmRecorridos >= 0
Post: estado == DISPONIBLE && kmActuales += kmRecorridos

// invariante
estado ∈ {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
```

**Clases de Equivalencia (Algoritmo 1.6):**

| Variable | CE | Condición | Tipo | Representante |
|----------|-----|-----------|------|---------------|
| kmEstimados (asignar) | CE1 | 0 < km ≤ 500 | válida | 100 |
| kmEstimados (asignar) | CE2 | km ≤ 0 | inválida | 0 |
| kmEstimados (asignar) | CE3 | km > 500 | inválida | 501 |
| estado (asignar) | CE4 | estado == DISPONIBLE | válida | DISPONIBLE |
| estado (asignar) | CE5 | estado != DISPONIBLE | inválida | EN_USO |
| estado (liberar) | CE6 | estado == EN_USO | válida | EN_USO |
| estado (liberar) | CE7 | estado != EN_USO | inválida | DISPONIBLE |
| kmRecorridos (liberar) | CE8 | km ≥ 0 | válida | 150 |
| kmRecorridos (liberar) | CE9 | km < 0 | inválida | -10 |

**Tests implementados:**
- `TC-A1`: asignar con km=100 (CE1 + CE4) → éxito
- `TC-A2`: asignar con km=1 (límite inferior) → éxito
- `TC-A3`: asignar con km=500 (límite superior) → éxito
- `TC-A4`: asignar con km=0 (CE2) → IllegalArgumentException
- `TC-A5`: asignar con km=501 (CE3) → IllegalArgumentException
- `TC-A6`: asignar con estado EN_USO (CE5) → IllegalStateException
- `TC-L1`: liberar con km=150 (CE6 + CE8) → éxito
- `TC-L2`: liberar con estado DISPONIBLE (CE7) → IllegalStateException
- `TC-L3`: liberar con km=-10 (CE9) → IllegalArgumentException

---

### **Consigna 12**: Ejecutar Suite Heredada sobre Subclases

**Objetivo:** Aplicar el Algoritmo 2.4 (Paso 4) - ejecutar la suite heredada usando instancias de cada subclase.

**Archivos:**
- `AutoTest.java` - hereda de `VehiculoTest`
- `MotoTest.java` - hereda de `VehiculoTest`
- `CamionTest.java` - hereda de `VehiculoTest`

**Mecanismo:** 
Cada subclase implementa el método factory `crearVehiculo()`:
```java
@Override
protected Vehiculo crearVehiculo() {
    return new Auto("ABC123", 0.0, 4);  // o Moto o Camion
}
```

JUnit ejecuta **todos los tests de VehiculoTest** automáticamente sobre cada subclase gracias a la herencia.

---

### **Consigna 13**: Análisis LSP (Propiedad 2.3)

**Objetivo:** Analizar si `Moto` y `Camion` violan LSP.

**Propiedad 2.3 (Liskov Substitution Principle):**
```
Pre_C ⟹ Pre_D  (precondición de D más débil o igual)
Post_C ⟸ Post_D (postcondición de D más fuerte o igual)
```

**Resultados esperados al ejecutar:**

#### **Auto** ✅ CUMPLE LSP
- Todos los tests **PASAN**
- No modifica pre/postcondiciones del contrato

#### **Moto** ❌ VIOLA LSP
- Test `TC-A3` (asignar con km=500) **FALLA**
- **Razón:** `Moto` restringe el límite de 500 km a 300 km
- **Análisis:** `Pre_Moto` es **más fuerte** que `Pre_Vehiculo`
  - `Pre_Vehiculo`: km ∈ (0, 500]
  - `Pre_Moto`: km ∈ (0, 300]
  - Los valores en (300, 500] son válidos para el padre pero inválidos para el hijo
- **Violación:** Un cliente que espera poder asignar 400 km a un `Vehiculo` falla cuando recibe una `Moto`

#### **Camion** ❌ VIOLA LSP
- Los tests **PASAN** si cargaActual=0 (estado inicial normal)
- **Razón:** `Camion` agrega precondición adicional: `cargaActual <= capacidad`
- **Análisis:** `Pre_Camion` es **más fuerte** que `Pre_Vehiculo`
  - `Pre_Vehiculo`: solo requiere estado=DISPONIBLE y km válidos
  - `Pre_Camion`: además requiere que no esté sobrecargado
- **Violación:** Un cliente que usa `Vehiculo` no espera que `asignar()` falle por una condición de carga desconocida

---

### **Consigna 14**: Documentación Formal de Auto

**Objetivo:** Documentar el contrato de la subclase que cumple LSP.

**Auto (extensión válida):**
```
Pre_Auto:  kmEstimados ∈ (0, 500] ∧ estado == DISPONIBLE
           ∧ legajo != null ∧ legajo no vacío

Post_Auto: estado == EN_USO ∧ retorno != null
           ∧ conductor == legajo (extensión documentada)

Invariante_Auto:
  estado ∈ {DISPONIBLE, EN_USO, MANTENIMIENTO, BAJA}
  ∧ kmActuales >= 0
  ∧ numPuertas ∈ {2, 4, 5}
  ∧ (conductor == null) == (estado != EN_USO)
```

**Tests específicos de Auto** (en `AutoTest.java`):
- `TE-A1`: Auto registra conductor tras asignar
- `TE-A2`: Auto libera conductor tras liberar
- `TE-A3`: Invariante numPuertas ∈ {2, 4, 5}

---

### **Consigna 15**: Verificación Formal LSP

**Objetivo:** Aplicar Propiedad 2.3 formalmente.

#### **Auto** ✅
```
Pre_Vehiculo ⟹ Pre_Auto: ✓ (idénticas)
Post_Auto ⟹ Post_Vehiculo: ✓ (extiende sin debilitar)
```

#### **Moto** ❌
```
Pre_Vehiculo ⟹ Pre_Moto: ✗ 
  Contraejemplo: km=400 ∈ Pre_Vehiculo pero ∉ Pre_Moto
  
Corrección: Aumentar el límite de Moto a 500 km, o manejar el exceso
            como advertencia interna sin lanzar excepciones que rompan
            el contrato heredado.
```

#### **Camion** ❌
```
Pre_Vehiculo ⟹ Pre_Camion: ✗
  Contraejemplo: estado=DISPONIBLE ∈ Pre_Vehiculo pero si 
                 cargaActual > capacidad entonces ∉ Pre_Camion
                 
Corrección: Aplicar patrón State o permitir asignación independientemente
            de la carga, delegando el control de pesaje a un proceso
            posterior que no bloquee el método asignar() heredado.
```

---

## Comandos de Ejecución

### Prerrequisitos
```bash
cd c:\Users\jason\Downloads\Verificacion-Validacion-sw\Unidad_2_code\ejercicio_3\auto
```

### **Consigna 11**: Ver los tests de la Suite Heredada
```bash
# ver el codigo de la suite abstracta
code src\test\java\com\enunciado_3\VehiculoTest.java
```

### **Consigna 12**: Ejecutar Suite Heredada sobre cada subclase
```bash
# ejecutar todos los tests (3 subclases × tests heredados)
mvn test

# ejecutar solo Auto (deberia pasar todos)
mvn test -Dtest=AutoTest

# ejecutar solo Moto (deberia fallar TC-A3)
mvn test -Dtest=MotoTest

# ejecutar solo Camion (pasa si carga=0, pero viola LSP conceptualmente)
mvn test -Dtest=CamionTest
```

### **Consignas 13-15**: Análisis de resultados

**Paso 1:** Ejecutar todos los tests y observar qué falla
```bash
mvn test
```

**Paso 2:** Analizar la salida:
- ✅ `AutoTest`: X passed, 0 failed
- ❌ `MotoTest`: X-1 passed, 1 failed (TC-A3)
- ⚠️ `CamionTest`: X passed, 0 failed (pero LSP violado conceptualmente)

**Paso 3:** Revisar el test específico que falla en Moto
```bash
mvn test -Dtest=MotoTest#asignar_limite_superior
```

**Paso 4:** Documentar en el informe LaTeX:
- Para Moto: precondición más fuerte
- Para Camion: precondición adicional
- Para Auto: cumple LSP perfectamente

---

## Archivos del Proyecto

### **Archivos de Producción** (`src/main/java`)
- `Vehiculo.java` - clase abstracta padre
- `Auto.java` - subclase que cumple LSP
- `Moto.java` - subclase que viola LSP (límite 300 km)
- `Camion.java` - subclase que viola LSP (precondición de carga)
- `EstadoVehiculo.java` - enum de estados
- `SobrecargaException.java` - excepción personalizada

### **Archivos de Test** (`src/test/java`)

**✅ NECESARIOS (para las consignas 11-15):**
- `VehiculoTest.java` - Suite Heredada (Consigna 11)
- `AutoTest.java` - Tests de Auto (Consigna 12)
- `MotoTest.java` - Tests de Moto (Consigna 12)
- `CamionTest.java` - Tests de Camion (Consigna 12)

**❌ OPCIONALES (no requeridos por el ejercicio):**
- `FormalContractTest.java` - documentación formal redundante con el .tex
- `LspAnalysisTest.java` - análisis formal redundante con el .tex

---

## Relación con los Algoritmos de la Unidad II

### **Algoritmo 2.4** (Verificación LSP)
```
Paso 1: Identificar superclase (Vehiculo) y subclases (Auto, Moto, Camion)
Paso 2: Documentar contrato de cada método público (Pre, Post, Invariante)
Paso 3: Construir Suite Heredada (VehiculoTest.java)
Paso 4: Ejecutar suite sobre cada subclase (AutoTest, MotoTest, CamionTest)
Paso 5: Analizar fallas → detectar violaciones LSP
```

### **Algoritmo 1.6** (Clases de Equivalencia)
Usado en Consigna 11 para identificar las particiones de entrada:
- Para cada parámetro, dividir en clases válidas e inválidas
- Seleccionar un representante de cada clase
- Crear un test por cada clase de equivalencia

### **Propiedad 2.3** (Liskov Substitution Principle)
```
Pre_C ⟹ Pre_D: la precondición del hijo debe ser más débil
Post_C ⟸ Post_D: la postcondición del hijo debe ser más fuerte
```

Verificado en Consignas 13-15 mediante:
1. Ejecución dinámica (tests fallan)
2. Análisis estático (comparación formal de contratos)

---

## Interpretación de Resultados

### Salida esperada de `mvn test`:

```
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 (AutoTest)
[INFO] Tests run: 9, Failures: 1, Errors: 0, Skipped: 0 (MotoTest)
        ↑ TC-A3 falla porque Moto rechaza km=500
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 (CamionTest)
        ↑ Pasa porque carga=0, pero LSP está violado conceptualmente
```

### Para el informe LaTeX (Consignas 13-15):

**Moto:** 
- Violación: `Pre_Moto` más fuerte que `Pre_Vehiculo`
- Test que falla: `TC-A3` con km=500
- Corrección: Aumentar límite a 500 km

**Camion:**
- Violación: precondición adicional de carga
- Tests pasan pero LSP violado
- Corrección: Eliminar validación de carga en `asignar()` o usar patrón State

**Auto:**
- Cumple LSP perfectamente
- Extiende con `conductor` sin romper contrato
- Todos los tests pasan

---

## Notas Adicionales

### 1. ¿Por qué NO se usa GFC en este ejercicio?

**GFC (Grafo de Flujo de Control - Algoritmo 1.2)** es una técnica de **testing de caja blanca** que:
- Construye un grafo dirigido donde cada nodo es un bloque de código
- Calcula la complejidad ciclomática: M = arcos - nodos + 2
- Diseña casos de test para cubrir todos los caminos (C0/C1)

**Este ejercicio usa testing de caja NEGRA basado en contratos:**
- **Objetivo:** Verificar LSP (sustitución polimórfica)
- **Técnica:** Suite Heredada (Definición 2.11)
- **Base:** Clases de Equivalencia sobre pre/postcondiciones
- **Foco:** Comportamiento observable, no estructura interna

**Cuándo usar GFC:**
- Testing de una función/método específico (Unit Testing)
- Necesidad de cobertura C1 (todas las ramas)
- Código con lógica compleja (múltiples ifs, loops)
- Ver ejercicio_4 para ejemplo de GFC aplicado

**Cuándo usar Suite Heredada:**
- Verificar jerarquías de herencia (LSP)
- Testing polimórfico sobre múltiples subclases
- Contratos formales (Pre/Post/Invariante)
- Este ejercicio_3

---

### 2. ¿Por qué existen FormalContractTest y LspAnalysisTest?

Estos archivos **NO son necesarios** para completar las Consignas 11-15. Son documentación redundante con el archivo `.tex` del ejercicio.

**Archivos esenciales:**
- `VehiculoTest.java` ← Suite Heredada (Consigna 11)
- `AutoTest.java`, `MotoTest.java`, `CamionTest.java` ← Ejecución (Consigna 12)
- Análisis LSP va en el informe LaTeX, no en código de test

**Para ejecutar SOLO lo necesario:**
```bash
mvn test -Dtest=AutoTest,MotoTest,CamionTest
```

---

### 3. Diferencia entre Unidad I y II

| Aspecto | Unidad I | Unidad II |
|---------|----------|-----------|
| **Paradigma** | Procedural/Funcional | Orientado a Objetos |
| **Técnicas** | GFC, CE, AVL, C0/C1 | Suite Heredada, LSP, Invariantes |
| **Foco** | Estructura del código | Contratos y comportamiento |
| **Testing** | Caja blanca | Caja negra + contratos |
| **Ejemplo** | ejercicio_1 (funciones) | ejercicio_3 (jerarquías) |

---

### 4. Suite Heredada vs Tests Específicos
   - Suite Heredada: verifica el contrato del padre
   - Tests Específicos: verifican funcionalidades propias de cada hijo
