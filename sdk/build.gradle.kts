plugins {
    id("kmp-library")
}

dependencies {
    commonMainApi(projects.types)
    commonMainImplementation(projects.api)
    commonMainImplementation(libs.kotlinxCoroutines)
}
