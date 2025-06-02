package com.project.university_directory.security;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.repository.SecurityUserRepository;
import com.project.university_directory.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig implements UserDetailsService {

    private final SecurityUserRepository securityUserRepository;

    @Autowired
    public SecurityConfig(SecurityUserRepository theSecurityUserRepository) {
        this.securityUserRepository = theSecurityUserRepository;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/auth/loginForm",
                                        "/authenticateTheUser",
                                        "/auth/registrationForm",
                                        "/auth/registration").permitAll()
                                //student controller
                                .requestMatchers("/students/list/*").hasRole("STUDENT")
                                .requestMatchers("/students/groups/*").hasRole("STUDENT")
                                .requestMatchers("/students/create/*").hasRole("TEACHER")
                                .requestMatchers("/students/update/*").hasRole("TEACHER")
                                .requestMatchers("/students/save").hasRole("TEACHER")
                                .requestMatchers("/students/delete*").hasRole("TEACHER")
                                //teacher controller
                                .requestMatchers("/teachers/list/*").hasRole("STUDENT")
                                .requestMatchers("/teachers/create/*").hasRole("TEACHER")
                                .requestMatchers("/teachers/save").hasRole("TEACHER")
                                .requestMatchers("/teachers/update/*").hasRole("TEACHER")
                                .requestMatchers("/teachers/delete*").hasRole("TEACHER")
                                //course controller
                                .requestMatchers("/courses/view/*").hasRole("STUDENT")
                                .requestMatchers("/courses/save/*").hasRole("TEACHER")
                                .requestMatchers("/courses/update/*").hasRole("TEACHER")
                                .requestMatchers("/courses/details/*").hasRole("STUDENT")
                                .requestMatchers("/courses/delete/*").hasRole("TEACHER")
                                .requestMatchers("/courses/{courseId}/post/delete/{postId}").hasRole("TEACHER")
                                .requestMatchers("/courses/{courseId}/post/add").hasRole("TEACHER")
                                .requestMatchers("/courses/${courseId}/add-student/{email}").hasRole("TEACHER")
                                .requestMatchers("/courses/add/group/{courseId}").hasRole("TEACHER")
                                .requestMatchers("/courses/add/student/{id}").hasRole("TEACHER")
                                .requestMatchers("/courses/remove/{courseId}/student/{studentId}").hasRole("TEACHER")
                                .requestMatchers("/{courseId}/post/{postId}/update").hasRole("TEACHER")



                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/auth/loginForm")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/general/profile", true)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling( exception ->
                        exception.accessDeniedPage("/errors/access-denied"));

    return http.build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserRepository.findByEmail(email).orElse(null);
        if (securityUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new User(securityUser.getEmail(), securityUser.getPassword(), securityUser.getAuthorities());
    }
}
