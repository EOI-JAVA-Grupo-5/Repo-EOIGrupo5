package com.eoi.grupo5.config;

import com.eoi.grupo5.repositories.ListaRepository;
import com.eoi.grupo5.security.UsuarioLoginCorrectoHandler;
import com.eoi.grupo5.services.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Utiliza las anotaciones de Spring Security para definir y personalizar
 * la seguridad de la aplicación, incluyendo la configuración de solicitudes
 * HTTP, autenticación y control de acceso.
 *
 * @Configuration Marca esta clase como una clase de configuración.
 * @EnableWebSecurity Habilita la seguridad web de Spring Security en la aplicación.
 *
 * @Author No se especificó autor.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final Environment environment;

    private final UsuarioLoginCorrectoHandler loginSuccessHandler;

    public SecurityConfig(Environment environment, UsuarioLoginCorrectoHandler loginSuccessHandler) {
        this.environment = environment;
        this.loginSuccessHandler = loginSuccessHandler;
    }


    /**
     * Método que configura un {@link UserDetailsService} para la autenticación en memoria.
     * Este método crea un usuario en memoria utilizando un nombre de usuario y contraseña
     * definidos en las propiedades de configuración `spring.security.user.name` y
     * `spring.security.user.password`, o valores predeterminados si no están configurados.
     *
     * <p>Nota: Se utiliza el identificador `{noop}` en la contraseña para evitar el uso
     * de un encoder, ideal solo para pruebas. No debe ser utilizado en un entorno de producción.</p>
     *
     * @return Un {@link InMemoryUserDetailsManager} que contiene los detalles del usuario configurado.
     *
     * @Author No se especificó autor.
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        String name = environment.getProperty("spring.security.user.name", "user");
//        String password = environment.getProperty("spring.security.user.password", "password");
//
//        var user = User.withUsername(name)
//                .password("{noop}" + password) // {noop} indica que no se usa encoder para simplificar (solo pruebas)
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Configura una cadena de filtros de seguridad para gestionar la seguridad HTTP de la aplicación.
     * Permite personalizar los comportamientos de seguridad como protección CSRF, autenticación básica,
     * inicio de sesión con formulario y control de acceso a las solicitudes HTTP.
     *
     * <p>Este método define, entre otras configuraciones:
     * <ul>
     *   <li>Deshabilitar la protección CSRF, comúnmente usado en entornos de pruebas o para APIs REST.</li>
     *   <li>Autenticación HTTP básica y a través de formulario por defecto.</li>
     *   <li>Permitir el acceso público a ciertas rutas específicas, mientras que otras rutas
     *       requieren autenticación.</li>
     * </ul>
     * </p>
     *
     * @param http Objeto {@link HttpSecurity} provisto por Spring Security, utilizado para personalizar
     *             la configuración de seguridad web.
     * @return Un objeto {@link SecurityFilterChain} que representa la configuración de seguridad
     *         HTTP personalizada.
     * @throws Exception En caso de que ocurra algún error durante la configuración.
     *
     * @Author No se especificó autor.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // deshabilitado para pruebas o APIs
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/registro", "/foro/**", "/foro", "/foro/hilo/**").permitAll()
                        .requestMatchers("/carrito").permitAll()

                        .requestMatchers("/listas").authenticated()
                        .requestMatchers("/listas/*").authenticated()

                        .requestMatchers("/paginaDeProducto").permitAll()
                        .requestMatchers("/supermercados", "/supermercados/**").permitAll()

                        .requestMatchers("/entities/**").permitAll()
                        .requestMatchers("/css/**", "/images/**", "/workspace/uploads/**").permitAll()

                        .requestMatchers("/usuario", "/usuario/modificar", "/usuario/modificar/password").authenticated()

                        .requestMatchers(HttpMethod.POST, "/login", "/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/foro/crear").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/entidades/deleteHija/*").authenticated()

                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(loginSuccessHandler)    //Ejecucion de login personalizada, crea una lista nueva abierta si el usuario no tiene ya una
//                        .defaultSuccessUrl("/usuario", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/cerrarSesion")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?expired")
                )

        ;


        return http.build();
    }

    /**
     * Configura y proporciona un bean de tipo {@link AuthenticationManager}.
     * Este método utiliza {@link AuthenticationConfiguration} para recuperar
     * y devolver una instancia del manejador de autenticación.
     *
     * <p>El objeto {@link AuthenticationManager} es clave para gestionar los flujos
     * de autenticación en la aplicación.</p>
     *
     * @param authConfig Objeto {@link AuthenticationConfiguration} utilizado para crear
     *                   el manejador de autenticación.
     *
     * @return Una instancia de {@link AuthenticationManager} configurada a partir del
     *         {@link AuthenticationConfiguration} proporcionado.
     *
     * @throws Exception Si ocurre algún error durante la creación del manejador de autenticación.
     *
     * @Author No se especificó autor.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Registra un listener para detectar eventos de expiración de sesión
     *
     * @return instancia de HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }



}
