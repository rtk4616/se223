<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="java.util.ArrayList" %>
<%@page import="proj.Entity.*" %>

<jsp:include page="./header.jsp" />

  <div class="panel panel-primary">
  
    <div class="panel-heading">
      <h3>购物车</h3>
    </div>

    <table class="table table-striped" id="cart-tb"> 
	  <tr>
	    <th>ISBN</th>
	    <th>名称</th>
	    <th>数量</th>
	    <th>操作</th>
	  </tr>
	  
<% ArrayList<CartItemInfo> items
     = (ArrayList<CartItemInfo>)request.getAttribute("cart");
   for(CartItemInfo item : items) { %>
  <tr class="cart-item">
    <td class="c-isbn"><%= item.getIsbn() %></td>
    <td class="c-name"><%= item.getName() %></td>
    <td class="c-cnt"><%= String.valueOf(item.getCount()) %></td>
    <td class="c-ops">
      <a class="fixbtn">修改数量</a> | <a class="rmbtn">删除</a>
    </td> 
  </tr>
<% } %>

	</table>

    <hr />
	
	<div class="panel-body">
      <div class="row btn-row">
	    <div class="col-md-3 cart-col">
          <input type="button" id="clearbtn" value="清空" class="btn btn-block btn-primary cart-btns"/>
	    </div>
	    <div class="col-md-3 cart-col">
          <input type="button" id="orderbtn" value="下单" class="btn btn-block btn-primary cart-btns"/>
	    </div>
      </div>
	</div><!-- panel-body -->
	
  </div><!-- panel -->
	
  <script src="./js/cart.js" charset="UTF-8"></script>
	 
<jsp:include page="./footer.jsp" />