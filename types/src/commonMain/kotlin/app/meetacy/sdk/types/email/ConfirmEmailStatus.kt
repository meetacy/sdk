package app.meetacy.sdk.types.email

public sealed interface ConfirmEmailStatus {
    public data object Success : ConfirmEmailStatus
    public data object ExpiredLink : ConfirmEmailStatus
    public data object MaxAttemptsReached : ConfirmEmailStatus
}
