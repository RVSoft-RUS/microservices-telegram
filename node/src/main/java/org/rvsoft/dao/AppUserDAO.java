package org.rvsoft.dao;

import org.rvsoft.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}
