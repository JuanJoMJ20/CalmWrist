package com.calmwrist.wear.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors
import java.util.Locale

private val ONBOARDING_QUESTIONS = listOf(
    "¿Qué lugar te da paz?",
    "¿Qué persona o recuerdo te calma?",
    "¿Qué frase quieres escuchar en un mal momento?",
)

/**
 * Entrevista corta por voz (nada de escribir en una pantalla de reloj):
 * las respuestas se mandan una sola vez a la IA para armar la cápsula de
 * contención personalizada. Se puede omitir — la app funciona igual con
 * el texto genérico si la persona no quiere o no puede hacerla ahora.
 */
@Composable
fun OnboardingScreen(
    isGenerating: Boolean,
    generationFailed: Boolean,
    onComplete: (List<String>) -> Unit,
    onSkip: () -> Unit,
) {
    var showWelcome by remember { mutableStateOf(true) }
    var questionIndex by remember { mutableIntStateOf(0) }
    val answers = remember { mutableStateListOf<String>() }
    var voiceUnavailable by remember { mutableStateOf(false) }

    if (showWelcome) {
        OnboardingWelcome(onStart = { showWelcome = false }, onSkip = onSkip)
        return
    }

    val voiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                .orEmpty()
            if (spoken.isNotBlank()) {
                answers.add(spoken)
                if (questionIndex < ONBOARDING_QUESTIONS.lastIndex) {
                    questionIndex++
                } else {
                    onComplete(answers.toList())
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalmWristColors.background)
            .padding(top = 16.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when {
            isGenerating -> {
                CircularProgressIndicator(
                    indicatorColor = CalmWristColors.accentFor(StressBand.CALM),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Preparando tu cápsula…",
                    color = CalmWristColors.textSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }

            generationFailed -> {
                Text(
                    text = "No pude generar tu cápsula ahora",
                    color = CalmWristColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Revisá tu conexión. Mientras tanto uso el mensaje general.",
                    color = CalmWristColors.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Chip(
                    onClick = onSkip,
                    shape = RoundedCornerShape(50),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = CalmWristColors.surface,
                        contentColor = CalmWristColors.textPrimary,
                    ),
                    label = {
                        Text(
                            text = "Entendido",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                )
            }

            voiceUnavailable -> {
                Text(
                    text = "Este reloj no tiene reconocimiento de voz",
                    color = CalmWristColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Prueba en un reloj físico, o sigue con el mensaje general.",
                    color = CalmWristColors.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Chip(
                    onClick = onSkip,
                    shape = RoundedCornerShape(50),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = CalmWristColors.surface,
                        contentColor = CalmWristColors.textPrimary,
                    ),
                    label = {
                        Text(
                            text = "Entendido",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                )
            }

            else -> {
                Text(
                    text = "${questionIndex + 1} / ${ONBOARDING_QUESTIONS.size}",
                    color = CalmWristColors.textSecondary,
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ONBOARDING_QUESTIONS[questionIndex],
                    color = CalmWristColors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Chip(
                    onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                            )
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            putExtra(RecognizerIntent.EXTRA_PROMPT, ONBOARDING_QUESTIONS[questionIndex])
                        }
                        try {
                            voiceLauncher.launch(intent)
                        } catch (e: ActivityNotFoundException) {
                            voiceUnavailable = true
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = CalmWristColors.surface,
                        contentColor = CalmWristColors.accentFor(StressBand.CALM),
                    ),
                    label = {
                        Text(
                            text = "Toca para hablar",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                )
                // El Chip tiene un área táctil mínima más alta que su
                // forma visible (accesibilidad de Wear Compose): un
                // espacio chico acá hacía que tocar "Omitir" en realidad
                // tocara el Chip de arriba.
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Omitir por ahora",
