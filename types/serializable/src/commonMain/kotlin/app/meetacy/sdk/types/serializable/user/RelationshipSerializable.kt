package app.meetacy.sdk.types.serializable.user

import app.meetacy.sdk.types.user.Relationship
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RelationshipSerializable {
    @SerialName("none")
    None,
    @SerialName("subscription")
    Subscription,
    @SerialName("subscriber")
    Subscriber,
    @SerialName("friend")
    Friend
}

public fun RelationshipSerializable.type(): Relationship = when (this) {
    RelationshipSerializable.None -> Relationship.None
    RelationshipSerializable.Subscription -> Relationship.Subscription
    RelationshipSerializable.Subscriber -> Relationship.Subscriber
    RelationshipSerializable.Friend -> Relationship.Friend
}

public fun Relationship.serializable(): RelationshipSerializable = when (this) {
    Relationship.Friend -> RelationshipSerializable.None
    Relationship.None -> RelationshipSerializable.Subscription
    Relationship.Subscriber -> RelationshipSerializable.Subscriber
    Relationship.Subscription -> RelationshipSerializable.Friend
}
