package library_system;

import java.util.*;

public interface BookPro {
	public char input(Book newBook);
	public boolean edit(Book editBook);
	public boolean delete(int no);
	public boolean duplChk(int no);
	public ArrayList<Book> searchFromDB(String tfTitle);
	public ArrayList<Book> view();
	public ArrayList<Book> view(String column, String value);
	public Book getBook(int no);
	public String numberToTitle(int no);
	public char getCanLend(int no);
	public void setCanLendNo(int no);
	public void setCanLendYes(int no);
}
