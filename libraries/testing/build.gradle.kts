plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

android {
    namespace = "${Config.NAMESPACE}.testing"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.versions.composeCompiler.get()
    }
    @Suppress("UnstableApiUsage")
    testFixtures {
        enable = true
    }
}

dependencies {
    // Modules

    // Libs

    // TestLibs
    testImplementation(libs.bundles.defaultTestLibs)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test.jvm)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.core.testing)

    // Test Fixtures dependencies
    testFixturesImplementation(libs.bundles.defaultTestLibs)
    testFixturesImplementation(libs.koin.test.junit4)
    testFixturesImplementation(libs.koin.test)
    testFixturesImplementation(libs.mockk)
    testFixturesImplementation(libs.kotlin.test)
    testFixturesImplementation(libs.turbine)
    testFixturesImplementation(libs.kotlin.test.junit)
    testFixturesImplementation(libs.kotlinx.coroutines.test)
    testFixturesImplementation(libs.kotlinx.coroutines.test.jvm)
    testFixturesImplementation(libs.kotlinx.coroutines.core)
    testFixturesImplementation(libs.core.testing)
}
