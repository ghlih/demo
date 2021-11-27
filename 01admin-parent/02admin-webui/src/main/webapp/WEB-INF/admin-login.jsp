<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="utf-8" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <%-- base 标签必须写在head标签内部
        base 标签必须在所有“带具体路径”的标签的前面
        serverName 部分EL表达式和serverPort部分EL表达式之间必须写“:”
        serverPort 部分EL表达式和contextPath 部分EL表达式之间绝对不能写“/”
                   原因：contextPath 部分EL表达式本身就是“/”开头
                        如果多谢一个“/”会干扰Cookie的工作机制
        serverPort部分EL表达式后面必须写“/”
   --%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <style>

    </style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="admin/to/login/page.html" style="font-size:32px;">Demo</a></div>
        </div>
    </div>
</nav>

<div class="container">
    <form action="security/do/login.html" method="post" class="form-signin" role="form">
        <h2 class="form-signin-heading">
            <i class="glyphicon glyphicon-log-in"></i> 管理员登录
        </h2>
<%--        <p>${requestScope.exception.message }</p>--%>
                <p>${SPRING_SECURITY_LAST_EXCEPTION.message }</p>
        <div class="form-group has-success has-feedback">
            <input type="text" name="loginAcct" class="form-control" id="inputSuccess4"
                   placeholder="请输入登录账号" autofocus> <span
                class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="text" name="userPswd" class="form-control" id="inputSuccess4"
                   placeholder="请输入登录密码" style="margin-top: 10px;"> <span
                class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <input type="checkbox" name="remember-me"/> 记住我
        <button type="submit" class="btn btn-lg btn-success btn-block">登录</butto>
    </form>
</div>
</body>
</html>