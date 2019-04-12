/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthConfig {

  @Value("${jwt.access.token.signing-key}")
  private String jwtAccessTokenConverterSigningKey;

  @Value("${jwt.access.token.validity-seconds}")
  private int accessTokenValiditySeconds;

  @Bean
  @Primary
  public UserDetailsService userDetailsServiceBean() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserDetails greg = User.builder()
//		UserDetails greg = User.withDefaultPasswordEncoder()
        .username("greg")
        .password(encoder.encode("turnquist"))
        .roles("read")
        .build();
    return new InMemoryUserDetailsManager(greg);
  }


  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices tokenServices = new DefaultTokenServices();
    tokenServices.setTokenStore(tokenStore());
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
    return tokenServices;
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

    converter.setSigningKey(jwtAccessTokenConverterSigningKey);

    DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
    DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
    userTokenConverter.setUserDetailsService(userDetailsServiceBean());
    accessTokenConverter.setUserTokenConverter(userTokenConverter);

    converter.setAccessTokenConverter(accessTokenConverter);

    return converter;
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }


}