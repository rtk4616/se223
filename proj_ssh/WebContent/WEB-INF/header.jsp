<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
  <title>图书管理系统</title>
  <meta charset="utf-8" />
  <link rel="stylesheet" href="./css/bootstrap.css" />
  <link rel="stylesheet" href="./css/bootstrap-theme.min.css" />
  <link rel="stylesheet" href="./css/style.css" />
  <script src="./js/jquery.min.js"></script>
  <script src="./js/common.js"></script>
</head>
<body>
<div id="main" class="container">
 
  <div id="maindir" class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<s:url action='index' />">主页</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
<% if (request.getAttribute("IS_LOGIN") == null) { %>
            <li><a href="<s:url action='login' />">登录</a></li>
            <li><a href="<s:url action='reg' />">注册</a></li>
<% } else { %>
            <li><a href="<s:url action='book' />">图书</a></li>
            <li><a href="<s:url action='cart' />">购物车</a></li>
			<li><a href="<s:url action='order' />">订单</a></li>
<% if (request.getAttribute("IS_ADMIN") != null) { %>
            <li><a href="<s:url action='admin' />">用户管理</a></li>
            <li><a href="<s:url action='account' />">统计</a></li>
<% } %>
            <li><a href="<s:url action='logout' />">退出</a></li>
<% } %>
          </ul>
        </div><!--/.nav-collapse -->
  </div>
