plugins {
    id("kmp-library-convention")
}

group = "app.meetacy.sdk"
version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.types)
    commonMainApi(projects.api)
    commonMainApi(libs.kotlinxCoroutines)
}
