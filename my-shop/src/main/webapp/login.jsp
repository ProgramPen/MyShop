<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="css/login1.css">
<script type="text/javascript" src="js/jquery.min.js"></script>

<title>登录</title>
<script type="text/javascript">
	$(function(){
		//2.点击验证码 跟新验证码
		createCode();
		$("#code").click(function(){
			createCode();
		});

		//4.两周以内自动登录  友好提示 
		$("#autoLogin").click(function(){
			if(this.checked){
				$("#autoLoginMsg").html("公司电脑请勿勾选此项").css("color","red");
			}else{
				$("#autoLoginMsg").html("");
			}
		});



		//登录验证
		$("#btn").click(function () {
			//每次点击登录，清空登录消息
			$("#login_msg").html("");
			//判断用户名，密码是否为空
			if($("#username").val()==""||$("#pwd").val()==""){
				$("#login_msg").html('<font style="font-size: 15px;color: red">'+'用户名，密码不能为空！'+'</font>');
				createCode();
				return;
			}
			if($("#code").html() != $("#vcode").val()){
				$("#login_msg").html('<font style="font-size: 15px;color: red">'+'验证码错误'+'</font>');
				createCode();
				return;
			}

			$.ajax({
				url:"/user/login.do",
				type:"post",
				data:{
					"username":$("#username").val(),
					"pwd":$("#pwd").val(),
					"auto":$("#autoLogin").val()
				},
				dataType:"json",
				success:function (data){
					if(data.success){
						window.location.href="/index.jsp";
					}else {
						$("#login_msg").html('<font style="font-size: 15px;color: red">'+data.excMsg+'</font>');
						createCode();
					}
				}
			});

		});
	});

	//生成4个随机数
	function createCode() {
		var code = '';
		for(var i=0;i<4;i++){
			code += Math.floor(Math.random()*10);
		}
		$("#code").html(code);
	};
</script>
</head>
<body>
		<!-- login -->
		<div class="top center">
			<div class="logo center">
				<a href="${pageContext.request.contextPath }/index.jsp" target="_blank"><img src="./image/mistore_logo.png" alt=""></a>
			</div>
		</div>
		<form  method="post" action="user?method=login" class="form center" id="userLogin" >
		<div class="login">
			<div class="login_center">
				<div class="login_top">
					<div class="left fl">会员登录</div>
					<div class="right fr">您还不是我们的会员？<a href="${pageContext.request.contextPath }/register.jsp" target="_self">立即注册</a></div>
					<div class="clear"></div>
					<div class="xian center"></div>
				</div>
				<div class="login_main center">
					<div class="username">
						<div class="left fl">用户名:&nbsp;</div>
						<div class="right fl">
						<input class="shurukuang" type="text" name="username" id="username"  placeholder="请输入你的用户名"/>
						<label id="nameMsg"></label>
						</div>
					</div>
					<div class="username">
						<div class="left fl">密&nbsp;&nbsp;&nbsp;&nbsp;码:&nbsp;</div>
						<div class="right fl">
						<input class="shurukuang" type="password" name="password"  id="pwd"  placeholder="请输入你的密码"/>	
						</div>
					</div>
					<div class="username">
						<div class="left fl">验证码:&nbsp;</div>
						<div class="right fl" style="position: relative;"><input  id="vcode" name="code" type="text" placeholder="验证码"/>
						<font id="code" style="background:url('/image/yanzhengma.jpg');font-size: 15px;color: purple"></font>
						</div>
					</div>

					<span id="login_msg" style="position: absolute"></span>

					<div class="username">
						<div class="left fl">&nbsp;&nbsp;&nbsp;&nbsp;</div>
						<div class="right fl"><label id="checkMsg"></label></div>
					</div>
					<div class="username">
						<input id="autoLogin" name="auto" type="checkbox" value="true"/>&nbsp;&nbsp;两周以内自动登录
						<span id="autoLoginMsg"></span>
					</div>
					<div class="login_submit">
						<input class="submit" type="button" value="立即登录" id="btn"  >

					</div>
					<span style="color:red">${msg}</span>
				</div>	
			</div>
		</div>
		</form>
		<footer>
			<div class="copyright">简体 | 繁体 | English | 常见问题</div>
			<div class="copyright">小米公司版权所有-京ICP备10046444-<img src="./image/ghs.png" alt="">京公网安备11010802020134号-京ICP证110507号</div>

		</footer>
	</body>
</html>