package com.ead.notification.hex.core.services;

import com.ead.notification.hex.core.domain.NotificationDomain;
import com.ead.notification.hex.core.domain.PageInfo;
import com.ead.notification.hex.core.domain.enums.NotificationStatus;
import com.ead.notification.hex.core.ports.NotificationPersistencePort;
import com.ead.notification.hex.core.ports.NotificationServicePort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificationServicePortImpl implements NotificationServicePort {

    final NotificationPersistencePort notificationPersistencePort;

    public NotificationServicePortImpl(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @Override
    public NotificationDomain saveNotification(NotificationDomain notificationModel) {
        return notificationPersistencePort.save(notificationModel);
    }

    @Override
    public List<NotificationDomain> findAllNotificationByUser(UUID userId, PageInfo pageInfo) {
        return notificationPersistencePort.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageInfo);
    }

    @Override
    public Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationPersistencePort.findByNotificationIdAndUserId(notificationId, userId);
    }
}
