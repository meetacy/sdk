@file:Suppress("UNUSED_VARIABLE")

import org.gradle.kotlin.dsl.creating

tasks {
    val printSdkVersion by creating {
        group = "CI"

        doFirst {
            println(versionFromProperties())
        }
    }
}
