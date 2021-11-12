package library_system;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class NotMemberBooklistDialog extends JDialog {
	private JTextArea bookList_jta = new JTextArea();
	private JScrollPane bookList_jsp = new JScrollPane(bookList_jta);
	
	public void setBookList(ArrayList<Book> searchBookArr) {
		bookList_jta.setText("번호\t\t제목\t\t저자\t\t장르\t\t대여상태\n");
		for(Book book : searchBookArr) {
			String CanLend = book.getCanLend() ? "대여 가능" : "대여 중";
			bookList_jta.append(book.getNo()+"\t\t"+book.getTitle()+"\t\t"+book.getWriter()+"\t\t"+book.getGenre()+"\t\t"+CanLend+"\n");
		}
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.add(bookList_jsp);
		bookList_jta.setEnabled(false);
		bookList_jta.setFont(new Font("", Font.BOLD, 15));
	}
	
	public NotMemberBooklistDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		
		super.setSize(1000, 600);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos-100);
		super.setResizable(false);
	}
}
