package app.meetacy.api.engine.updates

import app.meetacy.types.state.StateId

public sealed interface MeetacyUpdate {
    public val id: StateId
}
