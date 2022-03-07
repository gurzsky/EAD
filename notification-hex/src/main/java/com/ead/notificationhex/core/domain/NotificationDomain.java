package com.ead.notificationhex.core.domain;

import com.ead.notificationhex.core.domain.enums.NotificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationDomain {

    private UUID notificationId;
    private UUID userId;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private NotificationStatus notificationStatus;
}
