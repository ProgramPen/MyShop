package com.quchaoqun.usermanager.mapper;

import com.quchaoqun.usermanager.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password);

    int checkByUsername(String username);

    int insertUser(User user);

    List<User> getUserList(Integer state);

    String getUserName(int uid);

    List<User> getUserByNameAndSex(@Param("username") String username, @Param("sex") String sex);

    int deleteUserByUid(Integer uid);
}
