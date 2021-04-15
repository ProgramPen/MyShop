package com.quchaoqun.shoppingcart.mapper;

import com.quchaoqun.shoppingcart.vo.CartsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int selectCart(@Param("uid") int uid, @Param("pid") int pid);

    int updateCart(@Param("uid") int uid,@Param("pid") int pid,@Param("price") double price);

    int addCart(@Param("uid") int uid, @Param("pid") int pid,@Param("price") double price);

    List<CartsVo> selectCartByUid(int uid);

    int update(@Param("uid") Integer uid, @Param("pid") Integer pid, @Param("cnum") Integer cnum, @Param("price") Double price);

    int delete(Integer cid);

    void clearByUid(Integer uid);
}
