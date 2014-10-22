package com.proj2;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.Vector;

public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
    private HttpServletResponse response;
    private UserInfo usr;
    private PrintWriter writer;
    private HttpSession session;
	
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
		request.setAttribute("IN_USE", true);
		usr = new UserInfo();
		usr.GetCookie(request);
		if(usr.IsValid())
		{
		    request.setAttribute("IS_LOGIN", true);
			if(usr.GetUID().compareTo("1") == 0)
			    request.setAttribute("IS_ADMIN", true);
		}
		
		String action = request.getParameter("action");
		if(action == null) action = "";
		if(action.compareTo("login") == 0)
		    doLogin();
		else if(action.compareTo("logout") == 0)
		    doLogout();
		else if(action.compareTo("reg") == 0)
		    doReg();
		else if(action.compareTo("book") == 0)
		    doBook();
		else if(action.compareTo("cart") == 0)
		    doCart();
		else if(action.compareTo("order") == 0)
		    doOrder();
		else if(action.compareTo("admin") == 0)
			doAdmin();
		else //index.jsp
		{
		    request.setAttribute("un", usr.GetUN());
			request.getRequestDispatcher("./template/index.jsp")
			       .forward(request, response);
		}
	}
	
	private void doLogin()
	{
		try {
			
		if(usr.IsValid())
		{
		    writer.write("<script>location.href=\"./index.jsp\";</script>");
		    return;
		}
		
		String un = request.getParameter("un"),
		       pw = request.getParameter("pw");
		if(un == null) un = "";
		if(pw == null) pw = "";
		if(un.length() == 0)
		{
		    request.getRequestDispatcher("./template/login.jsp")
		           .forward(request, response);
		}
		if(pw.length() == 0)
		{
		    writer.write(Common.show_msg("请输入密码!", "./index.jsp?action=login"));
		    return;
		}
		pw = Common.MD5(pw);
		
		DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
		Connection conn = DriverManager.getConnection(url);
		
		String sql = "SELECT * FROM Users WHERE u_un=? and u_pw=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, un);
		stmt.setString(2, pw);
		ResultSet res = stmt.executeQuery();
		if(!res.next())
		{
		 	writer.write(Common.show_msg("用户不存在或密码错误!",
		      		     "./index.jsp?action=login"));
		  	return;
		}
		
		int uid = res.getInt(1);
		usr.SetUInfo(String.valueOf(uid), un, pw);
		usr.SetCookie(response);
		writer.write(Common.show_msg("登录成功!", "./index.jsp"));
		
		} catch(SQLException sqlex) 
		{ writer.write(Common.show_msg("登录失败：" + sqlex.getMessage(),
		                               "./index.jsp")); }
		catch(Exception ex)
		{ writer.write(Common.show_msg("未知错误", "./index.jsp")); }
	}
	
	private void doLogout()
	{
		if(usr.IsValid())
  	     {
  		     Cookie co = new Cookie("token", "");
  		     response.addCookie(co);
  	     }
   	     writer.write("<script>location.href=\"./index.jsp\";</script>");
	}
	
	private void doReg()
	{
		try {
    		
	    if(usr.IsValid())
	    {
	         writer.write("<script>location.href=\"./index.jsp\";</script>");
	         return;
	    }  
	        	  
	    String un = request.getParameter("un"),
	 	       pw = request.getParameter("pw");
	 	if(un == null) un = "";
	 	if(pw == null) pw = "";
	 	if(un.length() == 0)
	 	{
	 	     request.getRequestDispatcher("./template/reg.jsp")
	 	            .forward(request, response);
	 	}
	 	if(pw.length() < 6 || pw.length() > 16)
	 	{
	 	     writer.write(Common.show_msg("密码应为6~16位!", "./index.jsp?action=reg"));
	 	     return;
	 	}
	 	pw = Common.MD5(pw);  
	 	     
	 	DBConfig cfg = new DBConfig();
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
		Connection conn = DriverManager.getConnection(url);
		     
		String sql = "INSERT INTO users (u_un, u_pw) VALUES (?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, un);
		stmt.setString(2, pw);
		/*if(stmt.executeUpdate() == 0)
		{
		  	 writer.write(Common.show_msg("用户已存在！", "./index.jsp?action=reg"));
		   	 return; 
		}*/
		try { stmt.executeUpdate(); }
		catch(Exception ex)
		{
			writer.write(Common.show_msg("用户已存在！", "./index.jsp?action=reg"));
		   	return;
		}
		
		ResultSet res = stmt.getGeneratedKeys();
		res.next();
		int uid = res.getInt(1);
		usr.SetUInfo(String.valueOf(uid), un, pw);
		usr.SetCookie(response);
		writer.write(Common.show_msg("注册成功!", "./index.jsp"));
		     
	    } catch(SQLException sqlex) 
	 	{ writer.write(Common.show_msg("注册失败：" + sqlex.getMessage(),
	                                   "./index.jsp")); }
	 	catch(Exception ex)
	 	{ writer.write(Common.show_msg("未知错误", "./index.jsp")); }
	}
	
	private void doCart()
			throws ServletException, IOException
	{
	     //try {
   		 
	     if(!usr.IsValid())
	     {
	        writer.write("<script>location.href=\"./index.jsp?action=login\";</script>");
	        return;
	     } 
	    	 
       	 Vector<CartItemInfo> cart = (Vector<CartItemInfo>)session.getAttribute("cart");
       	 if(cart == null)
       	 {
       		cart = new Vector<CartItemInfo>();
       	    session.setAttribute("cart", cart);
       	 }

       	 request.setAttribute("cart", cart);
      	 request.getRequestDispatcher("./template/cart.jsp")
      	        .forward(request, response);
	}
	
	private void doBook()
			throws ServletException, IOException
	{
		if(!usr.IsValid())
        {
            writer.write("<script>location.href=\"./index.jsp?action=login\";</script>");
            return;
        } 
        
        Vector<BookInfo> list = this.GetBookInfo();
        if(list == null)
        {
       	 Common.show_msg("图书加载失败！", "./index.jsp");
        }
        else
        {
            request.setAttribute("books", list);
            request.getRequestDispatcher("./template/book.jsp")
                   .forward(request, response);
        }
	}
	
	private void doOrder()
			throws ServletException, IOException
	{
		if(!usr.IsValid())
        {
            writer.write("<script>location.href=\"./index.jsp?action=login\";</script>");
            return;
        } 
		
		request.getRequestDispatcher("./template/order.jsp")
	           .forward(request, response);
	}
	
	private Vector<BookInfo> GetBookInfo()
            throws IOException
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
        Vector<BookInfo> list = new Vector<BookInfo>();
        while(res.next())
           list.add(new BookInfo(res.getString(1), res.getString(2)));
        return list;

        } catch(Exception ex) { return null; }
    }
	
	private void doAdmin()
	{	
		try {
		
		if(usr.GetUID().compareTo("1") != 0)
		{
			writer.write("<script>location.href=\"./index.jsp?action=login\";</script>");
            return;
		}
		
		DBConfig cfg = new DBConfig();
	 	Class.forName(cfg.GetDriver());
	 	String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
	 	             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
  		             "&password=" + cfg.GetPassword();
      	Connection conn = DriverManager.getConnection(url);
      	 
      	String sql = "SELECT u_id, u_un FROM users";
      	Statement stmt = conn.createStatement();
      	ResultSet res = stmt.executeQuery(sql);
      	Vector<UserInfo> users = new Vector<UserInfo>();
      	while(res.next())
      	{
      	   UserInfo ui = new UserInfo();
      	   ui.SetUInfo(String.valueOf(res.getInt(1)), res.getString(2), "");
      	   users.add(ui);
      	}
      	request.setAttribute("users", users);
      	
        request.getRequestDispatcher("./template/admin.jsp")
               .forward(request, response);
		 
		} catch(SQLException sqlex) 
	    { writer.write(Common.show_msg("获取用户失败：" + sqlex.getMessage(),
	                                   "./index.jsp")); }
	    catch(Exception ex)
	    { writer.write(Common.show_msg("未知错误", "./index.jsp")); }
		
	}

}
