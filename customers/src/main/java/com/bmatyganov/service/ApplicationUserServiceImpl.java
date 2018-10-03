package com.bmatyganov.service;

import java.util.Collections;

import javax.annotation.PostConstruct;

import com.bmatyganov.model.ApplicationUser;
import com.bmatyganov.repository.ApplicationUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationUserServiceImpl implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    protected void initialize() {
        save(new ApplicationUser("John", "Smith", "admin@test.com", "admin", "ROLE_ADMIN"));
        save(new ApplicationUser("Sara", "Conar", "user@test.com", "user", "ROLE_USER"));
    }

    @Transactional
    public ApplicationUser save(ApplicationUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserRepository.findOneByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return createUser(user);
    }

    public void signin(ApplicationUser user) {
        SecurityContextHolder.getContext().setAuthentication(authenticate(user));
    }

    private Authentication authenticate(ApplicationUser user) {
        return new UsernamePasswordAuthenticationToken(createUser(user), null,
                Collections.singleton(createAuthority(user)));
    }

    private User createUser(ApplicationUser user) {
        return new User(user.getEmail(), user.getPassword(), Collections.singleton(createAuthority(user)));
    }

    private GrantedAuthority createAuthority(ApplicationUser user) {
        return new SimpleGrantedAuthority(user.getRole());
    }
}
