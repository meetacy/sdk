plugins {
    id("network-generator-convention")
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainImplementation(libs.kotlinxCoroutines)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainApi(libs.ktorClient)
    commonMainApi(projects.api)
}

mokoNetwork {
    spec("meetacyApi") {
        inputSpec = file("meetacy-api.yml")
    }
}
