package com.quchaoqun.usermanager.service;

import com.quchaoqun.usermanager.domain.Address;
import com.quchaoqun.usermanager.domain.User;
import com.quchaoqun.usermanager.exception.UserLoginException;

import java.util.List;

public interface UserService {
    //登录验证
    User login(String username, String password) throws UserLoginException;

    int check(String username);

    boolean registry(User user);

    List<Address> selectAddress(Integer uid);

    boolean updateAddress(Integer aid,String aname,String aphone,String adetail);

    boolean deleteAddress(Integer aid);

    boolean updateState(Integer aid,Integer uid);

    void addAddress(Address address);

    List<User> getUserList(Integer state);

    List<User> getUserByNameAndSex(String username, String sex);

    boolean deleteUserByUid(Integer uid);
}
