<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="java.util.ArrayList" %>
<%@page import="proj.Entity.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>图书列表</h3>
    </div>

    <table class="table table-striped" id="book-tb"> 
	  <tr>
	    <th>ISBN</th>
	    <th>名称</th>
	    <th>操作</th>
	  </tr>
	  
<% boolean is_admin = request.getAttribute("IS_ADMIN") != null;
   ArrayList<BookInfo> books
     = (ArrayList<BookInfo>)request.getAttribute("books");
   for(BookInfo book : books) { %>
  <tr class="book-item">
    <td class="b-isbn"><%= book.getIsbn() %></td>
    <td class="b-name"><%= book.getName() %></td>
    <td class="b-ops">
      <a class="cartbtn">添加到购物车</a>
<% if(is_admin) { %>
       | <a class="rmbtn">删除</a>
<% } %>
    </td> 
  </tr>
<% } %>

	</table>

    <hr />
	
	<div class="panel-body">
      <div class="row btn-row">
<% if(is_admin) { %>
        <div class="col-md-3 book-col">
          <input type="button" id="addbtn" value="添加" class="btn btn-block btn-primary book-btns"/>
	    </div>
<% } %>
      </div>
	</div><!-- panel-body -->
	
  </div><!-- panel -->
	
  <script src="./js/book.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />