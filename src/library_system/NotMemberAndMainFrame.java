package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class NotMemberAndMainFrame extends JFrame implements ActionListener {
	private JLabel notice_jlb = new JLabel("�� ��ȸ���� ������ �����ϸ� �뿩�� �Ұ��մϴ�.", JLabel.CENTER);
	private JLabel search_jlb = new JLabel("������ : ");
	private JTextField search_jtf = new JTextField();
	private JButton search_jbt = new JButton("��  ��");
	private JButton login_jbt = new JButton("�α���");
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	
	private NotMemberLoginDialog loginDlg = new NotMemberLoginDialog(this, "�α���", true);
	private CommonMemberAddDialog signInDlg = new CommonMemberAddDialog(this, "ȸ�� ����", true);
	private NotMemberBooklistDialog bookListDlg = new NotMemberBooklistDialog(this, "���� �˻� ���", true);
	
	private AdminFrame adminFrame = new AdminFrame("������ ������");
	private MemberFrame memberFrame = new MemberFrame("ȸ�� ������");
	
	private MemberPro memberPro = new MemberProImpl();
	private BookPro bookPro = new BookProImpl();
	
	private static NotMemberAndMainFrame mainFrame = new NotMemberAndMainFrame("���� ���� �ý���");
	
	public void setVisibleCustom(boolean bool) {
		this.setVisible(bool);
	}
	
	public static NotMemberAndMainFrame getLibraryMainFrame() {
		return mainFrame;
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(3,1));
		con.add(north_jp);
		con.add(center_jp);
		con.add(notice_jlb);
		
		notice_jlb.setFont(new Font("", Font.BOLD, 15));
		search_jlb.setFont(new Font("", Font.BOLD, 15));
		
		north_jp.setLayout(new FlowLayout());
		
		north_jp.add("East", login_jbt);
		
		center_jp.setLayout(new BorderLayout());
		center_jp.add("West", search_jlb);
		center_jp.add("Center", search_jtf);
		center_jp.add("East", search_jbt);
	}
	
	public void start() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		login_jbt.addActionListener(this);
		search_jbt.addActionListener(this);
		search_jtf.addActionListener(this);
		loginDlg.login_jbt.addActionListener(this);
		loginDlg.signIn_jbt.addActionListener(this);
		signInDlg.duplChk_jbt.addActionListener(this);
		signInDlg.signIn_jbt.addActionListener(this);
		signInDlg.clear_jbt.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// LibraryMainFrame�� �ִ� ��ư�� (�� ó�� ���� ������)
		if(e.getSource() == login_jbt) {
			// �α��� ��ư�� ������ ��
			loginDlg.setVisible(true);
		}else if(e.getSource() == search_jbt) {
			// �˻� ��ư�� ������ ��
			String title = search_jtf.getText();
			ArrayList<Book> searchBookArr = bookPro.searchFromDB(title);
			bookListDlg.setBookList(searchBookArr);
			bookListDlg.setVisible(true);
		}else if(e.getSource() == search_jtf) {
			// �˻��� �ְ� ���͸� ������ ��
			String title = search_jtf.getText();
			ArrayList<Book> searchBookArr = bookPro.searchFromDB(title);
			bookListDlg.setBookList(searchBookArr);
			bookListDlg.setVisible(true);
		}
		
		// LibraryNotMemberLoginDialog�� �ִ� ��ư�� (���� ������->�α��� �������� ������ ���̾�α�)
		else if(e.getSource() == loginDlg.login_jbt) {
			// ID, PW�ְ� �α��� ��ư�� ������ ��
			
			char memChk = memberPro.login(loginDlg.getId(), loginDlg.getPw());
			// �α��� ó�� �� ����� char�� ���� (o:�Ϲ�ȸ��, s:������, n:PW����, x:ID/PW����)
			
			if(memChk == 'o') {
				JOptionPane.showMessageDialog(this, "ȸ�� �α��ο� �����Ͽ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				loginDlg.setVisible(false);		// �α���â �Ⱥ��̰� ����
				this.setVisible(false);			// mainFrame �Ⱥ��̰� ����
				memberFrame.setMember(loginDlg.getId());
				// ���� �α����� ȸ���� ID�� DB�˻��ؼ� ������ ���� �� memberFrame(ȸ���� ������)�� �ش� ȸ���� ������ �����ü�� �Ѱ���
				// ȸ������ ����, ���� ���� ó���ϱ� ���� �뵵
				
				loginDlg.clearAllTextField();	// �α���â�� TextField �ʱ�ȭ
				memberFrame.setVisible(true);	// memberFrame(ȸ���� ������) ���̰� ����
			}else if(memChk == 's') {
				JOptionPane.showMessageDialog(this, "������ �α��ο� �����Ͽ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				loginDlg.setVisible(false);		// �α���â �Ⱥ��̰� ����
				this.setVisible(false);			// mainFrame �Ⱥ��̰� ����
				loginDlg.clearAllTextField();	// �α���â�� TextField �ʱ�ȭ
				adminFrame.setVisible(true);	// adminFrame(�����ڿ� ������) ���̰� ����
			}else if(memChk == 'n') {
				JOptionPane.showMessageDialog(this, "PW�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else if(memChk == 'x') {
				JOptionPane.showMessageDialog(this, "ID/PW�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "DB �����Դϴ�. �ٽ� �õ��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == loginDlg.signIn_jbt) {
			// �α��� ���̾�α׿��� ȸ������ ��ư�� ������ ��
			signInDlg.setVisible(true);			// ȸ������ ���̾�α� ���̰� ����
		}
		
		// LibraryMemberAddDialog�� �ִ� ��ư�� (���� ������->�α���->ȸ�� ���� ������ �� ������ ���̾�α�)
		else if(e.getSource() == signInDlg.duplChk_jbt) {
			// �ߺ�üũ ��ư�� ������ ��
			
			String newId = signInDlg.getId();					// ȸ������ ���̾�α׿��� �Է¹��� ID�� ������
			boolean isDupl = memberPro.duplChk(newId);			// �ߺ� üũ �� �ߺ��̸� true ��ȯ
			
			if(isDupl) {
				JOptionPane.showMessageDialog(this, "�̹� ��� ���� ID�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				signInDlg.clearIdTextField();					// ID �ؽ�Ʈ�ʵ� �ʱ�ȭ
			}else {
				JOptionPane.showMessageDialog(this, "��� ������ ID�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == signInDlg.signIn_jbt) {
			// ȸ������ ���̾�α׿��� ���� ��ư�� ������ ��
			Member newMember = signInDlg.getMember();			// ȸ������ ���̾�α׿��� �Է¹��� ��ü ���� �����ü�� ������
			boolean isDone = memberPro.input(newMember);		// ������ ��ü�� DB�� ��� �� �������� ��ȯ
			if(isDone) {
				JOptionPane.showMessageDialog(this, "������ �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				signInDlg.clearAllTextField();					// ȸ������ �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
				signInDlg.setVisible(false);					// ȸ������ ���̾�α� �Ⱥ��̰� ����
			}else {
				JOptionPane.showMessageDialog(this, "���� ����. �ٽ� �õ����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == signInDlg.clear_jbt) {
			// ȸ������ ���̾�α׿��� �ʱ�ȭ ��ư�� ������ ��
			signInDlg.clearAllTextField();						// ȸ������ �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
		}
	}
	
	private NotMemberAndMainFrame(String title) {
		super(title);
		
		this.init();
		this.start();
		
		super.setSize(400, 150);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos);
		super.setResizable(false);
		
		super.setVisible(true);
	}
}
