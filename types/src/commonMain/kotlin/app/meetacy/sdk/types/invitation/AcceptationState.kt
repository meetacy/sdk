package app.meetacy.sdk.types.invitation

public sealed interface AcceptationState {
    public data object Accepted: AcceptationState
    public data object Waiting: AcceptationState
    public data object Declined: AcceptationState
}