package hu.inf.elte.musiclibrary;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
            .csrf()
                .disable() // H2 Console
            .authorizeRequests()
                .antMatchers("/h2/**", "/api/user")
                    .permitAll() // H2 Console
                .anyRequest()
                    .authenticated()              
                .and()
            .httpBasic()
                .and()
            .headers()
                .frameOptions()
                    .disable() // H2 Console
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/user/register", "/api/user/sign-in");
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {

        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        /*auth.inMemoryAuthentication()
            .withUser("user")
            .password("$2a$10$b2KUBuo5r0ReQqTOEFttkeIvx2PnBxariDTDxRK/Y9Xpui2/tMHs2")
            .roles("USER");*/
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}