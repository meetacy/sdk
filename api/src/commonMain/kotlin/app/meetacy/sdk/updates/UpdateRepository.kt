package app.meetacy.sdk.updates

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.notifications.NotificationRepository
import app.meetacy.sdk.types.update.Update
import app.meetacy.sdk.types.update.UpdateId

public sealed class UpdateRepository {
    public abstract val data: Update
    protected abstract val api: MeetacyApi

    public val id: UpdateId get() = data.id

    public class Notification(
        override val data: Update.Notification,
        override val api: MeetacyApi
    ) : UpdateRepository() {
        public val notification: NotificationRepository
            get() = NotificationRepository.of(data.notification, api)
    }

    public companion object {
        public fun of(data: Update, api: MeetacyApi): UpdateRepository {
            return when (data) {
                is Update.Notification -> Notification(data, api)
            }
        }
    }
}
