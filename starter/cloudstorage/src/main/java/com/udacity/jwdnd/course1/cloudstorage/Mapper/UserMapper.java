package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // getUser
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    Users getUser(String username);

    // insertUser
    @Insert("INSERT INTO USERS(username, salt, password, firstname, lastname) " +
            "VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(Users user);



    // deleteUser
    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void deleteUser(int userid);

}
