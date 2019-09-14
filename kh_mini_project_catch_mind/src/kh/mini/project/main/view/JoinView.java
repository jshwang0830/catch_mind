package kh.mini.project.main.view;

 

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.db.UserController;
import kh.mini.project.model.vo.User;

 

public class JoinView extends JFrame{

// TextField
	private JTextField id_tf; // ID�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JPasswordField pw_tf; // PW�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JPasswordField pwRe_tf; 
	private JTextField name_tf; // �̸��� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField dateOfBirth_tf; // ��������� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField eMail_tf; // �̸����� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�

// Network �ڿ� ����
	private Socket socket;// ����� ����
	private int port; // ��Ʈ��ȣ
	

// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����	
	private int mouseX; // ���콺 ��ǥ ����
	private int mouseY; // ���콺 ��ǥ ����
	private DataOutputStream dos = null; 
	//Ŀ��
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Image cursorBasic = tk.getImage(Main.class.getResource("/images/pencilCursor.png"));
	private Image cursorClicked = tk.getImage(Main.class.getResource("/images/pencilCursorClicked.png"));
	private Cursor myCursor = tk.createCustomCursor(cursorBasic, new Point(10,10), "Pencil Cursor");
	private Cursor myCursorClicked = tk.createCustomCursor(cursorClicked, new Point(10,10), "Pencil Cursor Clicked");
	

// ȸ������ �Է�����
	private String id="";
	private String pw="";
	private String pwRe=""; // �н����� Ȯ�ο�
	private String name="";
	private String dateOfBirth=""; // �������(19990101)
	private String eMail="";
	private int age; // dateOfBirth�� ����Ͽ� ����
	private char gender;

//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/loginmain.png")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)
 

//Button
	ImageIcon exitBasic = new ImageIcon(Main.class.getResource("/images/loginExitButtonBasic.png"));
	ImageIcon exitEntered = new ImageIcon(Main.class.getResource("/images/loginExitButtonEntered.png"));
	private JButton exitButton = new JButton(exitBasic); // ������ ��ư
	ImageIcon joinBasic = new ImageIcon(Main.class.getResource("/images/loginJoinButtonBasic.png"));
	ImageIcon joinEntered = new ImageIcon(Main.class.getResource("/images/loginJoinButtonEntered.png"));
	private JButton joinButton = new JButton(joinBasic); // ���� ��ư	
	
	ImageIcon checkBasic = new ImageIcon(Main.class.getResource("/images/checkButtonBasic.png"));
	ImageIcon checkEntered = new ImageIcon(Main.class.getResource("/images/checkButtonEntered.png"));
	private JButton idCheck = new JButton(checkBasic);
	
	
	
	public JoinView() {
		//����� ���ÿ� socket,port,ID�� MainView�κ��� �̾�޾ƿ´�.
		socket = MainView.getSocket();
		port = MainView.getPort();
		dos = MainView.getDos(); // MainView Output��Ʈ���� �̾� �޾ƿ´�.
		
		
		Font font = new Font("Inconsolata",Font.BOLD,15);
	// JFrame mainView
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(342, 405);
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);	
		
		
		
		// ���콺�� â�� ������ �� �ִ�.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
 
		// �ߺ�Ȯ�ι�ư
		idCheck.setBounds(255, 62, 34, 17);
		idCheck.setBorder(null);
		idCheck.setBackground(new Color(0, 0, 0, 0));
		add(idCheck);
		idCheck.addMouseListener(new MouseAdapter() {
 
			@Override
			public void mouseEntered(MouseEvent e) {
				idCheck.setIcon(checkEntered); // ���콺�� �÷������� �̹��� ����(Entered Image)
				idCheck.setCursor(myCursorClicked);
 
			}
 
			@Override
			public void mouseExited(MouseEvent e) {
				idCheck.setIcon(checkBasic); // ���콺�� �÷������� �̹��� ����(Entered Image)
				idCheck.setCursor(myCursor);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(e.getButton() == 1) {
					if(id_tf.getText().length()<6) {
						JOptionPane.showMessageDialog(null,"���̵�� 6~12�ڸ� ���� �� �ֽ��ϴ�.");
					} else {
						Boolean idCheckStatus=false; // ���콺 ���� Ŭ�� �Ҷ����� false�� �ʱ�ȭ
						String idCheck=id_tf.getText(); // ID �Է�â(TextField)���� �� �����ͼ� idCheck�� ����
						String existId = ""; // DB�� ����� ���̵� ������ ���ڿ� ����
						System.out.println("�Է��� ���̵�:"+idCheck);	// �Է��� ID ���
						Vector allUser=new UserController().dataLoadAll(); // userDB.dat���� ��� ���� �����ͼ� Vector allUser ������ ����
						
						for(int i=0; i<allUser.size(); i++) { // allUser(DB���� ������ ��� ����� ����) ��ŭ �ݺ��� ����
							existId=((User)allUser.get(i)).getId(); // allUser�� 0��°���� ���ʴ�� id �����ͼ� existId�� ����
							System.out.println(i+"��° �ε����� �����ϴ� ���̵�:"+existId);
							if(idCheck.equals(existId)) { // ���̵� �ߺ��Ǹ�
								idCheckStatus=true;	// idCheckStatus true�� �ٲٱ�(����)
								break; // �ߺ��Ǵ� ID�� ������ for��(�ݺ���) ������(����), �� �̻� ���� �ʿ䰡 �����Ƿ�!!
							}
						}
						
						if(idCheckStatus) { // ���� for������ ���Ͽ� idCheckStatus�� true�̸� (���̵� �ߺ��Ǹ�)
							JOptionPane.showMessageDialog(null, "�ߺ��� ���̵��Դϴ�. \n�ٸ� ���̵� �Է����ּ���.");
						}else { // ���� for������ ���Ͽ� idCheckStatus�� false�̸� (���̵� �ߺ��Ǵ� ���� ������)
							JOptionPane.showMessageDialog(null, "��� ������ ���̵� �Դϴ�.");
						}
					}
				}
			}
			
		});
		
