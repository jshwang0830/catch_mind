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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.GameRoom.GameRoom;
import kh.mini.project.GameRoom.Music;
import kh.mini.project.waiting_room.view.WaitingRoom;

public class MainView extends JFrame{
	
//Label
	private JLabel mainMenuBar = new JLabel();

// Textfield	
	private JTextField serverIp_tf; // Server IP�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField port_tf; // ��Ʈ��ȣ�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
	private JTextField id_tf; // ID�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	private JPasswordField pw_tf; // PW�� �Է¹ޱ� ���� �ؽ�Ʈ �ʵ�
	
// Network �ڿ� ����
	private static Socket socket; // ����� ����
	private static int port; // ��Ʈ��ȣ	
	private String ip=""; // 127.0.0.1 �� �ڱ� �ڽ�
	private static String id=null; // ����� ID
	private String pw=""; // ����� PW => ������ ���Ծ��� �α��� �����ϰ� �Ͽ� �׽�Ʈ ����
	private InputStream is;
	private OutputStream os;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	
// ���� ����
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics; // �׷��� ����� ����
	private int mouseX, mouseY; // ���콺 ��ǥ�� ����
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private boolean connectionIpCheck = false;
	private boolean connectionPortCheck = false; // Waiting room���� �Ѿ�� ���� Ŀ�ؼ� üũ.(ConnectException �� �߻��ϸ� �α��� ���� �˸��� �߻���Ű�� ����)
	private boolean loginCk = false;
	private boolean changePoint = true; // MainView���� WaitingRoom���� �Ѿ�� â��ȯ�� �߻��ϸ� false�� �ٲپ� �ش� run �޼ҵ��� ������ �����.
	private boolean flag = false;
	private WaitingRoom wr; // WaitingRoom Ŭ���� ��ü
	private GameRoom paint; // Paint Ŭ���� ��ü
	private Toolkit tk = Toolkit.getDefaultToolkit();
	//Ŀ��
	private Image cursorBasic = tk.getImage(Main.class.getResource("/images/pencilCursor.png"));
	private Image cursorClicked = tk.getImage(Main.class.getResource("/images/pencilCursorClicked.png"));
	private Cursor myCursor = tk.createCustomCursor(cursorBasic, new Point(10,10), "Pencil Cursor");
	private Cursor myCursorClicked = tk.createCustomCursor(cursorClicked, new Point(10,10), "Pencil Cursor Clicked");
	
//Image	
	// #MainView ���
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/MainViewBackground.png")).getImage();
			//Main Ŭ������ ��ġ�� �������� �̹��� ������ ��ġ�� ã�� ������ �̹��� �ν��Ͻ��� �ش� ������ �ʱ�ȭ ����(����� ���� ������)
	
