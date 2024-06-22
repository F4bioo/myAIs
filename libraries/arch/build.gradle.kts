plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.arch"
}

dependencies {
    // Modules

    // Libs
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.androidx.compose)
    api(libs.startup.runtime)
    implementation(libs.joda.time)
}
