package library_system;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class MemberBooklistDialog extends JDialog {
	private JTextArea bookList_jta = new JTextArea();
	private JScrollPane bookList_jsp = new JScrollPane(bookList_jta);
	
	JButton borrow_jbt = new JButton("대여 신청");
	JButton search_jbt = new JButton("도서 검색");
	JButton viewAll_jbt = new JButton("전체 목록");
	private JPanel south_jp = new JPanel();
	
	private BookPro bookPro = new BookProImpl();
	
	public void setAllBooklist() {
		ArrayList<Book> allBookArr = bookPro.view();
		bookList_jta.setText("번호\t\t제목\t\t저자\t\t장르\t\t대여상태\n");
		for(Book book : allBookArr) {
			String canLend = book.getCanLend() ? "대여가능" : "대여중";
			bookList_jta.append(book.getNo()+"\t\t"+book.getTitle()+"\t\t"+book.getWriter()+"\t\t"+book.getGenre()+"\t\t"+canLend+"\n");
		}
	}
	
	public void setBooklist(ArrayList<Book> searchBookArr) {
		bookList_jta.setText("번호\t\t제목\t\t저자\t\t장르\t\t대여상태\n");
		for(Book book : searchBookArr) {
			String canLend = book.getCanLend() ? "대여가능" : "대여중";
			bookList_jta.append(book.getNo()+"\t\t"+book.getTitle()+"\t\t"+book.getWriter()+"\t\t"+book.getGenre()+"\t\t"+canLend+"\n");
		}
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add("Center", bookList_jsp);
		bookList_jta.setEnabled(false);
		bookList_jta.setFont(new Font("", Font.BOLD, 15));
		con.add("South", south_jp);
		south_jp.setLayout(new GridLayout(1,3));
		south_jp.add(borrow_jbt);
		south_jp.add(search_jbt);
		south_jp.add(viewAll_jbt);
	}
	
	public MemberBooklistDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		this.setAllBooklist();
		
		super.setSize(1000, 600);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos-100);
		super.setResizable(false);
	}
}
