package app.meetacy.types.url

public data class Parameters(
    public val map: Map<String, String>
)

public inline fun buildParameters(
    block: MutableMap<String, String>.() -> Unit
): Parameters = Parameters(buildMap(block))

public fun parametersOf(
    vararg pairs: Pair<String, String>
): Parameters = Parameters(mapOf(*pairs))
