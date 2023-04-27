plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainApi(projects.io)
    commonMainImplementation(libs.kotlinxCoroutines)
    commonMainImplementation(libs.ktorClient)
}
