plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.design"
}

dependencies {
    implementation(libs.androidx.ui.text.google.fonts)
    // Modules

    // Libs
}
