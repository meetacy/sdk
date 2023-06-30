import org.gradle.api.Project

fun Project.versionFromProperties(acceptor: (String) -> Unit) {
    val snapshot = project.findProperty("snapshot")?.toString()?.toBooleanStrict()
    if (snapshot == null || !snapshot) return acceptor(project.version.toString())

    val commit = project.property("commit").toString()
    val attempt = project.property("attempt").toString().toInt()

    afterEvaluate {
        val version = buildString {
            append(project.version)
            append("-build")
            append(commit.take(n = 7))
            if (attempt > 1) {
                append(attempt)
            }
        }
        acceptor(version)
    }
}
