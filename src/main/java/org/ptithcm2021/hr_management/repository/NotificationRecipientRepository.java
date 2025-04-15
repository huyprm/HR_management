package org.ptithcm2021.hr_management.repository;

import lombok.Setter;
import org.ptithcm2021.hr_management.model.NotificationRecipient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {
    @Query("select nr from NotificationRecipient nr where " +
            "nr.user.id = :userId ORDER BY nr.notification.sendDate DESC")
    List<NotificationRecipient> findTop5ByUserId(@Param("userId")long userId, Pageable pageable);

    @Query("select nr from NotificationRecipient nr where nr.user.id = :userId ")
    List<NotificationRecipient> finAllByUserId(@Param("userId")long userId);
}
