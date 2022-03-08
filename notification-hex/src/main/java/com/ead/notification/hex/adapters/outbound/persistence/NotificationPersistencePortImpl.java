package com.ead.notification.hex.adapters.outbound.persistence;

import com.ead.notification.hex.adapters.outbound.persistence.entities.NotificationEntity;
import com.ead.notification.hex.core.domain.NotificationDomain;
import com.ead.notification.hex.core.domain.PageInfo;
import com.ead.notification.hex.core.domain.enums.NotificationStatus;
import com.ead.notification.hex.core.ports.NotificationPersistencePort;
import com.netflix.discovery.converters.Auto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationPersistencePortImpl implements NotificationPersistencePort {

    @Autowired
    private NotificationJpaRepository notificationJpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NotificationDomain save(NotificationDomain notificationDomain) {
        NotificationEntity notificationEntity = notificationJpaRepository.save(modelMapper.map(notificationDomain, NotificationEntity.class));
        return modelMapper.map(notificationEntity, NotificationDomain.class);
    }

    @Override
    public List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());
        return notificationJpaRepository.findAllByUserIdAndNotificationStatus(userId, notificationStatus, pageable)
                .stream()
                .map(entity -> modelMapper.map(entity, NotificationDomain.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        Optional<NotificationEntity> notificationEntity =  notificationJpaRepository.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationEntity.isPresent())
            return Optional.of(modelMapper.map(notificationEntity.get(), NotificationDomain.class));

        return Optional.empty();
    }
}
