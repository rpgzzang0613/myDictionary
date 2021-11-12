package library_system;

import java.awt.*;
import javax.swing.*;

public class NotMemberLoginDialog extends JDialog {
	private JLabel login_jlb = new JLabel("Login", JLabel.CENTER);
	private JLabel id_jlb = new JLabel("ID : ", JLabel.RIGHT);
	private JLabel pw_jlb = new JLabel("PW : ", JLabel.RIGHT);
	private JTextField id_jtf = new JTextField();
	private JPasswordField pw_jpf = new JPasswordField();
	JButton login_jbt = new JButton("로그인");						// LibraryMainFrame에서 사용할 멤버
	JButton signIn_jbt = new JButton("회원 가입");						// LibraryMainFrame에서 사용할 멤버
	private JPanel id_jp = new JPanel();
	private JPanel pw_jp = new JPanel();
	private JPanel bottom_jp = new JPanel();
	
	public String getId() {
		return id_jtf.getText();
	}
	
	public String getPw() {
		String pw = new String(pw_jpf.getPassword());
		
		return pw;
	}
	
	public void clearAllTextField() {
		id_jtf.setText("");
		pw_jpf.setText("");
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(4,1));
		con.add(login_jlb);
		con.add(id_jp);
		con.add(pw_jp);
		con.add(bottom_jp);
		
		login_jlb.setFont(new Font("", Font.BOLD, 20));
		
		id_jp.setLayout(new BorderLayout());
		id_jp.add("West", id_jlb);
		id_jp.add("Center", id_jtf);
		
		pw_jp.setLayout(new BorderLayout());
		pw_jp.add("West", pw_jlb);
		pw_jp.add("Center", pw_jpf);
		
		bottom_jp.setLayout(new GridLayout(1,2));
		bottom_jp.add(login_jbt);
		bottom_jp.add(signIn_jbt);
	}
	
	NotMemberLoginDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		
		super.setSize(250, 180);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos);
		super.setResizable(false);
	}
}
