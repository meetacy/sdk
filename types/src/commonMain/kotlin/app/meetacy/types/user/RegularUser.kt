package app.meetacy.types.user

public data class RegularUser(
    override val id: UserId,
    override val nickname: String
) : User
