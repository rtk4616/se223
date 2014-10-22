<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="java.util.ArrayList" %>
<%@page import="proj.Entity.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>销售统计</h3>
    </div>

    <table class="table table-striped" id="book-tb"> 
	  <tr>
	    <th>UID</th>
	    <th>图书总数</th>
	  </tr>
	  
<% ArrayList<AccountInfo> list
     = (ArrayList<AccountInfo>)request.getAttribute("list");
   for(AccountInfo item : list) { %>
  <tr class="book-item">
    <td class="ac-uid"><%= item.getUid().toString() %></td>
    <td class="ac-num"><%= item.getNum().toString() %></td>
  </tr>
<% } %>

	</table>
	
  </div><!-- panel -->
	
  <script src="./js/book.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />
