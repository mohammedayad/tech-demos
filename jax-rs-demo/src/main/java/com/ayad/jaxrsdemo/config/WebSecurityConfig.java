package com.ayad.jaxrsdemo.config;

import com.payconiq.shared.security.oauth2.jwt.servlet.CustomJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final CustomJwtAuthenticationConverter jwtAuthenticationConverter;


    @Value("${unsecured-paths}")
    private String[] unsecuredPaths;

    // Disable this Spring filter since it prevents Jersey to consume body sent with
    // Content-Type: application/x-www-form-urlencoded
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> registration() {
        FilterRegistrationBean<HiddenHttpMethodFilter> registration =
                new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(
                        sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    Arrays.stream(unsecuredPaths)
                            .forEach(path -> requests.requestMatchers(AntPathRequestMatcher.antMatcher(path)).permitAll());
                    requests.anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        server -> server.jwt(jwt -> jwt.decoder(jwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter))
                                );
        return http.build();
    }
}

