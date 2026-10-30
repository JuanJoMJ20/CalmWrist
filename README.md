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
