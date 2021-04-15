package com.quchaoqun.workbeach.service;

import com.quchaoqun.workbeach.domain.Product;
import com.quchaoqun.workbeach.domain.ProductType;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    List<ProductType> findAllType();

    ArrayList<String> fuzzyFindByName(String pname);

    boolean checkByName(String pname);

    List findBytId(Integer tId,Integer currentPage);

    List findByName(String pname, Integer currentPage);

    Product findDetailByPid(Integer pid);

    List<Product> getGoodsList();

    boolean addGoods(Product product);
}
