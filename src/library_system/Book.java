package library_system;

public class Book {
	private int no;				// 책 등록시 입력받는게 아니라 DB에 insert시 sequence를 통해 자동입력됨
	private String title;
	private String writer;
	private String genre;
	private boolean canLend;
	
	public Book(int no, String title, String writer, String genre, boolean canLend) {	// 책을 검색해서 보여줄 때 필요한 생성자
		this.no = no;
		this.title = title;
		this.writer = writer;
		this.genre = genre;
		this.canLend = canLend;
	}
	
	public Book(String title, String writer, String genre) {	// 관리자가 책을 등록할 때 필요한 생성자
		this.title = title;
		this.writer = writer;
		this.genre = genre;
		this.canLend = true;	// 관리자가 새 책을 등록하는 경우엔 항상 대여 가능하므로 true
	}
	
	public int getNo() {return no;}
	public String getTitle() {return title;}
	public String getWriter() {return writer;}
	public String getGenre() {return genre;}
	public boolean getCanLend() {return canLend;}
	
	public void setCanLend(boolean canLend) {this.canLend = canLend;}
}
