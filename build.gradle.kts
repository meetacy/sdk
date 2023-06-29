@file:Suppress("UNUSED_VARIABLE")

tasks {
    val printSdkVersion by creating {
        group = "terminal"
        doFirst {
            println(libs.versions.meetacySdk.get())
        }
    }
}
