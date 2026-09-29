import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

// La API key vive en local.properties (nunca se sube a git por convención
// de Android), no en el código fuente del repo.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(FileInputStream(file))
}
val geminiApiKey: String = localProperties.getProperty("geminiApiKey", "")

android {
