package com.ead.notification.hex.core.ports;

import com.ead.notification.hex.core.domain.NotificationDomain;
import com.ead.notification.hex.core.domain.PageInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationServicePort {
    NotificationDomain saveNotification(NotificationDomain notificationDomain);
    List<NotificationDomain> findAllNotificationByUser(UUID userId, PageInfo pageInfo);
    Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
}
