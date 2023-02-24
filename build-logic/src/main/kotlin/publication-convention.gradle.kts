plugins {
    id("org.gradle.maven-publish")
}

version = "0.0.1"

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
}
