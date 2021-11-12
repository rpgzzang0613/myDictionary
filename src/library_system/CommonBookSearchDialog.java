package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class CommonBookSearchDialog extends JDialog {
	private ButtonGroup searchOption_bg = new ButtonGroup();
	private JRadioButton title_jrbt = new JRadioButton("제목");
	private JRadioButton writer_jrbt = new JRadioButton("저자");
	private JRadioButton genre_jrbt = new JRadioButton("장르");
	
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
		
		searchOption_bg.add(title_jrbt);
		searchOption_bg.add(writer_jrbt);
		searchOption_bg.add(genre_jrbt);
		
		north_jp.setLayout(new GridLayout(1,3));
		north_jp.add(title_jrbt);
		north_jp.add(writer_jrbt);
		north_jp.add(genre_jrbt);
		
		title_jrbt.setActionCommand("title");
		writer_jrbt.setActionCommand("writer");
		genre_jrbt.setActionCommand("genre");
		
		title_jrbt.setSelected(true);
		
		south_jp.setLayout(new BorderLayout());
		south_jp.add("West", search_jlb);
		south_jp.add("Center", search_jtf);
		south_jp.add("East", search_jbt);
	}
	
	public CommonBookSearchDialog(Frame owner, String title, boolean modal) {
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
