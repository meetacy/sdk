package app.meetacy.sdk.types.annotation

@Target(AnnotationTarget.CONSTRUCTOR)
@RequiresOptIn(
    message = "This constructor is not safe for usage. Consider to use a factory function instead of constructor to safely create an instance.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class UnsafeConstructor
