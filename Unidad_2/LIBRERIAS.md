# Librerías LaTeX usadas en el informe

## Paquetes utilizados

| Paquete | Propósito | Usado en |
|---------|-----------|----------|
| `booktabs` | Tablas profesionales (toprule, bottomrule, midrule) | Tablas del informe |
| `babel[spanish]` | Traducciones al español (título, figuras, tablas) | Todo el documento |
| `inputenc[utf8]` | Codificación UTF-8 | Caracteres especiales (ñ, tildes) |
| `fontenc[T1]` | Codificación de fuentes T1 | Caracteres especiales |
| `geometry` | Márgenes y tamaño de página | Configuración de página |
| `amsmath` | Fórmulas matemáticas (ecuaciones, entornos math) | Ejercicio 2 (FSM, transiciones) |
| `graphicx` | Inclusión de imágenes | Figuras del informe |
| `caption` | Subtítulos de figuras/tablas | Todas las figuras |
| `multirow` | Celdas que abarcan múltiples filas | Tablas de invariantes |
| `float` | Opción [H] para posicionar figuras | Figuras con [H] |
| `placeins` | \FloatBarrier para evitar queflotantes | Entre secciones |
| `tikz` | Diagramas UML, grafos, árboles, gráficos | Diagrama de estados (ej. 2) |
| `listings` | Código fuente con syntax highlighting | Código Python y Java |
| `xcolor` | Colores para listings y sintaxis | Resaltado de código |
| `hyperref` | Enlaces PDF y书签 | Referencias cruzadas |
| `bookmark` | Marcadores PDF en el documento | Índice PDF |

## Paquetes eliminados (no usados)

- `lmodern` - fuente no utilizada explícitamente
- `textcomp` - símbolos no requeridos
- `amssymb` - símbolos matemáticos no usados (amsmath alcanza)
- `todonotes` - notas TODO no incluidas en el documento

## Paquetes duplicados

- `booktabs` - estava duplicado en líneas 2 y 30 (ya corregido)

---

# Gráficos, Árboles y Diagramas de Decisión

## TikZ (ya instalado)

Ya tenés TikZ instalado. Es el paquete más versátil para cualquier tipo de diagrama.

### Ejemplo: Árbol de decisión básico

```latex
\usepackage{tikz}
\usetikzlibrary{trees}

\begin{tikzpicture}[
    level 1/.style={sibling distance=40mm},
    level 2/.style={sibling distance=20mm},
    every node/.style={draw, rectangle, rounded corners, minimum width=2cm}
]
\node {Condición}
    child {node {Sí}
        child {node {Accción A}}}
    child {node {No}
        child {node {Acción B}}};
\end{tikzpicture}
```

### Ejemplo: Grafo/FSM (ya lo tenés en ejercicio 2)

```latex
\begin{tikzpicture}[
    state/.style={rectangle, rounded corners, draw, minimum width=2.5cm},
    arrow/.style={-Stealth, thick}
]
\node[state] (A) {DISPONIBLE};
\node[state, right=3cm of A] (B) {EN\_USO};

\draw[arrow] (A) -- node[above] {asignar()} (B);
\draw[arrow] (B) -- node[below] {liberar()} (A);
\end{tikzpicture}
```

---

## Paquetes adicionales recomendados (no instalados)

### 1. `forest` - Para árboles (sintácticos, genealógicos, decisión)

**Instalar:** `\usepackage{forest}` (necesita `texlive-pictures`)

**Ejemplo - Árbol de decisión para testing:**

```latex
\usepackage{forest}

\begin{forest}
[Condición inicial
    [Caso A
        [Sub-caso A1]
        [Sub-caso A2]
    ]
    [Caso B
        [Sub-caso B1]
        [Sub-caso B2]
    ]
]
\end{forest}
```

**Ventaja:** Mucho más fácil que TikZ puro para árboles jerárquicos.

---

### 2. `pgfplots` - Gráficos de funciones y datos

**Instalar:** `\usepackage{pgfplots}` (necesita `texlive-pgfplots`)

**Ejemplo - Gráfico de cobertura:**

```latex
\usepackage{pgfplots}

\begin{tikzpicture}
\begin{axis}[
    title={Cobertura de pruebas},
    xlabel={Casos de prueba},
    ylabel={Porcentaje \%},
    ymin=0, ymax=100
]
\addplot coordinates {
    (1, 20)
    (2, 45)
    (3, 70)
    (4, 87)
    (5, 100)
};
\end{axis}
\end{tikzpicture}
```

---

