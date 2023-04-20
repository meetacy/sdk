package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User

public sealed interface AuthorizedUserRepository {
    public val data: User
    public val base: UserRepository

    public companion object {
        public fun of(
            data: User,
            api: AuthorizedMeetacyApi
        ): AuthorizedUserRepository = when (data) {
            is RegularUser -> AuthorizedRegularUserRepository(data, api)
            is SelfUser -> AuthorizedSelfUserRepository(data, api)
        }
    }
}
