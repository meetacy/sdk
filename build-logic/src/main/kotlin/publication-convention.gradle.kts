plugins {
    id("org.gradle.maven-publish")
}

group = "app.meetacy.sdk"

publishing {
    repositories {
        maven {
            name = "MeetacySdk"
            url = uri("https://maven.pkg.github.com/meetacy/sdk")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications.withType<MavenPublication> {
        val snapshot = project.findProperty("snapshot")?.toString()?.toBooleanStrict()
        if (snapshot == null || !snapshot) return@withType

        val commit = project.property("commit").toString()
        val attempt = project.property("attempt").toString().toInt()

        afterEvaluate {
            val version = buildString {
                append(this@withType.version)
                append("-build")
                append(commit.take(n = 7))
                if (attempt > 1) {
                    append(attempt)
                }
            }
            this@withType.version = version
        }
    }
}
