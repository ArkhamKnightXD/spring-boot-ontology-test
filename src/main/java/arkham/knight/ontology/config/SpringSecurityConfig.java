package arkham.knight.ontology.config;

import arkham.knight.ontology.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configurable
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;

    public SpringSecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/","/css/**", "/js/**").permitAll()
                .antMatchers("/api/v1/**").permitAll()
//                .antMatchers("/words/individuals/**").hasAnyRole("ADMIN")
                .antMatchers("/users/**").hasAnyRole("ADMIN")
//                .antMatchers("/words/**").hasAnyRole("USER")
//                .antMatchers("/surveys/**").hasAnyRole("USER")
                //.antMatchers("/clients/**").hasAnyRole("ADMIN", "USER")
                //.anyRequest().authenticated() //cualquier llamada debe ser validada
                .and()
                .formLogin()
                .loginPage("/login")
// Aqui indico la url de la pagina que salga cuando haga login error
                .failureUrl("/login-error")
//Aqui indico la url por defecto que sera enviada la persona en caso de que haya hecho logout o no haya definido una url a la que entrara
                .defaultSuccessUrl("/words/")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
