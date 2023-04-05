package app.meetacy.api.users

import app.meetacy.api.AuthorizedMeetacyApi
import app.meetacy.api.MeetacyApi
import app.meetacy.types.auth.Token
import app.meetacy.types.user.RegularUser
import app.meetacy.types.user.SelfUser
import app.meetacy.types.user.User

public sealed interface UserRepository {
    public companion object {
        public fun of(
            token: Token,
            user: User,
            api: MeetacyApi
        ): UserRepository = of(user, api.authorized(token))

        public fun of(
            user: User,
            api: AuthorizedMeetacyApi
        ): UserRepository = when (user) {
            is RegularUser -> RegularUserRepository(user, api.base)
            is SelfUser -> SelfUserRepository(user, api)
        }
    }
}
