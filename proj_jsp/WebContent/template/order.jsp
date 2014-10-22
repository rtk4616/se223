<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Vector" %>
<%@page import="com.proj2.*" %>

<% if(request.getAttribute("IN_USE") == null) return; %>
<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>我的订单</h3>
    </div>

    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th class="cart-th">名称</th>
	    <th class="cart-th">数量</th>
	  </tr>
	  
<% 
  /*Vector<CartItemInfo> items = (Vector<CartItemInfo>)request.getAttribute("order");
  for(CartItemInfo item : items) 
  {
    out.print("<tr>\n");
    out.print("<td class=\"c-name\">" + item.name + "</td>\n" + 
              "<td class=\"c-cnt\">" + String.valueOf(item.count) + "</td>\n");
    out.print("</tr>\n");
  }*/
%>

	</table>
	
  </div><!-- panel -->
	
  <script src="./js/order.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />