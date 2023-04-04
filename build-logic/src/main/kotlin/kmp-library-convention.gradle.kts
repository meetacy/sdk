@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("publication-convention")
}

kotlin {
    jvm {
        jvmToolchain(8)
    }
    js(IR) {
        browser()
        nodejs()
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    explicitApi()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val commonTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        all {
            languageSettings.enableLanguageFeature("DataObjects")
        }
    }
}
