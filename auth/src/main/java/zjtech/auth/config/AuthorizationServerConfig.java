/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Value("${jwt.access.token.validity-seconds}")
  private int accessTokenValiditySeconds;

  @Value("${jwt.access.token.refresh-validity-seconds}")
  private int refreshTokenValiditySeconds;

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenEnhancer tokenEnhancer;

  private PasswordEncoder passwordEncoder = PasswordEncoderFactories
      .createDelegatingPasswordEncoder();

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security)
      throws Exception {
    security.passwordEncoder(passwordEncoder);
    security.checkTokenAccess("isAuthenticated()");
    security.tokenKeyAccess("passwordEncoder()");
//      security.realm();
  }


  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.tokenStore(tokenStore)
        .tokenEnhancer(tokenEnhancer)
        .userDetailsService(userDetailsService)
        .authenticationManager(this.authenticationManager);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
    /*    .withClient("clientapp")
        .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
        .authorities("ROLE_CLIENT")
        .scopes("read", "write")
        .resourceIds("restservice")
        .secret(passwordEncoder.encode("changeit"))
        .accessTokenValiditySeconds(accessTokenValiditySeconds)
        .and()*/
        .withClient("wzj")
        .authorizedGrantTypes("client_credentials", "password", "authorization_code",
            "refresh_token", "implicit")
        .scopes("read", "write")
        .resourceIds("wzjresource")
        .secret(passwordEncoder.encode("wzj"))
        .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
        .accessTokenValiditySeconds(accessTokenValiditySeconds);

  }

}
