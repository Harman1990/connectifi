package com.connectifi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.connectifi.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenicationSuccessHandler handler;

    @Bean
    public DaoAuthenticationProvider  authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        
        // Set user details service
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        
        // Set password encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        
        return daoAuthenticationProvider;
    }   

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // configuration

        // urls configure kiay hai ki koun se public rangenge aur koun se private
        // rangenge
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");
            // formLogin.defaultSuccessUrl("/home");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");


        });

        // form default login
        // agar hame kuch bhi change karna hua to hama yaha ayenge: form login se
        // related
        // httpSecurity.formLogin(formLogin -> {

        //     //
        //     formLogin.loginPage("/login");
        //     formLogin.loginProcessingUrl("/authenticate");
        //     formLogin.successForwardUrl("/user/profile");
        //     // formLogin.failureForwardUrl("/login?error=true");
        //     // formLogin.defaultSuccessUrl("/home");
        //     formLogin.usernameParameter("email");
        //     formLogin.passwordParameter("password");

        //     // formLogin.failureHandler(new AuthenticationFailureHandler() {

        //     // @Override
        //     // public void onAuthenticationFailure(HttpServletRequest request,
        //     // HttpServletResponse response,
        //     // AuthenticationException exception) throws IOException, ServletException {
        //     // // TODO Auto-generated method stub
        //     // throw new UnsupportedOperationException("Unimplemented method
        //     // 'onAuthenticationFailure'");
        //     // }

        //     // });

        //     // formLogin.successHandler(new AuthenticationSuccessHandler() {

        //     // @Override
        //     // public void onAuthenticationSuccess(HttpServletRequest request,
        //     // HttpServletResponse response,
        //     // Authentication authentication) throws IOException, ServletException {
        //     // // TODO Auto-generated method stub
        //     // throw new UnsupportedOperationException("Unimplemented method
        //     // 'onAuthenticationSuccess'");
        //     // }

        //     // });
        //     formLogin.failureHandler(authFailtureHandler);

        // });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // // oauth configurations

        // httpSecurity.oauth2Login(oauth -> {
        //     oauth.loginPage("/login");
        //     oauth.successHandler(handler);
        // });

        // httpSecurity.oauth2Login(Customizer.withDefaults());

        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // httpSecurity.oauth2Login(oauth -> {
        //     oauth.loginPage("/login");
        //     oauth.successHandler(handler);
        // });

        //httpSecurity.oauth2Login(Customizer.withDefaults());

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        // httpSecurity.oauth2Login(Customizer.withDefaults());

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
