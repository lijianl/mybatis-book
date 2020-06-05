package com.blog4java.mybatis.example.mapper;

import com.blog4java.mybatis.example.entity.UserEntity;
import com.blog4java.mybatis.example.query.UserQuery;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    /**
     * 分页的使用
     *
     * @param query
     * @return
     */
    @Select("select * from user")
    List<UserEntity> getUserPageable(UserQuery query);

}