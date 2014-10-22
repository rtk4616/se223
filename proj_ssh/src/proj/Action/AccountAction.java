package proj.Action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import proj.Entity.AccountInfo;
import proj.Entity.OrderItemInfo;
import proj.Entity.UserInfo;
import proj.Util.Common;
import proj.Util.DBConfig;
import proj.Util.HibernateUtil;

import com.opensymphony.xwork2.ActionSupport;

public class AccountAction extends ActionSupport
{
	private static final long serialVersionUID = 1L;

	//页面转发
    private static final String ACCOUNT = "account";
    
    private HttpServletRequest request;
	private HttpServletResponse response;
	private UserInfo usr;
	private PrintWriter writer;
	private HttpSession session;
	
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
			request.setAttribute("IS_LOGIN", true);
			if(usr.getId().equals(1))
				request.setAttribute("IS_ADMIN", true);
			else
			{
				writer.write("<script>location.href=\"./index.action\";</script>");
		        return NONE;
			}
		}
		else
	    {
	        writer.write("<script>location.href=\"./index.action\";</script>");
	        return NONE;
	    }
		
		try
		{
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			SQLQuery sql
			  = session.createSQLQuery("SELECT u_id, sum(b_num) FROM orders " +
					                   "natural join orderitems GROUP BY u_id");
			@SuppressWarnings("unchecked")
			ArrayList<Object[]> res = (ArrayList<Object[]>) sql.list();
			session.getTransaction().commit();
			ArrayList<AccountInfo> list = new ArrayList<AccountInfo>();
			for(Object[] row : res)
			{
				AccountInfo item = new AccountInfo();
				item.setUid((Integer)row[0]);
				item.setNum(((BigDecimal)row[1]).intValue());
				list.add(item);
			}
			request.setAttribute("list", list);
			return ACCOUNT;
		}
	    catch(Exception ex)
        {
	    	writer.write(Common.show_msg(ex.getMessage(), "./index.action")); 
	    	return NONE;
	    }
	}

}
