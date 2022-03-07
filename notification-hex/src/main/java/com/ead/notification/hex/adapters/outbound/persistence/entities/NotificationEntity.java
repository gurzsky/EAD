package com.ead.notification.hex.adapters.outbound.persistence.entities;

import com.ead.notification.hex.core.domain.enums.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_NOTIFICATIONS")
public class NotificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
}
