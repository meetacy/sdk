package app.meetacy.sdk.search

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.meetings.AuthorizedMeetingRepository
import app.meetacy.sdk.meetings.MeetingRepository
import app.meetacy.sdk.types.search.SearchItem
import app.meetacy.sdk.users.UserRepository
import app.meetacy.sdk.types.place.Place as PlaceData

/**
 * When modifying this class, corresponding classes should be altered:
 * - [AuthorizedMeetingRepository]
 */
public sealed class SearchItemRepository {
    public abstract val data: SearchItem

    public class Meeting(
        override val data: SearchItem.Meeting,
        private val api: MeetacyApi
    ) : SearchItemRepository() {
        public val meeting: MeetingRepository
            get() = MeetingRepository(data.meeting, api)
    }

    public class Place(
        override val data: SearchItem.Place
    ) : SearchItemRepository() {
        public val place: PlaceData
            get() = data.place
    }

    public class User(
        override val data: SearchItem.User,
        private val api: MeetacyApi
    ) : SearchItemRepository() {
        public val user: UserRepository
            get() = UserRepository.of(data.user, api)
    }

    public companion object {
        public fun of(data: SearchItem, api: MeetacyApi): SearchItemRepository {
            return when (data) {
                is SearchItem.Meeting -> Meeting(data, api)
                is SearchItem.Place -> Place(data)
                is SearchItem.User -> User(data, api)
            }
        }
    }
}
