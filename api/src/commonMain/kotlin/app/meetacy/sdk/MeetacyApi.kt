package app.meetacy.sdk

import app.meetacy.sdk.auth.AuthApi
import app.meetacy.sdk.engine.MeetacyRequestsEngine
import app.meetacy.sdk.engine.requests.GetMeRequest
import app.meetacy.sdk.engine.requests.SearchRequest
import app.meetacy.sdk.files.FilesApi
import app.meetacy.sdk.friends.FriendsApi
import app.meetacy.sdk.invitations.InvitationsApi
import app.meetacy.sdk.meetings.MeetingsApi
import app.meetacy.sdk.notifications.NotificationsApi
import app.meetacy.sdk.search.SearchItemRepository
import app.meetacy.sdk.types.annotation.UnsafeConstructor
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.user.SelfUser
import app.meetacy.sdk.updates.UpdatesApi
import app.meetacy.sdk.users.UsersApi

public class MeetacyApi(
    public val engine: MeetacyRequestsEngine
) {
    public val files: FilesApi = FilesApi(api = this)
    public val auth: AuthApi = AuthApi(api = this)
    public val friends: FriendsApi = FriendsApi(api = this)
    public val users: UsersApi = UsersApi(api = this)
    public val meetings: MeetingsApi = MeetingsApi(api = this)
    public val invitations: InvitationsApi = InvitationsApi(api = this)
    public val notifications: NotificationsApi = NotificationsApi(api = this)
    public val updates: UpdatesApi = UpdatesApi(api = this)

    public suspend fun getMe(token: Token): SelfUser {
        return engine.execute(GetMeRequest(token)).me
    }

    public suspend fun search(
        token: Token,
        location: Location,
        prompt: String
    ): List<SearchItemRepository> {
        return engine.execute(
            request = SearchRequest(
                token, location, prompt
            )
        ).items.map { searchItem ->
            SearchItemRepository.of(searchItem, api = this)
        }
    }

    @OptIn(UnsafeConstructor::class)
    public fun authorized(token: Token): AuthorizedMeetacyApi {
        return AuthorizedMeetacyApi(token, base = this)
    }



    public companion object
}
