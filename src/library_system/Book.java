package library_system;

public class Book {
	private int no;				// å ��Ͻ� �Է¹޴°� �ƴ϶� DB�� insert�� sequence�� ���� �ڵ��Էµ�
	private String title;
	private String writer;
	private String genre;
	private boolean canLend;
	
	public Book(int no, String title, String writer, String genre, boolean canLend) {	// å�� �˻��ؼ� ������ �� �ʿ��� ������
		this.no = no;
		this.title = title;
		this.writer = writer;
		this.genre = genre;
		this.canLend = canLend;
	}
	
	public Book(String title, String writer, String genre) {	// �����ڰ� å�� ����� �� �ʿ��� ������
		this.title = title;
		this.writer = writer;
		this.genre = genre;
		this.canLend = true;	// �����ڰ� �� å�� ����ϴ� ��쿣 �׻� �뿩 �����ϹǷ� true
	}
	
	public int getNo() {return no;}
	public String getTitle() {return title;}
	public String getWriter() {return writer;}
	public String getGenre() {return genre;}
	public boolean getCanLend() {return canLend;}
	
	public void setCanLend(boolean canLend) {this.canLend = canLend;}
}
