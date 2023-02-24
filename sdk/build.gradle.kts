plugins {
    id("kmp-library-convention")
}

group = "app.meetacy.sdk"
version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.types)
    commonMainImplementation(projects.api)
    commonMainImplementation(libs.kotlinxCoroutines)
}
