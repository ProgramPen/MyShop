package com.quchaoqun.usermanager.service.impl;

import com.quchaoqun.usermanager.domain.Address;
import com.quchaoqun.usermanager.domain.User;
import com.quchaoqun.usermanager.exception.UserLoginException;
import com.quchaoqun.usermanager.mapper.AddressMapper;
import com.quchaoqun.usermanager.mapper.UserMapper;
import com.quchaoqun.usermanager.service.UserService;
import com.quchaoqun.util.SqlSessionUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    //获取UserMapper对象
    private UserMapper userMapper = SqlSessionUtil.getSqlSession().getMapper(UserMapper.class);
    //获取AddressMapper对象
    private AddressMapper addressMapper = SqlSessionUtil.getSqlSession().getMapper(AddressMapper.class);

    @Override
    public User login(String username, String password) throws UserLoginException {

        User user = userMapper.login(username,password);
        if(user == null){
            throw new UserLoginException("用户，密码错误！");
        }else if(user.getStatus() !=1){
            throw new UserLoginException("用户被禁用，请联系客户！");
        }
        return user;
    }

    @Override
    public int check(String username) {
        int count = userMapper.checkByUsername(username);

        return count;
    }

    @Override
    public boolean registry(User user) {
        int count = userMapper.insertUser(user);
        boolean flag = true;
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Address> selectAddress(Integer uid) {
        List<Address> addresses = addressMapper.selectAddress(uid);
        return addresses;
    }

    @Override
    public boolean updateAddress(Integer aid,String aname,String aphone,String adetail) {
        int count = addressMapper.updateAddress(aid,aname,aphone,adetail);
        boolean flag = false;
        if(count == 1){
            flag =true;
        }
        return flag;
    }

    @Override
    public boolean deleteAddress(Integer aid) {
        int count = addressMapper.deleteAddress(aid);
        boolean flag = false;
        if(count ==1){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean updateState(Integer aid,Integer uid) {
        int count1 =addressMapper.updateStateByUid(uid);
        int count2 = addressMapper.updateStateByAid(aid);
        boolean flag = false;
        if(count1 == 1 && count2 == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public void addAddress(Address address) {
        //先找用户下是否已经有收货地址，如果没有，新添加的收货地址作为默认地址
        int count = addressMapper.selectAddressByUid(address.getUid());
        if (count == 0){
            address.setState(1);
        }
        int result = addressMapper.addAddress(address);
    }

    @Override
    public List<User> getUserList(Integer state) {
        List<User> userList = userMapper.getUserList(state);
        return userList;
    }

    @Override
    public List<User> getUserByNameAndSex(String username, String sex) {
        List<User> users = userMapper.getUserByNameAndSex(username,sex);
        return users;
    }

    @Override
    public boolean deleteUserByUid(Integer uid) {
        int count = userMapper.deleteUserByUid(uid);
        boolean flag = false;
        if(count == 1){
            flag = true;
        }
        return flag;
    }

}
