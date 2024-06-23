import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply("$rootDir/plugins/android-build.gradle")

val keystorePropertiesFile = rootProject.file("$rootDir/secrets/keystore.properties")

android {
    namespace = Config.NAMESPACE

    defaultConfig {
        applicationId = Config.APPLICATION_ID
        versionCode = Config.VERSION_CODE
        versionName = Config.VERSION_NAME
    }
    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            create("signing") {
                storeFile = file(keystoreProperties["STORE_FILE"] as String)
                storePassword = keystoreProperties["STORE_PASSWORD"] as String
                keyPassword = keystoreProperties["KEY_PASSWORD"] as String
                keyAlias = keystoreProperties["KEY_ALIAS"] as String
            }
        }
    }

    buildTypes {
        configureEach {
            if (name == "staging" || name == "release") {
                if (keystorePropertiesFile.exists()) {
                    signingConfig = signingConfigs.getByName("signing")
                } else {
                    println("keystore.properties not found for $name variant, skipping signing config")
                }
            }
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            libs.versions.composeCompiler.get()
    }
}

dependencies {
    // Modules
    implementation(project(Modules.home))
    implementation(project(Modules.design))
    implementation(project(Modules.arch))
    implementation(project(Modules.remote))
    implementation(project(Modules.domain))

    // Libs
    implementation(libs.common)
}
