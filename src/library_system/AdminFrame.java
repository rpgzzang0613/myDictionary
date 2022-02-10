package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class AdminFrame extends JFrame implements ActionListener {
	private JLabel notice_jlb = new JLabel("������ �������Դϴ�.", JLabel.CENTER);
	private JButton logout_jbt = new JButton("�α׾ƿ�");
	private JButton member_jbt = new JButton("ȸ�� ����");
	private JButton book_jbt = new JButton("���� ����");
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	
	private AdminMemberlistDialog memberlistAdmin = new AdminMemberlistDialog(this, "ȸ�� ����", true);
	private AdminBooklistDialog booklistAdmin = new AdminBooklistDialog(this, "���� ����", true);
	private CommonMemberAddDialog memberAddAdmin = new CommonMemberAddDialog(this, "ȸ�� �߰�", true);
	private AdminBookAddDialog bookAddAdmin = new AdminBookAddDialog(this, "���� �߰�", true);
	private AdminMemberSearchDialog memberSearchAdmin = new AdminMemberSearchDialog(this, "ȸ�� �˻�", true);
	private CommonBookSearchDialog bookSearchAdmin = new CommonBookSearchDialog(this, "���� �˻�", true);
	private CommonMemberEditDialog memberEditAdmin = new CommonMemberEditDialog(this, "ȸ�� ����", true);
	private AdminBookEditDialog bookEditAdmin = new AdminBookEditDialog(this, "���� ����", true);
	
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
		// LibraryMemberFrame�� �ִ� ��ư�� (������ �α��� �� ���ʷ� ���̴� ������)
		if(e.getSource() == member_jbt) {
			// ȸ�� ���� ��ư�� ������ ��
			memberlistAdmin.setVisible(true);
		}else if(e.getSource() == book_jbt) {
			// ���� ���� ��ư�� ������ ��
			booklistAdmin.setVisible(true);
		}else if(e.getSource() == logout_jbt) {
			// �α׾ƿ� ��ư�� ������ ��
			JOptionPane.showMessageDialog(this, "�α׾ƿ� �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);
		}
		
		// memberlistAdmin�� �ִ� ��ư�� (adminFrame->ȸ�� ���� ������ �� ������ ���̾�α�)
		else if(e.getSource() == memberlistAdmin.add_jbt) {
			// ȸ�� �߰� ��ư ������ ��
			memberAddAdmin.setVisible(true);	// memberAddAdmin ���̾�α� ���̰� ����
		}else if(e.getSource() == memberlistAdmin.search_jbt) {
			// ȸ�� �˻� ��ư ������ ��
			memberSearchAdmin.setVisible(true);	// memberSearchAdmin ���̾�α� ���̰� ����
		}else if(e.getSource() == memberlistAdmin.edit_jbt) {
			// ȸ�� ���� ��ư ������ ��
			String editId = JOptionPane.showInputDialog(this, "�����Ͻ� ȸ���� ID�� �Է����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			// ������ ȸ���� ID �Է¹���
			
			if(editId == null) return;
			
			boolean isDupl = memberPro.duplChk(editId);
			// ������ ȸ���� ID�� DB�� �ִ��� Ȯ�� �� ������ true ����
			
			if(isDupl) {
				// ������ ȸ�� ID�� ������ ���
				Member currentMember = memberPro.getMember(editId);	// ȸ��ID�� ���� DB���� �ش� ȸ���� ������ �����ͼ� Member��ü�� ����
				memberEditAdmin.setTextField(currentMember);		// memberEditAdmin ���̾�α��� �ؽ�Ʈ�ʵ忡 ���� ���� ǥ��
				memberEditAdmin.setVisible(true);					// memberEditAdmin ���̾�α� ���̰� ����
			}else {
				// ������ ȸ�� ID�� DB�� ���� ���
				JOptionPane.showMessageDialog(this, "�ش��ϴ� ȸ���� �����ϴ�. ID�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberlistAdmin.delete_jbt) {
			// ȸ�� ���� ��ư ������ ��
			
			String deleteId = JOptionPane.showInputDialog(this, "�����Ͻ� ȸ���� ID�� �Է��� �ּ���.", "���", JOptionPane.WARNING_MESSAGE);
			// ������ ȸ���� ID �Է¹���
			
			if(deleteId == null) return;
			
			if(deleteId.matches("(?!^\\d+$)^.+$")) {
				// ���ڷθ� �Է��ϸ� �ȵǵ��� ���� ����
				boolean isDeleted = memberPro.delete(deleteId);	// ���� ó�� �� ������ true ����
				
				if(isDeleted) {
					// ������ ������ ���
					memberlistAdmin.setAllMemberlist();			// ������ ���ΰ�ħ
					JOptionPane.showMessageDialog(this, "�ش� ȸ���� �����Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}else {
					// ������ ������ ���
					JOptionPane.showMessageDialog(this, "�ش� ȸ���� ������ �Ұ��մϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}
			}else {
				// ���ڸ� �Է��� ���
				JOptionPane.showMessageDialog(this, "ID�� �Է����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberlistAdmin.viewAll_jbt) {
			// ��ü ��� ��ư ������ ��
			memberlistAdmin.setAllMemberlist();					// ������ ���ΰ�ħ
		}
		
		// memberAddAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->ȸ�� ����->ȸ�� �߰� ������ �� ������ ���̾�α�)
		else if(e.getSource() == memberAddAdmin.signIn_jbt) {
			// ���� ��ư ������ ��
			Member newMember = memberAddAdmin.getMember();	// memberAddAdmin ���̾�α׿��� �Է¹��� ��ü ���� �����ü�� ������
			boolean isDone = memberPro.input(newMember);	// ������ ��ü�� DB�� �ֱ� ���� memberPro�� �Ѱܼ� ��� �� �������� ��ȯ
			
			if(isDone) {
				// ȸ�� �߰��� �������� ���
				JOptionPane.showMessageDialog(this, "ȸ�� �߰� �Ϸ��Ͽ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				memberAddAdmin.clearAllTextField();			// memberAddAdmin ���̾�α��� �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
				memberAddAdmin.setVisible(false);			// memberAddAdmin �Ⱥ��̰� ����
				memberlistAdmin.setAllMemberlist();			// memberlistAdmin ���ΰ�ħ
			}else {
				// ȸ�� �߰��� �������� ���
				JOptionPane.showMessageDialog(this, "ȸ�� �߰� ����. �ٽ� �õ����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberAddAdmin.duplChk_jbt) {
			// �ߺ�üũ ��ư ������ ��
			String newId = memberAddAdmin.getId();				// memberAddAdmin ���̾�α׿��� �Է¹��� ID���� ������
			boolean isDupl = memberPro.duplChk(newId);			// memberPro�� �Ѱ� �ߺ� üũ �� �ߺ��̸� true ��ȯ
			
			if(isDupl) {
				// ȸ��DB�� �ߺ��Ǵ� ID�� ���� ���
				JOptionPane.showMessageDialog(this, "�̹� ��� ���� ID�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				memberAddAdmin.clearIdTextField();				// ID �ؽ�Ʈ�ʵ� �ʱ�ȭ
			}else {
				// ȸ��DB�� �ߺ��Ǵ� ID�� ���� ���
				JOptionPane.showMessageDialog(this, "��� ������ ID�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberAddAdmin.clear_jbt) {
			// �ʱ�ȭ ��ư ������ ��
			memberAddAdmin.clearAllTextField();					// memberAddAdmin ���̾�α��� �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
		}

		// memberSearchAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->ȸ�� ����->ȸ�� �˻� ������ �� ������ ���̾�α�)
		else if(e.getSource() == memberSearchAdmin.search_jbt) {
			// �˻� ��ư ������ ��
			
			String column = memberSearchAdmin.getSearchOption();		// �����ڰ� ������ �˻� �ɼ��� �޾ƿ�
			String value = memberSearchAdmin.getTextField();			// �����ڰ� �Է��� �˻� ���� �޾ƿ�
			ArrayList<Member> searchMemberArr = memberPro.view(column, value);
			// �ش�Ǵ� ȸ������ DB���� �˻��ؼ� Member ��ü�� ArrayList ���·� ��ƿ�
			
			memberlistAdmin.setMemberlist(searchMemberArr);
			// �޾ƿ� ArrayList�� memberlistAdmin�� �Ѱܼ� textArea�� �ش� ��ϸ� ���̵��� ó��
		}
		
		// memberEditAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->ȸ�� ����->ȸ�� ����->��� �°� �Է� �� Ȯ�� ������ �� ������ ���̾�α�)
		else if(e.getSource() == memberEditAdmin.edit_jbt) {
			// ���� ��ư ������ ��
			
			Member editMember = memberEditAdmin.getMemberToEdit();
			// ������ ������ DB�� ������Ʈ �ϱ� ���� Member��ü ���� (�����ڰ� ���� �Է��� ���� ��� ����)
			
			boolean isDone = memberPro.edit(editMember);
			// ���� �Է��� ���� ���� Member��ü�� DB�� ������Ʈ�ϰ� ���� ���θ� ��ȯ����
			
			if(isDone) {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "ȸ�� ������ �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				memberEditAdmin.clearAllTextField();	// memberEditAdmin�� ��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
				memberEditAdmin.setVisible(false);		// memberEditAdmin ���̾�α� �Ⱥ��̰� ����
				memberlistAdmin.setAllMemberlist();		// memberlistAdmin ���ΰ�ħ
			}else {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "ȸ�� ���� ����. �ٽ� �õ����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == memberEditAdmin.clear_jbt) {
			memberEditAdmin.clearTextField();			// memberEditAdmin�� ��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
		}

		// booklistAdmin�� �ִ� ��ư�� (adminFrame->���� ���� ������ �� ������ ���̾�α�)
		else if(e.getSource() == booklistAdmin.add_jbt) {
			// ���� �߰� ��ư ������ ��
			bookAddAdmin.setVisible(true);		// bookAddAdmin ���̾�α� ���̰� ����
		}else if(e.getSource() == booklistAdmin.search_jbt) {
			// ���� �˻� ��ư ������ ��
			bookSearchAdmin.setVisible(true);	// bookSearchAdmin ���̾�α� ���̰� ����
		}else if(e.getSource() == booklistAdmin.edit_jbt) {
			// ���� ���� ��ư ������ ��
			
			String editNo = JOptionPane.showInputDialog(this, "�����Ͻ� ������ ��ȣ�� �Է����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			// ������ ��ȣ�� �Է¹���
			
			if(editNo == null) return;
			
			if(!editNo.matches("^[0-9]+$")) {
				JOptionPane.showMessageDialog(this, "���ڸ� �Է��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			boolean isDupl = bookPro.duplChk(Integer.parseInt(editNo));
			// ������ ������ ��ȣ�� DB�� �ִ��� Ȯ�� �� ������ true ����
			
			if(isDupl) {
				// ������ ���� ��ȣ�� ������ ���
				
				Book currentBook = bookPro.getBook(Integer.parseInt(editNo));
				// ������ȣ�� ���� DB���� �ش� ������ ������ �����ͼ� Book��ü�� ����
				bookEditAdmin.setTextField(currentBook);	// bookEditAdmin ���̾�α��� �ؽ�Ʈ�ʵ忡 ���� ���� ǥ��
				bookEditAdmin.setVisible(true);				// bookEditAdmin ���̾�α� ���̰� ����
			}else {
				// ������ ���� ��ȣ�� DB�� ���� ���
				JOptionPane.showMessageDialog(this, "�ش��ϴ� ������ �����ϴ�. ��ȣ�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == booklistAdmin.delete_jbt) {
			// ���� ���� ��ư ������ ��
			
			String deleteNo = JOptionPane.showInputDialog(this, "������ ������ ��ȣ�� �Է��� �ּ���.", "���", JOptionPane.WARNING_MESSAGE);
			// ������ ������ ��ȣ�� �Է¹���
			
			if(deleteNo == null) return;
			
			if(!deleteNo.matches("^[0-9]+$")) {
				JOptionPane.showMessageDialog(this, "���ڸ� �Է��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			boolean isDeleted = bookPro.delete(Integer.parseInt(deleteNo));
			// ���� ó�� �� ������ true ����
			
			if(isDeleted) {
				// ���� ���� ��
				booklistAdmin.setAllBooklist();		// ������� ���ΰ�ħ
				JOptionPane.showMessageDialog(this, "�ش� ������ �����Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "�ش� ������ ������ �Ұ��մϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == booklistAdmin.viewAll_jbt) {
			// ��ü ��� ��ư ������ ��
			booklistAdmin.setAllBooklist();
		}
		
		// bookAddAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->���� ����->���� �߰� ������ �� ������ ���̾�α�)
		else if(e.getSource() == bookAddAdmin.add_jbt) {
			// �߰� ��ư ������ ��
			
			Book book = bookAddAdmin.getBook();			// bookAddAdmin ���̾�α׿��� �Է¹��� ��ü ���� �����ü�� ������
			char bookChk = bookPro.input(book);
			// ������ ��ü�� DB�� �ֱ� ���� bookPro�� �Ѱܼ� ��� �� ��� ��ȯ (o : ���� ��� �Ϸ�, x : ���� ��� ����, ? : DB����)
			
			if(bookChk == 'o') {
				JOptionPane.showMessageDialog(this, "���� ����� �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				bookAddAdmin.clearAllTextField();		// bookAddAdmin ���̾�α��� �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
				booklistAdmin.setAllBooklist();			// booklistAdmin ���̾�α� ���ΰ�ħ
			}else if(bookChk == 'x') {
				JOptionPane.showMessageDialog(this, "����� ���ڰ� ���� å�� �̹� �ֽ��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else if(bookChk == '?') {
				JOptionPane.showMessageDialog(this, "DB �����Դϴ�. �����ڿ��� ������ �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == bookAddAdmin.clear_jbt) {
			// �ʱ�ȭ ��ư ������ ��
			bookAddAdmin.clearAllTextField();			// bookAddAdmin ���̾�α��� �ؽ�Ʈ�ʵ� ���� �ʱ�ȭ
		}
		
		// bookSearchAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->���� ����->���� �˻� ������ �� ������ ���̾�α�)
		else if(e.getSource() == bookSearchAdmin.search_jbt) {
			// �˻� ��ư ������ ��
			
			String column = bookSearchAdmin.getSearchOption();			// �����ڰ� ������ �˻� �ɼ��� �޾ƿ�
			String value = bookSearchAdmin.getTextField();				// �����ڰ� �Է��� �˻� ���� �޾ƿ�
			ArrayList<Book> searchBookArr = bookPro.view(column, value);
			// �ش�Ǵ� �������� DB���� �˻��ؼ� Book ��ü�� ArrayList ���·� ��ƿ�
			
			booklistAdmin.setBooklist(searchBookArr);
			// �޾ƿ� ArrayList�� booklistAdmin�� �Ѱܼ� textArea�� �ش� ��ϸ� ���̵��� ó��
		}
		
		// bookEditAdmin ���̾�α׿� �ִ� ��ư�� (adminFrame->���� ����->���� ����->��ȣ �°� �Է� �� Ȯ�� ������ �� ������ ���̾�α�)
		else if(e.getSource() == bookEditAdmin.edit_jbt) {
			// ���� ��ư ������ ��
			
			Book editBook = bookEditAdmin.getBookToEdit();
			// ������ ������ DB�� ������Ʈ �ϱ� ���� Book��ü ���� (�����ڰ� ���� �Է��� ���� ��� ����)
			
			boolean isDone = bookPro.edit(editBook);
			// ���� �Է��� ���� ���� Book��ü�� DB�� ������Ʈ�ϰ� ���� ���θ� ��ȯ����
			
			if(isDone) {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "���� ������ �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				bookEditAdmin.clearAllTextField();		// bookEditAdmin�� ��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
				bookEditAdmin.setVisible(false);		// bookEditAdmin ���̾�α� �Ⱥ��̰� ����
				booklistAdmin.setAllBooklist();			// booklistAdmin ���ΰ�ħ
			}else {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "���� ���� ����. �ٽ� �õ����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == bookEditAdmin.clear_jbt) {
			// �ʱ�ȭ ��ư ������ ��
			bookEditAdmin.clearTextField();
		}
	}
}
