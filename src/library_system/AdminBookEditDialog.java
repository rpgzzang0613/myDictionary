package library_system;

import java.awt.*;
import javax.swing.*;

public class AdminBookEditDialog extends JDialog {
	private JLabel edit_jlb = new JLabel("도서 수정", JLabel.CENTER);
	private JLabel no_jlb = new JLabel("번호 : ", JLabel.RIGHT);
	private JLabel title_jlb = new JLabel("제목 : ", JLabel.RIGHT);
	private JLabel writer_jlb = new JLabel("저자 : ", JLabel.RIGHT);
	private JLabel genre_jlb = new JLabel("장르 : ", JLabel.RIGHT);
	private JLabel canLend_jlb = new JLabel("대여상태 : ", JLabel.RIGHT);
	
	private JTextField no_jtf = new JTextField();
	private JTextField title_jtf = new JTextField();
	private JTextField writer_jtf = new JTextField();
	private JTextField genre_jtf = new JTextField();
	private JTextField canLend_jtf = new JTextField();
	
	private JPanel no_jp = new JPanel();
	private JPanel title_jp = new JPanel();
	private JPanel writer_jp = new JPanel();
	private JPanel genre_jp = new JPanel();
	private JPanel canLend_jp = new JPanel();
	private JPanel btn_jp = new JPanel();
	
	JButton edit_jbt = new JButton("수정");
	JButton clear_jbt = new JButton("초기화");
	
	public void setTextField(Book book) {
		no_jtf.setText(String.valueOf(book.getNo()));
		title_jtf.setText(book.getTitle());
		writer_jtf.setText(book.getWriter());
		genre_jtf.setText(book.getGenre());
		canLend_jtf.setText(book.getCanLend() == true ? "대여 가능" : "대여중");
		
		no_jtf.setEnabled(false);
		canLend_jtf.setEnabled(false);
	}
	
	public Book getBookToEdit() {
		boolean canLend = canLend_jtf.getText().equals("대여 가능") ? true : false;
		Book editBook = new Book(Integer.parseInt(no_jtf.getText()), title_jtf.getText(), writer_jtf.getText(), genre_jtf.getText(), canLend);
		return editBook;
	}
	
	public void clearAllTextField() {
		no_jtf.setText("");
		title_jtf.setText("");
		writer_jtf.setText("");
		genre_jtf.setText("");
		canLend_jtf.setText("");
	}
	
	public void clearTextField() {
		title_jtf.setText("");
		writer_jtf.setText("");
		genre_jtf.setText("");
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(7,1));
		con.add(edit_jlb);
		con.add(no_jp);
		con.add(title_jp);
		con.add(writer_jp);
		con.add(genre_jp);
		con.add(canLend_jp);
		con.add(btn_jp);
		
		edit_jlb.setFont(new Font("", Font.BOLD, 20));
		
		no_jp.setLayout(new BorderLayout());
		title_jp.setLayout(new BorderLayout());
		writer_jp.setLayout(new BorderLayout());
		genre_jp.setLayout(new BorderLayout());
		canLend_jp.setLayout(new BorderLayout());
		btn_jp.setLayout(new GridLayout(1,2));
		
		no_jp.add("West", no_jlb);
		no_jp.add("Center", no_jtf);
		title_jp.add("West", title_jlb);
		title_jp.add("Center", title_jtf);
		writer_jp.add("West", writer_jlb);
		writer_jp.add("Center", writer_jtf);
		genre_jp.add("West", genre_jlb);
		genre_jp.add("Center", genre_jtf);
		canLend_jp.add("West", canLend_jlb);
		canLend_jp.add("Center", canLend_jtf);
		btn_jp.add(edit_jbt);
		btn_jp.add(clear_jbt);
		
		no_jp.setEnabled(false);
		canLend_jtf.setEnabled(false);
	}
	
	public AdminBookEditDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		
		super.setSize(300, 200);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos);
		super.setResizable(false);
	}
}
