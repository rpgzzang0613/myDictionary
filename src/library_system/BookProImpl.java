package library_system;

import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

public class BookProImpl implements BookPro {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	private String sql;
	
	public BookProImpl() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// 메모리에 드라이버 올리기
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		ps = null; // 동적쿼리를 위한 객체
		
		sql = "";
	}
	
	@Override
	public char input(Book newBook) {	// 'o' : 등록 완료, 'x' : 책이 중복이라 등록불가, '?' : DB오류
		// 도서를 DB에 등록할 때 사용되는 메소드
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			
			sql = "select * from books where title=? and writer=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, newBook.getTitle());
			ps.setString(2, newBook.getWriter());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return 'x';
			}else {
				sql = "insert into books values(book_seq.nextval, ?, ?, ?, 'y')";
				ps = con.prepareStatement(sql);
				ps.setString(1, newBook.getTitle());
				ps.setString(2, newBook.getWriter());
				ps.setString(3, newBook.getGenre());
				
				int res = ps.executeUpdate();
				
				if(res>0) {
					return 'o';
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return '?';
	}

	@Override
	public boolean edit(Book editBook) {
		// 도서DB의 정보를 수정할 때 사용되는 메소드
		boolean isDone = false;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update books set title=?, writer=?, genre=? where no=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, editBook.getTitle());
			ps.setString(2, editBook.getWriter());
			ps.setString(3, editBook.getGenre());
			ps.setInt(4, editBook.getNo());
			
			int res = ps.executeUpdate();
			
			if(res>0) {
				isDone = true;
			}
		}catch(SQLException e) {
			isDone = false;
		}
		return isDone;
	}
	
	@Override
	public boolean delete(int no) {
		// 도서를 DB에서 삭제할 때 사용하는 메소드
		boolean canDelete = false;
		String canLend = "x";
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select canLend from books where no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, no);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				canLend = rs.getString("canLend");
			}
			
			if(canLend.equals("y")) {
				sql = "delete from books where no=?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
				canDelete = true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return canDelete;
	}

	@Override
	public boolean duplChk(int no) {
		// 도서DB에 해당 번호를 가진 도서가 있는지 체크할 때 쓰는 메소드
		boolean isDupl = true;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books where no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, no);
			rs = ps.executeQuery();
			isDupl = rs.next();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return isDupl;
	}
	
	@Override
	public Book getBook(int tfNo) {
		// 입력한 도서번호를 토대로 DB로부터 도서 정보를 가져와 Book객체로 리턴하는 메소드
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books where no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, tfNo);
			rs = ps.executeQuery();
			while(rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				String canLend = rs.getString("canLend");
				Book book = new Book(no, title, writer, genre, canLend.equals("y") ? true : false);
				return book;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ArrayList<Book> view() {
		// 도서DB의 모든 도서 목록을 가져와서 각각의 Book 객체를 만들어 ArrayList<Book>에 담아 리턴하는 메소드
		ArrayList<Book> allBookArr = new ArrayList<>();
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {		// 다음 값이 없으면 false가 됨
				int no = rs.getInt("no");	
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				boolean canLend = false;
				if(rs.getString("canLend").equals("y")) canLend = true;
				allBookArr.add(new Book(no, title, writer, genre, canLend));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return allBookArr;
	}

	@Override
	public ArrayList<Book> view(String column, String value) {
		// 입력받은 옵션과 값으로 도서DB를 검색해 해당하는 도서 목록을 가져와서 각각의 Book 객체를 만들어 ArrayList<Book>에 담아 리턴하는 메소드
		ArrayList<Book> searchBookArr = new ArrayList<>();
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books where " + column + " like ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+value+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				boolean canLend = rs.getString("canLend").equals("y") ? true : false;
				searchBookArr.add(new Book(no, title, writer, genre, canLend));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return searchBookArr;
	}
	
	@Override
	public ArrayList<Book> searchFromDB(String tfTitle) {
		// 입력받은 제목으로 도서DB를 검색해 해당하는 도서 목록을 가져와서 각각의 Book 객체를 만들어 ArrayList<Book>에 담아 리턴하는 메소드
		// (비회원 검색용)
		ArrayList<Book> searchBookArr = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books where title like ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+tfTitle+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {		// 다음 값이 없으면 false가 됨
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String genre = rs.getString("genre");
				boolean canLend = false;
				if(rs.getString("canLend").equals("y")) canLend = true;
				searchBookArr.add(new Book(no, title, writer, genre, canLend));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return searchBookArr;
	}

	@Override
	public String numberToTitle(int no) {
		// 입력받은 도서번호에 해당하는 도서 제목을 리턴하는 메소드
		String title = "";
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select title from books where no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, no);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				title = rs.getString("title");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return title;
	}

	@Override
	public char getCanLend(int no) {
		// 입력받은 도서번호에 해당하는 도서가 대여 가능한 상태인지 확인해 리턴하는 메소드
		String dbCanLend = null;
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select canLend from books where no=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, no);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				dbCanLend = rs.getString("canLend");
			}
			
			if(dbCanLend==null) {
				return '?';
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		char canLend = dbCanLend.equals("y") ? 'y' : 'n';
		
		return canLend;
	}

	@Override
	public void setCanLendNo(int no) {
		// 입력받은 도서번호에 해당하는 도서의 대여가능여부를 n으로 바꾸는 메소드 (회원프로그램의 setBorrowedBookNo와 달리, No는 Number가 아니라 no를 의미)
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update books set canLend=? where no=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "n");
			ps.setInt(2, no);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setCanLendYes(int no) {
		// 입력받은 도서번호에 해당하는 도서의 대여가능여부를 y로 바꾸는 메소드
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update books set canLend=? where no=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "y");
			ps.setInt(2, no);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
