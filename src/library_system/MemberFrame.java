package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class MemberFrame extends JFrame implements ActionListener {
	private JButton borrowBook_jbt = new JButton("도서 대여");
	private JButton returnBook_jbt = new JButton("도서 반납");
	private JButton logout_jbt = new JButton("로그아웃");
	private JButton editAccount_jbt = new JButton("정보 수정");
	private JButton deleteAccount_jbt = new JButton("회원 탈퇴");
	
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	private JLabel notice_jlb = new JLabel("", JLabel.CENTER);
	
	private String memberId = "";
	private Member currentMember;
	
	private MemberBooklistDialog borrowDlg = new MemberBooklistDialog(this, "도서 대여", true);
	private CommonBookSearchDialog bookSearchDlg = new CommonBookSearchDialog(this, "도서 검색", true);
	
	private MemberPasswordInputDialog pwdInputToDelete = new MemberPasswordInputDialog(this, "암호 확인", true);
	private MemberPasswordInputDialog pwdInputToEdit = new MemberPasswordInputDialog(this, "암호 확인", true);
	
	private CommonMemberEditDialog editDlg = new CommonMemberEditDialog(this, "정보 수정", true);
	
	private BookPro bookPro = new BookProImpl();
	private MemberPro memberPro = new MemberProImpl();
	
	public void setMember(String id) {
		this.currentMember = memberPro.getMember(id);
		memberId = currentMember.getId();
		notice_jlb.setText(memberId+"님 환영합니다.");
		notice_jlb.setFont(new Font("", Font.BOLD, 15));
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(3,1));
		con.add(north_jp);
		con.add(center_jp);
		con.add(notice_jlb);
		north_jp.setLayout(new FlowLayout());
		north_jp.add(logout_jbt);
		north_jp.add(editAccount_jbt);
		north_jp.add(deleteAccount_jbt);
		center_jp.setLayout(new GridLayout(1,2));
		center_jp.add(borrowBook_jbt);
		center_jp.add(returnBook_jbt);
	}
	
	public void start() {
		logout_jbt.addActionListener(this);
		editAccount_jbt.addActionListener(this);
		deleteAccount_jbt.addActionListener(this);
		borrowBook_jbt.addActionListener(this);
		returnBook_jbt.addActionListener(this);
		
		borrowDlg.borrow_jbt.addActionListener(this);
		borrowDlg.search_jbt.addActionListener(this);
		borrowDlg.viewAll_jbt.addActionListener(this);
		
		bookSearchDlg.search_jbt.addActionListener(this);
		
		pwdInputToDelete.confirm_jbt.addActionListener(this);
		pwdInputToEdit.confirm_jbt.addActionListener(this);
		
		editDlg.edit_jbt.addActionListener(this);
		editDlg.clear_jbt.addActionListener(this);
	}
	
	public MemberFrame(String title) {
		super(title);
		
		this.init();
		this.start();
		
		super.setSize(400, 150);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos+420, ypos);
		super.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// LibraryMemberFrame에 있는 버튼들 (회원 로그인 후 최초로 보이는 프레임)
		if(e.getSource() == logout_jbt) {
			// 로그아웃 버튼을 눌렀을 때
			JOptionPane.showMessageDialog(this, "로그아웃 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);				// memberFrame 안보이게 설정
			NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);	// mainFrame 보이게 설정
		}else if(e.getSource() == editAccount_jbt) {
			// 정보수정 버튼을 눌렀을 때
			pwdInputToEdit.setVisible(true);	// pwdInputToEdit 다이얼로그를 보이게 설정 (수정하기 전에 암호 재확인)
		}else if(e.getSource() == deleteAccount_jbt) {
			// 회원탈퇴 버튼을 눌렀을 때
			pwdInputToDelete.setVisible(true);	// pwdInputToDelete 다이얼로그를 보이게 설정 (삭제하기 전에 암호 재확인)
		}else if(e.getSource() == borrowBook_jbt) {
			// 도서대여 버튼을 눌렀을 때
			borrowDlg.setVisible(true);			// borrowDlg 다이얼로그를 보이게 설정
		}else if(e.getSource() == returnBook_jbt) {
			// 도서반납 버튼을 눌렀을 때
			String tfNo = JOptionPane.showInputDialog(this, "반납할 도서의 번호를 입력해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			// 반납할 도서의 번호를 입력받음(tfNo)
			
			if(tfNo == null) return;
			
			int borrowedBookNo = memberPro.getBorrowedBookNo(memberId);
			// 해당 회원이 빌린 도서 번호를 가져옴(borrowedBookNo)
			
			char canLend = bookPro.getCanLend(Integer.parseInt(tfNo));
			// 회원이 입력한 번호(tfNo)에 맞는 도서가 현재 대여가능상태인지 체크
			
			if(canLend == 'y') {
				// 입력한 번호가 대여가능상태일 경우
				JOptionPane.showMessageDialog(this, "해당 도서는 현재 대여중이지 않습니다. 번호를 다시 확인해 주세요.");
			}else if(canLend == '?') {
				// 없는 번호를 입력했을 경우
				JOptionPane.showMessageDialog(this, "도서의 번호를 다시 확인해 주세요.");
			}else {
				// 입력한 번호가 대여중인 상태일 경우
				if(borrowedBookNo < 0) {
					JOptionPane.showMessageDialog(this, "DB 오류입니다. 관리자에게 문의해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}else if(borrowedBookNo == 0) {
					JOptionPane.showMessageDialog(this, "회원님은 도서 대여를 하지 않으셨습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}else {
					// 입력한 번호가 대여중이면서 0이 아닌 경우
					if(Integer.parseInt(tfNo) == borrowedBookNo) {
						// 입력한 번호와 회원이 실제 빌린 도서의 번호가 일치할 경우
						memberPro.setBorrowedBookNoZero(memberId);		// 회원DB의 borrowedBookNo를 0으로 변경
						bookPro.setCanLendYes(Integer.parseInt(tfNo));	// 반납한 도서의 canLend를 y로 변경
						borrowDlg.setAllBooklist();						// 도서목록 textArea 새로고침
						JOptionPane.showMessageDialog(this, "도서 반납에 성공하셨습니다.");
					}else {
						JOptionPane.showMessageDialog(this, "회원님이 대여하신 도서가 아닙니다. 번호를 다시 확인해 주세요.");
					}
				}
			}
		}
		
		// borrowDlg에 있는 버튼들 (memberFrame->도서 대여 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == borrowDlg.borrow_jbt) {
			// 대여신청 버튼을 눌렀을 때
			String tfNo = JOptionPane.showInputDialog(this, "대여할 도서의 번호를 입력해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			// 대여할 도서의 번호를 입력받음(tfNo)
			
			if(tfNo == null) return;
			
			int borrowedBookNo = memberPro.getBorrowedBookNo(memberId);
			// 해당 회원이 빌린 도서 번호를 가져옴(borrowedBookNo), 없으면 0
			
			char canLend = bookPro.getCanLend(Integer.parseInt(tfNo));
			// 회원이 입력한 번호(tfNo)에 맞는 도서가 현재 대여가능상태인지 체크
			
			if(borrowedBookNo < 0) {
				JOptionPane.showMessageDialog(this, "DB 오류입니다. 관리자에게 문의해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else if(borrowedBookNo > 0) {
				// 회원이 빌린 도서 번호가 0이 아닐 경우(이미 다른 도서를 빌린 경우)
				JOptionPane.showMessageDialog(this, "도서는 1권만 대여 가능합니다. 대여하신 도서를 먼저 반납해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else {
				// 회원이 빌린 도서 번호가 0일 경우(도서를 빌릴 수 있는 경우)
				if(canLend == 'n') {
					// 입력한 번호의 도서가 대여중일 경우
					JOptionPane.showMessageDialog(this, "해당 도서는 현재 다른 회원님께 대여 중입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}else if(canLend == '?') {
					// 없는 번호를 입력했을 경우
					JOptionPane.showMessageDialog(this, "도서의 번호를 다시 확인해 주세요.");
				}else {
					// 입력한 번호의 도서가 대여가능상태일 경우
					memberPro.setBorrowedBookNo(memberId, Integer.parseInt(tfNo));	// 회원DB의 borrowedBookNo를 빌릴 도서의 번호로 변경
					bookPro.setCanLendNo(Integer.parseInt(tfNo));					// 빌릴 도서의 canLend를 n으로 변경
					borrowDlg.setAllBooklist();										// 도서목록 textArea 새로고침
					JOptionPane.showMessageDialog(this, "도서 대여에 성공하셨습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		}else if(e.getSource() == borrowDlg.search_jbt) {
			// 도서검색 버튼을 눌렀을 때
			bookSearchDlg.setVisible(true);
		}else if(e.getSource() == borrowDlg.viewAll_jbt) {
			// 전체목록 버튼을 눌렀을 때
			borrowDlg.setAllBooklist();
		}
		
		// bookSearchDlg에 있는 버튼들 (memberFrame->도서 대여->도서 검색 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == bookSearchDlg.search_jbt) {
			// 검색 버튼을 눌렀을 때
			String column = bookSearchDlg.getSearchOption();				// 회원이 선택한 검색 옵션을 받아옴
			String value = bookSearchDlg.getTextField();					// 회원이 입력한 검색 값을 받아옴
			ArrayList<Book> searchBookArr = bookPro.view(column, value);
			// 해당되는 도서들을 DB에서 검색해서 book 객체의 ArrayList 형태로 담아옴
			
			borrowDlg.setBooklist(searchBookArr);
			// 받아온 ArrayList를 borrowDlg에 넘겨서 textArea에 해당 목록만 보이도록 처리
		}
		
		// pwdInputToDelete에 있는 버튼 (memberFame->회원 탈퇴 눌렀을 때 나오는 비번입력 다이얼로그)
		else if(e.getSource() == pwdInputToDelete.confirm_jbt) {
			// 패스워드 입력 후 확인 버튼 눌렀을 때
			
			String tfPw = pwdInputToDelete.getPassword();			// 회원이 다시 입력한 비밀번호를 받아옴
			boolean isMatch = memberPro.pwdChk(memberId, tfPw);		// 회원DB의 비밀번호와 입력한 비밀번호를 비교
			
			if(isMatch) {
				// 회원DB상의 비밀번호와 입력한 비밀번호가 일치할 경우
				
				boolean isDeleted = memberPro.delete(memberId);
				// 삭제 처리 후 성공하면 true 반환, 실패하면 false 반환
				
				if(isDeleted) {
					// 삭제가 성공할 경우 (대여한 도서가 없을 경우)
					pwdInputToDelete.clearPassword();		// 비밀번호 입력 다이얼로그의 텍스트필드 초기화
					pwdInputToDelete.setVisible(false);		// 비밀번호 입력 다이얼로그 안보이게 설정
					JOptionPane.showMessageDialog(this, "탈퇴 처리가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(false);					// memberFrame 안보이게 설정
					NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);	// mainFrame 보이게 설정
				}else {
					// 삭제가 실패할 경우 (대여한 도서가 있을 경우)
					pwdInputToDelete.clearPassword();		// 비밀번호 입력 다이얼로그의 텍스트필드 초기화
					pwdInputToDelete.setVisible(false);		// 비밀번호 입력 다이얼로그 안보이게 설정
					JOptionPane.showMessageDialog(this, "도서를 반납한 후에 탈퇴가 가능합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}
			}else {
				// 회원DB상의 비밀번호와 입력한 비밀번호가 다를 경우
				pwdInputToDelete.clearPassword();
				JOptionPane.showMessageDialog(this, "비밀번호를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		// pwdInputToEdit에 있는 버튼 (memberFame->정보 수정 눌렀을 때 나오는 비번입력 다이얼로그)
		else if(e.getSource() == pwdInputToEdit.confirm_jbt) {
			// 패스워드 입력 후 확인 버튼 눌렀을 때
			
			String tfPw = pwdInputToEdit.getPassword();			// 회원이 다시 입력한 비밀번호를 받아옴
			boolean isMatch = memberPro.pwdChk(memberId, tfPw);	// 회원DB의 비밀번호와 입력한 비밀번호를 비교
			
			if(isMatch) {
				// 회원DB상의 비밀번호와 입력한 비밀번호가 일치할 경우
				pwdInputToEdit.clearPassword();				// 비밀번호 입력 다이얼로그의 텍스트필드 초기화
				pwdInputToEdit.setVisible(false);			// 비밀번호 입력 다이얼로그 안보이게 설정
				editDlg.setTextField(currentMember);
				// editDlg에 현재 로그인한 회원정보를 담은 객체를 넘겨서 텍스트필드에 기존 정보 표시
				editDlg.setVisible(true);					// editDlg 보이게 설정
			}else {
				// 회원DB상의 비밀번호와 입력한 비밀번호가 다를 경우
				pwdInputToEdit.clearPassword();				// 비밀번호 입력 다이얼로그의 텍스트필드 초기화
				JOptionPane.showMessageDialog(this, "비밀번호를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		// editDlg에 있는 버튼 (memberFrame->정보 수정->비번 맞게 입력후 확인 눌렀을 때 나오는 정보수정 다이얼로그)
		else if(e.getSource() == editDlg.edit_jbt) {
			// 바꾸고 싶은 정보를 수정한 후 수정 버튼 눌렀을 때
			
			Member editMember = editDlg.getMemberToEdit();
			// 수정할 정보를 DB에 업데이트 하기 위한 Member객체 생성 (회원이 새로 입력한 값을 담고 있음)
			boolean isDone = memberPro.edit(editMember);
			// 새로 입력한 값을 담은 Member객체를 DB에 업데이트하고 성공 여부를 반환받음
			
			if(isDone) {
				// 수정 성공 시
				JOptionPane.showMessageDialog(this, "수정이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				editDlg.clearAllTextField();	// editDlg의 모든 텍스트필드 초기화
				editDlg.setVisible(false);		// editDlg 다이얼로그 안보이게 설정
			}else {
				// 수정 실패 시
				JOptionPane.showMessageDialog(this, "수정 실패. 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == editDlg.clear_jbt) {
			editDlg.clearTextField();			// editDlg의 모든 텍스트필드 초기화
		}
	}
}
