package com.calmwrist.wear.biometrics

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.guava.await

data class HeartRateReading(val bpm: Double)

/**
 * Punto de integración con el sensor real del reloj vía Health Services.
 * Reemplaza al hook simulado del prototipo: en vez de un número generado
 * a mano, lee el pulso en segundo plano directamente del hardware.
 */
class BiometricStressService(context: Context) {
    private val measureClient: MeasureClient = HealthServices.getClient(context).measureClient

    suspend fun hasHeartRateCapability(): Boolean {
