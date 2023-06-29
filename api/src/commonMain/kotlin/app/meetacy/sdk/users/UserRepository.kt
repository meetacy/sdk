package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.user.RegularUser
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.types.user.User

public sealed interface UserRepository {
    public val data: User

    public companion object {
        public fun of(
            data: User,
            api: MeetacyApi
        ): UserRepository = when (data) {
            is RegularUser -> RegularUserRepository(data, api)
            is SelfUser -> SelfUserRepository(data, api)
        }
    }
}
