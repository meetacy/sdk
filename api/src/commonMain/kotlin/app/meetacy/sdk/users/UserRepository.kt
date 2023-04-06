package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User

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
