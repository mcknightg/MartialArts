package com.bluntsoftware.lib.security;

import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUserAuthority;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationPersistentTokenRepository;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationUserAuthorityRepository;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationUserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private ApplicationUserAuthorityRepository userAuthorityRepository;

    @Autowired
    private ApplicationPersistentTokenRepository persistentTokenRepository;

    public ApplicationUser getByLogin(final String login){
        HqlBuilder builder = new HqlBuilder(ApplicationUser.class).ieq("login",login);
        return userRepository.findOne(builder);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        ApplicationUser userFromDatabase = getByLogin(login);
        if (userFromDatabase == null) {
           throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        HqlBuilder hql = new HqlBuilder(ApplicationUserAuthority.class).eq("appuser",userFromDatabase);
        List<ApplicationUserAuthority> authorities = userAuthorityRepository.findAll(hql);

        for (ApplicationUserAuthority userAuthority : authorities) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userAuthority.getAuthority().getAuthority());
            grantedAuthorities.add(grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(lowercaseLogin, userFromDatabase.getPassword(),grantedAuthorities);
    }



}
