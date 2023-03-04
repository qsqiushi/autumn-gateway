//package com.autumn.gateway.admin.config;
//
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author qiushi
// * @program autumn-gateway
// * @description
// * @since 2022-06-28 14:50
// */
//@Configuration
//@EnableOpenApi
//@EnableKnife4j
//@EnableWebMvc
//public class SwaggerConfig {
//
//  @Bean
//  public Docket createRestApi() {
//    return new Docket(DocumentationType.OAS_30)
//        .useDefaultResponseMessages(false)
//        .apiInfo(apiInfo())
//        .select()
//        .apis(RequestHandlerSelectors.basePackage("com.autumn.gateway.admin.controller"))
//        .paths(PathSelectors.any())
//        .build();
//  }
//
//  private ApiInfo apiInfo() {
//    return new ApiInfoBuilder()
//        .description("autumn gateway admin")
//        .contact(
//            new Contact("邱实", "https://github.com/qsqiushi/autumn-gateway", "qsqiushi@gmail.com"))
//        .version("v1.1.0")
//        .title("API测试文档")
//        .build();
//  }
//}
