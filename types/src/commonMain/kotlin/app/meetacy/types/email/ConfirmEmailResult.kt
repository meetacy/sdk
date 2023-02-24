package app.meetacy.types.email

public sealed interface ConfirmEmailResult {
    public data object Success : ConfirmEmailResult
    public data object ExpiredLink : ConfirmEmailResult
    public data object MaxAttemptsReached : ConfirmEmailResult
}
