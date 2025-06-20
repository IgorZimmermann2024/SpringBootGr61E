package de.ait.javalessons.utils;

import de.ait.javalessons.model.AppUser;
import de.ait.javalessons.repository.AppUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseUtil {

    private AppUserRepository appUserRepository;

    public DatabaseUtil(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostConstruct
    public void init() {
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("$2a$10$PrPtC1ZRO4opsclWLmYdSe.xE0YTH29sXsS55yr2bQ4LCuWYc3Tp6");
        user.setRole("ROLE_USER");
        appUserRepository.save(user);
        log.info("User {} created", user.getUsername());

        AppUser admin = new AppUser();
        admin.setUsername("admin");
        admin.setPassword("$2a$10$UMWsV/eJBbUyJJmqXXx8i.PO5Kmov0NOZTfDn/d6hq59vSafR4/tu");
        admin.setRole("ROLE_ADMIN");
        appUserRepository.save(admin);
        log.info("User {} created", admin.getUsername());
    }
}
