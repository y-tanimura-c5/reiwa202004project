package com.cfiv.sysdev.rrs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * パスワード認証
     * @param auth 認証管理
     * @throws Exception
     */
    @Autowired
    void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 認証設定
     * @param web 認証対象
     */
    @Override
    protected void configure(HttpSecurity web) throws Exception {
        web.formLogin().loginPage("/login").successForwardUrl("/").failureUrl("/login-error").permitAll();
        web.authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**").permitAll().anyRequest().authenticated();

        // サーバー証明書ページ認証用
        // web.authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**", "/.well-known/**").permitAll().anyRequest().authenticated();

        web.logout().logoutSuccessUrl("/login").permitAll();
        web.csrf().disable();
    }

    /**
     * パスワード暗号アルゴリズム
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
