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
@Suppress("UnstableApiUsage")
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
    ":core:data:local",
    ":core:data:remote",
    ":core:domain",
    ":core:navigation"
)

// Features
include(
    ":features:home",
    ":features:memories"
)

// Libraries
include(
    ":libraries:arch",
    ":libraries:design",
    ":libraries:testing",
)

// -=-= keep in alphabetical order =-=-
