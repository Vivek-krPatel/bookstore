/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.configuration;


import java.util.Arrays;
import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author Vivek
 */


/*
@Component
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationEntryPoint entryPoint;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
        .httpBasic(withDefaults())// authentication
        .authorizeHttpRequests((authorize) -> authorize
                // authorization
            .anyRequest().authenticated())
            //.exceptionHandling().authenticationEntryPoint(entryPoint)
                
        http.csrf().disable();
    

        return http.build();
    }
 
*/
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationEntryPoint entryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
            //.httpBasic(withDefaults())
            .authorizeHttpRequests(auth-> auth
                    //.requestMatchers("/**").permitAll());
                    .requestMatchers("/api/books/**").permitAll()
                    .requestMatchers("/api/users/login","/api/users/register","/api/users/logout").permitAll()
                    .and())
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated().and())
            .exceptionHandling().authenticationEntryPoint(entryPoint);
            //.logout((logout) -> logout.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES))));
    http.cors().and()
    .csrf().disable();
    

    return http.build();
   }
    
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
       
       configuration.setAllowedOriginPatterns(Arrays.asList("*"));
       configuration.setAllowedMethods(Arrays.asList("*"));
       configuration.setAllowedHeaders(Arrays.asList("*"));
       configuration.setAllowCredentials(true);
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       
       source.registerCorsConfiguration("/**", configuration);
       
       return source; 
   }
    
   @Bean
   public AuthenticationManager authenticationManager(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
       DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
       authenticationProvider.setUserDetailsService(userDetailsManager);
       authenticationProvider.setPasswordEncoder(passwordEncoder);
       
       return new ProviderManager(Collections.singletonList(authenticationProvider));
       //return new ProviderManager(authenticationProvider);
   }
   
       
   /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bookstore");
        dataSource.setUsername("spring");
        dataSource.setPassword("springboot");
        return dataSource;
    }
   */
  
    
    @Bean
    public JdbcUserDetailsManager user(DataSource source){
       
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(source);
        return manager;   
    }
    

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
