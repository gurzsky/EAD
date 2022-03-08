package com.ead.notification.hex.adapters.inbound.controllers;

import com.ead.notification.hex.adapters.dtos.NotificationDto;
import com.ead.notification.hex.core.domain.NotificationDomain;
import com.ead.notification.hex.core.domain.PageInfo;
import com.ead.notification.hex.core.ports.NotificationServicePort;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserNotificationController {

    final NotificationServicePort notificationServicePort;

    public UserNotificationController(NotificationServicePort notificationServicePort) {
        this.notificationServicePort = notificationServicePort;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationDomain>> notificationByUser(@PathVariable(value = "userId")UUID userId,
                                                                       @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC)Pageable pageable) {
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(pageable, pageInfo);
        List<NotificationDomain> notificationDomainList = notificationServicePort.findAllNotificationByUser(userId, pageInfo);
        return ResponseEntity.status(HttpStatus.OK).body(new PageImpl<NotificationDomain>(notificationDomainList, pageable, notificationDomainList.size()));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable(value = "userId") UUID userId,
                                                     @PathVariable(value = "notificationId") UUID notificationId,
                                                     @RequestBody @Validated NotificationDto notificationDto) {
        Optional<NotificationDomain> notificationModel = notificationServicePort.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModel.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found!");

        notificationModel.get().setNotificationStatus(notificationDto.getNotificationStatus());
        notificationServicePort.saveNotification(notificationModel.get());
        return ResponseEntity.status(HttpStatus.OK).body(notificationModel.get());
    }
}
