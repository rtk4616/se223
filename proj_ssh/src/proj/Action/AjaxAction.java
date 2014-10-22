package proj.Action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;

import proj.Entity.BookInfo;
import proj.Entity.CartItemInfo;
import proj.Entity.UserInfo;
import proj.Util.Common;
import proj.Util.DBConfig;
import proj.Util.HibernateUtil;

import com.opensymphony.xwork2.ActionSupport;

public class AjaxAction extends ActionSupport
{
	//version id
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserInfo usr;
	private PrintWriter writer;
	private HttpSession session;
	
	//表单：name
    private String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    
    //表单：isbn
    private String isbn;
    public String getIsbn() { return this.isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    //表单：num
    private String num;
    public String getNum() { return this.num; }
    public void setNum(String num) { this.num = num; }
    
    //表单：action
    private String action;
    public String getAction() { return this.action; }
    public void setAction(String action) { this.action = action; }
    
    //表单：id
    private String id;
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
	
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
		if(!usr.IsValid())
		{
		  writer.print(Common.app_error(1, "用户未登录"));
		  return NONE;
		}
		  
		String action = this.action;
		if(action == null) action = "";
		if(action.equals("addbook"))
		    doAddBook();
		else if(action.equals("rmbook"))
		    doRmBook();
		else if(action.equals("addcart"))
		    doAddCart();
		else if(action.equals("clearcart"))
		    doClearCart();
		else if(action.equals("rmcart"))
		    doRmCart();
		else if(action.equals("fixcart"))
		    doFixCart();
		else if(action.equals("rmuser"))
			doRmUser();
		else if(action.equals("addorder"))
			doAddOrder();
		else
		    writer.print(Common.app_error(2, "未指定操作"));
		  
		return NONE;
	}
	
