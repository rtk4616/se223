package com.proj2;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("unchecked")
public class BookServlet extends HttpServlet 
{
	private static final long serialVersionUID = 2L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserInfo usr;
    private PrintWriter writer;
    private HttpSession session;
 
	//add.php?name=...&isbn=...
	//return {"errno":xxx,"errmsg":"xxx"}

	//rm.php?name=...
	//return {"errno":xxx,"errmsg":"xxx"[, "id"=xxx]}

	//query.php
	//return {"errno":xxx,"errmsg":"xxx","data":[...]}
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    		throws IOException, ServletException
	{
    	req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		request = req;
		response = res;
		writer = res.getWriter();
		session = req.getSession();
		doRequest();
	}
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
    		throws IOException, ServletException
	{
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");
		request = req;
		response = res;
		writer = res.getWriter();
		session = req.getSession();
		doRequest();
	}
	
	private void doRequest()
			throws IOException, ServletException
	{
		  usr = new UserInfo();
		  usr.GetCookie(request);
		  if(!usr.IsValid())
		  {
			  writer.print(Common.app_error(1, "用户未登录"));
			  return;
		  }
		  
		  String action = request.getParameter("action");
		  if(action == null) action = "";
		  if(action.compareTo("add") == 0)
		      doAdd();
		  else if(action.compareTo("rm") == 0)
		      doRm();
		  else if(action.compareTo("addcart") == 0)
		      doAddCart();
		  else //query
		      doQuery();
	}
	
	private void doAdd()
	{
		try {
    		
	    if(usr.GetUID().compareTo("1") != 0)
	    {
	       	writer.write(Common.app_error(3, "无操作权限"));
	       	return;
	    }
	        
        String name = request.getParameter("name");
        String isbn = request.getParameter("isbn");
        if(name == null) name = "";
        if(isbn == null) isbn = "";
        if(name.length() == 0)
        {
        	writer.write(Common.app_error(4, "请输入名称"));
        	return;
        }
        if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }

        DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
	    Connection conn = DriverManager.getConnection(url);
	    
	    String sql = "INSERT INTO books VALUES (?,?)";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setString(1, isbn);
	    stmt.setString(2, name);
	    /*if(stmt.executeUpdate() == 0)
	    {
	    	writer.write(Common.app_error(6, "图书已存在"));
        	return;
	    }*/
	    try { stmt.executeUpdate(); }
	    catch(Exception ex)
	    {
	    	writer.write(Common.app_error(6, "图书已存在"));
        	return;
	    }
	    
	    JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	    	
    	} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }
	}
	
	private void doRm()
	{
		try {
			
		if(usr.GetUID().compareTo("1") != 0)
	    {
           	writer.write(Common.app_error(3, "无操作权限"));
	       	return;
	    }	
				
		String name = request.getParameter("name");
	    if(name == null) name = "";
	    if(name.length() == 0)
	    {
	        writer.write(Common.app_error(4, "请输入名称"));
	        return;
	    }
		        
	    DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
		Connection conn = DriverManager.getConnection(url);
			
		String sql = "DELETE FROM Books WHERE b_name=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, name);
		if(stmt.executeUpdate() == 0)
		{
			writer.write(Common.app_error(8, "图书不存在"));
        	return;
		}
			
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
				
		} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }
	}
	
	
	private void doQuery()
	{
		try {
			
		DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
		Connection conn = DriverManager.getConnection(url);
			
		String sql = "SELECT * FROM Books";
		Statement stmt = conn.createStatement();
		ResultSet res = stmt.executeQuery(sql);

        writer.write("{\"errno\":0,\"data\":[");
		while(res.next())
		{
			String bid = String.valueOf(res.getInt(1));
			String bname = res.getString(2);
			writer.write("{\"id\":" + bid + ",\"name\":\"" + bname + "\"},");
		}
		writer.write("]}");
				
		} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }
	}
	
	@SuppressWarnings("resource")
	private void doAddCart()
	{
		try {
			
		String name = request.getParameter("name");
		if(name == null) name = "";
		if(name.length() == 0)
		{
			writer.write(Common.app_error(4, "请输入名称"));
	        return;
		}
		String countstr = request.getParameter("count");
		if(countstr == null) countstr = "";
		if(!countstr.matches("^\\d+$"))
		{
			writer.write(Common.app_error(9, "数量格式有误"));
	        return;
		}
		int count = Integer.parseInt(countstr);
			
		DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
		Connection conn = DriverManager.getConnection(url);
			
		String sql = "SELECT * FROM Books WHERE b_name=?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setString(1, name);
	    ResultSet res = stmt.executeQuery();
	    if(!res.next())
	    {
	    	writer.write(Common.app_error(7, "图书不存在"));
        	return;
	    }
	    String isbn = res.getString(1);
			
	    Vector<CartItemInfo> cart = (Vector<CartItemInfo>)session.getAttribute("cart");
		if(cart == null)
		{
			cart = new Vector<CartItemInfo>();
			session.setAttribute("cart", cart);
		}
		boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.elementAt(i).GetName().equals(name))
	    	{
	    		int ori_count = cart.elementAt(i).GetCount();
	    		cart.elementAt(i).SetCount(count + ori_count);
	    		exist = true;
	    		break;
	    	}
	    }
		if(!exist)
		{
		  CartItemInfo item = new CartItemInfo(isbn, name, count);
		  cart.add(item);
		}
	    
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
		
		} catch(SQLException sqlex) 
	    { writer.write(Common.sql_error(sqlex)); }
        catch(Exception ex)
        { writer.write(Common.app_error(2, "未知错误")); }
		
	}

}
