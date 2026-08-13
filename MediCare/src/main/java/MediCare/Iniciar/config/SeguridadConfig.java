package MediCare.Iniciar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Configuracion de Spring Security: login, logout y permisos de cada rol sobre los modulos.
@Configuration
public class SeguridadConfig {

    private final RedireccionPorRolHandler redireccionPorRolHandler;

    public SeguridadConfig(RedireccionPorRolHandler redireccionPorRolHandler) {
        this.redireccionPorRolHandler = redireccionPorRolHandler;
    }

    // Algoritmo BCrypt para guardar y comparar las claves de los usuarios.
    @Bean
    public PasswordEncoder codificadorClaves() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain cadenaDeFiltros(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(peticiones -> peticiones
                        .requestMatchers("/", "/login", "/error").permitAll()
                        // ADMIN es el unico que administra usuarios y roles.
                        .requestMatchers("/usuarios/**", "/roles/**").hasRole("ADMIN")
                        // Solo ADMIN y MEDICO pueden modificar las citas medicas.
                        .requestMatchers("/citas/nuevo", "/citas/guardar", "/citas/editar/**", "/citas/eliminar/**")
                        .hasAnyRole("ADMIN", "MEDICO")
                        // PACIENTE unicamente visualiza las citas y las consultas.
                        .requestMatchers("/citas", "/citas/detalle/**", "/consultas/**")
                        .hasAnyRole("ADMIN", "MEDICO", "PACIENTE")
                        .anyRequest().authenticated())
                .formLogin(acceso -> acceso
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("clave")
                        .successHandler(redireccionPorRolHandler)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(salida -> salida
                        .logoutSuccessUrl("/login?salida")
                        .permitAll())
                .exceptionHandling(manejo -> manejo.accessDeniedPage("/acceso-denegado"));

        return http.build();
    }
}
