// package com.example.demoaad;

// import com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter;
// import com.azure.spring.aad.webapp.AADWebSecurityConfigurerAdapter;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
// public class AADWebApplicationAndResourceServerConfig {

//     @Order(1)
//     @Configuration
//     public static class ApiWebSecurityConfigurationAdapter extends AADResourceServerWebSecurityConfigurerAdapter {
//         protected void configure(HttpSecurity http) throws Exception {
//             super.configure(http);
//             http.antMatcher("/api/**")
//                 .authorizeRequests().anyRequest().authenticated();
//         }
//     }

//     @Configuration
//     public static class HtmlWebSecurityConfigurerAdapter extends AADWebSecurityConfigurerAdapter {

//         @Override
//         protected void configure(HttpSecurity http) throws Exception {
//             super.configure(http);
//             // @formatter:off
//             http.authorizeRequests()
//                     .antMatchers("/login").permitAll()
//                     .anyRequest().authenticated();
//             // @formatter:on
//         }
//     }
// }