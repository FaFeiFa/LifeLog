package com.hua.project.project_first.dao;

import com.hua.project.project_first.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

@Mapper
public interface UserMapper {
    void setUser(User user);
    User equalUser(String email , String password);
    void rePassword(String email , String password);
}