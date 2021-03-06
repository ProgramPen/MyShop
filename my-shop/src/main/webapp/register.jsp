<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<script type="text/javascript" src="js/jquery.min.js"></script>
		
<link rel="stylesheet" type="text/css" href="css/login1.css">
<script type="text/javascript">
	$(function(){
		//刷新页面清空表单所有信息
		$("#username").val("");
		$("#password").val("");
		$("#repassword").val("");
		$("#email").val("");

		//将注册按钮设为只可显示状态
		$("#registerBtn").attr("disabled",true);

		$("#username").change(function(){
			$.get("/user/checkUser","username="+$("#username").val(),function(data){

				if(data==1){
					$("#usernameMsg").html("用户名已经存在").css("color","red");
				}else{
					$("#usernameMsg").html("用户名可用").css("color","green");
				}
			})
		});


		//密码输入框获得光标时，将信息栏内容清空
		$("#password").focus(function () {
			$("#pwdMsg").html("");
		});

		$("#password").blur(function () {

			if($("#password").val().length < 6){
				$("#pwdMsg").html("请输入6位以上字符").css("color","red");
			}else {
				$("#pwdMsg").html("密码可用").css("color","green");
			}
		});

		//确认密码框获得光标
		$("#repassword").focus(function () {
			$("#repwdMsg").html("");
		});

		$("#repassword").blur(function () {
			if($("#password").val() != $("#repassword").val()){
				$("#repwdMsg").html("密码不一致，重新输入").css("color","red");
			}else {
				$("#repwdMsg").html("密码一致").css("color","green");
			}
		});

		//邮箱获取光标时，清空信息栏
		$("#email").focus(function () {
			$("#emailMsg").html("");
		});

		//邮箱失去光标时进行内容检查
		$("#email").blur(function () {
			var reg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;
			if(!reg.test($("#email").val())){
				$("#emailMsg").html("邮箱格式错误").css("color","red");
				$("#registerBtn").attr("disabled",true);
			}else {
				//单所有表单内容填写无误，注册按钮开启
				$("#emailMsg").html("邮箱正确").css("color","green");
				if($("#usernameMsg").html() == "用户名可用" && $("#pwdMsg").html() == "密码可用"&&
						$("#repwdMsg").html() == "密码一致" && $("#emailMsg").html()=="邮箱正确"){
					$("#registerBtn").removeAttr("disabled");
				}
			}
		});

		$("#registerBtn").click(function () {
				$.ajax({
					url:"/user/register.do",
					type:"post",
					data:{
						"username":$("#username").val(),
						"password":$("#password").val(),
						"email":$("#email").val(),
						"sex":$(":radio:checked").val()
					},
					dataType:"json",
					success:function (data) {
						if(data.success){
							window.location.href="/registerSuccess.jsp";
						}else {
							alert("注册失败！");
							$("#excMsg").html(data.excMsg).css("color","red");
						}
					}
				});
		});
	})
</script>
<title>注册</title>
</head>
<body>
	<div class="regist">
		<div class="regist_center">
			<div class="regist_top">
				<div class="left fl"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;会员注册</div>
				<div class="right fr">
					<a href="index.jsp" target="_black">小米商城</a>
				</div>
				<div class="clear"></div>
				<div class="xian center"></div>
			</div>
			<div class="center-block" style="margin-top: 80px;">
				<form class="form-horizontal" action="user?method=register" method="post">

					<div class="form-group">
						<label class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-8" style="width: 40%">
							<input type="text" id="username" name="username" class="form-control col-sm-10"
								placeholder="Username" />
						</div>
						<div class="col-sm-2">
							<p class="text-danger"><span class="help-block " id="usernameMsg">请输入用户名</span></p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">密码</label>
						<div class="col-sm-8" style="width: 40%">
							<input type="password" name="password" id="password"
								class="form-control col-sm-10" placeholder="Password" />
						</div>
						<div class="col-sm-2">
							<p class="text-danger"><span id="pwdMsg" class="help-block ">请输入6位以上字符</span></p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-8" style="width: 40%">
							<input type="password"  class="form-control col-sm-10" id="repassword"
								placeholder="Password Again" />
						</div>
						<div class="col-sm-2">
						<p class="text-danger"><span id="repwdMsg" class="help-block ">两次密码要输入一致哦</span></p>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-8" style="width: 40%">
							<input type="email" name="email" class="form-control col-sm-10" id="email"
								placeholder="Email" />
						</div>
						<div class="col-sm-2">
						<p class="text-danger"><span id="emailMsg" class="help-block ">填写正确邮箱格式</span></p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">性别</label>
						<div class="col-sm-8" style="width: 40%">
							<label class="radio-inline"> <input type="radio"
								name="usex" value="男" checked> 男
							</label> <label class="radio-inline"> <input type="radio"
								name="usex" value="女"> 女
							</label>
						</div>
						<div class="col-sm-2">
							<p class="text-danger"><span id="sexMsg" class="help-block ">你是帅哥 还是美女</span></p>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<div class="col-sm-7 col-sm-push-2">
							<input id="registerBtn" type="button" value="注册" class="btn btn-primary  btn-lg" style="width: 200px;" disabled/> &nbsp; &nbsp;
							<input type="reset" value="重置" class="btn btn-default  btn-lg" style="width: 200px;"  />
						</div>
						<span id="excMsg"></span>
					</div>
					<div>${registerMsg}</div>
				</form>

			</div>
		</div>
	</div>
	
</body>
</html>