	// TextField
		//ID �Է�
		id_tf = new JTextField();
		id_tf.setBounds(165, 55, 138, 30);
		id_tf.setBorder(null);
		id_tf.setBackground(new Color(0,0,0,0));
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //���̵� �ִ� 12�� ����
		
		//PW �Է�
		pw_tf = new JPasswordField();
		pw_tf.setBounds(165, 96, 138, 30);
		pw_tf.setBorder(null);
		pw_tf.setBackground(new Color(0,0,0,0));
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // ��й�ȣ �ִ� 12�� ����
		
		//PW RE
		pwRe_tf = new JPasswordField();
		pwRe_tf.setBounds(164, 137, 138, 30);
		pwRe_tf.setBorder(null);
		pwRe_tf.setBackground(new Color(0,0,0,0));
		add(pwRe_tf);
		pwRe_tf.setDocument(new JTextFieldLimit(12));
		
		//165, 137, 138, 30
		//�̸� �Է�
		name_tf = new JTextField();
		name_tf.setBounds(165, 178, 138, 30);
		name_tf.setBorder(null);
		name_tf.setBackground(new Color(0,0,0,0));
		add(name_tf);
		name_tf.setDocument(new JTextFieldLimit(5)); //�̸� �ִ� 5�� ����
		
		//������� �Է�
		dateOfBirth_tf = new JTextField();
		dateOfBirth_tf.setBounds(165, 221, 138, 30);
		dateOfBirth_tf.setBorder(null);
		dateOfBirth_tf.setBackground(new Color(0,0,0,0));
		add(dateOfBirth_tf);
		dateOfBirth_tf.setDocument(new JTextFieldLimit(6)); //������� �ִ� 6�� ����

		
		//e-Mail �Է�
		eMail_tf = new JTextField();
		eMail_tf.setBounds(165, 262, 138, 30);
		eMail_tf.setBorder(null);
		eMail_tf.setBackground(new Color(0,0,0,0));
		add(eMail_tf);
		eMail_tf.setDocument(new JTextFieldLimit(30)); //�̸��� �ִ� 30�� ����
	
