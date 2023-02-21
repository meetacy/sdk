
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.register("detektWithoutTests") {
    group = "verification"
    dependsOn(tasks.withType<Detekt>().matching { it.name.contains("Test").not() })
}

dependencies {
    "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
}
