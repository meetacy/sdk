package app.meetacy.api.updates.filter

import app.meetacy.api.updates.MeetacyUpdate
import app.meetacy.api.updates.SelfUserUpdate

public data object SelfUserUpdates : MeetacyUpdateFilter  {
    override fun filter(update: MeetacyUpdate): Boolean =
        update is SelfUserUpdate
}
