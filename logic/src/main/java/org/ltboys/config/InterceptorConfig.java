package org.ltboys.config;


import org.ltboys.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import java.util.Locale;

/**
 * @author kralarklite
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 添加jwt拦截器，并指定拦截路径
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns(
                        "/user/homepage",
                        "/user/updateuser",
                        "/user/myarticles",
                        "/user/mycomments",
                        "/information/addarticle",
                        "/information/addcomment",
                        "/picture/save");
    }

    /**
     * jwt拦截器
     * */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
