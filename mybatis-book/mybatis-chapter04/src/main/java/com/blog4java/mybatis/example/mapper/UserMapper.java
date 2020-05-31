package com.blog4java.mybatis.example.mapper;

import com.blog4java.mybatis.example.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


// 接口namespace
public interface UserMapper {

    // xml
    List<UserEntity> listAllUser();

    // 注解
    @Select("select * from user where id=#{userId,jdbcType=INTEGER}")
    UserEntity getUserById(@Param("userId") String userId);

    List<UserEntity> getUserByEntity(UserEntity user);

    UserEntity getUserByPhone(@Param("phone") String phone);

}