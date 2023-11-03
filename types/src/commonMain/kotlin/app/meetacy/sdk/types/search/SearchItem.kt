package app.meetacy.sdk.types.search

import app.meetacy.sdk.types.meeting.Meeting as MeetingView
import app.meetacy.sdk.types.place.Place as PlaceView
import app.meetacy.sdk.types.user.User as UserView

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.search.SearchItemRepository]
 * - [app.meetacy.sdk.search.AuthorizedSearchItemRepository]
 */

public sealed interface SearchItem {
    public class Meeting(public val meeting: MeetingView) : SearchItem
    public class User(public val user: UserView) : SearchItem
    public class Place(public val place: PlaceView) : SearchItem
}
