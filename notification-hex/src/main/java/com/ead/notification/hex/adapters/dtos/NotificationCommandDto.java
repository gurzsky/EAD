package com.ead.notification.hex.adapters.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationCommandDto {

    private String title;
    private String message;
    private UUID userId;
}
