package com.quchaoqun.workbeach.mapper;


import com.quchaoqun.workbeach.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface ProductMapper {
    List<Product> findBytId(@Param("tId") Integer tId, @Param("currentPage") Integer currentPage);

    ArrayList<String> fuzzyFindByName(String pname);

    int checkByName(String pname);


    Integer totalBytId(Integer tId);

    List<Product> findByName(@Param("pname") String pname, @Param("currentPage") int currentPage);

    int totalByName(String pname);

    Product findDetailByPid(Integer pid);

    List<Product> getGoodsList();

    int addGoods(Product product);
}
