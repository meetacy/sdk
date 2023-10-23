
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    api(libs.kotlinxSerializationPlugin)
    api(libs.kotlinPlugin)
}
