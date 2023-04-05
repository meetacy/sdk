package app.meetacy.api.users

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.User

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
