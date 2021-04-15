package com.quchaoqun.usermanager.mapper;

import com.quchaoqun.usermanager.domain.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressMapper {
    List<Address> selectAddress(Integer uid);

    int updateAddress(@Param("aid") Integer aid, @Param("aname") String aname, @Param("aphone") String aphone, @Param("adetail") String adetail);

    int deleteAddress(Integer aid);

    int updateStateByUid(Integer uid);

    int updateStateByAid(Integer aid);

    int selectAddressByUid(int uid);

    int addAddress(Address address);
}