	// RadioButton
		//���� ����
		JRadioButton  genderMale = new JRadioButton("��"); // JRadioButton ����
		genderMale.setFont(font); 
		JRadioButton  genderFemale = new JRadioButton("��"); 
		genderFemale.setFont(font);
		ButtonGroup  genderGroup = new ButtonGroup(); //������ư �׷�ȭ�� ���� ��ư�׷� ����. ���� �׷쳢���� �׷��߿� 1���� ���õȴ�.
		genderGroup.add(genderMale);  
		genderGroup.add(genderFemale); //�׷쿡 �׷�ȭ��ų ��ư���� �߰�	
		genderMale.setBounds(165, 305, 50, 30);
		genderMale.setBackground(new Color(0,0,0,0)); 
		genderMale.setFont(font);
		genderMale.setForeground(Color.BLACK);
		genderMale.setSelected(true);
		add(genderMale);
		genderFemale.setBounds(220, 305, 50, 30);
		genderFemale.setBackground(new Color(0,0,0,0)); 
		genderFemale.setFont(font);
		genderFemale.setForeground(Color.BLACK);
		add(genderFemale);
 
 
	// Button
		// #������ ��ư
		exitButton.setBounds(210, 350, 88, 35);
		exitButton.setBorder(null);
		exitButton.setBackground(new Color(0,0,0,0));
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEntered); // ���콺�� �÷������� �̹��� ����(Entered Image)
				exitButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasic); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				dispose(); // �ϳ��� �����Ӹ� �����ϱ� ���� �޼ҵ�
			}
		});
		
		
		
		// #���� ��ư
		joinButton.setBounds(60, 350, 88, 35);
		joinButton.setBorder(null);
		joinButton.setBackground(new Color(0,0,0,0));
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� ��(������ ��) �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEntered); // ���콺�� �÷������� �̹��� ����(Entered Image)
				joinButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}
			
			// ���콺�� ��ư���� ������ ��(���� ���� ��) �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinBasic); // ���콺�� �������� �̹��� ����(Basic Image)
				joinButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String emailCheck = "^[a-zA-Z0-9]+@[a-zA-Z0-9]*+.([a-zA-Z0-9])+$";
				String nuII = "^[\\s]*$";
				String dateCheck = "^[0-9]*$";
				String pwCheck = "^[a-zA-Z0-9]*$";
				String nameCheck = "^[��-�R]*$";
				String idCheck = "^[a-zA-Z0-9]*$";
				
				boolean ch = false;
				boolean formatcheck = true;
				String msg = null;
				
				// ȸ�� ���� �Է� ���� �����鿡 ����
				
				// ���̵� Format check
				id=id_tf.getText();
				if(formatcheck && id.matches(nuII)) {
					msg = "���̵� ������ ���� �� �����ϴ�.";
					formatcheck = false;
				} else if (formatcheck && id.length()<6) {
					msg = "���̵�� 6~12�ڸ� ���� �� �ֽ��ϴ�.";
					formatcheck = false;
				} else if (formatcheck && !id.matches(idCheck)) {
					msg = "���̵�� ���� �� ���ڸ� ���� �� �ֽ��ϴ�.";
					formatcheck = false;
				}
				
				//  ��й�ȣ Format check
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
				char[] tempPwRe = pwRe_tf.getPassword();
				for(char a : tempPwRe) {
					pwRe += a;
				}
				if(formatcheck && (pw.length()<6 || !pw.matches(pwCheck) || pw.matches(nuII))) {
					msg = "�н������ 6~12�� ��/�빮��, ���ڸ� ���� �� �ֽ��ϴ�.";
					formatcheck = false;
				} else if(formatcheck && !pw.equals(pwRe)) {
					msg = "�н����尡 ��ġ���� �ʽ��ϴ�.";
					formatcheck = false;
				}  
				
				
				name=name_tf.getText();
				if(formatcheck && name.matches(nuII)) {
					msg = "�̸��� ������ ���� �� �����ϴ�.";
					formatcheck = false;
				} else if (formatcheck && !name.matches(nameCheck)) {
					msg = "�̸��� �ѱ۸� ��� �����մϴ�. (ex.ȫ�浿)";
					formatcheck = false;
				}
				
				dateOfBirth=dateOfBirth_tf.getText();
				if(formatcheck && dateOfBirth.matches(nuII)) {
					msg = "������Ͽ� ������ ���� �� �����ϴ�.";
					formatcheck = false;
				} else if(formatcheck && !dateOfBirth.matches(dateCheck)) {
					msg = "���� �������� �Է��Ͻʽÿ�. (ex.901010)";
					formatcheck = false;
				}
				
				eMail=eMail_tf.getText();
				if(formatcheck && !eMail.matches(emailCheck)) {
					msg = "�̸��� ���Ŀ� �°� �Է��Ͻʽÿ�.";
					formatcheck = false;
				}
				
				if(genderMale.isSelected()) {
					gender = (genderMale.getText()).charAt(0);
				} else if (genderFemale.isSelected()) {
					gender = (genderFemale.getText()).charAt(0);
				}
				
				
				if(formatcheck) {
					// �Է¹��� ������ ������ ������.
					send_message("JoinRequest/"+id +"/"+ pw +"/" + name+"/" + dateOfBirth+"/" + eMail+"/" + gender);
					JOptionPane.showMessageDialog(null, "���������� ���ԵǾ����ϴ�.");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, msg);
					msg = "";
					formatcheck = true;
					pw = "";
					pwRe = "";
				}
			}
		});
	}


	// �ؽ�Ʈ �ʵ� ���� �� ������ ���� Ŭ���� �� �޼ҵ�
	public class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}
			
		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			if (str == null) return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
	
	private void send_message(String str) // �������� �޼����� ������ �κ�
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ���� ó�� �κ�
		{
			e.printStackTrace();
		}
	}
	
	
	
	/* �Ʒ� paint() �޼ҵ�� GUI Application�� ����ǰų� 
	 * Ȱ��/��Ȱ������ ���� ���� ������ ����������, ����Ǵ� �޼ҵ��̴�. */
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(342, 405);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}



	public static void main(String[] args) {
		new JoinView();
	}
}