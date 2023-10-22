package app.meetacy.sdk.engine.requests

import app.meetacy.sdk.types.auth.Token

public sealed interface MeetacyRequestWithToken<out T> : MeetacyRequest<T> {
    public val token: Token
}

public interface TokenProviderEmpty<out T> : MeetacyRequest<T>


