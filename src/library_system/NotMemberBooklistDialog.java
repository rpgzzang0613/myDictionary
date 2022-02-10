package library_system;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class NotMemberBooklistDialog extends JDialog {
	private JTextArea bookList_jta = new JTextArea();
	private JScrollPane bookList_jsp = new JScrollPane(bookList_jta);
	
	public void setBookList(ArrayList<Book> searchBookArr) {
		bookList_jta.setText("��ȣ\t\t����\t\t����\t\t�帣\t\t�뿩����\n");
		for(Book book : searchBookArr) {
			String CanLend = book.getCanLend() ? "�뿩 ����" : "�뿩 ��";
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
