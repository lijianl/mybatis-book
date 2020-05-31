package com.blog4java.example.mapper;

import com.blog4java.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 查看{@link @MapperScan}的源码..这里可以不需要注解,很多个性化的使用方式
 */
public interface UserMapper {

    @Insert("insert into user(createTime, name, password, phone, nickName, gender) " +
            "values (#{user.createTime}, #{user.name}, #{user.password}, #{user.phone}, #{user.nickName}, #{user.gender})")
    int insert(@Param("user") User user);

    @Select("select * from user")
    List<User> getAllUserInfo();
}
