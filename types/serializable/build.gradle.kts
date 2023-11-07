plugins {
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainImplementation(projects.types)
    commonMainImplementation(libs.kotlinxSerialization)
}
