package library_system;

import java.awt.*;
import javax.swing.*;

public class CommonMemberAddDialog extends JDialog {						// 비회원 회원가입, 관리자 회원추가시 공용으로 사용
	private JLabel signIn_jlb = new JLabel("", JLabel.CENTER);
	private JLabel id_jlb = new JLabel("아이디 : ", JLabel.RIGHT);
	private JLabel pw_jlb = new JLabel("비밀번호 : ", JLabel.RIGHT);
	private JLabel name_jlb = new JLabel("이름 : ", JLabel.RIGHT);
	private JLabel tel_jlb = new JLabel("전화번호 : ", JLabel.RIGHT);
	private JLabel addr_jlb = new JLabel("주소 : ", JLabel.RIGHT);
	
	private JTextField id_jtf = new JTextField();
	private JPasswordField pw_jpf = new JPasswordField();
	private JTextField name_jtf = new JTextField();
	private JTextField tel_jtf = new JTextField();
	private JTextField addr_jtf = new JTextField();
	
	private JPanel id_jp = new JPanel();
	private JPanel pw_jp = new JPanel();
	private JPanel name_jp = new JPanel();
	private JPanel tel_jp = new JPanel();
	private JPanel addr_jp = new JPanel();
	private JPanel btn_jp = new JPanel();
	
	JButton duplChk_jbt = new JButton("중복체크");
	JButton signIn_jbt = new JButton("가입");
	JButton clear_jbt = new JButton("초기화");
	
	public Member getMember() {
		String pw = new String(pw_jpf.getPassword());
		Member newMember = new Member(id_jtf.getText(), pw, name_jtf.getText(), tel_jtf.getText(), addr_jtf.getText());
		return newMember;
	}
	
	public String getId() {
		String newId = id_jtf.getText();
		return newId;
	}
	
	public void clearAllTextField() {
		id_jtf.setText("");
		pw_jpf.setText("");
		name_jtf.setText("");
		tel_jtf.setText("");
		addr_jtf.setText("");
	}
	
	public void clearIdTextField() {
		id_jtf.setText("");
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(7,1));
		con.add(signIn_jlb);
		con.add(id_jp);
		con.add(pw_jp);
		con.add(name_jp);
		con.add(tel_jp);
		con.add(addr_jp);
		con.add(btn_jp);
		
		signIn_jlb.setFont(new Font("", Font.BOLD, 20));
		
		id_jp.setLayout(new BorderLayout());
		pw_jp.setLayout(new BorderLayout());
		name_jp.setLayout(new BorderLayout());
		tel_jp.setLayout(new BorderLayout());
		addr_jp.setLayout(new BorderLayout());
		btn_jp.setLayout(new GridLayout(1,2));
		
		id_jp.add("West", id_jlb);
		id_jp.add("Center", id_jtf);
		id_jp.add("East", duplChk_jbt);
		pw_jp.add("West", pw_jlb);
		pw_jp.add("Center", pw_jpf);
		name_jp.add("West", name_jlb);
		name_jp.add("Center", name_jtf);
		tel_jp.add("West", tel_jlb);
		tel_jp.add("Center", tel_jtf);
		addr_jp.add("West", addr_jlb);
		addr_jp.add("Center", addr_jtf);
		btn_jp.add(signIn_jbt);
		btn_jp.add(clear_jbt);
	}
	
	public CommonMemberAddDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		signIn_jlb.setText(title);
		this.init();
		
		super.setSize(300, 300);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos);
		super.setResizable(false);
	}
}
