package arkham.knight.ontology.config;

import arkham.knight.ontology.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configurable
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SpringSecurityConfig(MyUserDetailsService myUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.myUserDetailsService = myUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/dashboard/").permitAll()
                .antMatchers("/rae/**").permitAll()
                .antMatchers("/dashboard/individuals/**").hasAnyRole("ADMIN")
                .antMatchers("/users/**").hasAnyRole("ADMIN")
                .antMatchers("/surveys/**").hasAnyRole("USER", "ADMIN")
                .and().formLogin().loginPage("/login")
// Aqui indico la url de la pagina que salga cuando haga login error
                .failureUrl("/login-error")
//Aqui indico la url por defecto que sera enviada la persona en caso de que haya hecho logout o no haya definido una url a la que entrara
                .defaultSuccessUrl("/surveys/simple/")
                .permitAll()
                .and().logout().logoutSuccessUrl("/login")
                .permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
