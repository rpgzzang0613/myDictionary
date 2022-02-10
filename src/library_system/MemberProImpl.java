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
			Class.forName("oracle.jdbc.driver.OracleDriver");		// �޸𸮿� ����̹� �ø���
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		ps = null; // ���������� ���� ��ü
		
		sql = "";
	}
	
	@Override
	public char login(String tfId, String tfPw) {
		// ID,PW�� �Է¹޾� �α����� ó���ϴ� �޼ҵ�
		// s:������, o:�Ϲ�ȸ��, n:PW����, x:��ȸ��, ?:DB����
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
		// ȸ���� DB�� ����� �� ���Ǵ� �޼ҵ� (�������� ȸ���߰�, ��ȸ���� ȸ������)
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
		// ȸ��DB�� ������ ������ �� ���Ǵ� �޼ҵ� (�������� ȸ������, �Ϲ�ȸ���� ��������)
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
		// ȸ���� DB���� ������ �� ����ϴ� �޼ҵ� (�������� ȸ������, �Ϲ�ȸ���� ȸ��Ż��)
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
		// ȸ��DB�� �ش� ID�� ���� ȸ���� �ִ��� üũ�� �� ���� �޼ҵ�
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
		// ȸ��DB�� �ش� ID�� ���� ȸ���� �ڽ��� PW�� ����� �Է��ߴ��� üũ�� �� ���� �޼ҵ�
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
		// �Է��� ID�� ���� DB�κ��� ȸ�� ������ ������ Member��ü�� �����ϴ� �޼ҵ�
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
		// ȸ��DB�� ��� ȸ�� ����� �����ͼ� ������ Member ��ü�� ����� ArrayList<Member>�� ��� �����ϴ� �޼ҵ�
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
				allMemberArr.add(new Member(no, id, name, tel, addr, borrowedBookNo));	// ���, ������ ��� ��½� ���� �ʿ�����Ƿ� �����ϰ� ����
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return allMemberArr;
	}
	
	@Override
	public ArrayList<Member> view(String column, String value) {
		// �Է¹��� �ɼǰ� ������ ȸ��DB�� �˻��� �ش��ϴ� ȸ�� ����� �����ͼ� ������ Member ��ü�� ����� ArrayList<Member>�� ��� �����ϴ� �޼ҵ�
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
				searchMemberArr.add(new Member(no, id, name, tel, addr, borrowedBookNo));	// ���, ������ ��� ��½� ���� �ʿ�����Ƿ� �����ϰ� ����
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return searchMemberArr;
	}

	@Override
	public int getBorrowedBookNo(String id) {
		// �Է¹��� ID�� �ش��ϴ� ȸ���� �뿩�� å�� �������� Ȯ���� �����ϴ� �޼ҵ�
		// 0���� ū��:���� ������ ������ȣ, 0:���� ������ ����, -1:DB����
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
		// �Է¹��� ID�� �ش��ϴ� ȸ���� ���� ���� ��ȣ�� �Է��ϴ� �޼ҵ� (�������α׷��� setCanLendNo�� �޸�, No�� no�� �ƴ϶� Number�� �ǹ�)
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
		// �Է¹��� ID�� �ش��ϴ� ȸ���� ���� ���� ��ȣ�� 0���� �ٲٴ� �޼ҵ� (���� ���� �ݳ�)
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