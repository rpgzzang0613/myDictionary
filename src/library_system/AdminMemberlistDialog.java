package library_system;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class AdminMemberlistDialog extends JDialog {
	private JTextArea memberList_jta = new JTextArea();
	private JScrollPane memberList_jsp = new JScrollPane(memberList_jta);
	
	JButton add_jbt = new JButton("회원 추가");
	JButton search_jbt = new JButton("회원 검색");
	JButton edit_jbt = new JButton("회원 수정");
	JButton delete_jbt = new JButton("회원 삭제");
	JButton viewAll_jbt = new JButton("전체 목록");
	private JPanel south_jp = new JPanel();
	
	private MemberPro memberPro = new MemberProImpl();
	private BookPro bookPro = new BookProImpl();
	
	public void setAllMemberlist() {
		ArrayList<Member> allMemberArr = memberPro.view();
		memberList_jta.setText("번호\t\tID\t\t이름\t\tTel\t\t주소\t\t대여도서\n");
		for(Member member : allMemberArr) {
			String borrowedBook = member.getBorrowedBookNo()==0 ? "없음" : bookPro.numberToTitle(member.getBorrowedBookNo());
			memberList_jta.append(member.getNo()+"\t\t"+member.getId()+"\t\t"+member.getName()
			+"\t\t"+member.getTel()+"\t"+member.getAddr()+"\t\t"+borrowedBook+"\n");
		}
	}
	
	public void setMemberlist(ArrayList<Member> searchMemberArr) {
		memberList_jta.setText("번호\t\tID\t\t이름\t\tTel\t\t주소\t\t대여도서\n");
		for(Member member : searchMemberArr) {
			String borrowedBook = member.getBorrowedBookNo()==0 ? "없음" : bookPro.numberToTitle(member.getBorrowedBookNo());
			memberList_jta.append(member.getNo()+"\t\t"+member.getId()+"\t\t"+member.getName()
			+"\t\t"+member.getTel()+"\t"+member.getAddr()+"\t\t"+borrowedBook+"\n");
		}
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add("Center", memberList_jsp);
		memberList_jta.setEnabled(false);
		memberList_jta.setFont(new Font("", Font.BOLD, 15));
		con.add("South", south_jp);
		south_jp.setLayout(new GridLayout(1,5));
		south_jp.add(add_jbt);
		south_jp.add(search_jbt);
		south_jp.add(edit_jbt);
		south_jp.add(delete_jbt);
		south_jp.add(viewAll_jbt);
	}
	
	public AdminMemberlistDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		
		this.init();
		this.setAllMemberlist();
		
		super.setSize(1100, 600);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos-400, ypos);
		super.setResizable(false);
	}
}
