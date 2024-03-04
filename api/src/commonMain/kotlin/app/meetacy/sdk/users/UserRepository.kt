package app.meetacy.sdk.users

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.user.*

public sealed interface UserRepository {
    public val data: User

    public val isSelf: Boolean
    public val id: UserId
    public val email: Email?
    public val nickname: String
    public val emailVerified: Boolean?
    public val username: Username?
    public val relationship: Relationship?
    public val avatar: FileRepository?

    public suspend fun details(token: Token): UserDetailsRepository

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
