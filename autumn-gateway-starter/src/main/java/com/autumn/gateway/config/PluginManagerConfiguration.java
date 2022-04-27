package com.autumn.gateway.config;

import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <pf4j Spring 配置>
 *
 * @author qiushi
 * @since 2021/7/22 14:07
 */
@Configuration
@DependsOn({"applicationContextUtil"})
public class PluginManagerConfiguration {

  @Value("${plugin.folder}")
  private String pluginFolder;

  @Bean
  public SpringPluginManager pluginManager() {
    return new SpringPluginManager(Paths.get(pluginFolder));
  }
}
