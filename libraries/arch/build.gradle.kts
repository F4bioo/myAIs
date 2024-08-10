plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.libraries.arch"

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Modules

    // Libs
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.androidx.compose)
    api(libs.startup.runtime)
    api(libs.joda.time)

    implementation(libs.bundles.cameraLibs)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.mockk)
    implementation(libs.play.services.auth)
    implementation(libs.work.runtime.ktx)
    implementation(libs.navigation.animation)

    api("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")

    // Identity, AuthorizationRequest
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    // CredentialManager
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    // GetGoogleIdOption, GoogleIdTokenCredential, GoogleIdTokenParsingException
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    // Coroutines await
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
}
