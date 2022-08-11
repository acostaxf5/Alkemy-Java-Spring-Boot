package api;

import api.security.services.UsuarioService;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.*;
import org.springframework.boot.SpringApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Disney Spring", version = "1.0"))
public class DisneySpringApplication extends WebSecurityConfigurerAdapter {

    private final UsuarioService usuarioService;

    @Autowired
    public DisneySpringApplication(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DisneySpringApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder
                .userDetailsService(this.usuarioService)
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers("/autenticacion/**").permitAll()
                .antMatchers("/help.html", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated();
    }

}
