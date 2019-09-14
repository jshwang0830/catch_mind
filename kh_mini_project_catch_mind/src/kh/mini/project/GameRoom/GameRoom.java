package kh.mini.project.GameRoom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;
import kh.mini.project.waiting_room.view.WaitingRoom;

public class GameRoom extends JFrame implements ActionListener {

	private StartThread startT;
	private StopWatch clock = new StopWatch();

	// ������ �ȿ� �ִ� ��ҵ�
	private Canvas canvas = new Canvas();
	
	// Panel, Label (���� ���� �� ä��)
	private JLabel[] user_lb = new JLabel[6]; // ���� ������ ��� �� (�ִ� �μ� 6�� ����)
	private JLabel[] chatting_lb = new JLabel[6]; // ������ ä���� ��ǳ������ ���� ��(�ִ� �ο� 6�� ����)
	private JLabel[] chattingCover_lb = new JLabel[6];
	private JPanel[] user_pn = new JPanel[6];
	private JLabel roomTitle_lb; // ������ ǥ�� ��
	private JLabel resultImage_lb; // ���� ��� ǥ�� �̹����� ��
	private JLabel endGameImage_lb; // ������ ������ �� ǥ���� �̹����� ��
	private JLabel suggest_lb; // ���þ� ǥ�� ��
	private JLabel roundImg_lb; // ���� �̹��� ��
	private JLabel nowTurnID; // ���� �󺧿� ��� ���� �� ����ID
	private JLabel nextTurnID; // ���� �󺧿� ��� ���� �� ����ID
	private JLabel lastTurnID; // ������ �� ����ID
	private JLabel desID; // �׸� ������ ���ִ� ������ ID
	private JLabel solvID; // ������ ���� ������ ID
	private JLabel timeReq; // �ҿ�ð� 
	private JLabel levelUpEvent_lb; // ������ �̺�Ʈ�� ��
	private JLabel suggest_Background;
	
	
	private JLabel gameRoombackground; //����̹��� ��
	
	//�޴��� ������
	private JLabel menuBar;
	private int mouseX, mouseY;

	// �׸��� ������ ���� ����
	private Color mypencolor = Color.black;
	private boolean eraser_Sel = false;
	private int thick = 8;
	private int eraserThick = 30;
	private String colorCode="black"; //�� ���� �����ϱ����� �ڵ弳��
	private int receiveThick;
	private boolean receiveEraserSel=false;

	// ����
	private ShapeSave newshape;
	private ArrayList<Point> sketSP = new ArrayList<Point>();
	private ArrayList<ShapeSave> shape = new ArrayList<ShapeSave>();
	
	
	// ��ư
	private JButton thick_Bold;
	private JButton thick_Sharp;
	private JButton eraser;

	private JButton color_yellow;
	private JButton color_blue;
	private JButton color_green;
	private JButton color_red;
	private JButton clear;
	private JButton color_black;
	private JButton giveUpBt;
	
	private JButton exit;
	
	private JButton mute=new JButton();
	private boolean muteSel=false;
	
	private JLabel levelUpImg;
	private JLabel readyImg;
	private JLabel startImg;
	private JLabel giveUpImg;
	
	private boolean canvasUse = false;
	
	private Point maindrow=new Point();
	private Point subdrow=new Point();
	
//	BGM
	private Music bgm;
	private Music buttonEnteredBGM;
	private Music popUpBGM;

// ���� ����
	private String id; // ������� id�� ����
	private int room_No; // ���ӹ� ��ȣ
	private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
	private RoomInfo roomInfo; // �������� ��ü�� �����Ѵ�.
	private boolean roomCaptain = false; // ������ ������Դ� true
	private String suggest; // ���� ���þ ������ ����(�����ڸ� �����Ѵ�.)
	private int round = 0; // ���� ���� ���� ����� ����
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private boolean giveUp_Sel=false;
	
	
	private ImageIcon roundImg = new ImageIcon();
	private ImageIcon resultImage = new ImageIcon(GameRoom.class.getResource("/images/resultImage.png"));
	private ImageIcon endGameImage = new ImageIcon(GameRoom.class.getResource("/images/endGameImage.png"));
	
	
	private Image pen_black = tk.getImage(GameRoom.class.getResource("/images/pen_black.png"));
	private Image pen_red = tk.getImage(GameRoom.class.getResource("/images/pen_red.png"));
	private Image pen_blue = tk.getImage(GameRoom.class.getResource("/images/pen_blue.png"));
	private Image pen_green = tk.getImage(GameRoom.class.getResource("/images/pen_green.png"));
	private Image pen_yellow = tk.getImage(GameRoom.class.getResource("/images/pen_yellow.png"));
	private Image eraserImg = tk.getImage(GameRoom.class.getResource("/images/eraser.png"));
	private Image clearImg = tk.getImage(GameRoom.class.getResource("/images/clear.png"));
	
	
	private Cursor blackCursor = tk.createCustomCursor(pen_black, new Point(0,0), "WaterDrop");
	private Cursor redCursor = tk.createCustomCursor(pen_red, new Point(0,0), "WaterDrop");
	private Cursor blueCursor = tk.createCustomCursor(pen_blue, new Point(0,0), "WaterDrop");
	private Cursor greenCursor = tk.createCustomCursor(pen_green, new Point(0,0), "WaterDrop");
	private Cursor yellowCursor = tk.createCustomCursor(pen_yellow, new Point(0,0), "WaterDrop");
	private Cursor eraserCursor = tk.createCustomCursor(eraserImg, new Point(0,0), "WaterDrop");
	private Cursor clearCursor = tk.createCustomCursor(clearImg, new Point(0,0), "WaterDrop");
	
	private Cursor myCursor;
	
// Textfield
	private JTextField chatting_tf; // ä�� ������ �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�	
	private ImageIcon chatAreaBackground = new ImageIcon(GameRoom.class.getResource("/images/wordBubble.png"));
	
	
// Network �ڿ� ����
	private DataOutputStream dos;

