package proj.Entity;

public class CartItemInfo 
{
	private String isbn = "";
	private String name = "";
	private int count;
  
	public CartItemInfo() {}
	
	public CartItemInfo(String isbn_, String name_, int count_)
	{
		isbn = isbn_;
		name = name_;
		count = count_;
	}
	
	public String getIsbn()
	{
		return isbn;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setIsbn(String isbn_)
	{
		isbn = isbn_;
	}
	
	public void setName(String name_)
	{
		name = name_;
	}
	
	public void setCount(int count_)
	{
		count = count_;
	}
}
