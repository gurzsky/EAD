package com.ead.notification.services;

import com.ead.notification.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {
    NotificationModel saveNotification(NotificationModel notificationModel);
    Page<NotificationModel> findAllNotificationByUser(UUID userId, Pageable pageable);
}
