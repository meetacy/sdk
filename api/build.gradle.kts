plugins {
    id("kmp-library-convention")
}

group = "app.meetacy.api"
version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.types)
    commonMainImplementation(libs.kotlinxCoroutines)
}
