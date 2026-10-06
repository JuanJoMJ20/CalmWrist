package com.calmwrist.wear.copy

import com.calmwrist.wear.model.PersonalCapsule
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.model.ToneCopy
import com.calmwrist.wear.model.ToneMode

/**
 * Voz (estable, en toda banda): cercana, en primera persona, nunca clínica.
 * Tono (varía): empático al inicio, imperativo-calmante en crisis — frases
 * cortas y verbos en imperativo suave para que una mente en pánico, con
 * atención reducida, pueda seguirlas sin esfuerzo de interpretación.
 *
 * En CRISIS, si la persona ya hizo el onboarding por voz, [capsule]
 * reemplaza el texto genérico por el que la IA generó a partir de lo que
 * ella misma contó que la calma — la Voz sigue siendo la misma, pero el
 * contenido es suyo.
 */
fun toneCopyFor(band: StressBand, capsule: PersonalCapsule? = null): ToneCopy = when (band) {
    StressBand.CALM -> ToneCopy(
        tone = ToneMode.EMPATHETIC,
        title = "Estoy contigo",
        subtitle = "Tu ritmo está estable. Aquí estoy si me necesitas.",
        actionLabel = "Respirar conmigo",
    )
    StressBand.ELEVATED -> ToneCopy(
        tone = ToneMode.EMPATHETIC,
        title = "Noto que tu cuerpo se activó",
        subtitle = "Es solo una señal, no un peligro. Vamos a bajar el ritmo.",
        actionLabel = "Empezar a respirar",
    )
    StressBand.CRISIS -> ToneCopy(
        tone = ToneMode.IMPERATIVE_CALMING,
        title = capsule?.title ?: "Mírame. Respira conmigo.",
        subtitle = capsule?.subtitle ?: "Esto va a pasar. Sigue el círculo con tu respiración.",
        actionLabel = "Ya estoy mejor",
    )
}

