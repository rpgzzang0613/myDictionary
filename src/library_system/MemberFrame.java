package library_system;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class MemberFrame extends JFrame implements ActionListener {
	private JButton borrowBook_jbt = new JButton("���� �뿩");
	private JButton returnBook_jbt = new JButton("���� �ݳ�");
	private JButton logout_jbt = new JButton("�α׾ƿ�");
	private JButton editAccount_jbt = new JButton("���� ����");
	private JButton deleteAccount_jbt = new JButton("ȸ�� Ż��");
	
	private JPanel north_jp = new JPanel();
	private JPanel center_jp = new JPanel();
	private JLabel notice_jlb = new JLabel("", JLabel.CENTER);
	
	private String memberId = "";
	private Member currentMember;
	
	private MemberBooklistDialog borrowDlg = new MemberBooklistDialog(this, "���� �뿩", true);
	private CommonBookSearchDialog bookSearchDlg = new CommonBookSearchDialog(this, "���� �˻�", true);
	
	private MemberPasswordInputDialog pwdInputToDelete = new MemberPasswordInputDialog(this, "��ȣ Ȯ��", true);
	private MemberPasswordInputDialog pwdInputToEdit = new MemberPasswordInputDialog(this, "��ȣ Ȯ��", true);
	
	private CommonMemberEditDialog editDlg = new CommonMemberEditDialog(this, "���� ����", true);
	
	private BookPro bookPro = new BookProImpl();
	private MemberPro memberPro = new MemberProImpl();
	
	public void setMember(String id) {
		this.currentMember = memberPro.getMember(id);
		memberId = currentMember.getId();
		notice_jlb.setText(memberId+"�� ȯ���մϴ�.");
		notice_jlb.setFont(new Font("", Font.BOLD, 15));
	}
	
	public void init() {
		Container con = this.getContentPane();
		con.setLayout(new GridLayout(3,1));
		con.add(north_jp);
		con.add(center_jp);
		con.add(notice_jlb);
		north_jp.setLayout(new FlowLayout());
		north_jp.add(logout_jbt);
		north_jp.add(editAccount_jbt);
		north_jp.add(deleteAccount_jbt);
		center_jp.setLayout(new GridLayout(1,2));
		center_jp.add(borrowBook_jbt);
		center_jp.add(returnBook_jbt);
	}
	
	public void start() {
		logout_jbt.addActionListener(this);
		editAccount_jbt.addActionListener(this);
		deleteAccount_jbt.addActionListener(this);
		borrowBook_jbt.addActionListener(this);
		returnBook_jbt.addActionListener(this);
		
		borrowDlg.borrow_jbt.addActionListener(this);
		borrowDlg.search_jbt.addActionListener(this);
		borrowDlg.viewAll_jbt.addActionListener(this);
		
		bookSearchDlg.search_jbt.addActionListener(this);
		
		pwdInputToDelete.confirm_jbt.addActionListener(this);
		pwdInputToEdit.confirm_jbt.addActionListener(this);
		
		editDlg.edit_jbt.addActionListener(this);
		editDlg.clear_jbt.addActionListener(this);
	}
	
	public MemberFrame(String title) {
		super(title);
		
		this.init();
		this.start();
		
		super.setSize(400, 150);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int)(screen.getWidth() - this.getWidth()) / 2;
		int ypos = (int)(screen.getHeight() - this.getHeight()) / 2;
		super.setLocation(xpos+420, ypos);
		super.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// LibraryMemberFrame�� �ִ� ��ư�� (ȸ�� �α��� �� ���ʷ� ���̴� ������)
		if(e.getSource() == logout_jbt) {
			// �α׾ƿ� ��ư�� ������ ��
			JOptionPane.showMessageDialog(this, "�α׾ƿ� �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);				// memberFrame �Ⱥ��̰� ����
			NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);	// mainFrame ���̰� ����
		}else if(e.getSource() == editAccount_jbt) {
			// �������� ��ư�� ������ ��
			pwdInputToEdit.setVisible(true);	// pwdInputToEdit ���̾�α׸� ���̰� ���� (�����ϱ� ���� ��ȣ ��Ȯ��)
		}else if(e.getSource() == deleteAccount_jbt) {
			// ȸ��Ż�� ��ư�� ������ ��
			pwdInputToDelete.setVisible(true);	// pwdInputToDelete ���̾�α׸� ���̰� ���� (�����ϱ� ���� ��ȣ ��Ȯ��)
		}else if(e.getSource() == borrowBook_jbt) {
			// �����뿩 ��ư�� ������ ��
			borrowDlg.setVisible(true);			// borrowDlg ���̾�α׸� ���̰� ����
		}else if(e.getSource() == returnBook_jbt) {
			// �����ݳ� ��ư�� ������ ��
			String tfNo = JOptionPane.showInputDialog(this, "�ݳ��� ������ ��ȣ�� �Է��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			// �ݳ��� ������ ��ȣ�� �Է¹���(tfNo)
			
			if(tfNo == null) return;
			
			int borrowedBookNo = memberPro.getBorrowedBookNo(memberId);
			// �ش� ȸ���� ���� ���� ��ȣ�� ������(borrowedBookNo)
			
			char canLend = bookPro.getCanLend(Integer.parseInt(tfNo));
			// ȸ���� �Է��� ��ȣ(tfNo)�� �´� ������ ���� �뿩���ɻ������� üũ
			
			if(canLend == 'y') {
				// �Է��� ��ȣ�� �뿩���ɻ����� ���
				JOptionPane.showMessageDialog(this, "�ش� ������ ���� �뿩������ �ʽ��ϴ�. ��ȣ�� �ٽ� Ȯ���� �ּ���.");
			}else if(canLend == '?') {
				// ���� ��ȣ�� �Է����� ���
				JOptionPane.showMessageDialog(this, "������ ��ȣ�� �ٽ� Ȯ���� �ּ���.");
			}else {
				// �Է��� ��ȣ�� �뿩���� ������ ���
				if(borrowedBookNo < 0) {
					JOptionPane.showMessageDialog(this, "DB �����Դϴ�. �����ڿ��� ������ �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}else if(borrowedBookNo == 0) {
					JOptionPane.showMessageDialog(this, "ȸ������ ���� �뿩�� ���� �����̽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}else {
					// �Է��� ��ȣ�� �뿩���̸鼭 0�� �ƴ� ���
					if(Integer.parseInt(tfNo) == borrowedBookNo) {
						// �Է��� ��ȣ�� ȸ���� ���� ���� ������ ��ȣ�� ��ġ�� ���
						memberPro.setBorrowedBookNoZero(memberId);		// ȸ��DB�� borrowedBookNo�� 0���� ����
						bookPro.setCanLendYes(Integer.parseInt(tfNo));	// �ݳ��� ������ canLend�� y�� ����
						borrowDlg.setAllBooklist();						// ������� textArea ���ΰ�ħ
						JOptionPane.showMessageDialog(this, "���� �ݳ��� �����ϼ̽��ϴ�.");
					}else {
						JOptionPane.showMessageDialog(this, "ȸ������ �뿩�Ͻ� ������ �ƴմϴ�. ��ȣ�� �ٽ� Ȯ���� �ּ���.");
					}
				}
			}
		}
		
		// borrowDlg�� �ִ� ��ư�� (memberFrame->���� �뿩 ������ �� ������ ���̾�α�)
		else if(e.getSource() == borrowDlg.borrow_jbt) {
			// �뿩��û ��ư�� ������ ��
			String tfNo = JOptionPane.showInputDialog(this, "�뿩�� ������ ��ȣ�� �Է��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			// �뿩�� ������ ��ȣ�� �Է¹���(tfNo)
			
			if(tfNo == null) return;
			
			int borrowedBookNo = memberPro.getBorrowedBookNo(memberId);
			// �ش� ȸ���� ���� ���� ��ȣ�� ������(borrowedBookNo), ������ 0
			
			char canLend = bookPro.getCanLend(Integer.parseInt(tfNo));
			// ȸ���� �Է��� ��ȣ(tfNo)�� �´� ������ ���� �뿩���ɻ������� üũ
			
			if(borrowedBookNo < 0) {
				JOptionPane.showMessageDialog(this, "DB �����Դϴ�. �����ڿ��� ������ �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else if(borrowedBookNo > 0) {
				// ȸ���� ���� ���� ��ȣ�� 0�� �ƴ� ���(�̹� �ٸ� ������ ���� ���)
				JOptionPane.showMessageDialog(this, "������ 1�Ǹ� �뿩 �����մϴ�. �뿩�Ͻ� ������ ���� �ݳ��� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}else {
				// ȸ���� ���� ���� ��ȣ�� 0�� ���(������ ���� �� �ִ� ���)
				if(canLend == 'n') {
					// �Է��� ��ȣ�� ������ �뿩���� ���
					JOptionPane.showMessageDialog(this, "�ش� ������ ���� �ٸ� ȸ���Բ� �뿩 ���Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}else if(canLend == '?') {
					// ���� ��ȣ�� �Է����� ���
					JOptionPane.showMessageDialog(this, "������ ��ȣ�� �ٽ� Ȯ���� �ּ���.");
				}else {
					// �Է��� ��ȣ�� ������ �뿩���ɻ����� ���
					memberPro.setBorrowedBookNo(memberId, Integer.parseInt(tfNo));	// ȸ��DB�� borrowedBookNo�� ���� ������ ��ȣ�� ����
					bookPro.setCanLendNo(Integer.parseInt(tfNo));					// ���� ������ canLend�� n���� ����
					borrowDlg.setAllBooklist();										// ������� textArea ���ΰ�ħ
					JOptionPane.showMessageDialog(this, "���� �뿩�� �����ϼ̽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		}else if(e.getSource() == borrowDlg.search_jbt) {
			// �����˻� ��ư�� ������ ��
			bookSearchDlg.setVisible(true);
		}else if(e.getSource() == borrowDlg.viewAll_jbt) {
			// ��ü��� ��ư�� ������ ��
			borrowDlg.setAllBooklist();
		}
		
		// bookSearchDlg�� �ִ� ��ư�� (memberFrame->���� �뿩->���� �˻� ������ �� ������ ���̾�α�)
		else if(e.getSource() == bookSearchDlg.search_jbt) {
			// �˻� ��ư�� ������ ��
			String column = bookSearchDlg.getSearchOption();				// ȸ���� ������ �˻� �ɼ��� �޾ƿ�
			String value = bookSearchDlg.getTextField();					// ȸ���� �Է��� �˻� ���� �޾ƿ�
			ArrayList<Book> searchBookArr = bookPro.view(column, value);
			// �ش�Ǵ� �������� DB���� �˻��ؼ� book ��ü�� ArrayList ���·� ��ƿ�
			
			borrowDlg.setBooklist(searchBookArr);
			// �޾ƿ� ArrayList�� borrowDlg�� �Ѱܼ� textArea�� �ش� ��ϸ� ���̵��� ó��
		}
		
		// pwdInputToDelete�� �ִ� ��ư (memberFame->ȸ�� Ż�� ������ �� ������ ����Է� ���̾�α�)
		else if(e.getSource() == pwdInputToDelete.confirm_jbt) {
			// �н����� �Է� �� Ȯ�� ��ư ������ ��
			
			String tfPw = pwdInputToDelete.getPassword();			// ȸ���� �ٽ� �Է��� ��й�ȣ�� �޾ƿ�
			boolean isMatch = memberPro.pwdChk(memberId, tfPw);		// ȸ��DB�� ��й�ȣ�� �Է��� ��й�ȣ�� ��
			
			if(isMatch) {
				// ȸ��DB���� ��й�ȣ�� �Է��� ��й�ȣ�� ��ġ�� ���
				
				boolean isDeleted = memberPro.delete(memberId);
				// ���� ó�� �� �����ϸ� true ��ȯ, �����ϸ� false ��ȯ
				
				if(isDeleted) {
					// ������ ������ ��� (�뿩�� ������ ���� ���)
					pwdInputToDelete.clearPassword();		// ��й�ȣ �Է� ���̾�α��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
					pwdInputToDelete.setVisible(false);		// ��й�ȣ �Է� ���̾�α� �Ⱥ��̰� ����
					JOptionPane.showMessageDialog(this, "Ż�� ó���� �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(false);					// memberFrame �Ⱥ��̰� ����
					NotMemberAndMainFrame.getLibraryMainFrame().setVisibleCustom(true);	// mainFrame ���̰� ����
				}else {
					// ������ ������ ��� (�뿩�� ������ ���� ���)
					pwdInputToDelete.clearPassword();		// ��й�ȣ �Է� ���̾�α��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
					pwdInputToDelete.setVisible(false);		// ��й�ȣ �Է� ���̾�α� �Ⱥ��̰� ����
					JOptionPane.showMessageDialog(this, "������ �ݳ��� �Ŀ� Ż�� �����մϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				}
			}else {
				// ȸ��DB���� ��й�ȣ�� �Է��� ��й�ȣ�� �ٸ� ���
				pwdInputToDelete.clearPassword();
				JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		// pwdInputToEdit�� �ִ� ��ư (memberFame->���� ���� ������ �� ������ ����Է� ���̾�α�)
		else if(e.getSource() == pwdInputToEdit.confirm_jbt) {
			// �н����� �Է� �� Ȯ�� ��ư ������ ��
			
			String tfPw = pwdInputToEdit.getPassword();			// ȸ���� �ٽ� �Է��� ��й�ȣ�� �޾ƿ�
			boolean isMatch = memberPro.pwdChk(memberId, tfPw);	// ȸ��DB�� ��й�ȣ�� �Է��� ��й�ȣ�� ��
			
			if(isMatch) {
				// ȸ��DB���� ��й�ȣ�� �Է��� ��й�ȣ�� ��ġ�� ���
				pwdInputToEdit.clearPassword();				// ��й�ȣ �Է� ���̾�α��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
				pwdInputToEdit.setVisible(false);			// ��й�ȣ �Է� ���̾�α� �Ⱥ��̰� ����
				editDlg.setTextField(currentMember);
				// editDlg�� ���� �α����� ȸ�������� ���� ��ü�� �Ѱܼ� �ؽ�Ʈ�ʵ忡 ���� ���� ǥ��
				editDlg.setVisible(true);					// editDlg ���̰� ����
			}else {
				// ȸ��DB���� ��й�ȣ�� �Է��� ��й�ȣ�� �ٸ� ���
				pwdInputToEdit.clearPassword();				// ��й�ȣ �Է� ���̾�α��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
				JOptionPane.showMessageDialog(this, "��й�ȣ�� Ȯ���� �ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		// editDlg�� �ִ� ��ư (memberFrame->���� ����->��� �°� �Է��� Ȯ�� ������ �� ������ �������� ���̾�α�)
		else if(e.getSource() == editDlg.edit_jbt) {
			// �ٲٰ� ���� ������ ������ �� ���� ��ư ������ ��
			
			Member editMember = editDlg.getMemberToEdit();
			// ������ ������ DB�� ������Ʈ �ϱ� ���� Member��ü ���� (ȸ���� ���� �Է��� ���� ��� ����)
			boolean isDone = memberPro.edit(editMember);
			// ���� �Է��� ���� ���� Member��ü�� DB�� ������Ʈ�ϰ� ���� ���θ� ��ȯ����
			
			if(isDone) {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "������ �Ϸ�Ǿ����ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
				editDlg.clearAllTextField();	// editDlg�� ��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
				editDlg.setVisible(false);		// editDlg ���̾�α� �Ⱥ��̰� ����
			}else {
				// ���� ���� ��
				JOptionPane.showMessageDialog(this, "���� ����. �ٽ� �õ����ּ���.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == editDlg.clear_jbt) {
			editDlg.clearTextField();			// editDlg�� ��� �ؽ�Ʈ�ʵ� �ʱ�ȭ
		}
	}
}
