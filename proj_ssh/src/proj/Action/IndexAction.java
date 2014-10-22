package proj.Action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import proj.Entity.UserInfo;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends ActionSupport
{
	//version id
	private static final long serialVersionUID = 1L;
	
	private static final String INDEX = "index";
	
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
			request.setAttribute("un", usr.getUn());
			if(usr.getId().equals(1))
				request.setAttribute("IS_ADMIN", true);
		}
		
		return INDEX;
	}
	
}
