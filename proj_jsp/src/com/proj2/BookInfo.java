package com.proj2;

public class BookInfo 
{
  private String isbn;
  private String name;
  
  public BookInfo(String isbn_, String name_)
  {
	  isbn = isbn_;
	  name = name_;
  }
  
  public String GetIsbn()
  {
	  return isbn;
  }
  
  public void SetIsbn(String isbn_)
  {
	  isbn = isbn_;
  }
  
  public String GetName()
  {
	  return name;
  }
  
  public void SetName(String name_)
  {
	  name = name_;
  }
}
