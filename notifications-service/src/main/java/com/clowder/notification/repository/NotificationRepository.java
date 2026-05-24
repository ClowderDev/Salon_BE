package com.clowder.notification.repository;

import com.clowder.notification.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUserId(Long userId);

  List<Notification> findBySalonId(Long salonId);
}
