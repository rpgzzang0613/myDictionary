package library_system;

import java.awt.*;
import javax.swing.*;

public class MemberPasswordInputDialog extends JDialog {
	private JLabel pw_jlb = new JLabel("비밀번호를 입력해 주세요.");
	private JPasswordField pw_jpf = new JPasswordField();
	JButton confirm_jbt = new JButton("확인");
	
	public String getPassword() {
		String pw = new String(pw_jpf.getPassword());
		return pw;
	}
	
	public void clearPassword() {
		pw_jpf.setText("");
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(3,1));
		con.add(pw_jlb);
		con.add(pw_jpf);
		con.add(confirm_jbt);
	}
	
	public MemberPasswordInputDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		this.init();
		
		super.setSize(250, 140);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos);
		super.setResizable(false);
	}
}
