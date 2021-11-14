package com.autumn.gateway.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-17:10:48
 */
public class ReactorHandlerClassLoader extends URLClassLoader {

  public ReactorHandlerClassLoader(ClassLoader parent) {
    super(new URL[] {}, parent);
  }
}
