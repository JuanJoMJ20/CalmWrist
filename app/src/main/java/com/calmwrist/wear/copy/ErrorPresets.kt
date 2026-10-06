package com.calmwrist.wear.copy

import com.calmwrist.wear.model.HumanizedErrorContent

object ErrorPresets {
    val looseSensor = HumanizedErrorContent(
        whatHappened = "No puedo leer tu pulso",
        why = "El reloj está muy suelto",
        whatToDoNow = "Ajusta la correa a tu muñeca",
    )

    val lowBattery = HumanizedErrorContent(
        whatHappened = "Me voy a apagar pronto",
        why = "Queda poca batería para seguir vigilando tu pulso",
        whatToDoNow = "Cárgame en los próximos 15 minutos",
    )

