package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User

public sealed interface AuthorizedUserRepository {
    public val base: UserRepository

    public companion object {
        public fun of(
            user: User,
            api: AuthorizedMeetacyApi
        ): AuthorizedUserRepository = when (user) {
            is RegularUser -> AuthorizedRegularUserRepository(user, api)
            is SelfUser -> AuthorizedSelfUserRepository(user, api)
        }
    }
}
