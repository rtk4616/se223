package proj.Action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import proj.Entity.UserInfo;
import proj.Util.Common;
import proj.Util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport
{
	//version id
	private static final long serialVersionUID = 1L;
			
	//页面转发
	private static final String LOGIN = "login";
		
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserInfo usr;
	private PrintWriter writer;
	private HttpSession session;
	
	//表单：un
	private String un;	
	public String getUn() { return this.un; }
	public void setUn(String un) { this.un = un; }
		
	//表单：pw
	private String pw;
	public String getPw() { return this.pw; }
	public void setPw(String pw) { this.pw = pw; }
	
	@Override
	public String execute() throws Exception 
	{
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		writer = response.getWriter();
		session = request.getSession();
			
		usr = new UserInfo();
		usr.GetCookie(request);
		if(usr.IsValid())
		{
			writer.write("<script>location.href=\"./index.action\";</script>");
		    return NONE;
		}
		
		String un = this.un,
			   pw = this.pw;
		if(un == null) un = "";
		if(pw == null) pw = "";
		if(un.length() == 0) return LOGIN;
		if(pw.length() == 0)
		{
		    writer.write(Common.show_msg("请输入密码!", "./login.action"));
		    return NONE;
		}
		pw = Common.MD5(pw);
		
		try 
		{			
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			SQLQuery sql = session.createSQLQuery("SELECT * FROM users WHERE u_un=? and u_pw=?");
			sql.setString(0, un);
			sql.setString(1, pw);
			sql.addEntity(UserInfo.class);
			ArrayList<UserInfo> users = (ArrayList<UserInfo>)sql.list();
			session.getTransaction().commit();
			
			if(users.size() == 0)
			{
			 	writer.write(Common.show_msg("用户不存在或密码错误!",
			      		     "./login.action"));
			  	return NONE;
			}
			UserInfo user = users.get(0);
			user.SetCookie(response);
			writer.write(Common.show_msg("登录成功!", "./index.action"));
			return NONE;
		}
		catch(Exception ex)
		{ 
			writer.write(Common.show_msg("未知错误：" + ex.getMessage(),
					     "./index.action")); 
			return NONE;
		}
	}

}
