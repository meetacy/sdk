package app.meetacy.api.engine.updates.filter

import app.meetacy.api.engine.updates.MeetacyUpdate
import app.meetacy.api.engine.updates.SelfUserUpdate

public data object SelfUserUpdates : MeetacyUpdateFilter<SelfUserUpdate> {
    override fun filter(update: MeetacyUpdate): Boolean =
        update is SelfUserUpdate
}
