# CalmWrist — Intervención proactiva en crisis de ansiedad (Wear OS)

**CalmWrist** es una aplicación nativa para relojes **Wear OS** diseñada en **Kotlin** y **Jetpack Compose for Wear OS** que detecta de forma proactiva episodios de estrés agudo o ataques de pánico a partir del sensor de frecuencia cardíaca del reloj y guía a la persona de vuelta a la calma mediante respiración háptica 4-7-8 y mensajes de contención personalizados con **Gemini AI**.

---

## Características principales

- **Detección biométrica en tiempo real (`Health Services`)**:
  - Lee el pulso (`DataType.HEART_RATE_BPM`) directamente desde el hardware del reloj.
  - Calcula una línea base personal en reposo mediante media móvil exponencial (`BaselineTracker`), congelando la línea base cuando el pulso sube a `ELEVATED` (+20 BPM) o `CRISIS` (+35 BPM) para no normalizar el pulso de una crisis.
- **Guía de respiración háptica 4-7-8 (`HapticBreathingLoop`)**:
  - Ciclo clínico de **inhalar (4s)**, **sostener (7s)** y **exhalar (8s)**.
  - Patrones de vibración diferenciados por cantidad de pulsos para poder seguir el ritmo con los ojos cerrados sin mirar la pantalla.
- **Cápsula personal de contención (`GeminiCapsuleService`)**:
  - Entrevista breve por voz de 3 preguntas durante el onboarding (lugar de paz, persona/recuerdo que calma y frase de apoyo).
  - Genera una sola vez el mensaje personalizado con `gemini-flash-lite-latest` y lo guarda localmente en el reloj (`CapsuleStore`) para no depender de conexión a internet durante una crisis real.
- **Carta mensual reflexiva e historial (`GeminiReflectionService` y `HistoryScreen`)**:
  - Registro local de episodios resueltos (`EpisodeHistoryStore`), cálculo de tendencia (`IMPROVING`, `STABLE`, `WORSENING`) y distribución por franja horaria (`Madrugada`, `Mañana`, `Tarde`, `Noche`).
  - Generación de una carta mensual cálida en segunda persona a partir de métricas agregadas (sin enviar datos biométricos crudos).
- **Errores humanizados (`HumanizedErrorScreen`)**:
  - Todo aviso del sistema explica obligatoriamente **qué pasó**, **por qué** y **qué hacer ahora** (sensor suelto, batería baja, modo silencio, permisos).

---

## Configuración y ejecución

1. Crea un archivo `local.properties` en la raíz del proyecto con la ruta de tu SDK de Android y tu clave de Gemini:
   ```properties
   sdk.dir=C\:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk
   geminiApiKey=TU_API_KEY_DE_GEMINI
   ```
2. Compila e instala el APK de depuración en el reloj o emulador Wear OS:
   ```bash
   ./gradlew :app:installDebug
   ```
3. Para iniciar el **Panel de control web de demostración** (puerto `4545` vía ADB):
   ```bash
   node control-panel/server.js
   ```
   Abre `http://localhost:4545` en el navegador para simular bandas (`CALM`, `ELEVATED`, `CRISIS`), simular historial de episodios, probar la generación de cápsulas y cartas mensuales en vivo, y consultar el estado del reloj.