	public GameRoom(int room_No) {
		// ������� id�� �̾�޾ƿ´�.
		id = MainView.getId();
		// ���ӹ� ��ȣ�� ���̹޾ƿ´�.
		this.room_No = room_No;
		// MainView�κ��� dos�� �̾�޾ƿ´�.
		dos = MainView.getDos();

		// �ʱ� ä�ÿ� ���� �����Ѵ�.
		createChattingLabel();
		
		// �ʱ� ���� �г��� �����Ѵ�. 
		createUserPanel();

		Font font = new Font("�޸�����ü", Font.BOLD, 17); // ��Ʈ����
		
		// �� ���� ǥ�� ��
		roomTitle_lb = new JLabel("", SwingConstants.CENTER);
		roomTitle_lb.setBounds(312, 0, 400, 30);
		roomTitle_lb.setHorizontalTextPosition(SwingConstants.CENTER);
		roomTitle_lb.setFont(font);
		getContentPane().add(roomTitle_lb);

		// ä�� �Է�â
		chatting_tf = new JTextField();
		chatting_tf.setBounds(134, 730, 300, 25);
		chatting_tf.setOpaque(true);
		chatting_tf.setDocument(new JTextFieldLimit(20)); // ä�� 45�� ����
		chatting_tf.setFont(font);
		chatting_tf.addKeyListener(new keyAdapter()); // Ŭ������ ������ Ű �̺�Ʈ�� ����
		getContentPane().add(chatting_tf);
		
		// ���þ� ǥ�� ��
		suggest_lb = new JLabel();
		suggest_lb.setBounds(470, 34, 100, 30);
		suggest_lb.setFont(font);
		suggest_lb.setVisible(false);
		getContentPane().add(suggest_lb);
		
		suggest_Background = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/suggest_Background.png")));
		suggest_Background.setBounds(430,32,148,35);
		getContentPane().add(suggest_Background);
		suggest_Background.setVisible(false);
		
		
		
		// ���� ��� ��
		resultImage_lb = new JLabel(resultImage);
		resultImage_lb.setBounds(306, 169, 400, 300);
		getContentPane().add(resultImage_lb);
		resultImage_lb.setVisible(false);
		
		// ���� ��� �̹����� ID�� �ҿ�ð��� ǥ���� ��
		desID = new JLabel();
		desID.setBounds(185, 140, 100, 30);
		desID.setFont(font);
		resultImage_lb.add(desID);
		solvID = new JLabel();
		solvID.setBounds(180, 185, 100, 30);
		solvID.setFont(font);
		resultImage_lb.add(solvID); 
		timeReq = new JLabel();
		timeReq.setBounds(230, 240, 120, 30);
		timeReq.setFont(font);
		resultImage_lb.add(timeReq);
		
		
		// ���� ���� �˸� ��
		endGameImage_lb = new JLabel(endGameImage);
		endGameImage_lb.setBounds(306, 169, 400, 300);
		getContentPane().add(endGameImage_lb);
		endGameImage_lb.setVisible(false);
		
		// ���� �̹��� ��
		roundImg_lb = new JLabel();
		roundImg_lb.setBounds(306, 169, 400, 300);
		getContentPane().add(roundImg_lb);
		roundImg_lb.setVisible(false);
		
		// ���� �̹��� ���� ID ǥ���� ��
		nowTurnID = new JLabel();
		nowTurnID.setBounds(90, 130, 100, 30);
		nowTurnID.setFont(font);
		roundImg_lb.add(nowTurnID);
		nextTurnID = new JLabel();
		nextTurnID.setBounds(80, 190, 100, 30);
		nextTurnID.setFont(font);
		roundImg_lb.add(nextTurnID);
		lastTurnID = new JLabel();
		lastTurnID.setBounds(80, 160, 100, 30);
		lastTurnID.setFont(font);
		roundImg_lb.add(lastTurnID);
		
		// ������ �̺�Ʈ�� ��
		levelUpEvent_lb = new JLabel();
		levelUpEvent_lb.setBounds(306, 169, 300, 350);
		getContentPane().add(levelUpEvent_lb);
		levelUpEvent_lb.setVisible(false);
		
		// â�� ���ڸ��� �ش� ��� ������ �濡 ������ ������� ������ ���� ������ ������� �޾ƿ������� �޽����� ������.
		send_message("GameRoomCheck/" + id + "/" + room_No);

		// ������ ����
		setSize(1024, 768);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		getContentPane().setLayout(null);
		
		setCursor(blackCursor);
		
		getContentPane().add(canvas);
		canvas.setBounds(216, 134, 593, 440);
		canvas.setBackground(Color.white);
		
		//�޴���
		menuBar = new JLabel();
		menuBar.setBounds(0,0,1024,30);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);
			}
		});
		getContentPane().add(menuBar);
		

		//���� ��ư
		color_black = new JButton("black");
		color_black.setIcon(new ImageIcon(GameRoom.class.getResource("/images/color_black.png")));
		color_black.setContentAreaFilled(false);
		color_black.setBounds(486, 620, 122, 60);
		color_black.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/color_blackCLK.png")));
		color_black.setFocusPainted(false);
		color_black.setBorderPainted(false);
		getContentPane().add(color_black);
		color_black.addActionListener(this);
		color_black.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		color_black.setVisible(true);

		color_red = new JButton("red");
		color_red.setIcon(new ImageIcon(GameRoom.class.getResource("/images/color_red.png")));
		color_red.setContentAreaFilled(false);
		color_red.setBounds(486, 685, 122, 60);
		color_red.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/color_redCLK.png")));
		color_red.setFocusPainted(false);
		color_red.setBorderPainted(false);
		getContentPane().add(color_red);
		color_red.addActionListener(this);
		color_red.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		color_red.setVisible(true);

		color_blue = new JButton("blue");
		color_blue.setIcon(new ImageIcon(GameRoom.class.getResource("/images/color_blue.png")));
		color_blue.setContentAreaFilled(false);
		color_blue.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/color_blueCLK.png")));
		color_blue.setFocusPainted(false);
		color_blue.setBorderPainted(false);
		color_blue.setBounds(615, 620, 122, 60);
		getContentPane().add(color_blue);
		color_blue.addActionListener(this);
		color_blue.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		color_blue.setVisible(true);

		color_green = new JButton("green");
		color_green.setIcon(new ImageIcon(GameRoom.class.getResource("/images/color_green.png")));
		color_green.setContentAreaFilled(false);
		color_green.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/color_greenCLK.png")));
		color_green.setFocusPainted(false);
		color_green.setBorderPainted(false);
		color_green.setBounds(615, 685, 122, 60);
		getContentPane().add(color_green);
		color_green.addActionListener(this);
		color_green.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		color_green.setVisible(true);

		color_yellow = new JButton("yellow");
		color_yellow.setIcon(new ImageIcon(GameRoom.class.getResource("/images/color_yellow.png")));
		color_yellow.setContentAreaFilled(false);
		color_yellow.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/color_yellowCLK.png")));
		color_yellow.setFocusPainted(false);
		color_yellow.setBorderPainted(false);
		color_yellow.setBounds(743, 620, 122, 60);
		getContentPane().add(color_yellow);
		color_yellow.addActionListener(this);
		color_yellow.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		color_yellow.setVisible(true);

		//���찳 ��ư
		eraser = new JButton(new ImageIcon(GameRoom.class.getResource("/images/eraser.png")));
		eraser.setContentAreaFilled(false);
		eraser.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/eraserCLK.png")));
		eraser.setFocusPainted(false);
		eraser.setBorderPainted(false);
		eraser.setBounds(752, 693, 50, 42);
		getContentPane().add(eraser);
		eraser.addActionListener(this);
		eraser.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		eraser.setVisible(true);

		//Ŭ���� ��ư
		clear = new JButton("clear");
		clear.setIcon(new ImageIcon(GameRoom.class.getResource("/images/clear.png")));
		clear.setContentAreaFilled(false);
		clear.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/clearCLK.png")));
		clear.setBackground(Color.lightGray);
		clear.setFocusPainted(false);
		clear.setBorderPainted(false);
		clear.setBounds(810, 693, 50, 42);
		getContentPane().add(clear);
		clear.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start();
				}
			}
			});
		clear.addActionListener(this);

		//�� ���� ��ư
		thick_Bold = new JButton(new ImageIcon(GameRoom.class.getResource("/images/thick_Bold.png")));
		thick_Bold.setContentAreaFilled(false);
		thick_Bold.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/thick_BoldCLK.png")));
		thick_Bold.setFocusPainted(false);
		thick_Bold.setBorderPainted(false);
		thick_Bold.setBounds(868, 628, 97, 23);
		getContentPane().add(thick_Bold);
		thick_Bold.addActionListener(this);
		thick_Bold.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		thick_Bold.setVisible(true);

		thick_Sharp = new JButton(new ImageIcon(GameRoom.class.getResource("/images/thick_Sharp.png")));
		thick_Sharp.setContentAreaFilled(false);
		thick_Sharp.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/thick_SharpCLK.png")));
		thick_Sharp.setFocusPainted(false);
		thick_Sharp.setBorderPainted(false);
		thick_Sharp.setBounds(868, 654, 97, 23);
		getContentPane().add(thick_Sharp);
		thick_Sharp.addActionListener(this);
		thick_Sharp.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start(); 
				}
			}
			});
		thick_Sharp.setVisible(true);

		exit = new JButton(new ImageIcon(GameRoom.class.getResource("/images/gameroom_Exit.png"))); // ��ư �׼� �ؾߵ�
		exit.setContentAreaFilled(false);
		exit.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/gameroom_ExitCLK.png")));
		exit.setBounds(991, 2, 19, 25);
		menuBar.add(exit);
		exit.setVisible(true);
		exit.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				if(!muteSel) {
				buttonEnteredBGM = new Music("exitBGM.mp3", false);
				buttonEnteredBGM.start();
				}
			}
			
			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override  
			public void mouseExited(MouseEvent e) {
				exit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					// BGM ����
					soundOn(true);
					// ��� ���̺�� ������ �����.
					roomInfo = null;
					System.out.println("���������� �� ���̵� Ȯ���մϴ�." + id);
					// ������ ���� �������� �˸��� 
					send_message("GameRoomOut/"+id+"/"+room_No);
					// â�� �ݴ´�.
					dispose();
				}
			}
		});
		
		giveUpBt = new JButton(new ImageIcon(GameRoom.class.getResource("/images/giveup.png")));
		giveUpBt.setContentAreaFilled(false);
		giveUpBt.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/giveupCLK.png")));
		giveUpBt.setFocusPainted(false);
		giveUpBt.setBorderPainted(false);
		giveUpBt.setBounds(885, 700, 63, 24);
		getContentPane().add(giveUpBt);
		giveUpBt.addActionListener(this);
		giveUpBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!muteSel) {
				Music buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
				buttonEnteredBGM.start();
				}
			}
		});
		giveUpBt.setVisible(true);
		
		mute.setBounds(30,730,30,30);
		mute.setIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOn.png")));
		mute.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOnCLK.png")));	
		mute.setContentAreaFilled(false);
		mute.setFocusPainted(false);
		mute.setBorderPainted(false);
		mute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(muteSel) {
					muteSel=false;
					mute.setIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOn.png")));
					mute.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOnCLK.png")));	
					
				}
				else {
					muteSel=true;
					mute.setIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOff.png")));
					mute.setRolloverIcon(new ImageIcon(GameRoom.class.getResource("/images/BsoundOffCLK.png")));
				}
				soundOn(muteSel);
			}
		});
		add(mute);
		mute.setVisible(true);
		
		
		//levelUp �̹���
		levelUpImg = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/levelUpImg.gif")));
		getContentPane().add(levelUpImg);
		levelUpImg.setBounds(356,169,300,350);
		levelUpImg.setVisible(false);
		
		//ready�̹���
		readyImg = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/readyImg.png")));
		getContentPane().add(readyImg);
		readyImg.setBounds(356,169,300,300);
		readyImg.setVisible(false);
		
		//start�̹���
		startImg = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/startImg.png")));
		getContentPane().add(startImg);
		startImg.setBounds(356,169,300,300);
		startImg.setVisible(false);
		
		//�����̹���
		giveUpImg = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/giveupImage.png")));
		getContentPane().add(giveUpImg);
		giveUpImg.setBounds(306, 169, 400, 300);
		giveUpImg.setVisible(false);
		
		clock.setBounds(156, 620, 252, 99);
		getContentPane().add(clock);
		
		gameRoombackground = new JLabel(new ImageIcon(GameRoom.class.getResource("/images/GameRoom_Background.png")));
		gameRoombackground.setBounds(0, 0, 1024, 768);
		getContentPane().add(gameRoombackground);
		
		canvas.setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		bgm = new Music("gameBGM.mp3",true);
		bgm.start();
	}
	
	public void soundOn(boolean muteSel) {
		if(muteSel) {
			bgm.close();
		}
		else
		{
			bgm = new Music("gameBGM.mp3", true);
			bgm.start();
		}
	}
	
	// �������� �޽����� ������ �κ�
	private void send_message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ���� ó�� �κ�
		{
			e.printStackTrace();
		}
	}

	// �����κ��� ������ ��� �޽���
	private void Inmessage(String str) {
		st = new StringTokenizer(str, "@"); // � ���ڿ��� ����� ������, � ���ڿ��� �ڸ� ������ => [ NewUser/�����ID ] ���·� ����

		String protocol = st.nextToken(); // ���������� �����Ѵ�.
		String mUserId = st.nextToken(); // �޽����� �����Ѵ�.

		System.out.println("�������� : " + protocol);
		System.out.println("���� : " + mUserId);

		// protocol ���� ó��
		switch (protocol) {
		// #���� ó���� �޾ƿ��� �� ����
		case "RoomInfo":
			String room_Name = st.nextToken(); // ������
			String state = st.nextToken(); // ����
			String room_Pw = st.nextToken(); // ��й�ȣ
			int fixed_User = Integer.parseInt(st.nextToken()); // �ִ� �ο�(����)
			int uCount = Integer.parseInt(st.nextToken()); // ���� �ο� ��
			String roomCaptainID = st.nextToken();
			
			//���� �ڽ��� �����̶��
			if(mUserId.equals(roomCaptainID)) {
				roomCaptain = true;
			}
			
			// ���� ������ roomInfo ��ü�� �����Ѵ�.
			roomInfo = new RoomInfo(room_No, room_Name, state, room_Pw, uCount, fixed_User);
			
			if(state.equals("����")) {
				roomTitle_lb.setText("[ " + room_Name + " ]");
			} else if(state.equals("�����")) {
				roomTitle_lb.setText("[ " + room_Name + " ] ( PW : " + room_Pw + " )");
			}
			
			break;

		// #���� �������� ������ �޾ƿ´�.(�տ��� �ڽ��� ������ �ֱ⶧���� ��� ������ ������� ������ ���ԵǾ�����)
		case "OldUser":
			// ���� ������� ������ ������ �����Ѵ�.
			int level = Integer.parseInt(st.nextToken()); // ����
			int exp = Integer.parseInt(st.nextToken()); // ����ġ
			int corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo oldUser = new UserInfo(mUserId, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(oldUser);

			// ���� �������� �г��� ����
			createUserLabel();
			
			// ���� �г� ������Ʈ
			updateUserPanel();
			
			Vector temp = roomInfo.getRoom_user_vc();
			for (int i = 0; i < temp.size(); i++) {
				UserInfo u = (UserInfo) temp.get(i);
				System.out.println("���� ���� [" + i + "] : " + u);
			}
			// ���� �������� ������ �ް� �̾ ������ ������ �̾�����Ƿ�, ��� ���� �Ŀ� �г� ������Ʈ�� �����Ѵ�.
			break;
			
			// ���� �����ڿ��� ���ο� �����ڸ� �˸���.
		case "NewUser":
			// �ű� ������� ������ ������ �����Ѵ�.
			level = Integer.parseInt(st.nextToken()); // ����
			exp = Integer.parseInt(st.nextToken()); // ����ġ
			corAnswer = Integer.parseInt(st.nextToken()); // ���� �����

			// ������ ������ ��ü�� ����
			UserInfo newUser = new UserInfo(mUserId, level, exp, corAnswer);

			// �ش� ��ü�� Vector�� �߰�(���� ��ü�� RooInfo ��ü�� ���Ϳ� �����Ѵ�)
			roomInfo.addRoom_user_vc(newUser);

			// �߰��� ���� ������ �г��� ����
			createUserLabel();
			
			// ���� �г� ������Ʈ
			updateUserPanel();
			
			// �г� ������Ʈ�� �����Ѵ�.
			/*
			 * �г� ������Ʈ �ڵ�
			 */

			break;
		// #ä��
		case "ChattingPA":
			st = new StringTokenizer(str, "/@", true); // ��ȹ����"/"�� ��ū���� �����Ѵ�.
			for (int i = 0; i < 4; i++) {
				st.nextToken(); // ��ū ���ſ�
			}
			ArrayList<String> chattingMsgList = new ArrayList<String>(); // ä�ø޽��� ������ ����Ʈ
			String totalChattingMsg = ""; // ��ü ä�� �޽��� ���� ����
			String tempMsg = "";
			while (st.hasMoreTokens()) { // ������ ���� ��ū�� ������ true�� ������ false�� �����Ѵ�.
				tempMsg = st.nextToken();
				System.out.println("ä�� ��ū�� ���:" + tempMsg);
				chattingMsgList.add(tempMsg); // �޽��� ��ū�� ArrayList�� �߰�
			}

			for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList�� ��� �޽����� totalChattingMsg�� �����Ѵ�.
				totalChattingMsg += chattingMsgList.get(i);
			}

			System.out.println("Paint ä�� ���� : " + totalChattingMsg);

			setChattingLabel(mUserId, totalChattingMsg);

			break;
			
		// # �ο��� ��� á���Ƿ� ���� ������ �ص� ���ٴ� �޽���
		case "GameStart":
			
			exit.setEnabled(false);
			
			//�׽�Ʈ �ڵ�
			System.out.println("GameStart �޽��� ����");
			/* �������� �ο��� �� á���Ƿ� ���� ������ �ϵ��� �ϴ� �ڵ� */
			startT = new StartThread();
			startT.start();
			
			break;	
		
		// # ���尡 �������� �˸�
		case "EndRound":
			String descriptor = st.nextToken(); // ���� ������ ID
			round = Integer.parseInt(st.nextToken()); // ���带 �޴´�.
			boolean giveUp_rec = Boolean.valueOf(st.nextToken()).booleanValue();
			boolean levelUpCheck = Boolean.valueOf(st.nextToken()).booleanValue();
			/*
			 * mUserId �� �������� ���̵� �����
			 * �̸� ���� ���尡 �������� �˸��� �ڵ帣 ����. 
			 * 
			 */
			
			clock.stopClock();
			canvas.repaint();
			shape.clear();
			
			// ���� �ʱ�ȭ
			mypencolor = Color.black;
			eraser_Sel = false;
			thick = 8;
			eraserThick = 30;
			colorCode = "black";
			receiveEraserSel = false;
			getContentPane().setCursor(blackCursor);
			
			// ĵ������ ���þ� ���� �������ʰ�
			canvas.setVisible(false);
			suggest_lb.setVisible(false);
			suggest_Background.setVisible(false);
			
			// ��ư ��Ȱ��ȭ ���·� ����
			setButtonEnabled(false);
			chatting_tf.setEnabled(true);
			
			if(giveUp_Sel || giveUp_rec) {
				giveUpImgUpdate();
				giveUp_Sel=false;
			}
			else {
				// ���� ��� �������� ����.
				roundresult(mUserId, descriptor, levelUpCheck);
			}
			
			/* 
			 *  �����ڿ� �������� ���� ������ ����Ƿ� 
			 *  �ش� �����鸸 ������ ���ſ�û�� �˸��� �޼ҵ带 ������.
			 *  ������ �̸� �ް�, �� ǥ�⿡ ��������� ����� ������ ���� �� 
			 *  ��ü �������� ������ �˸��� �޽����� ������.
			 *  
			 */
			break;
			
		// # �ڽ��� ���� ������ ��
		case "YourTurn":
			String nowTurn = st.nextToken(); // ���� ��
			String nextTurn = st.nextToken(); // ���� ��
			
			round++; // ���带 �����Ѵ�.
			roundImgUpdate(round, nowTurn, nextTurn, protocol); // ���� ������Ʈ
			
			suggest = st.nextToken(); // ���þ� ����
			updateSuggestLabel(suggest); // ���þ ���̰��Ѵ�.
			/*
			 *  [���þ ȭ�鿡 ���� �ڵ�]
			 *  ���ÿ� ��ž��ġ ����
			 */
			
			
			canvasUse=true;
			System.out.println("�� �����ھ�!");
			
			
			
			break;

		// # ������ Ǫ�� �ڵ�
		case "Solve":
			nowTurn = st.nextToken(); // ���� ��
			nextTurn = st.nextToken(); // ���� ��
			
			round++; // ���带 �����Ѵ�.
			roundImgUpdate(round, nowTurn, nextTurn, protocol); // ���� ������Ʈ
			
			canvasUse=false;
			System.out.println("�� ������ Ǯ��!");

			break;
			
		// # ����ġ ������Ʈ
		case "ExpUpdate" :
			level = Integer.parseInt(st.nextToken()); // ����
			exp = Integer.parseInt(st.nextToken()); // ����ġ
			corAnswer = Integer.parseInt(st.nextToken()); // ���� ����� 
			int answer = Integer.parseInt(st.nextToken()); // �߰��� ���� 
			
			// �� �������� �ش� ������ ã�� ������Ʈ ������ �����Ų��.
			for(int i=0; i<roomInfo.getRoom_user_vc().size(); i++) {
				UserInfo u = (UserInfo)roomInfo.getRoom_user_vc().get(i);
				if(mUserId.equals(u.getUserID())) {
					u.setExp(exp);
					u.setlevel(level);
					System.out.println("����ġ : " + exp +" ����, ���� ���� : " + level);
					u.setCorAnswer(corAnswer);
					u.setNowAnswer(u.getNowAnswer()+answer);
				}
			}
			
			updateUserPanel();
			
			break;
		// # ���� ������ �ȴٰ� �����.
		case "GrantOut" :
			
			
			break;
			
			
		// # ���� ���� ���� ������ �������� �˸���.
		case "UserOut":
			
			// BGM ����
			soundOn(true);
			// ��� ���̺�� ������ �����.
			roomInfo = null;
			System.out.println("���������� �� ���̵� Ȯ���մϴ�." + id);
			// â�� �ݴ´�.
			dispose();
			
			
//			Vector<UserInfo> v = new Vector<UserInfo>(); // ���͸� ���Ƿ� �����ϰ�
//			System.out.println(roomInfo.getRoom_user_vc().size());
//			for (int i = 0; i < roomInfo.getRoom_user_vc().size(); i++) {
//				// �ش� ������ ��Ͽ��� ã��
//				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);
//				if (u.getUserID().equals(mUserId)) {
//					roomInfo.getRoom_user_vc().remove(i); // �ش� ���� ����
//				}
//			}
//
//			
//			
//			
//			for (int i = 0; i < roomInfo.getRoom_user_vc().size(); i++) {
//				// �ش� ������ ��Ͽ��� ã��
//				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);
//				// ���̵� ��ġ���� ���� ������ �߰��Ѵ�.
//				System.out.println("�׽�Ʈ "+i+"�� : " + u);
//			}
//			
////			// ���� ���� �г� ����
////			removeUserPanel();
//			
//			
//			// ���� �г� �����
//			createUserPanel();
//			
//			// ���� �г� ������Ʈ
//			updateUserPanel();

			break;
		// # ���� ����
		case "GameOver"	:
			// ���� ���� �޼ҵ带 �����Ѵ�.
			showGameOver();
			break;

		// # �׸� �׸���
		case "GameRoomPaint" :
			
			String mouseState = st.nextToken();
			if(mouseState.equals("mousePress")) {
				String receiveColor = st.nextToken();

				System.out.println("mousePress");
				newshape = new ShapeSave();
				
				switch(receiveColor) {
				case "black": mypencolor=Color.black; break;
				case "red": mypencolor=Color.red; break;
				case "blue": mypencolor=Color.blue; break;
				case "green": mypencolor=Color.green; break;
				case "yellow": mypencolor=Color.yellow; break;
				case "white": mypencolor=Color.white; break;
				}
				newshape.mypencolor=mypencolor;
			
				
			}
			else if(mouseState.equals("mouseRelease")) {
				System.out.println("mouseRelease");
				shape.add(newshape);
				sketSP.clear();
				repaint();
			}
			
			else if(mouseState.equals("mouseDrag")) {
				int pointX1=Integer.parseInt(st.nextToken());
				int pointY1=Integer.parseInt(st.nextToken());
				int pointX2=Integer.parseInt(st.nextToken());
				int pointY2=Integer.parseInt(st.nextToken());
				receiveThick = Integer.parseInt(st.nextToken());
				receiveEraserSel = Boolean.valueOf(st.nextToken()).booleanValue();
				
				if(receiveEraserSel) {
					newshape.setThick(eraserThick);
				}
				else {
					newshape.setThick(receiveThick);
				}	
				thick=receiveThick;
	
				//���۹��� ��ǥ ����
				maindrow.setLocation(pointX1, pointY1);
				subdrow.setLocation(pointX2, pointY2);
				newshape.sketchSP.add(maindrow.getLocation());
				sketSP.add(subdrow.getLocation());
				
				repaint();
			
			}
			else if(mouseState.equals("canvasClear")) {
				System.out.println("canvasClear");
				canvas.repaint();
				shape.clear();
			}
		
			break;
		}
	}
		
		
	// MainView Ŭ�������� Paint Ŭ������ �޽����� �����ϱ� ���� ����ϴ� �޼ҵ�
	public void paint_Inmessage(String str) {
		Inmessage(str);
	}

	
	public void removeUserPanel() {
		for(int i=0; i<user_pn.length; i++) {
			remove(user_pn[i]);
		}
	}
	
	
	// ���� ���� ������ ���������� ���� ���� ���� ������ش�.
	private void createUserPanel() {
		
		for(int i=0; i<user_pn.length; i++) {
			user_pn[i] = new JPanel();
			
			// switch������ ������� ��
			switch (i) {
			case 0: // 1��ĭ ��ġ setBounds(9, 62, 188, 147);
					user_pn[i].setBounds(9, 62, 188, 147);
					break;
			case 1: // 2��ĭ ��ġ setBounds(825, 62, 188, 147);
					user_pn[i].setBounds(825, 62, 188, 147);
					break;
			case 2: // 3��ĭ ��ġ setBounds(9, 241, 188, 147);
					user_pn[i].setBounds(9, 241, 188, 147);
					break;
			case 3: // 4��ĭ ��ġ setBounds(825, 241, 188, 147);
					user_pn[i].setBounds(825, 241, 188, 147);
					break;
			case 4: // 5��ĭ ��ġ setBounds(9, 419, 188, 147);
					user_pn[i].setBounds(9, 419, 188, 147);
					break;
			case 5: // 6��ĭ ��ġ setBounds(825, 419, 188, 147);
					user_pn[i].setBounds(825, 419, 188, 147);
					break;
			}
			
			user_pn[i].setLayout(null);
			user_pn[i].setOpaque(true);
			user_pn[i].setBackground(new Color(0,0,0,0)); // ����
			getContentPane().add(user_pn[i]);
			
			
		}	
	}
	
	// ���忡 ���� �̹����� ���̴� �޼ҵ�
	private void roundImgUpdate(int round, String nowTurn, String nextTurn, String protocol) {
		Font font = new Font("�޸�����ü", Font.BOLD, 25);

		new Thread() {
			@Override
			public void run() {
				try {
					// 1.5�� ���� ��� ��
					sleep(1500);
					
					System.out.println("���� : " +round + ", " + nowTurn + ", " + nextTurn );
					
					// �̹����� ���忡 �°� �����ϰ� ���̰� �Ѵ�.
					String address = "/Images/Round" + round + ".png";
					roundImg = new ImageIcon(GameRoom.class.getResource(address));
					roundImg_lb.setIcon(roundImg);
					roundImg_lb.setVisible(true);
					
					if(round != 12) {
						// ���� �̹����� turn id�� ����.
						nowTurnID.setText(nowTurn);
						nextTurnID.setText(nextTurn);
					} else {
						nowTurnID.setVisible(false);
						nextTurnID.setVisible(false);
						
						// ���� �̹����� turn id�� ����.
						lastTurnID.setText(nowTurn);
					}
					if(!muteSel) {
						popUpBGM = new Music("roundBGM.mp3", false);
						popUpBGM.start();
					}
					
					// ���� �ʱ�ȭ
					mypencolor = Color.black;
					eraser_Sel = false;
					thick = 8;
					eraserThick = 30;
					colorCode = "black";
					receiveEraserSel = false;
					getContentPane().setCursor(blackCursor);
					
					
					// 3�� ���� ��� ��
					sleep(3000);
					canvas.repaint();
					shape.clear();
					roundImg_lb.setVisible(false); // �������ʰ� �Ѵ�.
					canvas.setVisible(true); // ĵ������ ���̰��Ѵ�.
					clock.startClock(); // Ÿ�̸� ����

					if(protocol.equals("YourTurn")) {
						// �����ڿ��Ը� ��ư Ȱ��ȭ
						setButtonEnabled(true);
						chatting_tf.setEnabled(false);
					}
					
					
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

		// �г��� ��������� �����ϱ����� �޼ҵ�
		revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
		repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
	}
	
	// ���� ����ÿ� �̹����� ���̴� �޼ҵ�
	private void roundresult(String solver , String descriptor, boolean levelUpCheck) { // ������ �������ִ� ����� �����ڸ� �μ��� �޴´�.
		Font font = new Font("�޸�����ü", Font.BOLD, 25);
	
		new Thread() {
			@Override
			public void run() {
				try {
					
					if(!muteSel) {
					popUpBGM = new Music("correctBGM.mp3", false);
					popUpBGM.start();
					}
					
					// 1.5�� ���� ��� ��
					sleep(1500);
					
					resultImage_lb.setVisible(true); // ��� �������� ���̰��Ѵ�.
					desID.setText(descriptor); 
					solvID.setText(solver);
					timeReq.setText(clock.getTime());
					
					sleep(3000);
					
					resultImage_lb.setVisible(false); // �ٽ� ������ �ʰ� �Ѵ�.
					
					
					// ���� ������ �� ������ �ִٸ�
					if(levelUpCheck) {
						// ������ �˸� �̹���
						levelUpImgUpdate();
						
						sleep(5000);
						
						// ������ ExpUpdate �������ݿ��� ó���ϹǷ� �̺�Ʈ ó���� �Ѵ�.
						updateUserPanel();
					}
					
					
					// ���� �����̶�� 
		            if(roomCaptain) {
		            	// ���� ������ �˸���.
						send_message("RoundStart/"+id+"/"+room_No);
		            }
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	//���� �̹��� ������
	private void giveUpImgUpdate() { 
		new Thread() {
			@Override
			public void run() {
				try {
					
					// 1�� ���� ��� ��
					sleep(1000);
					
					giveUpImg.setVisible(true);
					
					if(!muteSel) {
					popUpBGM = new Music("giveupBGM.mp3",false);
					popUpBGM.start();
					}
					
					sleep(3000);
					
					giveUpImg.setVisible(false); // �ٽ� ������ �ʰ� �Ѵ�.
					// ���� �����̶�� 
		            if(roomCaptain) {
		            	// ���� ������ �˸���.
						send_message("RoundStart/"+id+"/"+room_No);
		            }
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	//������ �̹��� ������
		private void levelUpImgUpdate() { 
			new Thread() {
				@Override
				public void run() {
					try {
						levelUpImg.setVisible(true);
						
						if(!muteSel) {
						popUpBGM = new Music("levelupBGM.mp3",false);
						popUpBGM.start();
						}
						
						sleep(3000);
						
						levelUpImg.setVisible(false); // �ٽ� ������ �ʰ� �Ѵ�.
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
			
		}
	
	// ���� �г� ���� �޼ҵ�
	private void updateUserPanel() {
		// ��� user_lb�� ������ �ڵ�. �� �迭�� ������ŭ ����(���� ���������� Ŭ�� �̺�Ʈ�� �߰��� ����)
		for (int i = 0; i < user_lb.length; i++) {
			
			// ������ �ο� �� ��ŭ �ش� �ݺ����� �����Ų��.
			if (i < roomInfo.getRoom_user_vc().size()) {
				// �� ���� ��ü���� ��������Ʈ�� �����Ͽ� �ش� ���� ��ü�� �����´�.
				// �ε����� ���� ���� ��Ű�Ƿ� ���� ������ �ʿ� ����
				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);

				// �ٽ� ��ȯ�ؼ� �����ϰ� ������ ������ �� �� �����Ѵ�.
				UserLabel ul = (UserLabel)user_lb[i];
				ul.setInGameUser(u);
				
				
				// ������ ���� ��ü�� �̿��ؼ� UserLabel ��ü�� ������ user_lb�� �����Ų��.
				user_lb[i]=ul;
				user_pn[i].add(user_lb[i]);

				System.out.println(i+1 + "�� ���� ���� :" + u.getUserID() +", " + u.getExp() +", " + u.getLevel());
				
			}
		}
		// �г��� ��������� �����ϱ����� �޼ҵ�
		revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
		repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
	}
	
	// ���� �� ����
	private void createUserLabel() {
		for (int i = 0; i < user_lb.length; i++) {
			// ������ �ο� �� ��ŭ �ش� �ݺ����� �����Ų��.
			if (i == roomInfo.getRoom_user_vc().size()-1) {
				// �� ���� ��ü���� ��������Ʈ�� �����Ͽ� �ش� ���� ��ü�� �����´�.
				// �ε����� ���� ���� ��Ű�Ƿ� ���� ������ �ʿ� ����
				UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);

				// ������ ���� ��ü�� �̿��ؼ� UserLabel ��ü�� ������ user_lb�� �����Ų��.
				user_lb[i] = new UserLabel(u);
				user_pn[i].add(user_lb[i]);
			}
		}
	}
	
	// ���þ� ���� �޼ҵ�
	private void updateSuggestLabel(String str) {
		
		new Thread() {
			@Override
			public void run() {
					// 1.5�� ���� ��� ��
					try {
						sleep(4500);
						// �Է¹��� ���ڷ� �ؽ�Ʈ ����
						suggest_lb.setText(str);
						suggest_lb.setVisible(true); // �� �� �ֵ��� ����
						suggest_Background.setVisible(true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}.start();
		
		// �г��� ��������� �����ϱ����� �޼ҵ�
		revalidate(); // ���̾ƿ� ��ȭ�� ��Ȯ�� ��Ų��.
		repaint(); // removeAll()�� ���� ���� �� ������ �ڽ��� �̹����� ����� �� �ʿ��ϴ�.
	}
	
	// #ä�� ���� ����� ��Ȱ�� ���·� �ʱ�ȭ ��Ų��. 
	private void createChattingLabel() {
		Font chatlbfont = new Font("�޸�����ü", Font.BOLD,15 );
		
		for(int i=0; i<chatting_lb.length; i++) {
			chatting_lb[i] = new JLabel(chatAreaBackground);
			chatting_lb[i].setBackground(new Color(0,0,0,0));
			
			chattingCover_lb[i] = new JLabel("", SwingConstants.CENTER);
			chattingCover_lb[i].setVerticalTextPosition(SwingConstants.CENTER); // ����Ʈ ���� ��� ����(�ڵ� ���� �Ǵ� �����̱��ѵ�..)
			chattingCover_lb[i].setHorizontalTextPosition(SwingConstants.CENTER); // �ؽ�Ʈ ���� ��� ����
			chattingCover_lb[i].setFont(chatlbfont);
			
			chatting_lb[i].add(chattingCover_lb[i]);
			getContentPane().add(chatting_lb[i]);
			
			// switch������ ������� ��
			switch(i) {
			case 0: chatting_lb[i].setBounds(10, 38, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 1: chatting_lb[i].setBounds(826, 38, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 2:	chatting_lb[i].setBounds(10, 217, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 3: chatting_lb[i].setBounds(826, 217, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 4: chatting_lb[i].setBounds(10, 395, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			case 5:	chatting_lb[i].setBounds(826, 395, 189, 60);
					chattingCover_lb[i].setBounds(10, 0, 180, 50);
					break;
			}
			
			// ������ �Ϸ������Ƿ� �ش� ���� �������ʰ� �Ѵ�.
			chatting_lb[i].setVisible(false);
			chattingCover_lb[i].setVisible(false);
		}
	}
	
	
	// # ä���� ������ �޽����� ���� �޼ҵ��, �ش� ������ ���̵� �޾� �����Ѵ�.
	private void setChattingLabel(String userID, String msg) {
		// �ش� ������ ������� ó���Ѵ�. (�溹ó���� ���� ������ �����尡 �ƴϸ� ä�� �ϳ��ϳ� ��ٷ�����)
		new Thread() {
			public void run() {
				
				// roomInfo���� ��������� ������ �ش� ������ ã�´�.
				for (int i = 0; i < roomInfo.getRoom_user_vc().size(); i++) {
					// �ش� ������ ��ü�� ������ UserInfo ��ü�� �����ϰ�
					UserInfo u = (UserInfo) roomInfo.getRoom_user_vc().get(i);
					// ������ ���̵�� ��ġ�ϴ� ������ ã����
					if (userID.equals(u.getUserID())) {
						// �ش� ������ �ε��� ���� �̿��� chattingCover_lb�� set�Ѵ�.
						// �켱 msg�� 20�� ������ �ɷ� ������, �ٹٲ� ó���� ���� 10�ڸ� �ִ�� ���ڸ� �����ش�.
						if (msg.length() > 10) { // �޽����� ���̰� 10 �ʰ����
							// �ش� ���ڿ��� �ٹٲ� ó���ϱ����� HTML�� ����Ѵ�.
							String brString = "<html><body>";
							brString += msg.substring(0, 9); // �ε��� ��ġ 0~9���� �ڸ��� ���ڿ� ����
							brString += "<br>"; // ���ڿ� �ٹٲ� �ٽ�
							brString += msg.substring(10, msg.length());
							brString += "</body></html>"; // HTML ���� ��
							
							// ó���� ���ڿ��� chattingCover_lb�� set
							chattingCover_lb[i].setText(brString);
							
							// �ش� ���� ���̰� ó���Ѵ�.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
							
						} else { // 10�� ���϶��
							// �׳� �޽����� ������.
							chattingCover_lb[i].setText(msg);
							
							// �ش� ���� ���̰� ó���Ѵ�.
							chatting_lb[i].setVisible(true);
							chattingCover_lb[i].setVisible(true);
						}

						// 5�ʵ��� �ش� �����带 ���߰�
						try {
							Thread.sleep(5000); // 0.5�ʵ� ����
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}

						// 5�� �� �ش� �󺧵��� �������ʰ� ó���Ѵ�.
						chatting_lb[i].setVisible(false);
						chattingCover_lb[i].setVisible(false);
					}
				}

			}
		}.start();
	}
	
	
	// �������� �̹����� ���̴� �޼ҵ�
	private void showGameOver() {
		new Thread() {
			public void run() {
				try {
					// 1.5���� ���� ���� �̹����� ����
					sleep(1500);
					endGameImage_lb.setVisible(true);
					
					if(!muteSel) {
					popUpBGM = new Music("endgameBGM.mp3", false);
					popUpBGM.start();
					}
					// 5�ʰ� ����� ������ ���� ���Ḧ �˸��� �������� �ݴ´�. �������� ���Ƿ� �̵��ȴ�.
					sleep(5000);
					send_message("GameOver/"+id+"/"+room_No+"/"+roomCaptain);
					bgm.close();
					dispose();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	// ���� ������ ���� �� ���� Ŭ���� 
	class UserLabel extends JLabel{
		private JLabel user_Image;
		private JLabel user_Id;
		private JLabel user_Level;
		private JLabel user_nowAnswer;
		
		private UserInfo inGameUser;
		
		// ���� x(190.909) y(151.695)
		// ������ x(184.67) y(146.738)
		// �����簢�� x(91.788) y(136.07)
		// �����簢��3�� x(77) y(42)
		public UserLabel(UserInfo inGameUser) { // ���� ��ü�� �Է¹޴´�.
			this.inGameUser = inGameUser;
			
			Font userLabelFont = new Font("�޸�����ü", Font.PLAIN,18 ); //��Ʈ����
			
			setSize(190,151);
			
			// user_Image ��  (������ ���� ĳ���� �̹��� ����)

			/*    ������ ���� ĳ���� �̹����� �ҷ����� �ڵ� �ʿ�      */
			
			user_Image = new JLabel(this.inGameUser.getCharImg(), SwingConstants.CENTER); // ��� ����
			user_Image.setBounds(8, 6, 91, 135);
			user_Image.setBackground(new Color(0,0,0,0));
			user_Image.setOpaque(true);
			add(user_Image);
			System.out.println(this.inGameUser.getCharImg());
			
			
			// user_Id ��
			user_Id = new JLabel("",SwingConstants.CENTER); // ��� ����
			user_Id.setText(this.inGameUser.getUserID());
			user_Id.setHorizontalTextPosition(JLabel.CENTER);
			user_Id.setBounds(104, 7, 76, 41);
			user_Id.setFont(userLabelFont);
			user_Id.setBackground(new Color(0,0,0,0));
			user_Id.setOpaque(true);
			add(user_Id);
			System.out.println(this.inGameUser.getUserID());
			
			// user_Level �� (������ ���� ��� �̹��� ����)
			
			/*    ������ ���� ��� �̹����� �ҷ����� �ڵ� �ʿ�      */
			
			user_Level = new JLabel(this.inGameUser.getGradeImg(),SwingConstants.CENTER); // ��� ����
			user_Level.setBounds(104, 53, 76, 41);
			user_Level.setBackground(new Color(0,0,0,0));
			user_Level.setOpaque(true);
			add(user_Level);
			System.out.println(this.inGameUser.getGradeImg());
			
			// user_CorAnswer
			user_nowAnswer = new JLabel("",SwingConstants.CENTER); // ��� ����
			user_nowAnswer.setText(Integer.toString(this.inGameUser.getNowAnswer()));
			user_nowAnswer.setBounds(104, 99, 76, 41);
			user_nowAnswer.setBackground(new Color(0,0,0,0));
			user_nowAnswer.setOpaque(true);
			user_nowAnswer.setFont(userLabelFont);
			add(user_nowAnswer);
			System.out.println(this.inGameUser.getNowAnswer());
		}
		
		public void setInGameUser(UserInfo inGameUser) {
			this.inGameUser = inGameUser;
			
			user_Image.setIcon(this.inGameUser.getCharImg());
			user_Level.setIcon(this.inGameUser.getGradeImg());
			user_Id.setText(this.inGameUser.getUserID());
			user_nowAnswer.setText(Integer.toString(this.inGameUser.getNowAnswer()));
		}
	}
	
	// �ؽ�Ʈ �ʵ� ���� �� ������ ���� Ŭ���� �� �޼ҵ�
	public class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	} // JTextFieldLimit class ��
	
	// Ű �̺�Ʈ�� �ֱ����� Ŭ����
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// ���͸� ������ ������ �ǰ� �ϱ����� �޼ҵ�
				String message = chatting_tf.getText();
				if (message.equals("")) { // �ƹ��͵� �Է����� �ʾ��� �� �˸�â�� ���
					JOptionPane.showMessageDialog(null, "������ �Է��Ͻñ� �ٶ��ϴ�.", "�˸�", JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingPA/" + id + "/" + roomInfo.getRoom_No() +"/"+ message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ��

	// �׸���
	class Canvas extends JPanel {

		MyMouseListener ml = new MyMouseListener();

		// �׸��� ���콺 ������
		Canvas() {
			addMouseListener(ml);
			addMouseMotionListener(ml);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			
			// �׸� �׸���
			for (int i = 0; i < shape.size(); i++) {
				g2.setStroke(new BasicStroke(shape.get(i).getThick(), BasicStroke.CAP_ROUND, 0));
				g2.setPaint(shape.get(i).mypencolor);
				for (int j = 1; j < shape.get(i).sketchSP.size(); j++)
					g2.drawLine(shape.get(i).sketchSP.get(j - 1).x, shape.get(i).sketchSP.get(j - 1).y,
							shape.get(i).sketchSP.get(j).x, shape.get(i).sketchSP.get(j).y);
			}	
			// �ܻ� �׸���
			if (eraser_Sel || receiveEraserSel) {
				g2.setStroke(new BasicStroke(eraserThick, BasicStroke.CAP_ROUND, 0));
				for (int i = 1; i < sketSP.size(); i++) {
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i - 1).x, sketSP.get(i - 1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
				g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, 0));
			} else {

				g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, 0));
				for (int i = 1; i < sketSP.size(); i++) {
					g2.setPaint(mypencolor);
					g2.drawLine(sketSP.get(i - 1).x, sketSP.get(i - 1).y, sketSP.get(i).x, sketSP.get(i).y);
				}
			}
		}	
	}
	   
	// ���� ���� �� ���� ���ۿ� ���� ��ư Ȱ��/��Ȱ�� ���
	private void setButtonEnabled(boolean flag) {

		thick_Bold.setEnabled(flag);
		thick_Sharp.setEnabled(flag);
		eraser.setEnabled(flag);
		color_yellow.setEnabled(flag);
		color_red.setEnabled(flag);
		color_blue.setEnabled(flag);
		color_green.setEnabled(flag);
		clear.setEnabled(flag);
		color_black.setEnabled(flag);
		giveUpBt.setEnabled(flag);
	}
	
	//��ǥ �ο����� �����ϸ� 3�ʵڿ� ���� �ڵ������ϴ� ������(�ο����� ���� ������ ���۵Ǵ°� ���� ����X)
	   class StartThread extends Thread{
	      @Override
	      public void run() {
	         try {
	            System.out.println("����");
	            setButtonEnabled(false); // ��� �׸� ��ư ��Ȱ��ȭ
	            sleep(2000);
	            
	            readyImg.setVisible(true);
	            if(!muteSel) {
	            popUpBGM = new Music("readyBGM.mp3", false);
	            popUpBGM.start();
	            }
	            sleep(2500);
	            readyImg.setVisible(false);
	            
	            startImg.setVisible(true);
	            if(!muteSel) {
	            popUpBGM = new Music("goBGM.mp3", false);
	            popUpBGM.start();
	            }
	            sleep(1500);
	            startImg.setVisible(false);
	            
	            // ���� �����̶�� 
	            if(roomCaptain) {
	            	// ���� ������ �˸���.
					send_message("RoundStart/"+id+"/"+room_No);
	            }
	         }catch(InterruptedException e) {
	            e.printStackTrace();
	         }
	      }
	   }
	   


	class MyMouseListener extends MouseAdapter implements MouseMotionListener {
		
		public void mousePressed(MouseEvent e) {
			if(canvasUse) {
				newshape = new ShapeSave();
				newshape.mypencolor = mypencolor;

				send_message("GameRoomPaint/" + id + "/" + room_No + "/" + "mousePress" + "/" + colorCode);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(canvasUse) {
				shape.add(newshape);
				sketSP.clear();

				send_message("GameRoomPaint/" + id + "/" + room_No + "/" + "mouseRelease");

				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(canvasUse) {
				if (eraser_Sel)
					newshape.setThick(eraserThick);
				else
					newshape.setThick(thick);

				newshape.sketchSP.add(e.getPoint());
				sketSP.add(e.getPoint());

				send_message("GameRoomPaint/" + id + "/" + room_No + "/" + "mouseDrag/" + e.getPoint().x + "/"
						+ e.getPoint().y + "/" + e.getPoint().x + "/" + e.getPoint().y + "/" + thick + "/"
						+ eraser_Sel);
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == giveUpBt){
			giveUp_Sel=true;
			send_message("GiveUp/pass/"+room_No+"/"+round+"/"+giveUp_Sel);
		}
			
		if (e.getSource() == clear) {
			canvas.repaint();
			shape.clear();
			clear.setCursor(clearCursor);
			getContentPane().setCursor(myCursor);
			send_message("GameRoomPaint/"+id+"/"+room_No+"/"+"canvasClear");
		} else {
			if (e.getSource() == eraser) {
				eraser_Sel = true;
				eraserThick = 30;
				mypencolor = Color.white;
				colorCode = "white";
				myCursor=eraserCursor;
				eraser.setCursor(myCursor);
				getContentPane().setCursor(myCursor);
			} else {
//				eraser_Sel = false;
				if (e.getSource() == thick_Bold)
					thick = 8;
				else if (e.getSource() == thick_Sharp)
					thick = 3;
				else if (e.getSource() == color_black) {
					eraser_Sel = false;
					mypencolor = Color.black;
					colorCode = "black";
					myCursor=blackCursor;
					color_black.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_red) {
					eraser_Sel = false;
					mypencolor = Color.red;
					colorCode = "red";
					myCursor=redCursor;
					color_red.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_blue) {
					eraser_Sel = false;
					mypencolor = Color.blue;
					colorCode = "blue";
					myCursor=blueCursor;
					color_blue.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
				}
				else if (e.getSource() == color_green) {
					eraser_Sel = false;
					mypencolor = Color.green;
					colorCode = "green";
					myCursor=greenCursor;
					color_green.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
					
				}
				else if (e.getSource() == color_yellow) {
					eraser_Sel = false;
					mypencolor = Color.yellow;
					colorCode = "yellow";
					myCursor=yellowCursor;
					color_yellow.setCursor(myCursor);
					getContentPane().setCursor(myCursor);
					System.out.println("yellow");
				}
			}
		}
	}
}
