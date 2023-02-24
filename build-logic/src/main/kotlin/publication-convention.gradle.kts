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
}
