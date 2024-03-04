package app.meetacy.sdk.users

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.files.FileRepository
import app.meetacy.sdk.types.email.Email
import app.meetacy.sdk.types.file.FileId
import app.meetacy.sdk.types.user.*

public sealed interface AuthorizedUserRepository {
    public val data: User
    public val base: UserRepository

    public val id: UserId
    public val email: Email?
    public val nickname: String
    public val emailVerified: Boolean?
    public val username: Username?
    public val relationship: Relationship?
    public val avatar: FileRepository?

    public suspend fun details(): AuthorizedUserDetailsRepository

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
