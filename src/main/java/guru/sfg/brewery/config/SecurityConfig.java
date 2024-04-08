package guru.sfg.brewery.config;

import guru.sfg.brewery.secutity.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlParameterAuthFilter restUrlParameterAuthFilter(AuthenticationManager authenticationManager){
        RestUrlParameterAuthFilter filter = new RestUrlParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
        //        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //        return new BCryptPasswordEncoder(12);
        //        return new StandardPasswordEncoder();
        //        return new LdapShaPasswordEncoder();
        //        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                    UsernamePasswordAuthenticationFilter.class)
                    .csrf().disable();

            http.addFilterBefore(restUrlParameterAuthFilter(authenticationManager()),
                    UsernamePasswordAuthenticationFilter.class);

            http
                    .authorizeRequests(
                            authorize -> {
                                authorize
                                        .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                                        .antMatchers("/beers/**").permitAll()
                                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll(); //MVC
                            })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$12$GG/Yls3B0QcyaPyCofD9J.NOHkrxGbrbvEUi0yI61g4WSbhK7bZdu")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}12279b869e6101c6e71467bc0ea8c9248b63437e502f798d43dd3804474b245f0b6cfc203b55c3eb")
                .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("scott")
                .password("{bcrypt15}$2a$15$eSbZP9wim4V3IrVss2fL3OEfl.mpP3cTlkOku0R8rzggEkPc4gK9i")
                .roles("CUSTOMER");
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails admin= User.withDefaultPasswordEncoder()
                .username("spring")
                .password("guru")
                .roles("ADMIN")
                .build();

       UserDetails user = User.withDefaultPasswordEncoder()
               .username("user")
               .password("password")
               .roles("USER").build();

       return new InMemoryUserDetailsManager(admin,user);
    }

}
