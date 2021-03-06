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

    //????????????
    private void addGoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //?????????????????????multipart/form-data
        if(!ServletFileUpload.isMultipartContent(req)){
            req.getRequestDispatcher("/admin/addGoods.jsp").forward(req,resp);
        }

        //FileItemFactory???????????????????????????
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //??????????????????????????????
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        //??????request???????????????item?????????UTF-8??????????????????
        servletFileUpload.setHeaderEncoding("UTF-8");

        //?????????????????????????????????????????????1MB
        servletFileUpload.setFileSizeMax(1024 * 1024 *1);
        Map<String,String> map = new HashMap<>();
        String filename ="";
        try {
            List<FileItem> fileList = servletFileUpload.parseRequest(req);
            for(FileItem fileItem : fileList){
                //????????????Item????????????????????????
                if(fileItem.isFormField()){
                    //?????????????????????name???value???
                    map.put(fileItem.getFieldName(),fileItem.getString("UTF-8"));
                }else { //???????????????
                    filename = UUID.randomUUID().toString().replaceAll("-","") + fileItem.getName();
                    //???????????????????????????
                    InputStream in = fileItem.getInputStream();
                    File file = new File("C:\\Users\\quchaoqun\\IdeaProjects\\MyShop\\my-shop\\src\\main\\webapp\\image"+"\\"+filename);

                    //???????????????????????????
                    OutputStream out = new FileOutputStream(file);
                    int len = 0;
                    byte[] bytes = new byte[1024];

                    while ((len =in.read(bytes))!=-1){
                        out.write(bytes,0,len);
                    }
                    //?????????
                    out.flush();
                    out.close();
                    in.close();
                }
            }
            //?????????????????????????????????
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
            req.setAttribute("success","???????????????");
            resp.sendRedirect("/admin/addGoods.jsp");
        }
    }

    //??????????????????
    private void getGoodsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List<Product> products = productService.getGoodsList();
        req.setAttribute("goodsList",products);
        req.getRequestDispatcher("/admin/showGoods.jsp").forward(req,resp);
    }

    //??????????????????
    private void addGoodsType(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("???????????????");
    }

    //????????????????????????
    private void getGoodsType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        List<ProductType> goodsTypeList = productService.findAllType();
        req.setAttribute("goodsTypeList",goodsTypeList);
        req.getRequestDispatcher("/admin/addGoodsType.jsp").forward(req,resp);
    }


    //??????????????????????????????????????????
    private void showGoodsType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        List<ProductType> goodsTypeList = productService.findAllType();
        req.setAttribute("goodsTypeList",goodsTypeList);
        req.getRequestDispatcher("/admin/showGoodsType.jsp").forward(req,resp);
    }

    //????????????pid????????????????????????
    private void findDetailByPid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer  pid = Integer.valueOf(req.getParameter("pid"));
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        Product product = productService.findDetailByPid(pid);
        req.setAttribute("product",product);
        req.getRequestDispatcher("/goodsDetail.jsp").forward(req,resp);
    }

    //????????????????????????????????????????????????
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

    //???????????????????????????
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

    //??????????????????????????????
    private void fuzzyFindByName(HttpServletRequest req, HttpServletResponse resp) {
        //??????????????????
        String pname = req.getParameter("pname");
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        ArrayList<String> pNames = productService.fuzzyFindByName(pname);
        PrintJson.printJsonObj(resp,pNames);
    }

    //??????????????????????????????????????????
    private void checkByName(HttpServletRequest req, HttpServletResponse resp) {
        String pname = req.getParameter("pname");
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());
        boolean flag = productService.checkByName(pname);
        PrintJson.printJsonFlag(resp,flag);
    }

    //????????????????????????
    private void findAll(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) ServiceFactory.getService(new ProductServiceImpl());

        List<ProductType> productTypes = productService.findAllType();

        PrintJson.printJsonObj(resp,productTypes);
    }
}
