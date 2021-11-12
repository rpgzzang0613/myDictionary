package library_system;

public class Member {
	private int no;				// 회원가입시 입력받는게 아니라 DB에 insert시 sequence를 통해 자동입력됨
	private String id;
	private String pw;
	private String name;
	private String tel;
	private String addr;
	private boolean perm;
	private int borrowedBookNo;
	
	public Member(int no, String id, String name, String tel, String addr, int borrowedBookNo) {	// 관리자에게 회원 목록을 보여줄 때 필요한 생성자
		this.no = no;
		this.id = id;
		this.name = name;
		this.tel = tel;
		this.addr = addr;
		this.borrowedBookNo = borrowedBookNo;
	}
	
	public Member(String id, String pw, String name, String tel, String addr) {		// 회원 가입시 필요한 생성자
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.tel = tel;
		this.addr = addr;
		this.perm = false;		// 멤버객체는 회원가입시 가입 다이얼로그에서 메인프레임으로 넘길때만 사용하므로 어차피 전부 일반회원임
		this.borrowedBookNo = 0;	// 회원가입시엔 모두 안 빌린 상태임
	}
	
	public Member(int no, String id, String pw, String name, String tel, String addr, int borrowedBookNo) { // 정보 수정시
		this.no = no;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.tel = tel;
		this.addr = addr;
		this.borrowedBookNo = borrowedBookNo;
	}
	
	public int getNo() {return no;}
	public String getId() {return id;}
	public String getPw() {return pw;}
	public String getName() {return name;}
	public String getTel() {return tel;}
	public String getAddr() {return addr;}
	public boolean getPerm() {return perm;}
	public int getBorrowedBookNo() {return borrowedBookNo;}
	
	public void setPw(String pw) {this.pw = pw;}
	public void setTel(String tel) {this.tel = tel;}
	public void setAddr(String addr) {this.addr = addr;}
}
