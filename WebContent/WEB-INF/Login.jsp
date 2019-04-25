<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Post-it Tree</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/logoIcon.ico"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginCSS.css">
</head>
<body>

<div id="image">
    <img src="img/mainIcon.png" alt="logoImage" id="logo">
</div>

<header id="cabecera">
    <h1>Post-it tree</h1>
</header>

<div id="loginDiv" class="tabcontent">
    <h1>Login</h1>
    <p>${messages}<p>
    <div class="loginResgisterForm">
        <form action="LoginServlet" method="post">
        	<input type="text" class="formElement" placeholder="Username" name="userNameLogin" value="${user.username}" required>
            <input type="password" class="formElement" placeholder="Password" name="contraseniaLogin" value="${user.password}" required>
            <br>
            <input type="submit" class="submitB" value="SIGN IN"><br>
        </form>
    </div>
    <p>New to Post-it tree?
    	<a href="${pageContext.request.contextPath}/pages/register.html">Sign up now</a>
    </p>
</div>

</body>
</html>