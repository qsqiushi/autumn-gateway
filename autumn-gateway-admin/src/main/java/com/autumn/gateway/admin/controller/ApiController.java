package com.autumn.gateway.admin.controller;

import com.autumn.gateway.admin.user.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-06-21 19:40
 */
@RestController
@RequestMapping("/api")
// @Api(tags = "API接口")
public class ApiController {

  @Resource private UserInfo userInfo;

  @GetMapping("/test/null")
  // @ApiOperation(value = "测试接口", notes = "测试")
  public String testNull(HttpServletRequest request) {
    return (userInfo == null) + "";
  }

  @GetMapping("/test")
  // @ApiOperation(value = "测试接口", notes = "测试")
  public String test(HttpServletRequest request) {
    return "knife4j";
  }
}
