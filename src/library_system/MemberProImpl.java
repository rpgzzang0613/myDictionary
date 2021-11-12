package library_system;

import java.sql.*;
import java.util.ArrayList;

public class MemberProImpl implements MemberPro {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	private String sql;
	
	public MemberProImpl() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");		// 메모리에 드라이버 올리기
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		ps = null; // 동적쿼리를 위한 객체
		
		sql = "";
	}
	
	@Override
	public char login(String tfId, String tfPw) {
		// ID,PW를 입력받아 로그인을 처리하는 메소드
		// s:관리자, o:일반회원, n:PW오류, x:비회원, ?:DB오류
		String dbId = "";
		String dbPw = "";
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select id,pw from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, tfId);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("pw").equals(tfPw)) {
					if(rs.getString("id").equals("admin")) {
						return 's';
					}
					return 'o';
				}else {
					return 'n';
				}
			}
			return 'x';
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return '?';
	}
	
	@Override
	public boolean input(Member newMember) {
		// 회원을 DB에 등록할 때 사용되는 메소드 (관리자의 회원추가, 비회원의 회원가입)
		boolean isDone = false;
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "insert into members values(member_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, newMember.getId());
			ps.setString(2, newMember.getPw());
			ps.setString(3, newMember.getName());
			ps.setString(4, newMember.getTel());
			ps.setString(5, newMember.getAddr());
			ps.setString(6, "n");
			ps.setInt(7, 0);
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
	public boolean edit(Member editMember) {
		// 회원DB의 정보를 수정할 때 사용되는 메소드 (관리자의 회원수정, 일반회원의 정보수정)
		boolean isDone = false;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update members set name=?, tel=?, addr=? where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, editMember.getName());
			ps.setString(2, editMember.getTel());
			ps.setString(3, editMember.getAddr());
			ps.setString(4, editMember.getId());
			
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
	public boolean delete(String id) {
		// 회원을 DB에서 삭제할 때 사용하는 메소드 (관리자의 회원삭제, 일반회원의 회원탈퇴)
		boolean canDelete = false;
		int borrowedBookNo = -1;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select borrowedBookNo from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				borrowedBookNo = rs.getInt("borrowedBookNo");
			}
			
			if(borrowedBookNo == 0) {
				sql = "delete from members where id=?";
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				ps.executeUpdate();
				canDelete = true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return canDelete;
	}

	@Override
	public boolean duplChk(String id) {
		// 회원DB에 해당 ID를 가진 회원이 있는지 체크할 때 쓰는 메소드
		boolean isDupl = true;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			isDupl = rs.next();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return isDupl;
	}
	
	@Override
	public boolean pwdChk(String id, String tfPw) {
		// 회원DB에 해당 ID를 가진 회원이 자신의 PW를 제대로 입력했는지 체크할 때 쓰는 메소드
		boolean isMatch = false;
		String dbPw = null;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				dbPw = rs.getString("pw");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		if(dbPw != null && dbPw.equals(tfPw)) isMatch = true;
		
		return isMatch;
	}

	@Override
	public Member getMember(String id) {
		// 입력한 ID를 토대로 DB로부터 회원 정보를 가져와 Member객체로 리턴하는 메소드
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				int no = rs.getInt("no");
				String pw = rs.getString("pw");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String addr = rs.getString("addr");
				int borrowedBookNo = rs.getInt("borrowedBookNo");
				Member member = new Member(no, id, pw, name, tel, addr, borrowedBookNo);
				return member;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ArrayList<Member> view() {
		// 회원DB의 모든 회원 목록을 가져와서 각각의 Member 객체를 만들어 ArrayList<Member>에 담아 리턴하는 메소드
		ArrayList<Member> allMemberArr = new ArrayList<>();
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from members";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int no = rs.getInt("no");
				String id = rs.getString("id");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String addr = rs.getString("addr");
				int borrowedBookNo = rs.getInt("borrowedBookNo");
				allMemberArr.add(new Member(no, id, name, tel, addr, borrowedBookNo));	// 비번, 권한은 목록 출력시 굳이 필요없으므로 제외하고 생성
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return allMemberArr;
	}
	
	@Override
	public ArrayList<Member> view(String column, String value) {
		// 입력받은 옵션과 값으로 회원DB를 검색해 해당하는 회원 목록을 가져와서 각각의 Member 객체를 만들어 ArrayList<Member>에 담아 리턴하는 메소드
		ArrayList<Member> searchMemberArr = new ArrayList<>();
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select * from members where " + column + " like ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+value+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int no = rs.getInt("no");
				String id = rs.getString("id");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String addr = rs.getString("addr");
				int borrowedBookNo = rs.getInt("borrowedBookNo");
				searchMemberArr.add(new Member(no, id, name, tel, addr, borrowedBookNo));	// 비번, 권한은 목록 출력시 굳이 필요없으므로 제외하고 생성
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return searchMemberArr;
	}

	@Override
	public int getBorrowedBookNo(String id) {
		// 입력받은 ID에 해당하는 회원이 대여한 책이 무엇인지 확인해 리턴하는 메소드
		// 0보다 큰수:빌린 도서의 도서번호, 0:빌린 도서가 없음, -1:DB오류
		int borrowedBookNo = -1;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "select borrowedBookNo from members where id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				borrowedBookNo = rs.getInt("borrowedBookNo");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return borrowedBookNo;
	}

	@Override
	public void setBorrowedBookNo(String id, int tfNo) {
		// 입력받은 ID에 해당하는 회원의 빌린 도서 번호를 입력하는 메소드 (도서프로그램의 setCanLendNo와 달리, No는 no가 아니라 Number를 의미)
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update members set borrowedBookNo=? where id=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, tfNo);
			ps.setString(2, id);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setBorrowedBookNoZero(String id) {
		// 입력받은 ID에 해당하는 회원의 빌린 도서 번호를 0으로 바꾸는 메소드 (빌린 도서 반납)
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "javaapi", "javaapi");
			sql = "update members set borrowedBookNo=? where id=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, 0);
			ps.setString(2, id);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}