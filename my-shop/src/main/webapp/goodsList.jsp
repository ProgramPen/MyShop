<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>商品列表页</title>

	<%@ include file="header.jsp"%>
</head>
<script type="text/javascript">
</script>
<body>


<div class="panel panel-default" style="margin: 0 auto;width: 95%;">
	<div class="panel-heading">
	    <h3 class="panel-title"><span class="glyphicon glyphicon-th-list"></span>&nbsp;&nbsp;商品列表</h3>
	</div>
	<div class="panel-body">
	   	   <!--列表开始-->
	    <div class="row" style="margin: 0 auto;">
	    	<c:forEach items="${products.list}" var="p" varStatus="i">
		    	<div class="col-sm-3">
				    <div class="thumbnail">
				      <img src="/${p.image}" width="180" height="180"  alt=${p.name} />
				      <div class="caption">
				        <h4>商品名称
							<a href="/product/detail?pid=${p.pid}">${p.name}</a>
						</h4>
				        <p>热销指数
				        	<c:forEach begin="1" end="${p.state}">
				        		<img src="/image/star_red.gif" alt="star"/>
				        	</c:forEach>
				        </p>
				         <p>上架日期:${p.time}</p>
			             <p style="color:orange">价格:${p.price}</p>
				      </div>
				    </div>
				  </div>
	    	</c:forEach>
			  
		</div>
        <center>
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<li disabled=${products.currentPage==1 ? "true" : ''}>
					<a style="pointer-events:${products.currentPage==1 ? "none" : ''} " href="/product/findBytId?tId=${param.tId}&page=${products.currentPage-1}" aria-label="Previous">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>

				<c:forEach begin="1" end="${products.totalPage}" step="1" var="index">
					<c:if test="${products.currentPage==index}">
						<li class="active"><a href="/product/findBytId?tId=${param.tId}&page=${index}">${index}</a></li>
					</c:if>

					<c:if test="${products.currentPage!=index}">
						<li ><a href="/product/findBytId?tId=${param.tId}&page=${index}">${index}</a></li>
					</c:if>
				</c:forEach>

				<li class="${products.currentPage == products.totalPage ? 'disabled':''}">
					<a style="pointer-events:${products.currentPage == products.totalPage ? "none" : ''} " href="/product/findBytId?tId=${param.tId}&page=${products.currentPage+1}" aari-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
			</ul>
		</nav>
		</center>
   	</div>
</div>
      <!-- 底部 -->
   <%@ include file="footer.jsp"%>
</body>
</html>