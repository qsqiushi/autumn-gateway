package com.autumn.gateway.admin.controller;

import com.autumn.gateway.admin.user.entity.UserInfo;
import com.autumn.gateway.admin.user.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-04-27 15:42
 */
@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserServiceImpl userService;

  @Test
  public void test() {
    List<UserInfo> list = userService.selectAll();

    System.out.println(new Gson().toJson(list));

    Integer id  = userService.save();
    System.out.println(id);
  }
}
