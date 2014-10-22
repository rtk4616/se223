package proj.Action;

import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import proj.Entity.UserInfo;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport
{
	//version id
	private static final long serialVersionUID = 1L;
	
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
  		     Cookie co = new Cookie("token", "");
  		     response.addCookie(co);
  	     }
   	     writer.write("<script>location.href=\"./index.action\";</script>");
   	     return NONE;
	}

}
