plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    // JVM
    jvm()
    // JS
    js(IR) {
        browser()
        nodejs()
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
    }
}
