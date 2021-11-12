package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class AdminFrame extends JFrame implements ActionListener {
	private JLabel notice_jlb = new JLabel("관리자 페이지입니다.", JLabel.CENTER);
	private JButton logout_jbt = new JButton("로그아웃");
	private JButton member_jbt = new JButton("회원 관리");
	private JButton book_jbt = new JButton("도서 관리");
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	
	private AdminMemberlistDialog memberlistAdmin = new AdminMemberlistDialog(this, "회원 관리", true);
	private AdminBooklistDialog booklistAdmin = new AdminBooklistDialog(this, "도서 관리", true);
	private CommonMemberAddDialog memberAddAdmin = new CommonMemberAddDialog(this, "회원 추가", true);
	private AdminBookAddDialog bookAddAdmin = new AdminBookAddDialog(this, "도서 추가", true);
	private AdminMemberSearchDialog memberSearchAdmin = new AdminMemberSearchDialog(this, "회원 검색", true);
	private CommonBookSearchDialog bookSearchAdmin = new CommonBookSearchDialog(this, "도서 검색", true);
	private CommonMemberEditDialog memberEditAdmin = new CommonMemberEditDialog(this, "회원 수정", true);
	private AdminBookEditDialog bookEditAdmin = new AdminBookEditDialog(this, "도서 수정", true);
	
	private MemberPro memberPro = new MemberProImpl();
	private BookPro bookPro = new BookProImpl();
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(3,1));
		con.add(north_jp);
		con.add(center_jp);
		con.add(notice_jlb);
		north_jp.setLayout(new FlowLayout());
		center_jp.setLayout(new GridLayout(1,2));
		north_jp.add(logout_jbt);
		center_jp.add(member_jbt);
		center_jp.add(book_jbt);
		
		member_jbt.setFont(new Font("", Font.BOLD, 15));
		book_jbt.setFont(new Font("", Font.BOLD, 15));
		notice_jlb.setFont(new Font("", Font.BOLD, 15));
	}
	
	public void start() {
		member_jbt.addActionListener(this);
		book_jbt.addActionListener(this);
		logout_jbt.addActionListener(this);
		
		memberlistAdmin.add_jbt.addActionListener(this);
		memberlistAdmin.search_jbt.addActionListener(this);
		memberlistAdmin.edit_jbt.addActionListener(this);
		memberlistAdmin.delete_jbt.addActionListener(this);
		memberlistAdmin.viewAll_jbt.addActionListener(this);
		
		booklistAdmin.add_jbt.addActionListener(this);
		booklistAdmin.search_jbt.addActionListener(this);
		booklistAdmin.edit_jbt.addActionListener(this);
		booklistAdmin.delete_jbt.addActionListener(this);
		booklistAdmin.viewAll_jbt.addActionListener(this);
		
		memberAddAdmin.signIn_jbt.addActionListener(this);
		memberAddAdmin.duplChk_jbt.addActionListener(this);
		memberAddAdmin.clear_jbt.addActionListener(this);
		
		memberEditAdmin.edit_jbt.addActionListener(this);
		memberEditAdmin.clear_jbt.addActionListener(this);
		
		bookAddAdmin.add_jbt.addActionListener(this);
		bookAddAdmin.clear_jbt.addActionListener(this);
		
		bookEditAdmin.edit_jbt.addActionListener(this);
		bookEditAdmin.clear_jbt.addActionListener(this);
		
		memberSearchAdmin.search_jbt.addActionListener(this);
		
		bookSearchAdmin.search_jbt.addActionListener(this);
	}
	
	AdminFrame(String title) {
		super(title);
		
		this.init();
		this.start();
		
		super.setSize(250, 180);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos, ypos-200);
		super.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// LibraryMemberFrame에 있는 버튼들 (관리자 로그인 후 최초로 보이는 프레임)
		if(e.getSource() == member_jbt) {
			// 회원 관리 버튼을 눌렀을 때
			memberlistAdmin.setVisible(true);
		}else if(e.getSource() == book_jbt) {
			// 도서 관리 버튼을 눌렀을 때
			booklistAdmin.setVisible(true);
		}else if(e.getSource() == logout_jbt) {
			// 로그아웃 버튼을 눌렀을 때
			JOptionPane.showMessageDialog(this, "로그아웃 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);
		}
		
		// memberlistAdmin에 있는 버튼들 (adminFrame->회원 관리 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == memberlistAdmin.add_jbt) {
			// 회원 추가 버튼 눌렀을 때
			memberAddAdmin.setVisible(true);	// memberAddAdmin 다이얼로그 보이게 설정
		}else if(e.getSource() == memberlistAdmin.search_jbt) {
			// 회원 검색 버튼 눌렀을 때
			memberSearchAdmin.setVisible(true);	// memberSearchAdmin 다이얼로그 보이게 설정
		}else if(e.getSource() == memberlistAdmin.edit_jbt) {
			// 회원 수정 버튼 눌렀을 때
			String editId = JOptionPane.showInputDialog(this, "수정하실 회원의 ID를 입력해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			// 수정할 회원의 ID 입력받음
			
			if(editId == null) return;
			
			boolean isDupl = memberPro.duplChk(editId);
			// 수정할 회원의 ID가 DB상에 있는지 확인 후 있으면 true 리턴
			
			if(isDupl) {
				// 수정할 회원 ID가 존재할 경우
				Member currentMember = memberPro.getMember(editId);	// 회원ID를 통해 DB에서 해당 회원의 정보를 가져와서 Member객체에 담음
				memberEditAdmin.setTextField(currentMember);		// memberEditAdmin 다이얼로그의 텍스트필드에 기존 정보 표시
				memberEditAdmin.setVisible(true);					// memberEditAdmin 다이얼로그 보이게 설정
			}else {
				// 수정할 회원 ID가 DB에 없을 경우
				JOptionPane.showMessageDialog(this, "해당하는 회원이 없습니다. ID를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberlistAdmin.delete_jbt) {
			// 회원 삭제 버튼 눌렀을 때
			
			String deleteId = JOptionPane.showInputDialog(this, "삭제하실 회원의 ID를 입력해 주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			// 삭제할 회원의 ID 입력받음
			
			if(deleteId == null) return;
			
			if(deleteId.matches("(?!^\\d+$)^.+$")) {
				// 숫자로만 입력하면 안되도록 조건 설정
				boolean isDeleted = memberPro.delete(deleteId);	// 삭제 처리 후 성공시 true 리턴
				
				if(isDeleted) {
					// 삭제에 성공할 경우
					memberlistAdmin.setAllMemberlist();			// 멤버목록 새로고침
					JOptionPane.showMessageDialog(this, "해당 회원이 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}else {
					// 삭제에 실패할 경우
					JOptionPane.showMessageDialog(this, "해당 회원은 삭제가 불가합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				}
			}else {
				// 숫자만 입력할 경우
				JOptionPane.showMessageDialog(this, "ID를 입력해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberlistAdmin.viewAll_jbt) {
			// 전체 목록 버튼 눌렀을 때
			memberlistAdmin.setAllMemberlist();					// 멤버목록 새로고침
		}
		
		// memberAddAdmin 다이얼로그에 있는 버튼들 (adminFrame->회원 관리->회원 추가 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == memberAddAdmin.signIn_jbt) {
			// 가입 버튼 눌렀을 때
			Member newMember = memberAddAdmin.getMember();	// memberAddAdmin 다이얼로그에서 입력받은 전체 값을 멤버객체로 가져옴
			boolean isDone = memberPro.input(newMember);	// 가져온 객체를 DB에 넣기 위해 memberPro로 넘겨서 등록 후 성공여부 반환
			
			if(isDone) {
				// 회원 추가에 성공했을 경우
				JOptionPane.showMessageDialog(this, "회원 추가 완료하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				memberAddAdmin.clearAllTextField();			// memberAddAdmin 다이얼로그의 텍스트필드 전부 초기화
				memberAddAdmin.setVisible(false);			// memberAddAdmin 안보이게 설정
				memberlistAdmin.setAllMemberlist();			// memberlistAdmin 새로고침
			}else {
				// 회원 추가에 실패했을 경우
				JOptionPane.showMessageDialog(this, "회원 추가 실패. 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberAddAdmin.duplChk_jbt) {
			// 중복체크 버튼 눌렀을 때
			String newId = memberAddAdmin.getId();				// memberAddAdmin 다이얼로그에서 입력받은 ID값을 가져옴
			boolean isDupl = memberPro.duplChk(newId);			// memberPro로 넘겨 중복 체크 후 중복이면 true 반환
			
			if(isDupl) {
				// 회원DB에 중복되는 ID가 있을 경우
				JOptionPane.showMessageDialog(this, "이미 사용 중인 ID입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				memberAddAdmin.clearIdTextField();				// ID 텍스트필드 초기화
			}else {
				// 회원DB에 중복되는 ID가 없을 경우
				JOptionPane.showMessageDialog(this, "사용 가능한 ID입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberAddAdmin.clear_jbt) {
			// 초기화 버튼 눌렀을 때
			memberAddAdmin.clearAllTextField();					// memberAddAdmin 다이얼로그의 텍스트필드 전부 초기화
		}

		// memberSearchAdmin 다이얼로그에 있는 버튼들 (adminFrame->회원 관리->회원 검색 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == memberSearchAdmin.search_jbt) {
			// 검색 버튼 눌렀을 때
			
			String column = memberSearchAdmin.getSearchOption();		// 괸리자가 선택한 검색 옵션을 받아옴
			String value = memberSearchAdmin.getTextField();			// 관리자가 입력한 검색 값을 받아옴
			ArrayList<Member> searchMemberArr = memberPro.view(column, value);
			// 해당되는 회원들을 DB에서 검색해서 Member 객체의 ArrayList 형태로 담아옴
			
			memberlistAdmin.setMemberlist(searchMemberArr);
			// 받아온 ArrayList를 memberlistAdmin에 넘겨서 textArea에 해당 목록만 보이도록 처리
		}
		
		// memberEditAdmin 다이얼로그에 있는 버튼들 (adminFrame->회원 관리->회원 수정->비번 맞게 입력 후 확인 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == memberEditAdmin.edit_jbt) {
			// 수정 버튼 눌렀을 때
			
			Member editMember = memberEditAdmin.getMemberToEdit();
			// 수정할 정보를 DB에 업데이트 하기 위한 Member객체 생성 (관리자가 새로 입력한 값을 담고 있음)
			
			boolean isDone = memberPro.edit(editMember);
			// 새로 입력한 값을 담은 Member객체를 DB에 업데이트하고 성공 여부를 반환받음
			
			if(isDone) {
				// 수정 성공 시
				JOptionPane.showMessageDialog(this, "회원 수정이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				memberEditAdmin.clearAllTextField();	// memberEditAdmin의 모든 텍스트필드 초기화
				memberEditAdmin.setVisible(false);		// memberEditAdmin 다이얼로그 안보이게 설정
				memberlistAdmin.setAllMemberlist();		// memberlistAdmin 새로고침
			}else {
				// 수정 실패 시
				JOptionPane.showMessageDialog(this, "회원 수정 실패. 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberEditAdmin.clear_jbt) {
			memberEditAdmin.clearTextField();			// memberEditAdmin의 모든 텍스트필드 초기화
		}

		// booklistAdmin에 있는 버튼들 (adminFrame->도서 관리 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == booklistAdmin.add_jbt) {
			// 도서 추가 버튼 눌렀을 때
			bookAddAdmin.setVisible(true);		// bookAddAdmin 다이얼로그 보이게 설정
		}else if(e.getSource() == booklistAdmin.search_jbt) {
			// 도서 검색 버튼 눌렀을 때
			bookSearchAdmin.setVisible(true);	// bookSearchAdmin 다이얼로그 보이게 설정
		}else if(e.getSource() == booklistAdmin.edit_jbt) {
			// 도서 수정 버튼 눌렀을 때
			
			String editNo = JOptionPane.showInputDialog(this, "수정하실 도서의 번호를 입력해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			// 도서의 번호를 입력받음
			
			if(editNo == null) return;
			
			if(!editNo.matches("^[0-9]+$")) {
				JOptionPane.showMessageDialog(this, "숫자만 입력해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			boolean isDupl = bookPro.duplChk(Integer.parseInt(editNo));
			// 수정할 도서의 번호가 DB상에 있는지 확인 후 있으면 true 리턴
			
			if(isDupl) {
				// 수정할 도서 번호가 존재할 경우
				
				Book currentBook = bookPro.getBook(Integer.parseInt(editNo));
				// 도서번호를 통해 DB에서 해당 도서의 정보를 가져와서 Book객체에 담음
				bookEditAdmin.setTextField(currentBook);	// bookEditAdmin 다이얼로그의 텍스트필드에 기존 정보 표시
				bookEditAdmin.setVisible(true);				// bookEditAdmin 다이얼로그 보이게 설정
			}else {
				// 수정할 도서 번호가 DB에 없을 경우
				JOptionPane.showMessageDialog(this, "해당하는 도서가 없습니다. 번호를 확인해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == booklistAdmin.delete_jbt) {
			// 도서 삭제 버튼 눌렀을 때
			
			String deleteNo = JOptionPane.showInputDialog(this, "삭제할 도서의 번호를 입력해 주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			// 삭제할 도서의 번호를 입력받음
			
			if(deleteNo == null) return;
			
			if(!deleteNo.matches("^[0-9]+$")) {
				JOptionPane.showMessageDialog(this, "숫자만 입력해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			boolean isDeleted = bookPro.delete(Integer.parseInt(deleteNo));
			// 삭제 처리 후 성공시 true 리턴
			
			if(isDeleted) {
				// 삭제 성공 시
				booklistAdmin.setAllBooklist();		// 도서목록 새로고침
				JOptionPane.showMessageDialog(this, "해당 도서가 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else {
				// 삭제 실패 시
				JOptionPane.showMessageDialog(this, "해당 도서는 삭제가 불가합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == booklistAdmin.viewAll_jbt) {
			// 전체 목록 버튼 눌렀을 때
			booklistAdmin.setAllBooklist();
		}
		
		// bookAddAdmin 다이얼로그에 있는 버튼들 (adminFrame->도서 관리->도서 추가 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == bookAddAdmin.add_jbt) {
			// 추가 버튼 눌렀을 때
			
			Book book = bookAddAdmin.getBook();			// bookAddAdmin 다이얼로그에서 입력받은 전체 값을 멤버객체로 가져옴
			char bookChk = bookPro.input(book);
			// 가져온 객체를 DB에 넣기 위해 bookPro로 넘겨서 등록 후 결과 반환 (o : 도서 등록 완료, x : 도서 등록 실패, ? : DB오류)
			
			if(bookChk == 'o') {
				JOptionPane.showMessageDialog(this, "도서 등록이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				bookAddAdmin.clearAllTextField();		// bookAddAdmin 다이얼로그의 텍스트필드 전부 초기화
				booklistAdmin.setAllBooklist();			// booklistAdmin 다이얼로그 새로고침
			}else if(bookChk == 'x') {
				JOptionPane.showMessageDialog(this, "제목과 저자가 같은 책이 이미 있습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}else if(bookChk == '?') {
				JOptionPane.showMessageDialog(this, "DB 오류입니다. 관리자에게 문의해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == bookAddAdmin.clear_jbt) {
			// 초기화 버튼 눌렀을 때
			bookAddAdmin.clearAllTextField();			// bookAddAdmin 다이얼로그의 텍스트필드 전부 초기화
		}
		
		// bookSearchAdmin 다이얼로그에 있는 버튼들 (adminFrame->도서 관리->도서 검색 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == bookSearchAdmin.search_jbt) {
			// 검색 버튼 눌렀을 때
			
			String column = bookSearchAdmin.getSearchOption();			// 관리자가 선택한 검색 옵션을 받아옴
			String value = bookSearchAdmin.getTextField();				// 관리자가 입력한 검색 값을 받아옴
			ArrayList<Book> searchBookArr = bookPro.view(column, value);
			// 해당되는 도서들을 DB에서 검색해서 Book 객체의 ArrayList 형태로 담아옴
			
			booklistAdmin.setBooklist(searchBookArr);
			// 받아온 ArrayList를 booklistAdmin에 넘겨서 textArea에 해당 목록만 보이도록 처리
		}
		
		// bookEditAdmin 다이얼로그에 있는 버튼들 (adminFrame->도서 관리->도서 수정->번호 맞게 입력 후 확인 눌렀을 때 나오는 다이얼로그)
		else if(e.getSource() == bookEditAdmin.edit_jbt) {
			// 수정 버튼 눌렀을 때
			
			Book editBook = bookEditAdmin.getBookToEdit();
			// 수정할 정보를 DB에 업데이트 하기 위한 Book객체 생성 (관리자가 새로 입력한 값을 담고 있음)
			
			boolean isDone = bookPro.edit(editBook);
			// 새로 입력한 값을 담은 Book객체를 DB에 업데이트하고 성공 여부를 반환받음
			
			if(isDone) {
				// 수정 성공 시
				JOptionPane.showMessageDialog(this, "도서 수정이 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
				bookEditAdmin.clearAllTextField();		// bookEditAdmin의 모든 텍스트필드 초기화
				bookEditAdmin.setVisible(false);		// bookEditAdmin 다이얼로그 안보이게 설정
				booklistAdmin.setAllBooklist();			// booklistAdmin 새로고침
			}else {
				// 수정 실패 시
				JOptionPane.showMessageDialog(this, "도서 수정 실패. 다시 시도해주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == bookEditAdmin.clear_jbt) {
			// 초기화 버튼 눌렀을 때
			bookEditAdmin.clearTextField();
		}
	}
}
