package com.autumn.gateway.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-06-21 19:40
 */
@RestController
@RequestMapping("/api")
@Api(tags = "API接口")
public class ApiController {

  @GetMapping("/test")
  @ApiOperation(value = "测试接口", notes = "测试")
  public String test() {
    return "knife4j";
  }


}
