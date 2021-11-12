package library_system;

import java.util.*;

public interface MemberPro {
	public char login(String tfId, String tfPw);
	public boolean input(Member newMember);
	public boolean delete(String id);
	public boolean edit(Member editMember);
	public boolean duplChk(String id);
	public boolean pwdChk(String id, String tfPw);
	public Member getMember(String id);
	public ArrayList<Member> view();
	public ArrayList<Member> view(String column, String value);
	public int getBorrowedBookNo(String id);
	public void setBorrowedBookNo(String id, int tfNo);
	public void setBorrowedBookNoZero(String id);
}
