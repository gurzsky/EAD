package com.ead.notification.hex.core.ports;

import com.ead.notification.hex.core.domain.NotificationDomain;
import com.ead.notification.hex.core.domain.enums.NotificationStatus;
import com.ead.notification.hex.core.domain.PageInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationPersistencePort {

    NotificationDomain save(NotificationDomain notificationDomain);
    List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, PageInfo pageInfo);
    Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
}
