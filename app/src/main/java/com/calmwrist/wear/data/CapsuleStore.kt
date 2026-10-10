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
            .putString(KEY_TITLE, capsule.title)
            .putString(KEY_SUBTITLE, capsule.subtitle)
            .apply()
    }

    fun load(): PersonalCapsule? {
        val title = prefs.getString(KEY_TITLE, null) ?: return null
        val subtitle = prefs.getString(KEY_SUBTITLE, null) ?: return null
        return PersonalCapsule(title, subtitle)
    }

    fun markOnboardingDismissed() {
        prefs.edit().putBoolean(KEY_DISMISSED, true).apply()
    }

    fun isOnboardingPending(): Boolean =
        load() == null && !prefs.getBoolean(KEY_DISMISSED, false)

    /** Usado por el panel de control de demo para resetear el estado de la cápsula. */
    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "calmwrist_capsule"
        const val KEY_TITLE = "capsule_title"
        const val KEY_SUBTITLE = "capsule_subtitle"
        const val KEY_DISMISSED = "onboarding_dismissed"
    }
}
