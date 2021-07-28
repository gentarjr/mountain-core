package com.mountain.config;

import com.mountain.spring.security.ApiAccessDeniedHandler;
import com.mountain.spring.security.ApiUserAuthEntryPoint;
import com.mountain.spring.security.ApiUserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiUserConfigurationConfig extends WebSecurityConfigurerAdapter {

    private final ApiUserAuthEntryPoint apiAuthEntryPoint;
    private final ApiAccessDeniedHandler apiAccessDeniedHandler;

    @Bean
    public ApiUserAuthenticationFilter authenticationUserFilter() {
        return new ApiUserAuthenticationFilter();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/*/users/**", "/*/mountain/**")
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(apiAuthEntryPoint)
                .accessDeniedHandler(apiAccessDeniedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/v1/users/auth/**").permitAll()
                .antMatchers("/v1/mountain/auth/**").permitAll()
                .antMatchers("/v1/users/*/logout").permitAll()
                .antMatchers("/v1/users/register/**").permitAll()
                .antMatchers("/v1/img/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationUserFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
