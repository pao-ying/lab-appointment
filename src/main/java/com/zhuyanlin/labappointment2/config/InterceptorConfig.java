package com.zhuyanlin.labappointment2.config;

import com.zhuyanlin.labappointment2.interceptor.AdminLoginInterceptor;
import com.zhuyanlin.labappointment2.interceptor.LoginInterceptor;
import com.zhuyanlin.labappointment2.interceptor.TeacherLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Autowired
    private TeacherLoginInterceptor teacherLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login");
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/api/admin/**");
        registry.addInterceptor(teacherLoginInterceptor)
                .addPathPatterns("/api/teacher/**");
//        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
