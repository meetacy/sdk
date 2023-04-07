
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(libs.kotlinSerializationGradle)
    api(libs.kotlinGradlePlugin)
    api(libs.mokoNetworkGenerator)
}
