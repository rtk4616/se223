<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Vector" %>
<%@page import="com.proj2.*" %>

<% if(request.getAttribute("IN_USE") == null) return; %>
<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>用户管理</h3>
    </div>
    
    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th>uid</th>
	    <th>用户名</th>
	    <th>操作</th>
	  </tr>

<%
  Vector<UserInfo> users = (Vector<UserInfo>)request.getAttribute("users");
  for(UserInfo ui : users)
  {
     out.print("<tr class=\"admin-item\">\n");
     out.print("<td class=\"a-uid\">" + ui.GetUID() + "</td>\n");
     out.print("<td class=\"a-un\">" + ui.GetUN() + "</td>\n");
     out.print("<td class=\"a-ops\">");
     out.print("<a class=\"rmbtn\">删除用户</a>");
     out.print("</td>");
     out.print("</tr>");
  }
%>
	  
    </table>
    
  </div><!-- 。panel -->
  
  <script src="./js/admin.js" charset="UTF-8"></script>

<jsp:include page="./footer.jsp" />