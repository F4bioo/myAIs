import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}
apply("$rootDir/plugins/android-build.gradle")

val keystorePropertiesFile = rootProject.file("$rootDir/secrets/keystore.properties")

val apiKeyPropertiesFile = rootProject.file("$rootDir/secrets/apiKey.properties")
val apiKeyProperties = Properties()
if (apiKeyPropertiesFile.exists()) {
    apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))
} else {
    println("The apiKey.properties file not found. Creating a new one, please fill out with your own API key.")
    // Note: Do not hard-code your API key here. This line is merely for the purpose of creating the apiKey.properties file.
    apiKeyProperties["GEMINI_API_KEY"] = "\"YOUR_DEFAULT_API_KEY_HERE\""
    apiKeyProperties.store(FileOutputStream(apiKeyPropertiesFile), null)
}

android {
    namespace = Config.NAMESPACE

    defaultConfig {
        applicationId = Config.APPLICATION_ID
        versionCode = Config.VERSION_CODE
        versionName = Config.VERSION_NAME

        buildConfigField(
            type = "String",
            name = "GEMINI_API_KEY",
            value = apiKeyProperties["GEMINI_API_KEY"] as String
        )
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
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(project(Modules.home))

    // Libs

}
