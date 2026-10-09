package com.calmwrist.wear.data

import android.content.Context
import com.calmwrist.wear.model.PersonalCapsule

/**
 * Guarda la cápsula personalizada directo en el reloj (SharedPreferences),
 * nunca en la nube: en una crisis real la app no puede depender de red
 * para mostrar el texto que más importa.
 */
class CapsuleStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(capsule: PersonalCapsule) {
        prefs.edit()
