package com.quchaoqun.workbeach.service.impl;

import com.quchaoqun.util.SqlSessionUtil;
import com.quchaoqun.workbeach.domain.Product;
import com.quchaoqun.workbeach.domain.ProductType;
import com.quchaoqun.workbeach.mapper.ProductMapper;
import com.quchaoqun.workbeach.mapper.ProductTypeMapper;
import com.quchaoqun.workbeach.service.ProductService;

import java.util.ArrayList;
import java.util.List;


public class ProductServiceImpl implements ProductService {
    //产品表dao对象
    private ProductMapper productMapper = SqlSessionUtil.getSqlSession().getMapper(ProductMapper.class);

    //获取产品类别dao对象
    private ProductTypeMapper productTypeMapper = SqlSessionUtil.getSqlSession().getMapper(ProductTypeMapper.class);

    //获取产品类
    @Override
    public List<ProductType> findAllType() {
        List<ProductType> productTypes = productTypeMapper.findAll();

        return productTypes;
    }

    @Override
    public ArrayList<String> fuzzyFindByName(String pname) {
        ArrayList<String> pNames = productMapper.fuzzyFindByName(pname);
        return pNames;
    }

    @Override
    public boolean checkByName(String pname) {
        int count = productMapper.checkByName(pname);
        boolean flag = false;
        if(count >= 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public List findBytId(Integer tId,Integer currentPage) {

        List<Product> products = productMapper.findBytId(tId,currentPage);
        int totalPage = (int)Math.ceil(productMapper.totalBytId(tId)/8.0);
        List result = new ArrayList();
        result.add(products);
        result.add(totalPage);
        return result;
    }

    @Override
    public List findByName(String pname, Integer currentPage) {
        List<Product> products = productMapper.findByName(pname,currentPage);
        int totalPage = (int)Math.ceil(productMapper.totalByName(pname)/8.0);
        List result = new ArrayList();
        result.add(products);
        result.add(totalPage);
        return result;
    }

    @Override
    public Product findDetailByPid(Integer pid) {
        Product product = productMapper.findDetailByPid(pid);
        return product;
    }

    @Override
    public List<Product> getGoodsList() {
        List<Product> products = productMapper.getGoodsList();
        return products;
    }

    @Override
    public boolean addGoods(Product product) {
        int count = productMapper.addGoods(product);
        boolean flag = false;
        if(count == 1){
            flag = true;
        }
        return flag;
    }

}
