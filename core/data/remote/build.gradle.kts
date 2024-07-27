import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}
apply("$rootDir/plugins/android-build.gradle")

val clientSecretPropertiesFile = rootProject.file("$rootDir/secrets/clientSecret.properties")
val clientSecretProperties = Properties()
clientSecretProperties.load(FileInputStream(clientSecretPropertiesFile))

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
    namespace = "${Config.NAMESPACE}.core.data.remote"

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "APP_VERSION",
            value = "\"${Config.VERSION_NAME}\""
        )
        buildConfigField(
            type = "String",
            name = "CLIENT_ID_APP",
            value = clientSecretProperties["CLIENT_ID_APP"] as String
        )
        buildConfigField(
            type = "String",
            name = "CLIENT_ID_WEB",
            value = clientSecretProperties["CLIENT_ID_WEB"] as String
        )
        buildConfigField(
            type = "String",
            name = "DRIVE_BASE_URL",
            value = "\"https://www.googleapis.com/\""
        )
        buildConfigField(
            type = "String",
            name = "GEMINI_API_KEY",
            value = apiKeyProperties["GEMINI_API_KEY"] as String
        )
        buildConfigField(
            type = "String",
            name = "PROMPT_BASE_URL",
            value = "\"http://localhost/\""
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
    api(libs.paging.compose)
    api(libs.paging.common)
    api(libs.paging.runtime.ktx)
    api(libs.paging.common.android)
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.gson)
    api(libs.timber)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Google Sign-In and Drive API
    // DriveScopes
    api("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
    // Identity, AuthorizationRequest
    api("com.google.android.gms:play-services-auth:21.2.0")
    // CredentialManager
    api("androidx.credentials:credentials-play-services-auth:1.2.2")
    // GetGoogleIdOption, GoogleIdTokenCredential, GoogleIdTokenParsingException
    api("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    // Coroutines await
    api("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    implementation("androidx.annotation:annotation:1.8.1")

    // TestLibs
    testImplementation(testFixtures(project(Modules.testing)))
    androidTestImplementation(testFixtures(project(Modules.testing)))
}
