plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.home"

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
    implementation(libs.navigation.common.ktx)
    implementation(libs.navigation.runtime.ktx)
    implementation(libs.navigation.compose)

    // TestLibs
    testImplementation(testFixtures(project(Modules.testing)))
}
