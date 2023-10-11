package com.example.shopping.config.security;

import com.example.shopping.config.jwt.JwtAccessDeniedHandler;
import com.example.shopping.config.jwt.JwtAuthenticationEntryPoint;
import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.config.oauth2.OAuth2SuccessHandler;
import com.example.shopping.config.oauth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// @EnableGlobalMethodSecurity 어노테이션은 Spring Security에서 메서드 수준의 보안 설정을 활성화하는데
// 사용되는 어노테이션입니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 스프링 시큐리티에서 제공하는 로그인 페이지를 안쓰는 설정
                .httpBasic()
                .disable()
                // JWT 방식을 사용하려면 프론트엔드가 분리된 환경을 가정해야 한다.
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .logout()
                .disable()
                // JWT 방식은 세션저장을 사용하지 않기 때문에 꺼줍니다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/api/v1/users")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/{memberId}")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/boards/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/boards/{boardId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/boards")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/boards/{boardId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/comments/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/items/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/items")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/items/{itemId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/v1/items/{itemId}")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll();

        http
                // JWT Token을 위한 Filter를 아래에서 만들어 줄건데,
                // 이 Filter를 어느위치에서 사용하겠다고 등록을 해주어야 Filter가 작동이 됩니다.
                .apply(new JwtSecurtityConfig(jwtProvider));

        // 에러 방지
        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        http
                // oauth2Login() 메서드는 OAuth 2.0 프로토콜을 사용하여 소셜 로그인을 처리하는 기능을 제공합니다.
                .oauth2Login()
                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userInfoEndpoint()
                // OAuth2 로그인 성공 시, 후작업을 진행할 서비스
                .userService(principalOAuth2UserService)
                .and()
                .successHandler(oAuth2SuccessHandler);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1 페이지 부터 시작
            p.setMaxPageSize(10);       // 한 페이지에 10개씩 출력
        };
    }
}
