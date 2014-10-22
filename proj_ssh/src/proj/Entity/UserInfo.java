package proj.Entity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.discuz.AuthCode;

public class UserInfo 
{
	private Integer id = 0; //PK
	private String un = "";
	private String pw = "";
	
	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }
	public String getUn() { return this.un; }
	public void setUn(String un) { this.un = un; }
	public String getPw() { return this.pw; }
	public void setPw(String pw) { this.pw = pw; } 
	
	public void SetUInfo(Integer id, String un, String pw)
	{
		this.id = id;
		this.un = un;
		this.pw = pw;
	}
	
	public boolean  IsValid()
	{
		return !id.equals(0) && !un.equals("") && !pw.equals("");
	}
	
	public void SetCookie(HttpServletResponse response)
	{
		String uid = id.toString();
		String token = uid + "|" + un + "|" + pw;
		token = AuthCode.Encode(token, "");
	    Cookie co = new Cookie("token", token);
	    co.setMaxAge(3600 * 24 * 30);
	    response.addCookie(co);	
	}
	
	public void GetCookie(HttpServletRequest request)
	{
	    Cookie[] cookies = request.getCookies();
	    Cookie co = FindCookie(cookies, "token");
	    if(co == null) return;
	    String token = co.getValue();
	    token = AuthCode.Decode(token, "");
	    if(token.length() == 0) return;
	    String[] arr = token.split("\\|");
	    if(arr.length < 3) return;
	    id = Integer.parseInt(arr[0]);
	    un = arr[1];
	    pw = arr[2];
	}
	
	private static Cookie FindCookie(Cookie[] cookies, String name)
	{
		if(cookies == null) return null;
		for(Cookie co : cookies)
	    {
	    	if(co.getName().compareTo(name) == 0)
	    	  return co;
	    }
		return null;
	}
}
