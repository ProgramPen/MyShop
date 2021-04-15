<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/css/bootstrap.min.css" />
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<title>无效会员管理</title>
<script type="text/javascript">
	$(function () {
		$.ajax({
			url:"${pageContext.request.contextPath}/admin/getUserList.do?state=0",
			method:"get",
			dataType:"json",
			success:function(data){
				var html = '';
				$.each(data,function (i,user) {
					html += "<tr>";
					html += "<td>"+i+"</td>";
					html += "<td>"+user.email+"</td>";
					html += "<td>"+user.name+"</td>";
					html += "<td>"+user.sex+"</td>";
					html += "<td>"+(user.role==0?"会员":"管理员")+"</td>";
					html += "<td>"+"<a href='javascript:delUser("+user.id+")' class='btn btn-primary btn-xs'>"+"删除"+"</a></td>";
					html +="</tr>";
				});
				$("#body").html(html);
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
			}
		});
	});
	//删除用户
	function delUser(id){
		if(confirm("确认要删除吗?")){
			$.ajax({
				url:"/admin/deleteUserByUid.do?uid="+id,
				method:"post",
				dataType:"json",
				success:function(data){
					if(data.success){
						window.location.href="/admin/invalidUser.jsp";
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
					alert("失败"+XMLHttpRequest.status+":"+textStatus+":"+errorThrown);
				}
			})
		}
	}
</script>
</head>
<body>
<div class="row" style="width:98%;margin-left: 1%;margin-top: 5px;">
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				已停用会员列表
			</div>
			<div class="panel-body">
				<table id="tb_list" class="table table-striped table-hover table-bordered">
                    <tr class='tr_head'>
                        <td>编号</td>
                        <td>邮箱</td>
                        <td>姓名</td>
                        <td>性别</td>
                        <td>类别</td>
                        <td>操作</td>
                    </tr>
					<tbody id="body">

					</tbody>
				</table>
				
			</div>
		</div>
	</div>
</div>
</body>
</html>