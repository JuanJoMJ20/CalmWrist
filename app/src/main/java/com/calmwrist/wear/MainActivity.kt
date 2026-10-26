package com.calmwrist.wear

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.calmwrist.wear.copy.ErrorPresets
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors
import com.calmwrist.wear.theme.CalmWristTheme
import com.calmwrist.wear.ui.HistoryScreen
import com.calmwrist.wear.ui.HumanizedErrorScreen
import com.calmwrist.wear.ui.InterventionScreen
import com.calmwrist.wear.ui.MonthlyLetterScreen
import com.calmwrist.wear.ui.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmWristTheme {
                CalmWristApp()
            }
        }
    }
}
