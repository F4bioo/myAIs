plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.navigation"

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
    implementation(project(Modules.domain))
    implementation(libs.navigation.common.ktx)
    implementation(libs.navigation.runtime.ktx)

    // Libs

}
