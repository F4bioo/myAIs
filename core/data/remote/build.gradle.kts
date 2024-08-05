plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.secrets.gradle.plugin)
}
apply("$rootDir/plugins/android-build.gradle")

secrets {
    propertiesFileName = "$rootDir/secrets/apiKey.properties"
    defaultPropertiesFileName = "$rootDir/default.properties"
}

android {
    namespace = "${Config.NAMESPACE}.core.data.remote"

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "APP_VERSION",
            value = "\"${Config.VERSION_NAME}\""
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(project(Modules.arch))
    implementation(project(Modules.domain))

    // Libs
    api(libs.paging.compose)
    api(libs.paging.common)
    api(libs.paging.runtime.ktx)
    api(libs.paging.common.android)
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.gson)
    api(libs.timber)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.annotation)

    // Google Sign-In and Drive API - DriveScopes
    api(libs.google.api.services.drive) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
    // Identity, AuthorizationRequest
    api(libs.play.services.auth)
    // CredentialManager
    api(libs.credentials.play.services.auth)
    // GetGoogleIdOption, GoogleIdTokenCredential, GoogleIdTokenParsingException
    api(libs.googleid)
    // Coroutines await
    api(libs.kotlinx.coroutines.play.services)

    // TestLibs
    testImplementation(testFixtures(project(Modules.testing)))
    androidTestImplementation(testFixtures(project(Modules.testing)))
}
