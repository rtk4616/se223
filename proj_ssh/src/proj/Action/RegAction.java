package proj.Action;

import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import proj.Entity.UserInfo;
import proj.Util.Common;
import proj.Util.HibernateUtil;

import com.opensymphony.xwork2.ActionSupport;

public class RegAction extends ActionSupport
{
	//version id
	private static final long serialVersionUID = 1L;
			
	//页面转发
	private static final String REG = "reg";
		
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
		if(un.length() == 0) return REG;
		if(pw.length() < 6 || pw.length() > 16)
		{
		     writer.write(Common.show_msg("密码应为6~16位!", "./reg.action"));
		     return NONE;
		}
		pw = Common.MD5(pw); 
		
		try 
		{		        	    		 	
		 	Session session 
			  = HibernateUtil.getSession();
		    session.beginTransaction();
		    UserInfo u = new UserInfo();
		    u.setUn(un);
		    u.setPw(pw);
		    session.save(u);
		    session.getTransaction().commit();

			u.SetCookie(response);
			writer.write(Common.show_msg("注册成功!", "./index.action"));
			return NONE;
		}
		catch (ConstraintViolationException ex)
		{
			writer.write(Common.show_msg("用户已存在！", "./index.action"));
			return NONE;
		}
		catch(Exception ex)
		{ 
			writer.write(Common.show_msg("未知错误：" + ex.getLocalizedMessage(), 
					                     "./index.action"));
			return NONE;
		}
	}

}