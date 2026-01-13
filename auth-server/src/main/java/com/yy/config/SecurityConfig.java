package com.yy.config;

import com.yy.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import static com.yy.config.CustomClientMetadataConfig.configureCustomClientMetadataConverters;

/**
 * 授权服务器安全配置类
 * @ClassName: SecurityConfig
 * @author: yangfeng
 * @date: 2025/12/23 10:55
 * @version: 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // 授权服务器安全配置
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 应用OAuth2授权服务器的默认安全配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 获取OAuth2授权服务器配置器，配置OIDC和客户端注册端点
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(oidc -> oidc
                        // 启用OIDC 1.0（第一个配置的功能）
                        .clientRegistrationEndpoint(clientRegistrationEndpoint ->
                                // 启用动态客户端注册并配置自定义认证提供者（第二个配置的功能）
                                clientRegistrationEndpoint.authenticationProviders(configureCustomClientMetadataConverters())
                        )
                );

        // 异常处理配置（来自第一个配置）
        http.exceptionHandling((exceptions) -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        // 资源服务器配置（两个配置均有，合并保留）
        http.oauth2ResourceServer(resourceServer -> resourceServer
                .jwt(Customizer.withDefaults())
        );

        return http.build();
    }


    // 默认安全配置
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(Customizer.withDefaults())
                .userDetailsService(customUserDetailsService)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 按顺序从具体到通用进行匹配
//                        .requestMatchers("/health").permitAll() // 健康检查
//                        .requestMatchers("/public/api/v1/notice").permitAll() // 特定的公告接口
//                        .requestMatchers("/public/api/**").permitAll() // 整个公共API组
//                        .requestMatchers("/docs/api.html", "/docs/**").permitAll() // 文档
                        .requestMatchers("/test/**").permitAll() // 您的测试接口
                        .requestMatchers("/login", "/favicon.ico").permitAll() // 登录页和网站图标
                        .anyRequest().authenticated() // 其余所有接口均需认证
                );
        return http.build();
    }

    // 用户详情服务（生产环境应从数据库加载）
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 默认构造使用强度10， 也可用 new BCryptPasswordEncoder(12) 调整强度
        return new BCryptPasswordEncoder();
    }



}