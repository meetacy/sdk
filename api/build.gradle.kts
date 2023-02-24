plugins {
    id("kmp-library")
}

dependencies {
    commonMainApi(projects.types)
    commonMainImplementation(libs.kotlinxCoroutines)
}
