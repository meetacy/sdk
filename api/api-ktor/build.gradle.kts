import org.gradle.jvm.tasks.Jar

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
            val macosTasks = if (System.getProperty("os.name") == "Mac OS X") {
                listOf(
                    tasks.named<Jar>("iosX64SourcesJar"),
                    tasks.named<Jar>("iosArm64SourcesJar"),
                    tasks.named<Jar>("iosSimulatorArm64SourcesJar")
                )
            } else {
                emptyList()
            }
            val jarTasks = with(tasks) {
                listOf(
                    sourcesJar,
                    jsSourcesJar,
                    jvmSourcesJar
                )
            }

            val outputDir = this.outputDir.get()
            (jarTasks + macosTasks).forEach { task ->
                task {
                    dependsOn(this@configureTask)
                    inputs.dir(outputDir)
                }
            }
        }
    }
}
