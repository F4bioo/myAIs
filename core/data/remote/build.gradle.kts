import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}
apply("$rootDir/plugins/android-build.gradle")

val apiKeyPropertiesFile = rootProject.file("$rootDir/secrets/apiKey.properties")
val apiKeyProperties = Properties()
if (apiKeyPropertiesFile.exists()) {
    apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))
} else {
    println("The apiKey.properties file not found. Creating a new one, please fill out with your own API key.")
    // Note: Do not hard-code your API key here. This line is merely for the purpose of creating the apiKey.properties file.
    apiKeyProperties["GEMINI_API_KEY"] = "\"YOUR_DEFAULT_API_KEY_HERE\""
    apiKeyProperties.store(FileOutputStream(apiKeyPropertiesFile), null)
}

android {
    namespace = "${Config.NAMESPACE}.remote"

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "GEMINI_API_KEY",
            value = apiKeyProperties["GEMINI_API_KEY"] as String
        )
        buildConfigField(
            type = "String",
            name = "GEMINI_BASE_URL",
            value = "\"https://generativelanguage.googleapis.com/\""
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(project(Modules.arch))
    implementation(project(Modules.domain))

    // Libs
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.generativeai)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.timber)
}
