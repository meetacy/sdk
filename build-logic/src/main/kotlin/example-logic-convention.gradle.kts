import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

afterEvaluate {
    val xcodeDir = File(project.buildDir, "xcode")

    tasks.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask>()
        .forEach { xcFrameworkTask ->
            val syncName: String = xcFrameworkTask.name.replace("assemble", "sync")
            val xcframeworkDir =
                File(xcFrameworkTask.outputDir, xcFrameworkTask.buildType.getName())

            tasks.create(syncName, Sync::class) {
                this.group = "xcode"

                this.from(xcframeworkDir)
                this.into(xcodeDir)

                this.dependsOn(xcFrameworkTask)
            }
        }
}
