/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.auth.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Value("${logout.success.url}")
  private String logoutSuccessUrl;

  PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .anyRequest().authenticated()
        .and().httpBasic()
        .and().csrf().disable();

    http
        .cors()
        .and()
        .csrf().ignoringAntMatchers("/v1.0/**", "/logout")
        .and()
        .authorizeRequests()

        .antMatchers("/oauth/authorize").authenticated()
        //Anyone can access the urls
        .antMatchers("/actuator/health").permitAll()
//        .antMatchers("/actuator/**").hasAuthority(Permission.READ_ACTUATOR_DATA)
        .antMatchers("/actuator/**").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
//        .and().httpBasic()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=true")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl(logoutSuccessUrl)
        .permitAll();
  }

/*
  @Override
  protected void configure(HttpSecurity http) throws Exception {
*//*
    http
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .anyRequest().authenticated()
        .and().httpBasic()
        .and().csrf().disable();

*//*
    http
        .cors()
        .and()
        .csrf().ignoringAntMatchers("/v1.0/**", "/logout")
        .and()
        .authorizeRequests()

        .antMatchers("/oauth/authorize").authenticated()
        //Anyone can access the urls
        .antMatchers("/actuator/health").permitAll()
//        .antMatchers("/actuator/**").hasAuthority(Permission.READ_ACTUATOR_DATA)
        .antMatchers("/actuator/**").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
//        .and().httpBasic()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .failureUrl("/login?error=true")
        .usernameParameter("username")
        .passwordParameter("password")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl(logoutSuccessUrl)
        .permitAll();
  }*/

  /**
   * Configures the authentication manager bean which processes authentication requests.
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration
        .setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
