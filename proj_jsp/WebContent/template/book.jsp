<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Vector" %>
<%@page import="com.proj2.*" %>

<% if(request.getAttribute("IN_USE") == null) return; %>
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
	  
<% 
  boolean is_admin = request.getAttribute("IS_ADMIN") != null;
  Vector<BookInfo> books = (Vector<BookInfo>)request.getAttribute("books");
  for(BookInfo book : books) 
  {
    out.print("<tr class=\"book-item\">\n");
    out.print("<td class=\"b-isbn\">" + book.GetIsbn() + "</td>\n"); 
    out.print("<td class=\"b-name\">" + book.GetName() + "</td>\n"); 
    out.print("<td class=\"b-ops\">");
    out.print("<a class=\"cartbtn\">添加到购物车</a>");
    if(is_admin)
    	out.print(" | <a class=\"rmbtn\">删除</a>");
    out.print("</td>\n");
    out.print("</tr>\n");
  }
%>

	</table>

    <hr />
	
	<div class="panel-body">
      <div class="row btn-row">
<% if(request.getAttribute("IS_ADMIN") != null) { %>
        <div class="col-md-3 book-col">
          <input type="button" id="addbtn" value="添加" class="btn btn-block btn-primary book-btns"/>
	    </div>
	    <!--<div class="col-md-3 book-col">
          <input type="button" id="rmbtn" value="删除" class="btn btn-block btn-primary book-btns"/>
	    </div>
	    <div class="col-md-3 book-col">
          <input type="button" id="fixbtn" value="修改" class="btn btn-block btn-primary book-btns"/>
	    </div>-->
<% } %>
        <!--<div class="col-md-3 book-col">
          <input type="button" id="cartbtn" value="添加到购物车" class="btn btn-block btn-primary book-btns"/>
	    </div>-->
      </div>
	</div><!-- panel-body -->
	
  </div><!-- panel -->
	
  <script src="./js/book.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />