package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class AdminMemberSearchDialog extends JDialog {
	private ButtonGroup searchOption_bg = new ButtonGroup();
	private JRadioButton id_jrbt = new JRadioButton("ID");
	private JRadioButton name_jrbt = new JRadioButton("이름");
	private JRadioButton tel_jrbt = new JRadioButton("TEL");
	
	private JLabel search_jlb = new JLabel("검색어 : ");
	private JTextField search_jtf = new JTextField();
	JButton search_jbt = new JButton("검색");
	
	private JPanel north_jp = new JPanel();
	private JPanel south_jp = new JPanel();
	
	public String getSearchOption() {
		return searchOption_bg.getSelection().getActionCommand();
	}
	
	public String getTextField() {
		return search_jtf.getText();
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(2,1));
		con.add(north_jp);
		con.add(south_jp);
		
		searchOption_bg.add(id_jrbt);
		searchOption_bg.add(name_jrbt);
		searchOption_bg.add(tel_jrbt);
		
		north_jp.setLayout(new GridLayout(1,3));
		north_jp.add(id_jrbt);
		north_jp.add(name_jrbt);
		north_jp.add(tel_jrbt);
		
		id_jrbt.setActionCommand("id");
		name_jrbt.setActionCommand("name");
		tel_jrbt.setActionCommand("tel");
		
		name_jrbt.setSelected(true);
		
		south_jp.setLayout(new BorderLayout());
		south_jp.add("West", search_jlb);
		south_jp.add("Center", search_jtf);
		south_jp.add("East", search_jbt);
	}
	
	public AdminMemberSearchDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		
		super.setSize(300, 100);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos+400, ypos);
		super.setResizable(false);
	}
}
