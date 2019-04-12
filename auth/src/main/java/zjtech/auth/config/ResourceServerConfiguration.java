/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.auth.config;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Autowired
  private ResourceServerTokenServices tokenService;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId("wzjresource")
        .tokenServices(tokenService);
  }

  /**
   * for resource security, you can specify the endpoint with a specific role within this method, or
   * you can explictly add the annotation on endpoint
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {
    //final
    http.anonymous().disable()
        .requestMatcher(new OAuthRequestedMatcher())
        .authorizeRequests()
        .antMatchers("/actuator/**", "/login", "/logout").permitAll()
        .antMatchers("/person/**").access("hasRole('read')")
        .anyRequest().authenticated()
        .and()
        .cors()
        .and()
        .csrf().ignoringAntMatchers("/person/**")
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

//    http.
//        anonymous().disable()
//        .authorizeRequests().antMatchers("/actuator/**").permitAll()
//        .antMatchers("/login").permitAll()
//        .requestMatchers().antMatchers("/person/**")
//        .anyRequest().authenticated();
    //.antMatchers("/person/**").access("hasRole('read')");
/*
    http.requestMatcher(new OAuthRequestedMatcher())
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .anyRequest().authenticated();*/
  }

  private static class OAuthRequestedMatcher implements RequestMatcher {

    public boolean matches(HttpServletRequest request) {
      String auth = request.getHeader("Authorization");
      // Determine if the client request contained an OAuth Authorization
      boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
      boolean haveAccessToken = request.getParameter("access_token") != null;
      return haveOauth2Token || haveAccessToken;
    }
  }


/*  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
//        .antMatcher("/v1.0/**")
        .authorizeRequests()
        .antMatchers("/v1.0/search/**").permitAll()
        .antMatchers("/v1.0/users/**").permitAll()
        .antMatchers("/v1.0/decisiongroups/**").permitAll()
        .antMatchers("/swagger**").permitAll()
        .anyRequest().authenticated()
        .and()
        .cors()
        .and()
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(STATELESS);
    // @formatter:on
  }*/

}