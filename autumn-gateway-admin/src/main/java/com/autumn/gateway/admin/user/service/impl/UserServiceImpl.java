package com.autumn.gateway.admin.user.service.impl;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import com.autumn.gateway.admin.user.entity.UserInfo;
import com.autumn.gateway.admin.user.mapper.UserInfoMapper;
import com.autumn.gateway.admin.user.service.IUserService;
import com.autumn.gateway.admin.user.wrapper.UserInfoQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 10:43
 */
@Service
public class UserServiceImpl implements IUserService {

  @Resource
  private UserInfoMapper userInfoMapper;

  public List<UserInfo> selectAll() {

    IQuery<UserInfo> query = new UserInfoQuery().selectAll();

    List<UserInfo> list = userInfoMapper.listEntity(query);

    return list;
  }

  public Integer save() {
    return userInfoMapper.save(new UserInfo());
  }
}
