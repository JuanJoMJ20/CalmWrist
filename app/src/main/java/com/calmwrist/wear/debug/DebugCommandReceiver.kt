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
        val vm = DebugBridge.viewModel ?: return

        when (intent.action) {
            ACTION_SET_BAND -> intent.getStringExtra(EXTRA_BAND)?.let(vm::debugSetBand)
            ACTION_CLEAR_DATA -> vm.debugClearAll()
            ACTION_DUMP_STATE -> vm.debugDumpState()
            ACTION_RUN_CAPSULE_TEST -> {
                val answers = listOf(
                    intent.getStringExtra(EXTRA_Q1) ?: "la playa al atardecer",
                    intent.getStringExtra(EXTRA_Q2) ?: "mi abuela",
                    intent.getStringExtra(EXTRA_Q3) ?: "esto ya pasó antes y vas a estar bien",
                )
                vm.debugRunCapsuleTest(answers)
            }
            ACTION_RUN_LETTER_TEST -> vm.debugRunLetterTest()
