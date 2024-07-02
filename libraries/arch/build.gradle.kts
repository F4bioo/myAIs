plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.arch"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.versions.composeCompiler.get()
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
}
