package com.shashi.config;

import com.shashi.entity.Admin;
import com.shashi.entity.Customer;
import com.shashi.repository.AdminRepository;
import com.shashi.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder bcrypt = 
                    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword == null || rawPassword == null) return false;
                // Support legacy demo plain text passwords (e.g. "shashi", "admin") or BCrypt hashes
                if (rawPassword.toString().equals(encodedPassword)) {
                    return true;
                }
                try {
                    return bcrypt.matches(rawPassword, encodedPassword);
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepo, AdminRepository adminRepo) {
        return username -> {
            String clean = username.trim().toLowerCase();
            Optional<Admin> optAdmin = adminRepo.findByMailIdIgnoreCase(clean);
            if (optAdmin.isPresent()) {
                Admin admin = optAdmin.get();
                return User.withUsername(admin.getMailId())
                        .password(admin.getPword())
                        .roles("ADMIN")
                        .build();
            }

            Optional<Customer> optCust = customerRepo.findByMailIdIgnoreCase(clean);
            if (optCust.isPresent()) {
                Customer cust = optCust.get();
                return User.withUsername(cust.getMailId())
                        .password(cust.getPword())
                        .roles("CUSTOMER")
                        .build();
            }

            throw new UsernameNotFoundException("User not found: " + username);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/UserLogin.html",
                    "/AdminLogin.html",
                    "/UserRegister.html",
                    "/login",
                    "/userlogin",
                    "/adminlogin",
                    "/register",
                    "/userreg",
                    "/UserHome_Css.css",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()
                .requestMatchers("/admin/**", "/adminhome", "/adminviewtrainfwd", "/addtrainfwd", "/cancletrainfwd", "/updatetrain").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/index.html")
                .loginProcessingUrl("/userlogin")
                .usernameParameter("uname")
                .passwordParameter("pword")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    if (isAdmin) {
                        response.sendRedirect("/adminhome");
                    } else {
                        response.sendRedirect("/userhome");
                    }
                })
                .failureUrl("/index.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/userlogout")
                .logoutSuccessUrl("/index.html?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
