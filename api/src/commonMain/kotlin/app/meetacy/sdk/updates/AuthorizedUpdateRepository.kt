package app.meetacy.sdk.updates

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.notifications.AuthorizedNotificationRepository
import app.meetacy.sdk.types.update.Update

public sealed class AuthorizedUpdateRepository {
    public abstract val data: Update
    protected abstract val api: AuthorizedMeetacyApi

    public val base: UpdateRepository get() = UpdateRepository.of(data, api.base)

    public class Notification(
        override val data: Update.Notification,
        override val api: AuthorizedMeetacyApi
    ) : AuthorizedUpdateRepository() {
        public val notification: AuthorizedNotificationRepository
            get() = AuthorizedNotificationRepository.of(data.notification, api)
    }

    public companion object {
        public fun of(data: Update, api: AuthorizedMeetacyApi): AuthorizedUpdateRepository =
            when (data) {
                is Update.Notification -> Notification(data, api)
            }
    }
}
