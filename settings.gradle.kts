pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "myAIs"

// -=-= keep in alphabetical order =-=-

// App
include(":app")

// Core
include(
    ":core:data:remote",
    ":core:domain"
)

// Features
include(
    ":features:home",
    ":libraries:arch",
    ":libraries:design",
)

// -=-= keep in alphabetical order =-=-
include(":core:domain")
