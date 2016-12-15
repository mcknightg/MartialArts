package com.bluntsoftware.lib.social;

import com.bluntsoftware.app.modules.user_manager.domain.ApplicationAuthority;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUser;
import com.bluntsoftware.app.modules.user_manager.domain.ApplicationUserAuthority;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationAuthorityRepository;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationUserAuthorityRepository;
import com.bluntsoftware.app.modules.user_manager.repository.ApplicationUserRepository;
import com.bluntsoftware.lib.security.AuthoritiesConstants;
import com.bluntsoftware.lib.jpa.repository.support.HqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

/**
 * Created by Alex Mcknight on 7/6/2016.
 */
@Component
public class AccountConnectionSignUpService implements ConnectionSignUp {

    private static final Logger LOG = LoggerFactory.getLogger(AccountConnectionSignUpService.class);
    @Autowired
    ApplicationAuthorityRepository authorityRepository;
    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    ApplicationUserAuthorityRepository applicationUserAuthorityRepository;
    @Override
    public String execute(Connection<?> connection) {
       // connection.getDisplayName()
        ConnectionKey connectionKey = connection.getKey();
        String providerId = connectionKey.getProviderId();
        UserProfile userProfile =  connection.fetchUserProfile();
        String email = userProfile.getEmail();
        HqlBuilder builder = new HqlBuilder(ApplicationUser.class).eq("login",email);
        ApplicationUser user = applicationUserRepository.findOne(builder);

       if(user == null){
           user = new ApplicationUser();
           user.setActivated(false);
           user.setLogin(email);
           user.setLangKey("en");
           user.setFirstName(userProfile.getFirstName());
           user.setLastName(userProfile.getLastName());
           user.setPassword(email);
           user.setEmail(email);
           ApplicationAuthority authority = findAuthorityByRole(AuthoritiesConstants.USER);
           if(authority == null){
               authority = new ApplicationAuthority();
               authority.setAuthority(AuthoritiesConstants.USER);
           }
           ApplicationUserAuthority userAuthority = new ApplicationUserAuthority();
           userAuthority.setAuthority(authority);
           userAuthority.setAppuser(user);
           //should we save the user authority ?
           //applicationUserAuthorityRepository.saveAndUpdate(userAuthority);
        }
        if(providerId.equalsIgnoreCase("google")){
            user.setGoogleId(userProfile.getUsername());
        }
        if(providerId.equalsIgnoreCase("linkedin")){
            user.setLinkedinId(userProfile.getEmail());
        }

        applicationUserRepository.saveAndUpdate(user);
        return user.getLogin();
    }
    ApplicationAuthority findAuthorityByRole(String role){
        HqlBuilder builder = new HqlBuilder(ApplicationAuthority.class).eq("authority",role);
        return authorityRepository.findOne(builder);
    }
}
