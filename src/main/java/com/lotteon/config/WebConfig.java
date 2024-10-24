package com.lotteon.config;


import com.lotteon.interceptor.AppInfoInterceptor;
import com.lotteon.interceptor.FooterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //  configurer.mediaType("css", MediaType.valueOf("text/css"));
    }

    @Autowired
    private AppInfo appInfo;

    @Autowired
    private FooterInterceptor footerInterceptor;

    @Autowired
    public WebConfig(FooterInterceptor footerInterceptor) {
        this.footerInterceptor = footerInterceptor;
    }




    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo));
        registry.addInterceptor(footerInterceptor)
                .addPathPatterns("/**");  // 모든 경로에 대해 인터셉터 적용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**","/uploads/**")
                .addResourceLocations("classpath:/static/css/")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0);
    }


}
