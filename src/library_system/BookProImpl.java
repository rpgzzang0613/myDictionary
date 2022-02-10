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
			Class.forName("oracle.jdbc.driver.OracleDriver");		// �޸𸮿� ����̹� �ø���
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		ps = null; // ���������� ���� ��ü
		
		sql = "";
	}
	
	@Override
	public char input(Book newBook) {	// 'o' : ��� �Ϸ�, 'x' : å�� �ߺ��̶� ��ϺҰ�, '?' : DB����
		// ������ DB�� ����� �� ���Ǵ� �޼ҵ�
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
		// ����DB�� ������ ������ �� ���Ǵ� �޼ҵ�
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
		// ������ DB���� ������ �� ����ϴ� �޼ҵ�
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
		// ����DB�� �ش� ��ȣ�� ���� ������ �ִ��� üũ�� �� ���� �޼ҵ�
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
		// �Է��� ������ȣ�� ���� DB�κ��� ���� ������ ������ Book��ü�� �����ϴ� �޼ҵ�
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
		// ����DB�� ��� ���� ����� �����ͼ� ������ Book ��ü�� ����� ArrayList<Book>�� ��� �����ϴ� �޼ҵ�
		ArrayList<Book> allBookArr = new ArrayList<>();
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {		// ���� ���� ������ false�� ��
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
		// �Է¹��� �ɼǰ� ������ ����DB�� �˻��� �ش��ϴ� ���� ����� �����ͼ� ������ Book ��ü�� ����� ArrayList<Book>�� ��� �����ϴ� �޼ҵ�
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
		// �Է¹��� �������� ����DB�� �˻��� �ش��ϴ� ���� ����� �����ͼ� ������ Book ��ü�� ����� ArrayList<Book>�� ��� �����ϴ� �޼ҵ�
		// (��ȸ�� �˻���)
		ArrayList<Book> searchBookArr = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from books where title like ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+tfTitle+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {		// ���� ���� ������ false�� ��
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
		// �Է¹��� ������ȣ�� �ش��ϴ� ���� ������ �����ϴ� �޼ҵ�
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
		// �Է¹��� ������ȣ�� �ش��ϴ� ������ �뿩 ������ �������� Ȯ���� �����ϴ� �޼ҵ�
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
		// �Է¹��� ������ȣ�� �ش��ϴ� ������ �뿩���ɿ��θ� n���� �ٲٴ� �޼ҵ� (ȸ�����α׷��� setBorrowedBookNo�� �޸�, No�� Number�� �ƴ϶� no�� �ǹ�)
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
		// �Է¹��� ������ȣ�� �ش��ϴ� ������ �뿩���ɿ��θ� y�� �ٲٴ� �޼ҵ�
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
