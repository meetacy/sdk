plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainImplementation(libs.kotlinxCoroutines)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.kotlinxSerialization)
    commonMainImplementation(libs.ktorClientLogging)
    commonMainImplementation(libs.ktorClientWebSockets)
    commonMainImplementation(libs.rsocketKtorClient)

    commonMainApi(projects.api)

    jvmTestImplementation(libs.ktorClientCio)
}

kotlin.sourceSets.all {
    languageSettings {
        optIn("app.meetacy.sdk.types.annotation.UnstableApi")
    }
}
