package app.meetacy.sdk.notifications

import app.meetacy.sdk.MeetacyApi
import app.meetacy.sdk.engine.requests.ListNotificationsRequest
import app.meetacy.sdk.engine.requests.ReadNotificationRequest
import app.meetacy.sdk.types.amount.Amount
import app.meetacy.sdk.types.auth.Token
import app.meetacy.sdk.types.notification.NotificationId
import app.meetacy.sdk.types.paging.PagingId
import app.meetacy.sdk.types.paging.PagingRepository
import app.meetacy.sdk.types.paging.PagingSource

/**
 * When modifying this class, corresponding classes should be altered:
 * - [app.meetacy.sdk.notifications.AuthorizedNotificationsApi]
 * - [app.meetacy.sdk.notifications.NotificationRepository]
 */
public class NotificationsApi(
    public val api: MeetacyApi
) {
    public suspend fun list(
        token: Token,
        amount: Amount,
        pagingId: PagingId? = null
    ): PagingRepository<NotificationRepository> = PagingRepository(
        amount = amount,
        startPagingId = pagingId
    ) { currentAmount, currentPagingId ->
        api.engine.execute(
            request = ListNotificationsRequest(
                token = token,
                amount = currentAmount,
                pagingId = currentPagingId
            )
        ).paging.mapItems { notification ->
            NotificationRepository.of(notification, api)
        }
    }

    public fun paging(
        token: Token,
        chunkSize: Amount,
        startPagingId: PagingId? = null,
        limit: Amount? = null
    ): PagingSource<NotificationRepository> {
        return PagingSource(
            chunkSize, startPagingId, limit
        ) { currentAmount, currentPagingId ->
            list(token, currentAmount, currentPagingId).response
        }
    }

    public suspend fun read(
        token: Token,
        lastNotificationId: NotificationId
    ) {
        api.engine.execute(
            request = ReadNotificationRequest(
                token = token,
                lastNotificationId = lastNotificationId
            )
        )
    }
}
