package app.meetacy.api.updates.filter

import app.meetacy.api.updates.MeetacyUpdate

public fun interface MeetacyUpdateFilter {
    public fun filter(update: MeetacyUpdate): Boolean
}
