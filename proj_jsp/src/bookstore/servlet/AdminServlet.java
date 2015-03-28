package bookstore.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import java.sql.*;

import bookstore.entitybean.UserBean;
import bookstore.utility.Common;
import bookstore.utility.DBConn;
import bookstore.utility.PageName;

@WebServlet("/" + PageName.ADMIN_PG)
public class AdminServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 14L;
	
	
	private HttpServletRequest request;
	private HttpServletResponse response;
    private UserBean usr;
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
		usr = new UserBean();
		usr.getCookie(request);
		if(!usr.isValid())
		{
		    writer.print(Common.app_error(1, "用户未登录"));
		    return;
		}
		
		String action = request.getParameter("action");
		if(action == null)
			action = "";
		if(action.equals("rmuser"))
			doRmUser();
		else
			writer.print(Common.app_error(2, "未指定操作"));
		
		
	}

	private void doRmUser() 
	{
		try 
		{
			if(!usr.getId().equals("1"))
		    {
	           	writer.write(Common.app_error(3, "无操作权限"));
		       	return;
		    }
			
			String idstr = request.getParameter("id");
			if(idstr == null) idstr = "";
			if(!idstr.matches("^\\d+$"))
			{
				writer.write(Common.app_error(9, "id格式有误"));
		        return;
			}
			int id = Integer.parseInt(idstr);
			if(id == 1)
			{
				writer.write(Common.app_error(10, "不允许删除管理员"));
		        return;
			}
			
			Connection conn = DBConn.getDbConn();
			String sql = "DELETE FROM Users WHERE u_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
			if(stmt.getUpdateCount() == 0)
			{
				writer.write(Common.app_error(8, "用户不存在"));
	        	return;
			}
			
			JSONObject json = new JSONObject();
		    json.put("errno", 0);
		    writer.write(json.toJSONString());
		}
        catch(Exception ex)
        { writer.write(Common.app_error(2, ex.getMessage())); }
		
	}

}
