package app.meetacy.api.engine.updates

import app.meetacy.types.update.UpdateId

public sealed interface MeetacyUpdate {
    public val id: UpdateId
}
