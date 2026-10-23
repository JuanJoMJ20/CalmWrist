package com.calmwrist.wear.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.calmwrist.wear.BuildConfig

/**
 * Receptor de comandos del panel de control web de la demo (lo dispara
 * el panel vía "adb shell am broadcast"). Solo hace algo en builds debug:
 * nunca queda expuesto en una build de release.
 */
class DebugCommandReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!BuildConfig.DEBUG) return
