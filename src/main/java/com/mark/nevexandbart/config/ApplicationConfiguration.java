package com.mark.nevexandbart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * Created by Mark Cunningham on 6/24/2017.
 */
@Configuration
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

//    @Bean
//    public ServletContextTemplateResolver servletContextTemplateResolver() {
//        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
//        resolver.setPrefix("/static/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode("HTML5");
//        return resolver;
//    }
//
//    @Bean
//    public SpringTemplateEngine springTemplateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(servletContextTemplateResolver());
//        return engine;
//    }
//
//    @Bean
//    public ThymeleafViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(springTemplateEngine());
//        resolver.setOrder(1);
//        return resolver;
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePattern = "/resources/**";
        if ( !registry.hasMappingForPattern(resourcePattern)) {
            registry.addResourceHandler(resourcePattern).addResourceLocations("/resources/");
        }
    }

}
