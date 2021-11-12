package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class NotMemberAndMainFrame extends JFrame implements ActionListener {
	private JLabel notice_jlb = new JLabel("※ 비회원은 열람만 가능하며 대여는 불가합니다.", JLabel.CENTER);
	private JLabel search_jlb = new JLabel("도서명 : ");
	private JTextField search_jtf = new JTextField();
	private JButton search_jbt = new JButton("검  색");
	private JButton login_jbt = new JButton("로그인");
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	
	private NotMemberLoginDialog loginDlg = new NotMemberLoginDialog(this, "로그인", true);
	private CommonMemberAddDialog signInDlg = new CommonMemberAddDialog(this, "회원 가입", true);
	private NotMemberBooklistDialog bookListDlg = new NotMemberBooklistDialog(this, "도서 검색 결과", true);
	
	private AdminFrame adminFrame = new AdminFrame("관리자 페이지");
	private MemberFrame memberFrame = new MemberFrame("회원 페이지");
	
	private MemberPro memberPro = new MemberProImpl();
	private BookPro bookPro = new BookProImpl();
	
	private static NotMemberAndMainFrame mainFrame = new NotMemberAndMainFrame("도서 관리 시스템");
	
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
		// LibraryMainFrame에 있는 버튼들 (맨 처음 메인 프레임)
		if(e.getSource() == login_jbt) {
			// 로그인 버튼을 눌렀을 때
			loginDlg.setVisible(true);
		}else if(e.getSource() == search_jbt) {
			// 검색 버튼을 눌렀을 때
			String title = search_jtf.getText();
			ArrayList<Book> searchBookArr = bookPro.searchFromDB(title);
			bookListDlg.setBookList(searchBookArr);
			bookListDlg.setVisible(true);
		}else if(e.getSource() == search_jtf) {
			// 검색어 넣고 엔터를 눌렀을 때
			String title = search_jtf.getText();
			ArrayList<Book> searchBookArr = bookPro.searchFromDB(title);
			bookListDlg.setBookList(searchBookArr);
			bookListDlg.setVisible(true);
		}
		
		// LibraryNotMemberLoginDialog에 있는 버튼들 (메인 프레임->로그인 눌렀을때 나오는 다이얼로그)
		else if(e.getSource() == loginDlg.login_jbt) {
			// ID, PW넣고 로그인 버튼을 눌렀을 때
			
			char memChk = memberPro.login(loginDlg.getId(), loginDlg.getPw());
			// 로그인 처리 후 결과를 char로 받음 (o:일반회원, s:관리자, n:PW오류, x:ID/PW오류)
			
			if(memChk == 'o') {
				JOptionPane.showMessageDialog(this, "회원 로그인에 성공하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				loginDlg.setVisible(false);		// 로그인창 안보이게 설정
				this.setVisible(false);			// mainFrame 안보이게 설정
				memberFrame.setMember(loginDlg.getId());
				// 현재 로그인한 회원의 ID로 DB검색해서 정보를 받은 뒤 memberFrame(회원용 프레임)에 해당 회원의 정보를 멤버객체로 넘겨줌
				// 회원정보 수정, 삭제 등을 처리하기 위한 용도
				
				loginDlg.clearAllTextField();	// 로그인창의 TextField 초기화
				memberFrame.setVisible(true);	// memberFrame(회원용 프레임) 보이게 설정
			}else if(memChk == 's') {
				JOptionPane.showMessageDialog(this, "관리자 로그인에 성공하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				loginDlg.setVisible(false);		// 로그인창 안보이게 설정
				this.setVisible(false);			// mainFrame 안보이게 설정
				loginDlg.clearAllTextField();	// 로그인창의 TextField 초기화
				adminFrame.setVisible(true);	// adminFrame(관리자용 프레임) 보이게 설정
			}else if(memChk == 'n') {
				JOptionPane.showMessageDialog(this, "PW를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else if(memChk == 'x') {
				JOptionPane.showMessageDialog(this, "ID/PW를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(this, "DB 오류입니다. 다시 시도해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == loginDlg.signIn_jbt) {
			// 로그인 다이얼로그에서 회원가입 버튼을 눌렀을 때
			signInDlg.setVisible(true);			// 회원가입 다이얼로그 보이게 설정
		}
		
		// LibraryMemberAddDialog에 있는 버튼들 (메인 프레임->로그인->회원 가입 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == signInDlg.duplChk_jbt) {
			// 중복체크 버튼을 눌렀을 때
			
			String newId = signInDlg.getId();					// 회원가입 다이얼로그에서 입력받은 ID를 가져옴
			boolean isDupl = memberPro.duplChk(newId);			// 중복 체크 후 중복이면 true 반환
			
			if(isDupl) {
				JOptionPane.showMessageDialog(this, "이미 사용 중인 ID입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				signInDlg.clearIdTextField();					// ID 텍스트필드 초기화
			}else {
				JOptionPane.showMessageDialog(this, "사용 가능한 ID입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == signInDlg.signIn_jbt) {
			// 회원가입 다이얼로그에서 가입 버튼을 눌렀을 때
			Member newMember = signInDlg.getMember();			// 회원가입 다이얼로그에서 입력받은 전체 값을 멤버객체로 가져옴
			boolean isDone = memberPro.input(newMember);		// 가져온 객체를 DB에 등록 후 성공여부 반환
			if(isDone) {
				JOptionPane.showMessageDialog(this, "가입이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				signInDlg.clearAllTextField();					// 회원가입 텍스트필드 전부 초기화
				signInDlg.setVisible(false);					// 회원가입 다이얼로그 안보이게 설정
			}else {
				JOptionPane.showMessageDialog(this, "가입 실패. 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == signInDlg.clear_jbt) {
			// 회원가입 다이얼로그에서 초기화 버튼을 눌렀을 때
			signInDlg.clearAllTextField();						// 회원가입 텍스트필드 전부 초기화
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
