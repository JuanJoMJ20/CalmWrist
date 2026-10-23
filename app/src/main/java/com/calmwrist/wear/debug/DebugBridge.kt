package com.calmwrist.wear.debug

import com.calmwrist.wear.MainViewModel

/**
 * Puente entre el BroadcastReceiver (que no puede llamar directo a
 * funciones del ViewModel) y la instancia del ViewModel que está viva
 * en memoria. Solo existe para el panel de control de la demo.
 */
object DebugBridge {
    var viewModel: MainViewModel? = null

    fun attach(vm: MainViewModel) {
        viewModel = vm
    }
}
