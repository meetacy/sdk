package app.meetacy.api.engine.updates

import app.meetacy.types.user.SelfUser

public data class SelfUserUpdate(
    override val id: UpdateId,
    val newUser: SelfUser
) : MeetacyUpdate
