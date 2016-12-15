package com.bluntsoftware.app.config;
/**
 * Created by Alex Mcknight on 7/6/2016.
 */

import com.bluntsoftware.lib.social.AccountConnectionSignUpService;
import com.bluntsoftware.lib.social.google.GoogleContact;
import com.bluntsoftware.lib.social.google.GoogleContactFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;

import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInConnections;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
@ComponentScan(basePackages = {"com.bluntsoftware.lib.social"})
public class SocialContext implements SocialConfigurer, EnvironmentAware {

    Environment environment;

     @Autowired
     private AccountConnectionSignUpService accountConnectionSignUpService;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(
                env.getProperty("twitter.consumer.key"),
                env.getProperty("twitter.consumer.secret")
        ));

        cfConfig.addConnectionFactory(new FacebookConnectionFactory(
                env.getProperty("facebook.app.id"),
                env.getProperty("facebook.app.secret")
        ));

        GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(
                env.getProperty("google.consumerKey"),
                env.getProperty("google.consumerSecret"));
        googleConnectionFactory.setScope("profile https://www.googleapis.com/auth/contacts");
        cfConfig.addConnectionFactory(googleConnectionFactory);

        LinkedInConnectionFactory linkedInConnectionFactory = new LinkedInConnectionFactory(
                env.getProperty("linkedin.app.id"),
                env.getProperty("linkedin.app.secret"));
        linkedInConnectionFactory.setScope("public");
        cfConfig.addConnectionFactory(linkedInConnectionFactory);


    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
         repository.setConnectionSignUp(accountConnectionSignUpService);

        return repository;
    }
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Twitter twitter(ConnectionRepository repository) {
        Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public LinkedIn linkedin(ConnectionRepository repository) {
        Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public Google google(ConnectionRepository repository) {
        Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        return connection != null ? connection.getApi() : null;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public GoogleContact googleContact(ConnectionRepository repository) {
        Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        if(connection != null){
            Google google = connection.getApi();
            GoogleContactFactory factory = new GoogleContactFactory();
            String clientId = environment.getProperty("google.consumerKey");
            String clientSecret = environment.getProperty("google.consumerSecret");
            String accessToken =  google.getAccessToken();
            return factory.getApi(accessToken,clientId,clientSecret);
        }
        return null;
    }


    @Bean
    public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
        return new ReconnectFilter(usersConnectionRepository, userIdSource);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
