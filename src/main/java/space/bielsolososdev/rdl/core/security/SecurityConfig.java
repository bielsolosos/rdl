package space.bielsolososdev.rdl.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.domain.users.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) 
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] API_PUBLIC_ROUTES = {
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] WEB_PUBLIC_ROUTES = {
            "/",
            "/login",
            "/register",
            "/error/**",
            "/r/**", // Redirecionamento público
            "/css/**",
            "/js/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**"
    };

    private static final String[] ADMIN_ROUTES = {
            "/admin/**"
    };

    private static final String[] WEB_AUTHENTICATED_ROUTES = {
            "/urls/**",
            "/profile/**"
    };

    /**
     * Configuração específica para a API REST.
     * - Stateless (sem sessão)
     * - CSRF desabilitado
     * - Autenticação HTTP Basic
     * 
     * @param http
     * @return SecurityFilterChain para API
     * @throws Exception
     */
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_PUBLIC_ROUTES).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    /**
     * Configuração para rotas Web (Thymeleaf).
     * - Sessões habilitadas
     * - CSRF habilitado (proteção contra ataques CSRF em formulários)
     * - Form Login para autenticação
     * 
     * @param http
     * @return SecurityFilterChain para Web
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/r/**")) // Ignora CSRF apenas para redirects públicos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WEB_PUBLIC_ROUTES).permitAll()
                        .requestMatchers(ADMIN_ROUTES).hasRole("ADMIN")
                        .requestMatchers(WEB_AUTHENTICATED_ROUTES).authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/urls", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }

    /**
     * PasswordEncoder para criptografar senhas usando BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provedor de autenticação que usa o CustomUserDetailsService.
     * - Busca usuário no banco via UserDetailsService
     * - Valida senha usando PasswordEncoder
     */
    @Bean
    @SuppressWarnings("deprecation")
    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Gerenciador de autenticação usado nos controllers.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
