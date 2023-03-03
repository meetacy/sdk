plugins {
    id("network-generator-convention")
    id("kmp-library-convention")
}

version = libs.versions.meetacySdk.get()

dependencies {
    commonMainImplementation(libs.kotlinxCoroutines)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClientLogging)
    commonMainApi(libs.ktorClient)
    commonMainApi(projects.api)
    jvmTestImplementation(libs.ktorClientCio)
}

mokoNetwork {
    spec("meetacyApi") {
        inputSpec = file("meetacy-api.yml")

        configureTask {
            val jarTasks = with(tasks) {
                listOf(
                    sourcesJar,
                    jsSourcesJar,
                    jvmSourcesJar,
                    iosX64SourcesJar,
                    iosArm64SourcesJar,
                    iosSimulatorArm64SourcesJar
                )
            }
            val outputDir = this.outputDir.get()
            jarTasks.forEach {
                it {
                    dependsOn(this@configureTask)
                    inputs.dir(outputDir)
                }
            }
        }
    }
}
