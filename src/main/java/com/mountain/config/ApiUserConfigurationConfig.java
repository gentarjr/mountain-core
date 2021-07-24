package com.mountain.config;

import com.mountain.spring.principal.PrincipalServiceImpl;
import com.mountain.spring.security.ApiAccessDeniedHandler;
import com.mountain.spring.security.ApiUserAuthEntryPoint;
import com.mountain.spring.security.ApiUserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
@RequiredArgsConstructor
public class ApiUserConfigurationConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalServiceImpl principalServiceImpl;
    private final ApiUserAuthEntryPoint apiAuthEntryPoint;
    private final ApiAccessDeniedHandler apiAccessDeniedHandler;

    @Bean
    public ApiUserAuthenticationFilter authenticationUserFilter() {
        return new ApiUserAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(principalServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Bean(name = "userAuthManager")
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
                .antMatchers("/*/users/**", "/*/users/**")
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(apiAuthEntryPoint)
                .accessDeniedHandler(apiAccessDeniedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/v1/users/auth/**").permitAll()
                .antMatchers("/v1/mountain/auth/**").permitAll()
                .antMatchers("/v1/users/*/logout").permitAll()
                .antMatchers("/v1/mountain/*/logout").permitAll()
                .antMatchers("/v1/users/register/**").permitAll()
                .antMatchers("/v1/mountain/register/**").permitAll()
                .antMatchers("/v1/users/**").hasAnyAuthority("USER")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationUserFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