	//Button Icon (basic : ��ư�� �⺻ ����, Entered : ��ư�� ���콺�� ������ ����) 
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exitButtonBasic.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exitButtonEntered.png")); 
	private ImageIcon conncetBasicImage = new ImageIcon(Main.class.getResource("/images/connectButtonBasic.png"));
	private ImageIcon connectEnteredImage = new ImageIcon(Main.class.getResource("/images/connectButtonEntered.png")); 
	private ImageIcon loginBasicImage = new ImageIcon(Main.class.getResource("/images/loginButtonBasic.png"));
	private ImageIcon loginEnteredImage = new ImageIcon(Main.class.getResource("/images/loginButtonEntered.png")); 
	private ImageIcon joinBasicImage = new ImageIcon(Main.class.getResource("/images/joinButtonBasic.png"));
	private ImageIcon joinEnteredImage = new ImageIcon(Main.class.getResource("/images/joinButtonEntered.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư
	private JButton connetButton = new JButton(conncetBasicImage); // ���� ��ư
	private JButton loginButton = new JButton(loginBasicImage); // �α��� ��ư
	private JButton joinButton = new JButton(joinBasicImage); // ȸ������ ��ư
	private JButton mute = new JButton();
	
	boolean muteSel=false;
	
//BGM
	private Music bgm;
	private Music buttonEnteredBGM;
	
	MainView() {
	//BGM		
		bgm = new Music("loginBGM.mp3", true);
		bgm.start();
	// JFrame mainView
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������)
		setTitle("Catch Mind"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null); // ��ġ ������ ����
		setCursor(myCursor);
		
	// Label
		// #�޴���
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		mainMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
			// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(mainMenuBar);
	
	// TextField
		Font mainViewFont = new Font("�޸�����ü", Font.PLAIN,18 ); //��Ʈ����
		
		//IP �Է�
		serverIp_tf = new JTextField(); 
		serverIp_tf.setBounds(525, 550, 150, 30);
		serverIp_tf.setBorder(null); // �׵θ� ����
		serverIp_tf.setBackground(new Color(0,0,0,0)); // ��� �����
		serverIp_tf.setFont(mainViewFont);
		add(serverIp_tf); 
		serverIp_tf.setDocument(new JTextFieldLimit(15)); //15�� ����(ip �ִ� �Է°��� ũ�� 000.000.000.000)
		serverIp_tf.setText("127.0.0.1"); //IP �⺻�� 127.0.0.1(�ڱ� �ڽ�)
		
		//��Ʈ��ȣ �Է�
		port_tf = new JTextField();  
		port_tf.setBounds(525, 590, 150, 30); 
		port_tf.setBorder(null); // �׵θ� ����
		port_tf.setBackground(new Color(0,0,0,0)); // ��� �����
		port_tf.setFont(mainViewFont);
		add(port_tf);
		port_tf.setDocument(new JTextFieldLimit(5)); // 5�� ���� (��Ʈ��ȣ ���� 0~65535)	 
		port_tf.setText("12345");//��Ʈ��ȣ �⺻�� 12345
		
		//ID �Է�
		id_tf = new JTextField();
		id_tf.setBounds(525, 630, 150, 30); 
		id_tf.setBorder(null); // �׵θ� ����
		id_tf.setBackground(new Color(0,0,0,0)); // ��� �����
		id_tf.setFont(mainViewFont);
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //���̵� �ִ� 12�� ����
				
		//PW �Է�
		pw_tf = new JPasswordField();
		pw_tf.setBounds(525, 670, 150, 30);
		pw_tf.setBorder(null); // �׵θ� ����
		pw_tf.setBackground(new Color(0,0,0,0)); // ��� �����
		pw_tf.setFont(mainViewFont);
		pw_tf.setEchoChar('*'); // ȭ�鿡 ǥ���� ���ڸ� '*'�� ����
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // ��й�ȣ �ִ� 12�� ����
		
	// Button
		// #������ ��ư
		exitButton.setBounds(575, 710, 117, 47);
		exitButton.setBorder(null); // �׵θ� ����
		exitButton.setBackground(new Color(0,0,0,0)); // ��� �����
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				exitButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				if(!muteSel) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start(); 
				}
				
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				
				
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == 1) {
					System.exit(0); // ���μ��� ����.
				}
			}
		});
		// #���� ��ư
		connetButton.setBounds(458, 710, 117, 47);
		connetButton.setBorder(null); // �׵θ� ����
		connetButton.setBackground(new Color(0,0,0,0)); // ��� �����
		add(connetButton);
		connetButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				connetButton.setIcon(connectEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				connetButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				if(!muteSel) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start(); 
				}
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				connetButton.setIcon(conncetBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				connetButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("���� ��ư Ŭ��");
				// ���� ��ư�� ������ Server IP, port�� �����Ѵ�.
				ip = serverIp_tf.getText().trim(); // �� ���� ����
				try {
					port = Integer.parseInt(port_tf.getText().trim());
				} catch(NumberFormatException ee) {
					
				}
				
				// ��Ʈ��ũ ���� ����
				Network();
				
				// connetionCheck ���¿� ���� ������ �� �˸�â ������ȯ
				if(!connectionIpCheck) { // ip�� ���������� ���� ��(UnknownHostException ���� �߻� ��)
					JOptionPane.showMessageDialog(null, 
							"���� ����!\nIP�� �ٽ� Ȯ�����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
				} else if (!connectionPortCheck) { // port�� ���������� �ʰų� ������ �񰡵����� ��(ConnectException ���� �߻� ��)
					JOptionPane.showMessageDialog(null, 
							"���� ����!\nServer Port Number�� ��ġ���� �ʰų�"
							+"\n������ ���������� �ʽ��ϴ�.\n�ٽ� Ȯ�����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
				} else { // ���ܰ� �߻����� �ʾҴٴ� ���� �������� ������ �̷�����ٴ� �ǹ̷� ó��(���� ����)
					connetionChecked(connectionPortCheck);
					connectToLogin();
					JOptionPane.showMessageDialog(null,"������ ���������� �̷�������ϴ�."
							+ "\n���� �α����� �Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		// #�α��� ��ư
		loginButton.setBounds(458, 710, 117, 47);
		loginButton.setBorder(null); // �׵θ� ����
		loginButton.setBackground(new Color(0,0,0,0)); // ��� �����
		loginButton.setEnabled(false); // �ʱ���� ������ �ʰ�!
		add(loginButton);
		loginButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(loginEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				loginButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				if(!muteSel) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start(); 
				}
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				loginButton.setIcon(loginBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				loginButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println("�α��� ��ư Ŭ��");
				// �α��� ��ư�� ������ Server IP, port, id, pw�� �����Ѵ�.
				
				id = id_tf.getText().trim();
				//JPasswordField�� getText()�޼ҵ带 ������ �ʴ´� �Ͽ� �Ʒ��� ���� ������� ����
				pw = "";
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
					
				if(connectionPortCheck) {
					send_message("LoginCheck/" + id + "/" + pw);
				} else { // 
					JOptionPane.showMessageDialog(null, 
							"�α��� ����!\n���̵�� �н����带 �ٽ� Ȯ�����ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);

				}
			}
			
		});
		
		
		// #ȸ������ ��ư
		joinButton.setBounds(341, 710, 117, 47);
		joinButton.setBorder(null); // �׵θ� ����
		joinButton.setBackground(new Color(0,0,0,0)); // ��� �����
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				joinButton.setCursor(myCursorClicked); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				if(!muteSel) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start(); 
				}
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				joinButton.setCursor(myCursor); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
			
			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override 
			public void mousePressed(MouseEvent e) {
				if(connectionPortCheck) {
					new JoinView();
				} else {
					JOptionPane.showMessageDialog(null, 
							"������ ���� ���� �Ŀ� �õ��Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);

				}
			}
			
		});
		
		connetionChecked(connectionPortCheck);
		
		mute.setBounds(960,730,30,30);
		mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOn.png")));
		mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOnCLK.png")));	
		mute.setContentAreaFilled(false);
		mute.setFocusPainted(false);
		mute.setBorderPainted(false);
		mute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(muteSel) {
					muteSel=false;
					mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOn.png")));
					mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOnCLK.png")));	
					
				}
				else {
					muteSel=true;
					mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOff.png")));
					mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/YsoundOffCLK.png")));
				}
				soundOn(muteSel);
			}
		});
		add(mute);
		mute.setVisible(true);
	}
	
	public void soundOn(boolean muteSel) {
		if(muteSel) {
			bgm.close();
		}
		else
		{
			bgm = new Music("loginBGM.mp3", true);
			bgm.start();
		}
	}
	
	// #���ἳ���� �Ϸ�Ǹ� �α��� ��ư���� �ٲ��.
	private void connectToLogin() {
		connetButton.setEnabled(false);
		connetButton.setVisible(false);
		loginButton.setEnabled(true);
	}
	
	// #connetionCheck ���� ���� IP/Port �ʵ�� ID/PW �ʵ��� Ȱ��ȭ�� �ٲ�
	private void connetionChecked(boolean connetionCheck) {
		if(connetionCheck) { // ���� �����Ǿ� �ִ� ������ ��
			serverIp_tf.setEnabled(false);
			port_tf.setEnabled(false);
			id_tf.setEnabled(true);
			pw_tf.setEnabled(true);
		} else { // ���� ������ �Ǿ��ֱ� ���� ������ ��
			serverIp_tf.setEnabled(true);
			port_tf.setEnabled(true);
			id_tf.setEnabled(false);
			pw_tf.setEnabled(false);
		}
	}
	
	// #�Էµ� IP�� �������� IP���� üũ�Ѵ�.
