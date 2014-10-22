package proj.Action;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import proj.Entity.BookInfo;
import proj.Entity.OrderItemInfo;
import proj.Entity.UserInfo;
import proj.Util.Common;
import proj.Util.DBConfig;
import proj.Util.HibernateUtil;

import com.opensymphony.xwork2.ActionSupport;

public class OrderAction extends ActionSupport 
{
	//version id
    private static final long serialVersionUID = 1L;
	
    //页面转发
  	private static final String ORDER = "order";
    
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
			  = session.createSQLQuery("SELECT o_id, isbn, b_num, o_time " +
			  		                   "FROM orders natural join orderitems where u_id=?");
			sql.setInteger(0, usr.getId());
			@SuppressWarnings("unchecked")
			ArrayList<Object[]> res = (ArrayList<Object[]>) sql.list();
			session.getTransaction().commit();
			
			ArrayList<OrderItemInfo> list = new ArrayList<OrderItemInfo>();
			for(Object[] row : res)
			{
				OrderItemInfo item = new OrderItemInfo();
				item.setId((Integer)row[0]);
				item.setIsbn((String)row[1]);
				item.setNum((Integer)row[2]);
				item.setTime((Integer)row[3]);
				list.add(item);
			}
			request.setAttribute("order", list);
			return ORDER;
		}
		/*catch(SQLException sqlex)
		{
			writer.write(Common.show_msg("数据库错误：：" + sqlex.getMessage(),
	                                     "./index.action"));
            return NONE;
		}*/
		catch(Exception ex)
		{ 
		   	writer.write(Common.show_msg("未知错误：" + ex.getMessage(),
		   			                     "./index.action"));
		   	return NONE;
		}
	}
}
