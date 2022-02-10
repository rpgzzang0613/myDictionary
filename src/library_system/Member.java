package library_system;

public class Member {
	private int no;				// ȸ�����Խ� �Է¹޴°� �ƴ϶� DB�� insert�� sequence�� ���� �ڵ��Էµ�
	private String id;
	private String pw;
	private String name;
	private String tel;
	private String addr;
	private boolean perm;
	private int borrowedBookNo;
	
	public Member(int no, String id, String name, String tel, String addr, int borrowedBookNo) {	// �����ڿ��� ȸ�� ����� ������ �� �ʿ��� ������
		this.no = no;
		this.id = id;
		this.name = name;
		this.tel = tel;
		this.addr = addr;
		this.borrowedBookNo = borrowedBookNo;
	}
	
	public Member(String id, String pw, String name, String tel, String addr) {		// ȸ�� ���Խ� �ʿ��� ������
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.tel = tel;
		this.addr = addr;
		this.perm = false;		// �����ü�� ȸ�����Խ� ���� ���̾�α׿��� �������������� �ѱ涧�� ����ϹǷ� ������ ���� �Ϲ�ȸ����
		this.borrowedBookNo = 0;	// ȸ�����Խÿ� ��� �� ���� ������
	}
	
	public Member(int no, String id, String pw, String name, String tel, String addr, int borrowedBookNo) { // ���� ������
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
