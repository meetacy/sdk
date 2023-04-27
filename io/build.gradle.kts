plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainImplementation(libs.kotlinxCoroutines)
}
