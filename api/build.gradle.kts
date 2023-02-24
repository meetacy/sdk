plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.types)
    commonMainImplementation(libs.kotlinxCoroutines)
}
