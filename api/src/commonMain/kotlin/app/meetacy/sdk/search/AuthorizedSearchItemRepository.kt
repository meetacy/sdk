package app.meetacy.sdk.search

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.types.search.SearchItem
import app.meetacy.sdk.users.AuthorizedUserRepository
import app.meetacy.sdk.types.place.Place as PlaceData

public sealed class AuthorizedSearchItemRepository {
    public abstract val data: SearchItem
    public abstract val base: SearchItemRepository

    public class Meeting(
        override val data: SearchItem.Meeting,
        public val api: AuthorizedMeetacyApi
    ) : AuthorizedSearchItemRepository() {
        override val base: SearchItemRepository
            get() = SearchItemRepository.Meeting(data, api.base)
        public val meeting: AuthorizedMeetingRepository
            get() = AuthorizedMeetingRepository(data.meeting, api)
    }

    public class Place(
        override val data: SearchItem.Place,
        public val api: AuthorizedMeetacyApi
    ) : AuthorizedSearchItemRepository() {
        override val base: SearchItemRepository
            get() = SearchItemRepository.Place(data)

        public val place: PlaceData
            get() = data.place
    }

    public class User(
        override val data: SearchItem.User,
        public val api: AuthorizedMeetacyApi
    ) : AuthorizedSearchItemRepository() {

        override val base: SearchItemRepository
            get() = SearchItemRepository.User(data, api.base)
        public val user: AuthorizedUserRepository
            get() = AuthorizedUserRepository.of(data.user, api)
    }

    public companion object {
        public fun of(data: SearchItem, api: AuthorizedMeetacyApi): AuthorizedSearchItemRepository =
            when (data) {
                is SearchItem.Meeting -> Meeting(data, api)
                is SearchItem.Place -> Place(data, api)
                is SearchItem.User -> User(data, api)
            }

    }
}
