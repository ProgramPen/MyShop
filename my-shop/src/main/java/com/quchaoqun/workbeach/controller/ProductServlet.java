package com.quchaoqun.workbeach.controller;

import com.quchaoqun.util.PrintJson;
import com.quchaoqun.util.ServiceFactory;
import com.quchaoqun.workbeach.domain.Product;
import com.quchaoqun.workbeach.domain.ProductType;
import com.quchaoqun.workbeach.service.ProductService;
import com.quchaoqun.workbeach.service.impl.ProductServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if("/product/findAllType".equals(path)){
            findAll(req,resp);
        }else if("/product/checkByName".equals(path)){
            checkByName(req,resp);
        }else if("/product/fuzzyFindByName".equals(path)){
            fuzzyFindByName(req,resp);
        }else if("/product/findBytId".equals(path)){
            findBytId(req,resp);
        }else if("/product/findByName".equals(path)){
            findByName(req,resp);
        }else if("/product/detail".equals(path)){
            findDetailByPid(req,resp);
        }else if("/admin/showGoodsType.do".equals(path)){
            showGoodsType(req,resp);
        }else if("/admin/getGoodsType.do".equals(path)){
            getGoodsType(req,resp);
        }else if("/admin/addGoodsType.do".equals(path)){
            addGoodsType(req,resp);
        }else if("/admin/getGoodsList.do".equals(path)){
            getGoodsList(req,resp);
        }else if("/admin/addGoods.do".equals(path)){
            addGoods(req,resp);
        }
    }

    //添加商品
    private void addGoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //判断请求是不是multipart/form-data
        if(!ServletFileUpload.isMultipartContent(req)){
            req.getRequestDispatcher("/admin/addGoods.jsp").forward(req,resp);
        }

        //FileItemFactory对象，接收参数信息
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //创建文件上床核心对象
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        //设置request在解析每个item时采用UTF-8字符编码问题
        servletFileUpload.setHeaderEncoding("UTF-8");

        //设置每个上传文件的大小不能超过1MB
        servletFileUpload.setFileSizeMax(1024 * 1024 *1);
        Map<String,String> map = new HashMap<>();
        String filename ="";
        try {
            List<FileItem> fileList = servletFileUpload.parseRequest(req);
            for(FileItem fileItem : fileList){
                //如果一个Item是一个普通表单项
                if(fileItem.isFormField()){
                    //获取请求参数的name和value值
                    map.put(fileItem.getFieldName(),fileItem.getString("UTF-8"));
                }else { //是一个文件
                    filename = UUID.randomUUID().toString().replaceAll("-","") + fileItem.getName();
                    //获取文件输入流对象
                    InputStream in = fileItem.getInputStream();
                    File file = new File("C:\\Users\\quchaoqun\\IdeaProjects\\MyShop\\my-shop\\src\\main\\webapp\\image"+"\\"+filename);

                    //获取文件输出流对象
                    OutputStream out = new FileOutputStream(file);
                    int len = 0;
                    byte[] bytes = new byte[1024];

                    while ((len =in.read(bytes))!=-1){
                        out.write(bytes,0,len);
                    }
                    //关闭流
                    out.flush();
                    out.close();
                    in.close();
                }
            }
            //将添加的商品插入到表中
            ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
            Product product = new Product();
            product.setTid(Integer.valueOf(map.get("typeid")));
            product.setName(map.get("name"));
            product.setTime(map.get("pubdate"));
            product.setImage("image/"+filename);
            product.setPrice(Integer.valueOf(map.get("price")));
            product.setState(Integer.valueOf(map.get("star")));
            product.setInfo(map.get("intro"));
            boolean flag = productService.addGoods(product);
            req.setAttribute("success",flag);
            req.getRequestDispatcher("/admin/addGoods.jsp").forward(req,resp);
        } catch (FileUploadException e) {
            e.printStackTrace();
            req.setAttribute("success","添加失败！");
            resp.sendRedirect("/admin/addGoods.jsp");
        }
    }

    //获取所有商品
    private void getGoodsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List<Product> products = productService.getGoodsList();
        req.setAttribute("goodsList",products);
        req.getRequestDispatcher("/admin/showGoods.jsp").forward(req,resp);
    }

    //添加商品类别
    private void addGoodsType(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("添加成功！");
    }

    //获取所有商品种类
    private void getGoodsType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        List<ProductType> goodsTypeList = productService.findAllType();
        req.setAttribute("goodsTypeList",goodsTypeList);
        req.getRequestDispatcher("/admin/addGoodsType.jsp").forward(req,resp);
    }


    //在管理员界面显示所有商品类别
    private void showGoodsType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        List<ProductType> goodsTypeList = productService.findAllType();
        req.setAttribute("goodsTypeList",goodsTypeList);
        req.getRequestDispatcher("/admin/showGoodsType.jsp").forward(req,resp);
    }

    //根据商品pid查询商品详细信息
    private void findDetailByPid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer  pid = Integer.valueOf(req.getParameter("pid"));
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        Product product = productService.findDetailByPid(pid);
        req.setAttribute("product",product);
        req.getRequestDispatcher("/goodsDetail.jsp").forward(req,resp);
    }

    //根据商品名称找商品，支持模糊查询
    private void findByName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pname = req.getParameter("pname");
        Integer currentPage = Integer.valueOf(req.getParameter("page"));
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List result = productService.findByName(pname,(currentPage-1)*8);
        List<Product> list = (List<Product>) result.get(0);
        int totalPage = (Integer) result.get(1);

        Map<String,Object> map = new HashMap<>();

        map.put("list",list);
        map.put("currentPage",currentPage);
        map.put("totalPage",totalPage);

        req.setAttribute("products",map);

        req.getRequestDispatcher("/goodsList.jsp?tId="+list.get(0).getTid()).forward(req,resp);
    }

    //根据商品类别找商品
    private void findBytId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer tId = Integer.valueOf(req.getParameter("tId"));
        Integer currentPage = Integer.valueOf(req.getParameter("page"));
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List result = productService.findBytId(tId,(currentPage-1)*8);
        List<Product> list = (List<Product>) result.get(0);
        int totalPage = (Integer) result.get(1);

        Map<String,Object> map = new HashMap<>();

        map.put("list",list);
        map.put("currentPage",currentPage);
        map.put("totalPage",totalPage);

        req.setAttribute("products",map);

        req.getRequestDispatcher("/goodsList.jsp").forward(req,resp);
    }

    //支持模糊查询商品信息
    private void fuzzyFindByName(HttpServletRequest req, HttpServletResponse resp) {
        //获取参数信息
        String pname = req.getParameter("pname");
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        ArrayList<String> pNames = productService.fuzzyFindByName(pname);
        PrintJson.printJsonObj(resp,pNames);
    }

    //根据商品名称查询商品是否存在
    private void checkByName(HttpServletRequest req, HttpServletResponse resp) {
        String pname = req.getParameter("pname");
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        boolean flag = productService.checkByName(pname);
        PrintJson.printJsonFlag(resp,flag);
    }

    //查询所有商品类别
    private void findAll(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List<ProductType> productTypes = productService.findAllType();

        PrintJson.printJsonObj(resp,productTypes);
    }
}