	private void doAddBook()
	{
		try 
		{	
			
	    if(!usr.getId().equals(1))
	    {
	       	writer.write(Common.app_error(3, "无操作权限"));
	       	return;
	    }
	        
        String name = this.name;
        String isbn = this.isbn;
        if(name == null) name = "";
        if(isbn == null) isbn = "";
        if(name.length() == 0)
        {
        	writer.write(Common.app_error(4, "请输入名称"));
        	return;
        }
        if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }

        Session session = HibernateUtil.getSession();
		session.beginTransaction();
		BookInfo book = new BookInfo();
		book.setIsbn(isbn);
		book.setName(name);
		session.save(book);
		session.getTransaction().commit();
	    
	    JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	    	
    	} 
		catch (ConstraintViolationException ex)
		{
			writer.write(Common.app_error(2, "图书已存在！"));
		}
		catch(Exception ex)
        { 
        	writer.write(Common.app_error(2, ex.getMessage())); 
        }
	}
	
	private void doRmBook()
	{
		try {
			
		if(!usr.getId().equals(1))
	    {
           	writer.write(Common.app_error(3, "无操作权限"));
	       	return;
	    }	
				
		String isbn = this.isbn;
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }

	    Session session = HibernateUtil.getSession();
		session.beginTransaction();
		//BookInfo book = (BookInfo) session.load(BookInfo.class, isbn);
		SQLQuery sql = session.createSQLQuery("SELECT * FROM books WHERE isbn=?");
		sql.setString(0, isbn);
		sql.addEntity(BookInfo.class);
		BookInfo book = (BookInfo) sql.uniqueResult();
		if(book == null)
		{
			session.getTransaction().commit();
			writer.write(Common.app_error(8, "图书不存在"));
        	return;
		}
		session.delete(book);
		session.getTransaction().commit();
		
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
				
		}
        catch(Exception ex)
        { writer.write(Common.app_error(2, ex.getMessage())); }
	}
	
	
	/*private void doQueryBook()
	{
		try 
		{
			
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			SQLQuery sql = session.createSQLQuery("SELECT * FROM Books");
			sql.addEntity(BookInfo.class);
			@SuppressWarnings("unchecked")
			ArrayList<BookInfo> list = (ArrayList<BookInfo>) sql.list();
			session.getTransaction().commit();

            writer.write("{\"errno\":0,\"data\":[");
		    for(BookInfo book : list)
			    writer.write("{\"id\":" + book.getIsbn() + ",\"name\":\"" +
		                 book.getName() + "\"},");
		    writer.write("]}");
		}
        catch(Exception ex)
        { writer.write(Common.app_error(2, ex.getMessage())); }
	}*/
	
	private void doAddCart()
	{
		String isbn = this.isbn;
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }
	    
	    String numstr = this.num;
		if(numstr == null) numstr = "";
		if(!numstr.matches("^\\d+$"))
		{
			writer.write(Common.app_error(9, "数量格式有误"));
	        return;
		}
		int num = Integer.parseInt(numstr);
		
		Session session 
		  = HibernateUtil.getSession();
	    session.beginTransaction();
	    SQLQuery sql = session.createSQLQuery("SELECT * FROM books WHERE isbn=?");
		sql.setString(0, isbn);
		sql.addEntity(BookInfo.class);
		BookInfo book = (BookInfo) sql.uniqueResult();
		if(book == null)
		{
			session.getTransaction().commit();
			writer.write(Common.app_error(8, "图书不存在"));
        	return;
		}
	    session.getTransaction().commit();
			
	    @SuppressWarnings("unchecked")
		ArrayList<CartItemInfo> cart 
	      = (ArrayList<CartItemInfo>)this.session.getAttribute("cart");
		if(cart == null)
			cart = new ArrayList<CartItemInfo>();
			
		boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		int ori_count = cart.get(i).getCount();
	    		cart.get(i).setCount(num + ori_count);
	    		exist = true;
	    		break;
	    	}
	    }
		if(!exist)
		{
		  CartItemInfo item = new CartItemInfo(isbn, book.getName(), num);
		  cart.add(item);
		}
		this.session.setAttribute("cart", cart);
    
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	}
	
	private void doClearCart()
	{	
    	@SuppressWarnings("unchecked")
		ArrayList<CartItemInfo> cart
    	  = (ArrayList<CartItemInfo>)session.getAttribute("cart");
		if(cart == null)
			cart = new ArrayList<CartItemInfo>();
		else
			cart.clear();
		session.setAttribute("cart", cart);
		JSONObject json = new JSONObject();
	    json.put("errno", 0);
	    writer.write(json.toJSONString());
	}		
	
	private void doRmCart()
	{
	
		String isbn = this.isbn;
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }
		    
	    @SuppressWarnings("unchecked")
		ArrayList<CartItemInfo> cart
		  = (ArrayList<CartItemInfo>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemInfo>();

	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		cart.remove(i);
	    		exist = true;
	    		break;
	    	}
	    }
	    session.setAttribute("cart", cart);
	    
	    JSONObject json = new JSONObject();
	    if(exist)
	    {
	    	json.put("errno", 0);
	    }
	    else
	    {
	    	json.put("errno", 7);
	    	json.put("errmsg", "图书不存在");
	    }
	    writer.write(json.toJSONString());
	}
	
	private void doFixCart()
	{
    	
		String isbn = this.isbn;
	    if(isbn == null) isbn = "";
	    if(!isbn.matches("^[\\d\\-]+$"))
        {
        	writer.write(Common.app_error(4, "ISBN格式有误"));
        	return;
        }
    	String numstr = this.num;
	    if(numstr == null) numstr = "";
	    if(!numstr.matches("^\\d+$"))
	    {
	    	writer.write(Common.app_error(9, "数量格式有误"));
	        return;
	    }
	    int num = Integer.parseInt(numstr);
		    
	    @SuppressWarnings("unchecked")
		ArrayList<CartItemInfo> cart
		  = (ArrayList<CartItemInfo>)session.getAttribute("cart");
	    if(cart == null)
	    	cart = new ArrayList<CartItemInfo>();
	    
	    boolean exist = false;
	    for(int i = 0; i < cart.size(); i++)
	    {
	    	if(cart.get(i).getIsbn().equals(isbn))
	    	{
	    		cart.get(i).setCount(num);
	    		exist = true;
	    		break;
	    	}
	    }
	    session.setAttribute("cart", cart);
	    
	    JSONObject json = new JSONObject();
	    if(exist)
	    {
	    	json.put("errno", 0);
	    }
	    else
	    {
	    	json.put("errno", 7);
	    	json.put("errmsg", "图书不存在");
	    }
	    writer.write(json.toJSONString());
	}
	
	private void doRmUser()
	{
		try 
		{
			if(!usr.getId().equals(1))
		    {
	           	writer.write(Common.app_error(3, "无操作权限"));
		       	return;
		    }
			
			String idstr = this.id;
			if(idstr == null) idstr = "";
			if(!idstr.matches("^\\d+$"))
			{
				writer.write(Common.app_error(9, "格式有误"));
		        return;
			}
			int id = Integer.parseInt(idstr);
			if(id == 1)
			{
				writer.write(Common.app_error(10, "不允许删除管理员"));
		        return;
			}
			
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			SQLQuery sql = session.createSQLQuery("SELECT * FROM Users WHERE u_id=?");
			sql.setInteger(0, id);
			sql.addEntity(UserInfo.class);
			@SuppressWarnings("unchecked")
			ArrayList<UserInfo> users = (ArrayList<UserInfo>)sql.list();
			if(users.size() == 0)
			{
				session.getTransaction().commit();
				writer.write(Common.app_error(8, "用户不存在"));
	        	return;
			}
			UserInfo user = users.get(0);
			session.delete(user);
			session.getTransaction().commit();
			
			JSONObject json = new JSONObject();
		    json.put("errno", 0);
		    writer.write(json.toJSONString());
		}
        catch(Exception ex)
        { writer.write(Common.app_error(2, ex.getMessage())); }
	}
	
	private void doAddOrder()
	{
		try
		{
			@SuppressWarnings("unchecked")
			ArrayList<CartItemInfo> cart 
			  = (ArrayList<CartItemInfo>)this.session.getAttribute("cart");
			if(cart == null)
			{
			    cart = new ArrayList<CartItemInfo>();
			    this.session.setAttribute("cart", cart);
			}
			if(cart.size() == 0)
			{
				writer.write(Common.app_error(8, "购物车中无图书"));
	        	return;
			}
			
			DBConfig cfg = new DBConfig();
			Class.forName(cfg.GetDriver());
			String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		                 "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		                 "&password=" + cfg.GetPassword();
			Connection conn = DriverManager.getConnection(url);
			
			String sql = "INSERT INTO orders (u_id, o_time) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, usr.getId());
			stmt.setLong(2, new Date().getTime() / 1000);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(!rs.next())
			{
				writer.write(Common.app_error(2, "创建订单失败！"));
				return;
			}
			int orderid = rs.getInt(1);

			sql = "INSERT INTO orderitems VALUES";
			for(CartItemInfo item : cart)
			{
				sql += String.format(" (%d, %s, %d),",
						             orderid, item.getIsbn(), item.getCount());
			}
			sql = sql.substring(0, sql.length() - 1);
			Statement st = conn.createStatement();
			st.execute(sql);
			
			cart.clear();
			this.session.setAttribute("cart", cart);
			JSONObject json = new JSONObject();
		    json.put("errno", 0);
		    writer.write(json.toJSONString());
		}
		catch(SQLException sqlex)
		{
			writer.write(Common.app_error(3, "数据库错误：" + sqlex.getMessage()));
		}
	    catch(Exception ex)
        {
	    	writer.write(Common.app_error(2, ex.getMessage())); 
	    }
	}

}
