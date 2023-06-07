
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("example-logic-convention")
}

dependencies {
    commonMainImplementation(libs.kotlinxCoroutines)
    commonMainApi(projects.api.apiKtor)
    commonMainApi(projects.api)
    commonMainApi(projects.io)

    iosMainImplementation(libs.ktorClientDarwin)
}

kotlin {
    val xcf = XCFramework("shared")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            export(projects.api.apiKtor)
            export(projects.api)
            export(projects.io)
            xcf.add(this)
        }
    }
}
