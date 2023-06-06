package app.meetacy.sdk.types.user

public sealed interface Relationship {
    public data object None: Relationship
    public data object Subscription: Relationship
    public data object Subscriber: Relationship
    public data object Friend: Relationship
}
