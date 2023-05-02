plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.types)
    commonMainApi(projects.io.ktorIo)
    commonMainImplementation(libs.kotlinxCoroutines)
}
