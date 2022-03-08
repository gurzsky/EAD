package com.ead.notification.hex.adapters.dtos;

import com.ead.notification.hex.core.domain.enums.NotificationStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NotificationDto {
    @NotNull
    private NotificationStatus notificationStatus;
}