//	private void ipCheck(String ip) {
//		StringTokenizer ipst = new StringTokenizer(ip,".");
//		for(int i=1; i<=4; i++) {
//			int temp = Integer.parseInt(ipst.nextToken());
//			if(temp>)
//		}
//	}
	
	private void Network() 
	{	
		connectionIpCheck = true; // ���� �������� ture�� �ʱ�ȭ
		connectionPortCheck = true; // ���� �������� ture�� �ʱ�ȭ
		try {
			socket = new Socket(ip,port); 
			
			if(socket != null) // ���������� ������ ����Ǿ��� ���
			{
				Connection();
			}
			
		} catch (UnknownHostException e) { // ȣ��Ʈ�� ã�� �� ���� ��
			connectionIpCheck = false; // UnknownHostException�� �߻��ϸ� false�� ����. ���� ���� �˸�â�� ���.
		} catch (ConnectException e) {
			connectionPortCheck = false; // ConnectException�� �߻��ϸ� false�� ����. ���� ���� �˸�â�� ���.
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void Connection() // �������� �޼ҵ� ����κ�
	{ 
		try 
		{
			is = socket.getInputStream(); // �������� ���� ��Ʈ���� is�� ����
			dis = new DataInputStream(is); // is�� DataInputStream���� dis�� ����
			
			os = socket.getOutputStream(); // �������� ������ ��Ʈ���� os�� ����
			dos = new DataOutputStream(os); // os�� DataOutputStream���� dos�� ����
		} 
		catch (IOException e) // ����ó�� �κ�
		{ 
			
		} // Stream ���� ��
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) 
				{
					try {
						
						System.out.println("MainView ���� �����");
						System.out.println(port);
						String msg = dis.readUTF(); // �޼��� ����
						
						System.out.println("�����κ��� ���ŵ� �޼��� : " + msg);
						
						inmessage(msg);
					} catch (IOException e) {
						
					}
				}
				
			}
		});
		
		th.start();
	}
	
	private static void send_message(String str) // �������� �޼����� ������ �κ�
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ���� ó�� �κ�
		{
			e.printStackTrace();
		}
	}
	
	
	/* �޽��� �ۼ��� ���¸� ��������(Protocol) ���Ŀ� ���缭 �Ѵ�.
 	 * [Protocol]/[Message] ���·� �޽����� �����Ѵ�.
 	 * ���ŵ� �޽����� "/"�� �������� �����Ͽ� ������ ���̰� 
 	 * Protocol �κ��� NewUser,OldUser,Chatting ������
 	 * ����� ���������� Message�� �����Ѵ�.
	 */
	private void inmessage(String str) // �����κ��� ������ ��� �޼���
	{
		st = new StringTokenizer(str, "/");  // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ =>  [ NewUser/�����ID ] ���·� ����
		
		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String Message = st.nextToken(); // �޽����� �����Ѵ�.
		
		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + Message);
		
		// protocol ���� ó��
		switch(protocol) {
		
		// #�α��� ���� �˸�
		case "LoginOK":
			setVisible(false);
			wr =new WaitingRoom(); // WaitingRoom�� �����Ѵ�. 
			bgm.close();
			break;
			
		// #���� �α��������� �˸�
		case "SigningIn" :
			JOptionPane.showMessageDialog(null, 
					"���� �α��� ���Դϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			break;
			
		// #�α��� ���� �˸�
		case "LoginFail":
			JOptionPane.showMessageDialog(null, 
					"�α��� ����!\n ���̵�/�н����带 �ٽ� Ȯ���Ͻñ� �ٶ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			break;
			
		// #������ �������� �˸�
		case "ServerStop":
			JOptionPane.showMessageDialog(null, 
					"�������� ������ ���������ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			// ������ ���� �Ŀ� 
			try {
				socket.close();
			} catch (IOException e) {
			}
			// ���α׷��� �����Ѵ�.
			System.exit(0); 
			break;
			
		// #���ӹ� ����
		case "EntryGameRoom":
			// �� ��ȣ�� �Ѱ� �޴´�.
			int room_No = Integer.parseInt(st.nextToken()); // �� ��ȣ
			
			// �Ѱ� ���� �� ��ȣ�� Paint â�� ����.
			paint = new GameRoom(room_No);
			
			// �濡 ��������  ������ �˸��� ���� Paint Ŭ�������� ����
			
			break;
			
		// #WaitingRoom���� �ѱ�
		case "WaitingRoom":
			st = new StringTokenizer(str, "/@", true);
			for (int i = 0; i < 4; i++) {
				st.nextToken(); // ��ū ���ſ�
			}

			String totalMessage = "";
			String tempMsg = "";
			ArrayList<String> msgList = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				tempMsg = st.nextToken();
				msgList.add(tempMsg);
			}

			for (int i = 0; i < msgList.size(); i++) {
				totalMessage += msgList.get(i);
			}
			System.out.println("�޽��� : " + totalMessage);
			wr.wr_Inmessage(totalMessage);
			break;

		// #Paint�� �ѱ�
		case "Paint":
			st = new StringTokenizer(str, "/@", true);
			st.nextToken(); // �������� ��ū�� ���� ����
			st.nextToken(); // ��ȹ���� "/" ���� ����
			st.nextToken(); // �޽��� ��ū�� ���� ����
			st.nextToken(); // ��ȹ���� "/" ���� ����

			totalMessage = "";
			tempMsg = "";
			msgList = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				tempMsg = st.nextToken();
				msgList.add(tempMsg);
			}

			for (int i = 0; i < msgList.size(); i++) {
				totalMessage += msgList.get(i);
			}
			System.out.println("�޽��� : " + totalMessage);
			paint.paint_Inmessage(totalMessage);
			break;
			
		// #GameRoom���� ��ǥ �ѱ�
		case "GameRoomPaint" :
			String mouseState = st.nextToken();
			if(mouseState.equals("mousePress")) {
				String receiveColor = st.nextToken();
				
				System.out.println("MainView���� ���� �� �÷�:"+ receiveColor);
				paint.paint_Inmessage("GameRoomPaint@pass@mousePress@"+receiveColor);
			}
			else if(mouseState.equals("mouseRelease"))
				paint.paint_Inmessage("GameRoomPaint@pass@mouseRelease");
			else if(mouseState.equals("mouseDrag")) {
				String pointX1=st.nextToken();
				String pointY1=st.nextToken();
				String pointX2=st.nextToken();
				String pointY2=st.nextToken();
				String receiveThick = st.nextToken();
				String receiveEraserSel = st.nextToken();
				
				System.out.println("MainView���� ���� x1��ǥ:"+pointX1+", y1��ǥ"+pointY1+", x2��ǥ:"+pointX2+", y1��ǥ"+pointY2+", �� ����: "+receiveThick+", "+"���찳���ÿ���: "+receiveEraserSel);
				paint.paint_Inmessage("GameRoomPaint@pass@mouseDrag@"+pointX1+"@"+pointY1+"@"
											+pointX2+"@"+pointY2+"@"+receiveThick+"@"+receiveEraserSel);
			}
			else if(mouseState.equals("canvasClear"))
				paint.paint_Inmessage("GameRoomPaint@pass@canvasClear");
			
			break;
		}
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
	
	
	// ��Ʈ��ȣ�� �ٸ� Ŭ�������� �˱� ���� �޼ҵ�
	public static int getPort() {
		return port;
	}
	
	// ������ �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static Socket getSocket() {
		return socket;
	}
	
	// ID�� �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static String getId() {
		return id;
	}
	
	// dis�� �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static DataInputStream getDis() {
		return dis;
	}
	
	// dos�� �ٸ� Ŭ�������� �̾�ޱ� ���� �޼ҵ�
	public static DataOutputStream getDos() {
		return dos;
	}
	
	//�ٸ� Ŭ�������� �޽����� ������ �����ֱ� ���� �޼ҵ�
//	public static void throw_send_message(String str) {
//		send_message(str);
//	}
	
	/* �Ʒ� paint() �޼ҵ�� GUI Application�� ����ǰų� 
	 * Ȱ��/��Ȱ������ ���� ���� ������ ����������, ����Ǵ� �޼ҵ��̴�. */
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}
}
