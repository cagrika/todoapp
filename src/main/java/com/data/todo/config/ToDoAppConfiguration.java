package com.data.todo.config;

import com.data.todo.repository.UserRepository;
import com.data.todo.service.CouchbaseUserDetailsService;
import com.data.todo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicates;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@RequiredArgsConstructor
@EnableAutoConfiguration
@EnableSwagger2
@EnableWebMvc
public class ToDoAppConfiguration {

    private final UserRepository userRepository;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MapperFacade mapperFactory(){
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        return mapperFactory.getMapperFacade();
    }

    @Bean
    public UserService userService(MapperFacade mapperFacade, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new UserService(userRepository, mapperFacade, bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CouchbaseUserDetailsService couchbaseUserDetailsService(UserService userService){
        return new CouchbaseUserDetailsService(userService);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(Predicates.or(Predicates.containsPattern("/task")))
                .build();
    }
}
