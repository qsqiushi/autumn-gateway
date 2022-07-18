package com.autumn.gateway.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-06-28 19:35
 */
@Configuration
public class WebAppMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    // 配置knife4j 显示文档
    registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    // 配置swagger-ui显示文档
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    // 公共部分内容
    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
