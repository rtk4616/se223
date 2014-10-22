package proj.Entity;

import java.io.Serializable;

public class BookInfo
  implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String isbn = ""; //pk
	private String name = "";
	
	public String getIsbn() { return this.isbn; }
	public void setIsbn(String isbn) { this.isbn = isbn; }
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
}
