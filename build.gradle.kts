plugins {
    id("kmp-library")
}

dependencies {
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorSerializationJson)
    commonMainImplementation(libs.ktorClientContentNegotiation)
    commonMainImplementation(libs.ktorClientLogging)
    jvmMainImplementation(libs.ktorClientCio)
}
