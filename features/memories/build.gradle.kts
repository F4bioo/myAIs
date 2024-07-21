plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.features.memories"

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
    implementation(project(Modules.design))
    implementation(project(Modules.arch))
    implementation(project(Modules.remote))
    implementation(project(Modules.domain))
    implementation(project(Modules.navigation))

    // Libs
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.ui.util)

    // TestLibs
    testImplementation(testFixtures(project(Modules.testing)))
}
