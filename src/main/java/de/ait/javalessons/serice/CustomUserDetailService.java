package de.ait.javalessons.serice;

import de.ait.javalessons.model.AppUser;
import de.ait.javalessons.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new User(appUser.getUsername(), appUser.getPassword(),List.of(() -> "ROLE_" + appUser.getRole()));
        }

}
