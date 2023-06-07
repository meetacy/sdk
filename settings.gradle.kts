enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

includeBuild("build-logic")

include(
    ":api",
    ":types",
    ":api:api-ktor",
    ":kotlinx-datetime",
    ":io",
    ":io:io-ktor"
)

include(":example:shared")

rootProject.name = "meetacy-sdk"
