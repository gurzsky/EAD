package com.ead.notification.hex.adapters.configs;

import com.ead.notification.hex.NotificationHexApplication;
import com.ead.notification.hex.core.ports.NotificationPersistencePort;
import com.ead.notification.hex.core.services.NotificationServicePortImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = NotificationHexApplication.class)
public class BeanConfiguration {

    @Bean
    public NotificationServicePortImpl notificationServicePortImpl(NotificationPersistencePort persistencePort) {
        return new NotificationServicePortImpl(persistencePort);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
