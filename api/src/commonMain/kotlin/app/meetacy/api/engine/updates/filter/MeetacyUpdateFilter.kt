package app.meetacy.api.engine.updates.filter

import app.meetacy.api.engine.updates.MeetacyUpdate

public fun interface MeetacyUpdateFilter<T : MeetacyUpdate> {
    /**
     * @contract this method should only filter updates that are
     * type of [T]
     */
    public fun filter(update: MeetacyUpdate): Boolean
}
