package app.meetacy.sdk.friends.location

import app.meetacy.sdk.types.location.Location
import app.meetacy.sdk.types.user.RegularUser

public class FriendsOnMapRepository(
    public val friends: List<Friend>
) {
    public data class Friend(
        public val user: RegularUser,
        public val location: Location
    )
}
