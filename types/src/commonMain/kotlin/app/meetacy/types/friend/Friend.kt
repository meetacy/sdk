package app.meetacy.types.friend

import app.meetacy.types.user.RegularUser

public data class Friend(
    public val ordinal: Int,
    public val user: RegularUser
)
