package app.meetacy.sdk.types.serializable.search

import app.meetacy.sdk.types.search.SearchItem
import app.meetacy.sdk.types.serializable.meeting.serializable
import app.meetacy.sdk.types.serializable.meeting.type
import app.meetacy.sdk.types.serializable.place.serializable
import app.meetacy.sdk.types.serializable.place.type
import app.meetacy.sdk.types.serializable.user.serializable
import app.meetacy.sdk.types.serializable.user.type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import app.meetacy.sdk.types.serializable.place.PlaceSerializable as PlaceViewSerializable
import app.meetacy.sdk.types.serializable.meeting.MeetingSerializable as MeetingViewSerializable
import app.meetacy.sdk.types.serializable.user.UserSerializable as UserViewSerializable

@Serializable
public sealed interface SearchItemSerializable {
    @SerialName("meeting")
    @Serializable
    public class Meeting(public val meeting: MeetingViewSerializable) : SearchItemSerializable
    @SerialName("user")
    @Serializable
    public class User(public val user: UserViewSerializable) : SearchItemSerializable
    @SerialName("place")
    @Serializable
    public class Place(public val place: PlaceViewSerializable) : SearchItemSerializable
}

public fun SearchItem.serializable(): SearchItemSerializable = when (this) {
    is SearchItem.Meeting -> SearchItemSerializable.Meeting(meeting.serializable())
    is SearchItem.Place -> SearchItemSerializable.Place(place.serializable())
    is SearchItem.User -> SearchItemSerializable.User(user.serializable())
}

public fun SearchItemSerializable.type(): SearchItem = when (this) {
    is SearchItemSerializable.Meeting -> SearchItem.Meeting(meeting.type())
    is SearchItemSerializable.Place -> SearchItem.Place(place.type())
    is SearchItemSerializable.User -> SearchItem.User(user.type())
}
