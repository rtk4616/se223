package com.proj2;

import org.discuz.AuthCode;
import javax.servlet.http.*;

public class UserInfo
{
	private String uid = "";
	private String un = "";
	private String pw = "";
	
	
	public void SetCookie(HttpServletResponse response)
	{
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
	    uid = arr[0];
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
	
	public String GetUID()
	{
		return uid;
	}
	
	public String GetUN()
	{
		return un;
	}
	
	public String GetPW()
	{
		return pw;
	}
	
	public void SetUInfo(String _uid, String _un, String _pw)
	{
		uid = _uid;
		un = _un;
		pw = _pw;
	}
	
	public boolean  IsValid()
	{
		return !uid.equals("") && !un.equals("") && !pw.equals("");
	}
	
}