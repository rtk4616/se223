package com.proj2;

public class CartItemInfo 
{
	private String isbn;
	private String name;
	private int count;
  
	public CartItemInfo(String isbn_, String name_, int count_)
	{
		isbn = isbn_;
		name = name_;
		count = count_;
	}
	
	public String GetIsbn()
	{
		return isbn;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public int GetCount()
	{
		return count;
	}
	
	public void SetIsbn(String isbn_)
	{
		isbn = isbn_;
	}
	
	public void SetName(String name_)
	{
		name = name_;
	}
	
	public void SetCount(int count_)
	{
		count = count_;
	}
}
