package app.meetacy.api.updates

import app.meetacy.sdk.user.SelfUser

public data class SelfUserUpdate(val user: SelfUser) : MeetacyUpdate
