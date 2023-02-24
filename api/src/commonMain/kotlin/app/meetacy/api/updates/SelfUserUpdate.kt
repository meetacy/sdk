package app.meetacy.api.updates

import app.meetacy.types.user.SelfUser

public data class SelfUserUpdate(val user: SelfUser) : MeetacyUpdate
