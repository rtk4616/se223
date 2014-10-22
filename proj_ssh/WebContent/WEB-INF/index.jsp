<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:include page="./header.jsp" />
  
  <div id="banner" class="jumbotron">
    <h1>图书管理系统</h1>
  </div>

<div class="alert alert-info">
<% if(request.getAttribute("IS_LOGIN") == null) {  %>
      您还没有<a href="<s:url action='login' />">登录</a>或<a href="<s:url action='reg' />">注册</a>。
<% } else { %>
      <p class="lead">Welcome back, <%= request.getAttribute("un") %>.</p>
<% } %>
</div>

<jsp:include page="./footer.jsp" />