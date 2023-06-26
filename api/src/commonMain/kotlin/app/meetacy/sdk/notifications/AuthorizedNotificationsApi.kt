package app.meetacy.sdk.notifications

import app.meetacy.sdk.AuthorizedMeetacyApi
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.mapItems

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.notifications.AuthorizedNotificationRepository]
 */
public class AuthorizedNotificationsApi(
    private val api: AuthorizedMeetacyApi
) {
    public val token: Token get() = api.token
    public val base: NotificationsApi get() = api.base.notifications

    public suspend fun read(lastNotificationId: NotificationId) {
        base.read(token, lastNotificationId)
    }

    public suspend fun list(
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<AuthorizedNotificationRepository> {
        return base.list(token, amount, pagingId).mapItems { notification ->
            AuthorizedNotificationRepository.of(
                data = notification.data,
                api = api
            )
        }
    }
}
