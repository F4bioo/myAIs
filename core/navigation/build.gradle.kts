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
}

dependencies {
    // Modules
    implementation(project(Modules.domain))
    implementation(project(Modules.arch))

    // Libs

}
