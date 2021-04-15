<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/css/login21.css">
<link href="/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="/js/jquery.min.js"></script>
<title>头部</title>
    <script type="text/javascript">
        $(document).ready(function(){
            //刷新页面，将信息栏内容清空
            $("#searchMsg").val("");
            //获取商品类别，并显示
            $.ajax({
                url:"${pageContext.request.contextPath}/product/findAllType",
                type:"GET",
                dataType:"json",
                success:function(data){
                    var html = '';
                    $.each(data,function (i,type) {
                        html += '<a href="/product/findBytId?page=1&tId='+type.tid+'" style=" margin-top:10px">'+type.tname+'</a>'
                    });
                    $("#goodsType").html(html);
                }
            });

            //给搜索按钮绑定点击事件
            $("#searchBtn").click(function () {
                $.ajax({
                    url:"/product/checkByName",
                    data:{
                        "pname":$("#searchMsg").val()
                    },
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        if (data.success){
                            window.location.href="/product/findByName?page=1&pname="+$("#searchMsg").val();
                        }else {
                            alert("查无此商品！");
                        }
                    }
                });
            });
            //给输入框绑定改变事件
            $("#searchMsg").change(function () {
                if( $("#searchMsg").val() !=""){
                    $.ajax({
                        url:"/product/fuzzyFindByName",
                        type:"get",
                        dataType:"json",
                        data:{
                            "pname":$("#searchMsg").val()
                        },
                        success:function (data) {
                            $("#tableSelect").attr("size",data.len);
                            var html = '';
                            $.each(data,function (i,product) {
                                var val = "\'"+product+"\'";
                                html +='<option onclick="change('+val+')">'+product+'</option>';
                            });
                            $("#tableSelect").html(html);
                            if($("#tableSelect").html() == ""||$("#tableSelect").html() == "undefined"){
                                $("#tableSelect").css("display","none");
                            }else {
                                $("#tableSelect").css("display","");
                            }
                        }
                    });
                }
            });
            //给输入框绑定光标失去，下拉框消失
            $("#searchMsg").blur(function () {
                $("#tableSelect").css("display","none");
            })
        });

        //一旦点击列表框内容，将选中的内容赋值到搜索框中
        function change(val) {
            $("#searchMsg").val(val);
            $("#tableSelect").html("");
            $("#tableSelect").css("display","none");
        }
    </script>
</head>
<body>
				
 <div id="top">
    	<div id="topdiv">
            <span>
                <a href="/index.jsp"  target="_blank">小米商城</a>
                <li>|</li>
                <a href="" >小米商城移动版</a>
                <li>|</li>
                <a href="" >问题反馈</a>
            </span>

            <span style="float:right;">
           		<c:if test="${empty user}">
                    <a href="/login.jsp" id="a_login">登录</a>
                    <li>|</li>
                    <a href="/register.jsp" id="a_registry">注册</a>
                </c:if>
       			<c:if test="${not empty user}">
                    <a href="/user/selectAddress.do?uid=${user.id}" id="a_address">${user.name}</a>
                    <li>|</li>
                    <a href="/user/logout.do" id="a_user">注销</a>
                    <li>|</li>
                    <a href="/order/selectByUid.do?uid=${user.id}" id="a_order">我的订单</a>
                    <li>|</li>
                    <a href="/user/selectAddress.do?uid=${user.id}" id="a_getAddress">地址管理</a>
                </c:if>
                <li>|</li>
                <a href="" id="a_top">消息通知</a>
                <a href="/cart/selectByUid.do?uid=${user.id}" id="shorpcar">购物车</a>
            </span>
        </div>
 </div>
<div id="second" >
    <p id="goodsType"></p>
    <a href="" id="seimg1" style=" margin-top:23px; margin-left: 400px"><img id="logo" src="/image/logo_top.png" width="55" height="54"/></a>
    <a href="" id="seimg2" style=" margin-top:17px;" ><img id="gif" src="/image/yyymix.gif" width="180" height="66" /></a>

    <form class="form-inline pull-right" style="margin-top: 45px;margin-right: 10px;">
        <div class="form-group">
            <input id="searchMsg" type="text" class="form-control" style="width: 300px"  placeholder="搜索一下好东西...">
        </div>
        <input type="button" class="btn btn-warning" id="searchBtn" value="搜索" style="height: 35px"><br>
        <select name="tableSelect" size="5" id="tableSelect" class="form-control" style="width:300px;display: none" > 
        </select>

	  </form>
</div>
</body>
</html>