### 3. `qtree` - Árboles simples (sintaxis)

**Instalar:** `\usepackage{qtree}` (más simple que forest)

**Ejemplo:**

```latex
\Tree [.S [.NP Det N ] [.VP V NP ] ]
```

---

## Recomendación para tu caso

Para **árboles de decisión** y **grafos de decisión** en testing:

1. **TikZ** (ya instalado) → Ideal para FSM, grafos complejos, diagramas de decisión
2. **forest** → Mejor para árboles jerárquicos simples

Ya tenés TikZ configurado con librerías:
```latex
\usetikzlibrary{arrows.meta, positioning, shapes.geometric}
```

Podés agregar más librerías según necesidad:
- `trees` → para árboles
- `graphdrawing` → para grafos automáticos
- `calc` → para cálculos

---

## Instalación de paquetes nuevos

Si necesitás un paquete nuevo, ejecutá en terminal:

```bash
tlmgr install <paquete>
```

O agregá la línea `\usepackage{<paquete>}` y TeX Live lo instalará automáticamente si está en los repositorios.

---

# Cómo Referenciar Código desde Repositorio

En lugar de pegar código grande en LaTeX, podés referenciar el archivo en tu repositorio.

## Opción 1: Solo referencia con hipervínculo

```latex
El código completo del test está disponible en:
\url{https://github.com/usuario/repo/blob/main/tests/test_auto.py}
```

## Opción 2: Referencia + mention del archivo

```latex
Los casos de prueba fueron implementados en el archivo \texttt{tests/test\_auto.py}
del repositorio \cite{repo}. El código completo se muestra a continuación:

%Código mínimo o solo las partes relevantes
```

## Opción 3: Incluir código desde archivo externo (sin pegar)

```latex
\lstinputlisting[language=Python]{./codigo/test_ejercicio_1.py}
```

Esto incluye el contenido del archivo directamente, pero no necesitás tener el código en el .tex.

---

## Ejemplo completo en LaTeX

```latex
\section{Test de unidad - Auto}

Los tests fueron desarrollados en Python utilizando pytest \cite{pytest}.
El código completo se encuentra disponible en el repositorio del proyecto.

\subsection{Código del test}

\lstinputlisting[language=Python, caption={Test para invariante de clase}]{./codigo/test_invariante.py}

\subsection{Resultados de ejecución}

Los resultados de cobertura fueron los siguientes (ver figura \ref{fig:cobertura}):

\begin{figure}[H]
    \centering
    \includegraphics[width=0.8\textwidth]{images/cobertura.png}
    \caption{Resultado de coverage}
    \label{fig:cobertura}
\end{figure}
```

## En el .bib agregás tu repo

```bibtex
@misc{repo,
    author = {Tu Nombre},
    title = {Repositorio del Trabajo Práctico},
    year = {2024},
    howpublished = {\url{https://github.com/usuario/verificacion-validacion-sw}},
    note = {Código fuente disponible en: /codigo/}
}
```

---

## Estructura recomendada de carpeta

```
Unidad_2/
├── informe.tex
├── referencias.bib
├── images/
│   └── ...
├── codigo/           ← nueva carpeta para código fuente
│   ├── test_auto.py
│   ├── test_fsm.py
│   └── test_junit.java
└── ejercicios/
    ├── ejercicio_1/
    ├── ejercicio_2/
    └── ejercicio_3/
```

Con `\lstinputlisting{./codigo/test_auto.py}` se incluye automáticamente el código desde el archivo.

---

## Ejemplo: Cómo usarlo en tu informe

Supongamos que tenés el archivo `codigo/test_auto.py`:

```python
import pytest

def test_asignar_km_valido(auto_valido):
    auto_valido.asignar("legajo123", 100.0)
    assert auto_valido.get_estado() == EstadoVehiculo.EN_USO
```

**En tu ejercicio_1.tex:**

```latex
Los tests de asignación fueron implementados en pytest \cite{pytest}.
El código completo del test se muestra a continuación:

\lstinputlisting[
    language=Python,
    caption={Test de asignar con km válido},
    label=cod:test_asignar
]{./codigo/test_auto.py}

Como se observa en el código \ref{cod:test_asignar}, el test verifica que...
```

**Resultado:** El código se renderiza automáticamente en el PDF, sin necesidad de pegarlo en el .tex.

**Si el código es muy largo**, podés especificar qué líneas mostrar:

```latex
\lstinputlisting[firstline=1, lastline=20, language=Python]{./codigo/test_auto.py}
```

Esto muestra solo las líneas 1 a 20 del archivo.