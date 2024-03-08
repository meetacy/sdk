package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location

public data class PushLocationRequest(
    val token: Token,
    val location: Location
) : SimpleMeetacyRequest
