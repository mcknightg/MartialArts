package com.bluntsoftware.app.config;

import com.bluntsoftware.lib.security.*;
import com.bluntsoftware.lib.social.SimpleSocialUserDetailsService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@ComponentScan({"com.bluntsoftware.lib.security"})
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Autowired
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RememberMeServices rememberMeServices;

    @Bean
        public SocialUserDetailsService socialUsersDetailService() {
        return new SimpleSocialUserDetailsService(userDetailsService());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("user").password("password").roles("USER");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
            web.ignoring()
            .antMatchers("/bower_components/**")
            .antMatchers("/components/**")
            .antMatchers("/index.html")
            .antMatchers("/favicon.ico")
            .antMatchers("/i18n/**")
            .antMatchers("/fonts/**")
            .antMatchers("/images/**")
            .antMatchers("/img/**")
            .antMatchers("/scripts/**")
            .antMatchers("/styles/**")
            .antMatchers("/lacet/**")
            .antMatchers("/view/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
                .and()
            .rememberMe()
                .rememberMeServices(rememberMeServices)
                .key("cool2bkind")
                .and()
            .formLogin()
                .loginProcessingUrl("/app/authentication")
                .successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler)
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/app/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .csrf()
                .disable()
            .headers()
                .frameOptions().disable()
            .authorizeRequests()
                .antMatchers("/user_manager/register").permitAll()
                .antMatchers("/user_manager/activate").permitAll()
                .antMatchers("/user_manager/resetpassword").permitAll()
                .antMatchers("/user_manager/changepassword").permitAll()
                .antMatchers("/mongo/*/pages").permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/user_manager/applicationAuthority/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/user_manager/applicationPersistentToken/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/user_manager/applicationUserAuthority/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/user_manager/applicationUser/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/user_manager/appPassResetToken/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/**").authenticated()
                .antMatchers("/#/**").authenticated()
                //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
            .and()
                .apply(new SpringSocialConfigurer()
                .postLoginUrl("/#/index/dashboard")
                .alwaysUsePostLoginUrl(true));

    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }
}