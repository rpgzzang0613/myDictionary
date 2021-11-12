package library_system;

import java.awt.*;
import javax.swing.*;

public class AdminBookAddDialog extends JDialog {
	private JLabel bookAdd_jlb = new JLabel("도서 추가", JLabel.CENTER);
	private JLabel title_jlb = new JLabel("제목 : ", JLabel.RIGHT);
	private JLabel writer_jlb = new JLabel("저자 : ", JLabel.RIGHT);
	private JLabel genre_jlb = new JLabel("장르 : ", JLabel.RIGHT);
	
	private JTextField title_jtf = new JTextField();
	private JTextField writer_jtf = new JTextField();
	private JTextField genre_jtf = new JTextField();
	
	private JPanel title_jp = new JPanel();
	private JPanel writer_jp = new JPanel();
	private JPanel genre_jp = new JPanel();
	private JPanel btn_jp = new JPanel();
	
	JButton add_jbt = new JButton("추가");
	JButton clear_jbt = new JButton("초기화");
	
	public void clearAllTextField() {
		title_jtf.setText("");
		writer_jtf.setText("");
		genre_jtf.setText("");
	}
	
	public Book getBook() {
		Book book = new Book(title_jtf.getText(), writer_jtf.getText(), genre_jtf.getText());
		return book;
	}
	
	public String getTitle() {
		String title = title_jtf.getText();
		return title;
	}
	
	public String getWriter() {
		String writer = writer_jtf.getText();
		return writer;
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(5,1));
		con.add(bookAdd_jlb);
		con.add(title_jp);
		con.add(writer_jp);
		con.add(genre_jp);
		con.add(btn_jp);
		
		bookAdd_jlb.setFont(new Font("", Font.BOLD, 20));
		
		title_jp.setLayout(new BorderLayout());
		writer_jp.setLayout(new BorderLayout());
		genre_jp.setLayout(new BorderLayout());
		btn_jp.setLayout(new GridLayout(1,2));
		
		title_jp.add("West", title_jlb);
		title_jp.add("Center", title_jtf);
		writer_jp.add("West", writer_jlb);
		writer_jp.add("Center", writer_jtf);
		genre_jp.add("West", genre_jlb);
		genre_jp.add("Center", genre_jtf);
		btn_jp.add(add_jbt);
		btn_jp.add(clear_jbt);
	}
	
	public AdminBookAddDialog(JFrame owner, String title, boolean modal) {
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
