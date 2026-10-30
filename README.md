# CalmWrist — Intervención proactiva en crisis de ansiedad (Wear OS)

**CalmWrist** es una aplicación nativa para relojes **Wear OS** diseñada en **Kotlin** y **Jetpack Compose for Wear OS** que detecta de forma proactiva episodios de estrés agudo o ataques de pánico a partir del sensor de frecuencia cardíaca del reloj y guía a la persona de vuelta a la calma mediante respiración háptica 4-7-8 y mensajes de contención personalizados con **Gemini AI**.

---

## Características principales

- **Detección biométrica en tiempo real (`Health Services`)**:
  - Lee el pulso (`DataType.HEART_RATE_BPM`) directamente desde el hardware del reloj.
  - Calcula una línea base personal en reposo mediante media móvil exponencial (`BaselineTracker`), congelando la línea base cuando el pulso sube a `ELEVATED` (+20 BPM) o `CRISIS` (+35 BPM) para no normalizar el pulso de una crisis.
- **Guía de respiración háptica 4-7-8 (`HapticBreathingLoop`)**:
  - Ciclo clínico de **inhalar (4s)**, **sostener (7s)** y **exhalar (8s)**.
