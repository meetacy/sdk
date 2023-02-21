plugins {
    id("kmp-module-convention")
    id("detekt-convention")

}

dependencies {
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorSerializationJson)
    commonMainImplementation(libs.ktorClientContentNegotiation)
    commonMainImplementation(libs.ktorClientLogging)

    jvmMainImplementation(libs.ktorClientCio)
}
