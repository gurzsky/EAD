package com.ead.notification.controllers;

import com.ead.notification.dtos.NotificationDto;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserNotificationController {

    final NotificationService notificationService;

    public UserNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> notificationByUser(@PathVariable(value = "userId")UUID userId,
                                                                      @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC)Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllNotificationByUser(userId, pageable));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable(value = "userId") UUID userId,
                                                     @PathVariable(value = "notificationId") UUID notificationId,
                                                     @RequestBody @Validated NotificationDto notificationDto) {
        Optional<NotificationModel> notificationModel = notificationService.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModel.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found!");

        notificationModel.get().setNotificationStatus(notificationDto.getNotificationStatus());
        notificationService.saveNotification(notificationModel.get());
        return ResponseEntity.status(HttpStatus.OK).body(notificationModel.get());
    }
}
