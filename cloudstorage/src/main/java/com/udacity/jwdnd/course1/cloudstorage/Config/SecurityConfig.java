package com.udacity.jwdnd.course1.cloudstorage.Config;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    // overrides the configure method which takes AuthenticationManagerProvider as an argument
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.authorizeRequests()
                .antMatchers("/sessions/signup").permitAll()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/sessions/login")
                .failureHandler(authenticationFailureHandler())
                //.failureForwardUrl("/sessions/login-error")
                .defaultSuccessUrl("/home",true)
                .permitAll();

        http.logout()
                .logoutUrl("/sessions/logout")
                .logoutSuccessUrl("/sessions/login-error")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();





    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (httpServletRequest, httpServletResponse, e) -> {

            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            httpServletResponse.sendRedirect("/login?error=true");
        };
    }


}
