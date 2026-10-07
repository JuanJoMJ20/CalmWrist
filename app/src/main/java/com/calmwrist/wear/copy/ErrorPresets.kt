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

    val hapticUnavailable = HumanizedErrorContent(
        whatHappened = "No puedo vibrar ahora",
        why = "El modo silencio del reloj está bloqueando la vibración",
        whatToDoNow = "Desactiva el modo silencio para sentir la guía",
    )

    val sensorPermissionDenied = HumanizedErrorContent(
        whatHappened = "No puedo vigilar tu pulso",
        why = "No me diste permiso para leer el sensor del reloj",
        whatToDoNow = "Abre Ajustes y activa el permiso de sensores corporales",
    )

    val sensorUnsupported = HumanizedErrorContent(
        whatHappened = "No puedo vigilar tu pulso de forma automática",
        why = "Este reloj no tiene un sensor de pulso compatible",
        whatToDoNow = "Usa el botón de respiración manual cuando lo necesites",
    )
}
