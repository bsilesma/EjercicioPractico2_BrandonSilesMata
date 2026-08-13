package MediCare.Iniciar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

    // Algoritmo BCrypt para guardar y comparar las claves de los usuarios
    @Bean
    public PasswordEncoder codificadorClaves() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain cadenaDeFiltros(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(peticiones -> peticiones.anyRequest().permitAll());
        return http.build();
    }
}
