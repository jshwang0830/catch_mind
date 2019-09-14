package kh.mini.project.waiting_room.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import kh.mini.project.GameRoom.GameRoom;
import kh.mini.project.GameRoom.Music;
import kh.mini.project.main.view.Main;
import kh.mini.project.main.view.MainView;
import kh.mini.project.model.vo.RoomInfo;
import kh.mini.project.model.vo.UserInfo;

public class WaitingRoom extends JFrame{
// Frame, Panel
	private JScrollPane chattingView = new JScrollPane(); // Ã¤ÆÃÀ» º¸ÀÌ°ÔÇÏ´Â ½ºÅ©·Ñ ÆäÀÎ
	private JPanel userListView = new JPanel(); // À¯Àú ¸®½ºÆ® ÆĞ³Î
	private JPanel gameRoomView = new JPanel(); // °ÔÀÓ¹æ ÆĞ³Î
	private JPanel[] gameRoom = new JPanel[6]; // 24°³ÀÇ ¹æÀ» °³¼³ => ¹öÆ°À¸·Î ÇØº¼±î??
	private JPanel[] userList = new JPanel[10]; // 50¸íÀÇ À¯Àú ¸®½ºÆ®¸¦ ¶ç¿ì´Â ÆĞ³Î
	
// Label
	private JLabel mainMenuBar = new JLabel();
//	private JLabel[] userID_lb = new JLabel[userList.length]; 		// À¯Àú ID ¶óº§ ¹è¿­
	private JLabel userViewLb;
	private JLabel userGradeLb;
	private JLabel userNameLb;
	private JLabel userCorCount;
	
	
// Textfield	
	private JTextField chatting_tf; // Ã¤ÆÃ ³»¿ëÀ» ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ®ÇÊµå	
	private JTextArea chattingArea = new JTextArea(); // Ã¤ÆÃ ½ºÅ©·Ñ ÆäÀÎ¿¡ ¿Ã·Á³õÀ» Ã¤ÆÃ TextArea
	
// Network ÀÚ¿ø º¯¼ö
	private String id =""; 
	private DataOutputStream dos;
	
// °¢Á¾ º¯¼ö
	private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
	private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
	private int mouseX, mouseY; // ¸¶¿ì½º ÁÂÇ¥¿ë º¯¼ö
	private StringTokenizer st; // ÇÁ·ÎÅäÄİ ±¸ÇöÀ» À§ÇØ ÇÊ¿äÇÔ. ¼ÒÄÏÀ¸·Î ÀÔ·Â¹ŞÀº ¸Ş½ÃÁö¸¦ ºĞ¸®ÇÏ´Âµ¥ ¾²ÀÓ.
	private UserInfo userInfo; // Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ ÀúÀåÇÏ´Â °´Ã¼(ÀÌ ÀÌ¸§À¸·Î Á¢±ÙÇÏ¸é »ç¿ëÀÚ º»ÀÎÀÇ Á¤º¸¿¡ Á¢±ÙÇÒ ¼ö ÀÖ´Ù.)
	private Vector<RoomInfo> room_list = new Vector<RoomInfo>(); // ¹æ ¸®½ºÆ® Vector
	private Vector<UserInfo> user_list = new Vector<UserInfo>();
	private Toolkit tk = Toolkit.getDefaultToolkit();
	// Ä¿¼­ Å×½ºÆ®
	private Image cursorBasic = tk.getImage(Main.class.getResource("/images/pencilCursor.png"));
	private Image cursorClicked = tk.getImage(Main.class.getResource("/images/pencilCursorClicked.png"));
	private Cursor myCursor = tk.createCustomCursor(cursorBasic, new Point(10,10), "Pencil Cursor");
	private Cursor myCursorClicked = tk.createCustomCursor(cursorClicked, new Point(10,10), "Pencil Cursor Clicked");
	// ¹æ ¸¸µé±â¿¡ ÇÊ¿äÇÑ º¯¼ö
	private String room_name; // ¹æÁ¦¸ñ
	private String roomPW; // ¹æ ºñ¹Ğ¹øÈ£
	private int fixed_User; // ¹æ ÀÎ¿ø¼ö
	private int room_No; // ¹æ ¹øÈ£
	private boolean scrollpanemove = false;  // ½ºÅ©·Ñ ÆĞÀÎ¿¡ »ç¿ëµÇ´Â º¯¼ö(½ºÅ©·Ñ Çã¿ë °ü·Ã)
	private RoomInfo roomInfo; // »ç¿ëÀÚ°¡ »ı¼ºÇÑ ¹æÀÌ³ª ÀÔÀåÇÏ·Á´Â ¹æÀÇ °´Ã¼
	private Font wrFont = new Font("ÈŞ¸ÕÆíÁöÃ¼", Font.PLAIN,18 ); //ÆùÆ®¼³Á¤
	
	
//Image	
	// #MainView ¹è°æ
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/WaitingRoom_Background.png")).getImage();
			//Main Å¬·¡½ºÀÇ À§Ä¡¸¦ ±âÁØÀ¸·Î ÀÌ¹ÌÁö ÆÄÀÏÀÇ À§Ä¡¸¦ Ã£Àº ´ÙÀ½¿¡ ÀÌ¹ÌÁö ÀÎ½ºÅÏ½º¸¦ ÇØ´ç º¯¼ö¿¡ ÃÊ±âÈ­ ÇØÁÜ(»ó´ë°æ·Î °°Àº Àı´ë°æ·Î)	
	
	// Button Icon (basic : ¹öÆ°ÀÇ ±âº» »óÅÂ, Entered : ¹öÆ°¿¡ ¸¶¿ì½º¸¦ °¡Á®°£ »óÅÂ) 
	// => ¹öÆ° ±âº»»óÅÂ, ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ» ¶§ »óÅÂ, ´­·¶À» ¶§ »óÅÂ 3°¡Áö °¡´É?
	private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/wrExitButtonBasic.png"));
	private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/wrExitButtonEntered.png")); 
	private ImageIcon createRoomBasicImage = new ImageIcon(Main.class.getResource("/images/wrCreateRoomButtonBasic.png"));
	private ImageIcon createRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/wrCreateRoomButtonEntered.png"));
	private ImageIcon rightRBasicImage = new ImageIcon(Main.class.getResource("/images/arrowRButtonBasic.png"));
	private ImageIcon rightREnteredImage = new ImageIcon(Main.class.getResource("/images/arrowRButtonEntered.png")); 
	private ImageIcon leftRBasicImage = new ImageIcon(Main.class.getResource("/images/arrowLButtonBasic.png"));
	private ImageIcon leftREnteredImage = new ImageIcon(Main.class.getResource("/images/arrowLButtonEntered.png")); 
	private ImageIcon gamgeRoomBasicImage = new ImageIcon(Main.class.getResource("/images/gameroom.png")); 
	private ImageIcon gamgeRoomEnteredImage = new ImageIcon(Main.class.getResource("/images/gameroomEntered.png")); 
	private ImageIcon gamgeRoomPressedImage = new ImageIcon(Main.class.getResource("/images/gameroomPressed.png"));
	private ImageIcon userInfoPanelImage = new ImageIcon(Main.class.getResource("/images/userInfoPanel.png")); 
	private ImageIcon inputPwPanelImage = new ImageIcon(Main.class.getResource("/images/inputPwBackground.png")); 
	
//Button
	private JButton exitButton = new JButton(exitBasicImage); // ³ª°¡±â ¹öÆ°
	private JButton createRoomButton = new JButton(createRoomBasicImage); // ¹æ¸¸µé±â ¹öÆ°
	private JButton rightRButton = new JButton(rightRBasicImage); // ¹æ ¿À¸¥ÂÊ ³Ñ±â±â ¹öÆ°
	private JButton leftRButton = new JButton(leftRBasicImage); // ¹æ ¿ŞÂÊ ³Ñ±â±â ¹öÆ°
	private JButton mute = new JButton();
	
	private boolean muteSel=false;
	
//BGM
	private Music bgm;
	private Music buttonEnteredBGM;
	
	public WaitingRoom() {
		//½ÇÇà°ú µ¿½Ã¿¡ ³×Æ®¿öÅ© ÀÚ¿ø°ú id¸¦ MainView·ÎºÎÅÍ ÀÌ¾î¹Ş¾Æ¿Â´Ù.
		id = MainView.getId();
		dos = MainView.getDos();
		
		Font font = new Font("Inconsolata",Font.BOLD,15); // ÆùÆ® ¼³Á¤
		
		setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ)
		setTitle("Catch Mind"); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù ÀÌ¸§(Å¸ÀÌÆ² ¹Ù¸¦ ¾ø¾Ù ¿¹Á¤ÀÌ±â ¶§¹®¿¡ ¾ø¾îµµ µÇ´Â ÄÚµå)
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main¿¡¼­ °íÁ¤½ÃÅ² È­¸é ÇØ»óµµ¸¦ »ç¿ë
		setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
		setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
		setBackground(new Color(0,0,0,0)); // ¹è°æ»öÀ» Åõ¸íÇÏ°Ô ÇÑ´Ù.(paint()¸Ş¼Òµå·Î ±×¸®´Â ¹è°æÀ» º¸ÀÌ°Ô ÇÏ±â À§ÇÔ)
		setVisible(true); // À©µµ¿ì¸¦ º¼ ¼ö ÀÖÀ½.
		setLayout(null);
		setCursor(myCursor);
		
	// Label
		// #¸Ş´º¹Ù
		mainMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		mainMenuBar.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				mainMenuBar.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		mainMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
			// #¸Å´º¹Ù µå·¡±× ½Ã, ¿òÁ÷ÀÏ ¼ö ÀÖ°Ô ÇÑ´Ù.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
		add(mainMenuBar);
		
	// JScrollPane
		// #Ã¤ÆÃºä
		chattingView = new JScrollPane(chattingArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chattingView.setComponentZOrder(chattingView.getVerticalScrollBar(), 0);
		chattingView.setComponentZOrder(chattingView.getViewport(), 1);
		chattingView.getVerticalScrollBar().setOpaque(false);
		/* ÀÌÇÏ ÄÚµå´Â ½ºÅ©·Ñ ¹Ù º¯°æÀ» À§ÇÑ ÄÚµå·Î
		 * ¸¸µé¾îº¸·Á°í ³ë·ÂÇßÁö¸¸ µÇÁö ¾Ê¾Æ °Ë»öÇØ¼­ ¾Ë¾Æº½.
		 * ÄÚµå ÀÌÇØ°¡ ¾î·Á¿ö ÁÖ¼® ¾øÀÌ ºÙ¿©³Ö¾î »ç¿ëÇÔ.¤Ğ¤Ğ
		 */
		chattingView.setLayout(new ScrollPaneLayout() {
			@Override
			public void layoutContainer(Container parent) {
				JScrollPane scrollPane = (JScrollPane) parent;

				Rectangle availR = scrollPane.getBounds();
				availR.x = availR.y = 0;

				Insets insets = parent.getInsets();
				availR.x = insets.left;
				availR.y = insets.top;
				availR.width -= insets.left + insets.right;
				availR.height -= insets.top + insets.bottom;

				Rectangle vsbR = new Rectangle();
				vsbR.width = 12;
				vsbR.height = availR.height;
				vsbR.x = availR.x + availR.width - vsbR.width;
				vsbR.y = availR.y;

				if (viewport != null) {
					viewport.setBounds(availR);
				}
				if (vsb != null) {
					vsb.setVisible(true);
					vsb.setBounds(vsbR);
				}
			}
		});
		chattingView.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			private final Dimension d = new Dimension();

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Color color = null;
				JScrollBar sb = (JScrollBar) c;
				if (!sb.isEnabled() || r.width > r.height) {
					return;
				} else if (isDragging) {
					color = new Color(200, 200, 100, 200);
				} else if (isThumbRollover()) {
					color = new Color(255, 255, 100, 200);
				} else {
					color = new Color(220, 220, 200, 200);
				}
				g2.setPaint(color);
				g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
				g2.setPaint(Color.WHITE);
				g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
				g2.dispose();
			}

			@Override
			protected void setThumbBounds(int x, int y, int width, int height) {
				super.setThumbBounds(x, y, width, height);
				scrollbar.repaint();
			}
		});
		chattingView.setBounds(264, 492, 720, 182);
		chattingView.setBackground(new Color(0,0,0,0));
		chattingView.setViewportView(chattingArea);
		chattingView.setBorder(null); // Å×µÎ¸® Á¦°Å
		chattingArea.setBackground(new Color(0,0,0,0)); 
		chattingArea.setFont(font);
		chattingArea.setForeground(Color.BLACK);
		chattingArea.setEditable(false); // ÇØ´ç ÇÊµå¸¦ ¼öÁ¤ÇÒ ¼ö ¾øÀ½
		chattingArea.setLineWrap(true); // ÀÚµ¿ ÁÙ¹Ù²Ş
		chattingView.setViewportView(chattingArea);
		/* ÀÌÇÏ ÄÚµå´Â ¾²·¹µå È¯°æ¿¡¼­µµ ÀÚµ¿ ½ºÅ©·ÑÀÌ µÇ°ÔÇÏ·Á´Â ¸Ş¼ÒµåÀÌ´Ù. */
		chattingView.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollpanemove = true;
			}
		});
		chattingView.getVerticalScrollBar().addAdjustmentListener(new	AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) { // ¼öÁ¤¸®½º³Ê¿¡¼­ º¯¼ö(ÈÙÀÇ ±æÀÌ,À§Ä¡)°¡ º¯°æµÉ½Ã ¸Ş¼Òµå ÀÛ¼º
				if (scrollpanemove) { // ¸¸¾à ½ºÅ©·Ñ ¹«ºê°¡ Çã¿ëµÇÀÖÀ»½Ã
					scrollpanemove = false; // ¹ØÀ¸·Î ³»¸®´Â °ÍÀ» ÇÏÁö¾Ê°í, ºñÇã¿ëÀ¸·Î ¹Ù²Û´Ù.
				} else {
					JScrollBar src = (JScrollBar) e.getSource();
					src.setValue(src.getMaximum());
				}
			}
		});
		add(chattingView);
		
		// #À¯Àú Á¤º¸(ÀÚ½Å) ºä
		userViewLb = new JLabel(new ImageIcon(WaitingRoom.class.getResource("/images/userInfo.png")));
		userViewLb.setBounds(30, 251, 190, 66);
		getContentPane().add(userViewLb);	
		userGradeLb = new JLabel();
		userGradeLb.setBounds(16,12,77,42);
		userViewLb.add(userGradeLb);
		userGradeLb.setVisible(true);
		userNameLb = new JLabel("", SwingConstants.CENTER);
		userNameLb.setBounds(116,5,52,32);
		userNameLb.setFont( new Font("Times", Font.BOLD, 15));
		userViewLb.add(userNameLb);
		userNameLb.setVisible(true);
		userCorCount = new JLabel("", SwingConstants.CENTER);
		userCorCount.setBounds(116,32,52,32);
		userCorCount.setFont( new Font("Times", Font.BOLD, 15));
		userViewLb.add(userCorCount);
		userCorCount.setVisible(true);
		
		
		// #À¯Àú ¸®½ºÆ® ºä
		userListView.setBounds(30, 336, 190, 374);
		userListView.setLayout(new FlowLayout(FlowLayout.CENTER));
		userListView.setBackground(new Color(40,40,40,40));
		allocationUserInfo();
		add(userListView); 
		
		// #°ÔÀÓ¹æ ºä
		gameRoomView.setBounds(264, 110, 720, 320);
		gameRoomView.setLayout(new FlowLayout(FlowLayout.CENTER));
		gameRoomView.setBackground(new Color(0,0,0,0));
		allocationRoom(); // ´ë±â½Ç¿¡ °ÔÀÓ¹æÀÌ º¸ÀÌµµ·Ï ÇÏ´Â ¸Ş¼Òµå
		add(gameRoomView); 
		
		
	// TextField
		chatting_tf = new JTextField(); 
		chatting_tf.setBounds(280, 674, 688, 30);
		chatting_tf.setBorder(null);
		chatting_tf.setBackground(new Color(0,0,0,0));
		chatting_tf.setDocument(new JTextFieldLimit(45)); // Ã¤ÆÃ 45ÀÚ Á¦ÇÑ 	 
		chatting_tf.setFont(font);
		chatting_tf.setForeground(Color.BLACK);
		chatting_tf.addKeyListener(new keyAdapter()); // Å¬·¡½º·Î Á¤ÀÇÇÑ Å° ÀÌº¥Æ®¸¦ Àû¿ë
		add(chatting_tf);
			
	// Button
		// #³ª°¡±â ¹öÆ°
		exitButton.setBounds(400, 36, 135, 53);
		exitButton.setBorder(null);
		exitButton.setContentAreaFilled(false);
		exitButton.setBackground(new Color(0,0,0,0));
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				exitButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
//				exitButton.setCursor(myCursor);
				if(muteSel==false) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start();
				}
			}
			
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				exitButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
//			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					// ¼­¹ö¿¡ ÇØ´ç À¯Àú°¡ ·Î±×¾Æ¿ô ÇÏ¿´À½À» ¾Ë¸®°í
					send_message("UserLogout/"+userInfo.getUserID());
					// ÇÁ·Î¼¼½º Á¾·á
					System.exit(0); 
				}
			}
		});
		
		mute.setBounds(960,730,30,30);
		mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOn.png")));
		mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOnCLK.png")));	
		mute.setContentAreaFilled(false);
		mute.setFocusPainted(false);
		mute.setBorderPainted(false);
		mute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(muteSel) {
					muteSel=false;
					mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOn.png")));
					mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOnCLK.png")));	
					
				}
				else {
					muteSel=true;
					mute.setIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOff.png")));
					mute.setRolloverIcon(new ImageIcon(WaitingRoom.class.getResource("/images/soundOffCLK.png")));
				}
				soundOn(muteSel);
			}
		});
		add(mute);
		mute.setVisible(true);
		
		// #¹æ¸¸µé±â ¹öÆ°
		createRoomButton.setBounds(260, 37, 135, 53);
		createRoomButton.setBorder(null);
		createRoomButton.setContentAreaFilled(false);
		createRoomButton.setBackground(new Color(0,0,0,0));
		add(createRoomButton);
		createRoomButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				createRoomButton.setIcon(createRoomEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				createRoomButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				if(muteSel==false) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start();
				}
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				createRoomButton.setIcon(createRoomBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				createRoomButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					new CreateRoom();
				}
			}
		});
		
		// #¹æ ¿À¸¥ÂÊ ³Ñ±â±â ¹öÆ°
		rightRButton.setBounds(647, 437, 36, 21);
		rightRButton.setBorder(null);
		rightRButton.setBackground(new Color(0,0,0,0));
		add(rightRButton);
		rightRButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				rightRButton.setIcon(rightREnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				rightRButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				if(muteSel==false) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start();
				}
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				rightRButton.setIcon(rightRBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				rightRButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});

		// #¹æ ¿ŞÂÊ ³Ñ±â±â ¹öÆ°
		leftRButton.setBounds(568, 437, 36, 21);
		leftRButton.setBorder(null);
		leftRButton.setBackground(new Color(0,0,0,0));
		add(leftRButton);
		leftRButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				leftRButton.setIcon(leftREnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				leftRButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
				if(muteSel==false) {
					buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
					buttonEnteredBGM.start();
				}
			}

			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseExited(MouseEvent e) {
				leftRButton.setIcon(leftRBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				leftRButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1) {
					
				}
			}
		});
		
		//´ë±â½Ç ÀÔÀå½Ã, È¯¿µÇÏ´Â ¹®±¸ Ãâ·Â
		chattingArea.append("["+id+"]´Ô È¯¿µÇÕ´Ï´Ù! \n");
		
		bgm = new Music("waitingRoomBGM.mp3", true);
		bgm.start();
	} // WaitingRoom() »ı¼ºÀÚ ³¡
	
	public void soundOn(boolean muteSel) {
		if(muteSel) {
			bgm.close();
		}
		else
		{
			bgm = new Music("waitingRoomBGM.mp3", true);
			bgm.start();
		}
	}

	private void Inmessage(String str) // ¼­¹ö·ÎºÎÅÍ µé¾î¿À´Â ¸ğµç ¸Ş¼¼Áö
	{
		st = new StringTokenizer(str, "@");  // ¾î¶² ¹®ÀÚ¿­À» »ç¿ëÇÒ °ÍÀÎÁö, ¾î¶² ¹®ÀÚ¿­·Î ÀÚ¸¦ °ÍÀÎÁö =>  [ NewUser/»ç¿ëÀÚID ] ÇüÅÂ·Î µé¾î¿È
		
		String protocol = st.nextToken(); // ÇÁ·ÎÅäÄİÀ» ÀúÀåÇÑ´Ù.
		String mUserId = st.nextToken(); // ¸Ş½ÃÁö¸¦ ÀúÀåÇÑ´Ù.
		
		System.out.println("ÇÁ·ÎÅäÄİ : " + protocol);
		System.out.println("³»¿ë : " + mUserId);
		
		// protocol ¼ö½Å Ã³¸®
		switch(protocol) {
		
		// #»õ·Î¿î Á¢¼ÓÀÚ ¾Ë¸²
		case "NewUser": 
			// »õ·Î¿î Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ °¡Á®¿Í ÀúÀåÇÑ´Ù.
			int level = Integer.parseInt(st.nextToken()); //·¹º§
			int exp = Integer.parseInt(st.nextToken()); //°æÇèÄ¡
			int corAnswer = Integer.parseInt(st.nextToken()); //´©Àû Á¤´ä¼ö
			boolean checkID = true;
			
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			UserInfo u = new UserInfo(mUserId, level, exp, corAnswer);
			// Áßº¹ÀÌ ÀÖÀ»°æ¿ì Ãß°¡ÇÏÁö¾ÊÀ½.(ÇØ°á ¸øÇØ¼­ ³ÖÀº ÄÚµå..)
			for (int i = 0; i < user_list.size(); i++) {
				UserInfo checkUser = (UserInfo) user_list.get(i);
				if (checkUser.getUserID().equals(mUserId)) {
					// Áßº¹ÀÌ ÀÖÀ»°æ¿ì false
					checkID = false;
					break;
				}
			}

			if (checkID) {
				// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
				user_list.add(u);
			}
			
			
			updateUserInfo();  	// À¯Àú¸®½ºÆ® °»½Å
			
			break;
		
		// #Á¢¼ÓÀÚ Á¤º¸ ¹Ş¾Æ¿À±â(»ç¿ëÀÚ º»ÀÎÀÇ Á¤º¸)
		case "UserInfo":
			// »ç¿ëÀÚÀÇ Á¤º¸¸¦ °¡Á®¿Í ÀúÀåÇÑ´Ù.
			level = Integer.parseInt(st.nextToken()); //·¹º§
			exp = Integer.parseInt(st.nextToken()); //°æÇèÄ¡
			corAnswer = Integer.parseInt(st.nextToken()); //´©Àû Á¤´ä¼ö
			checkID = true;
			
			//Å×½ºÆ® ÄÚµå
			System.out.println("====UserInfo Àü Å×½ºÆ®ÇÕ´Ï´Ù.=====");
			for(int i=0; i<user_list.size(); i++) {
				UserInfo checkUser = (UserInfo)user_list.get(i);
				System.out.println("À¯ÀúÁ¤º¸ : " +checkUser);
				
			}
			System.out.println("==========Å×½ºÆ® ³¡===========");
			
			
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			userInfo = new UserInfo(mUserId, level, exp, corAnswer);
			
			// Áßº¹ÀÌ ÀÖÀ»°æ¿ì Ãß°¡ÇÏÁö¾ÊÀ½.(ÇØ°á ¸øÇØ¼­ ³ÖÀº ÄÚµå..)
			for(int i=0; i<user_list.size(); i++) {
				UserInfo checkUser = (UserInfo)user_list.get(i);
				if(checkUser.getUserID().equals(mUserId)) {
					// Áßº¹ÀÌ ÀÖÀ»°æ¿ì false
					checkID = false;
					break;
				}
			}
			
			if(checkID) {
				// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
				user_list.add(userInfo);
			}
			
			//Å×½ºÆ® ÄÚµå
			System.out.println("====UserInfo ÈÄ Å×½ºÆ®ÇÕ´Ï´Ù.=====");
			for(int i=0; i<user_list.size(); i++) {
				UserInfo checkUser = (UserInfo)user_list.get(i);
				System.out.println("À¯ÀúÁ¤º¸ : " +checkUser);
			}
			System.out.println("==========Å×½ºÆ® ³¡===========");
			
		
			System.out.println("Å×½ºÆ® : " + id + Integer.toString(userInfo.getCorAnswer()));
			userGradeLb.setIcon(userInfo.getGrade(level));
			userNameLb.setText(mUserId);
			userCorCount.setText(Integer.toString(corAnswer));
			
			
			
			updateUserInfo(); 	// À¯Àú¸®½ºÆ® °»½Å
			break;
		
		// #±âÁ¸ Á¢¼ÓÀÚÀÇ Á¤º¸¸¦ ¹Ş¾Æ¿Â´Ù.(ÃÊ±â ¼¼ÆÃ ÀÛ¾÷)
		case "OldUser":
			// ÇØ´ç ÇÁ·ÎÅäÄİ·Î ¹Ş´Â »ç¿ëÀÚ Á¤º¸´Â Ã¹ ·Î±×ÀÎ ÇÑ ¹ø¸¸ Àû¿ëµÈ´Ù.
			// ÀÌÀü¿¡ Á¢¼ÓÁßÀÌ´ø ¸ğµç À¯ÀúÀÇ Á¤º¸¸¦ °¡Á®¿Â´Ù.
			level = Integer.parseInt(st.nextToken()); //·¹º§
			exp = Integer.parseInt(st.nextToken()); //°æÇèÄ¡
			corAnswer = Integer.parseInt(st.nextToken()); //´©Àû Á¤´ä¼ö
			checkID = true;
			
			//Å×½ºÆ® ÄÚµå
			System.out.println("====OldUser Àü Å×½ºÆ®ÇÕ´Ï´Ù.=====");
			for(int i=0; i<user_list.size(); i++) {
				UserInfo checkUser = (UserInfo)user_list.get(i);
				System.out.println("À¯ÀúÁ¤º¸ : " +checkUser);
				
			}
			System.out.println("==========Å×½ºÆ® ³¡===========");
			
			
			// °¡Á®¿Â Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			u = new UserInfo(mUserId, level, exp, corAnswer);
			
			// Áßº¹ÀÌ ÀÖÀ»°æ¿ì Ãß°¡ÇÏÁö¾ÊÀ½.(ÇØ°á ¸øÇØ¼­ ³ÖÀº ÄÚµå..)
			for (int i = 0; i < user_list.size(); i++) {
				UserInfo checkUser = (UserInfo) user_list.get(i);
				if (checkUser.getUserID().equals(mUserId)) {
					// Áßº¹ÀÌ ÀÖÀ»°æ¿ì false
					checkID = false;
					break;
				}
			}
			
			if(checkID) {
				// ÇØ´ç °´Ã¼¸¦ Vector¿¡ Ãß°¡
				user_list.add(u);
			}
			
			//Å×½ºÆ® ÄÚµå
			System.out.println("====OldUser ÈÄ Å×½ºÆ®ÇÕ´Ï´Ù.=====");
			for(int i=0; i<user_list.size(); i++) {
				UserInfo checkUser = (UserInfo)user_list.get(i);
				System.out.println("À¯ÀúÁ¤º¸ : " +checkUser);
				
			}
			System.out.println("==========Å×½ºÆ® ³¡===========");
			

			// ¸¶Áö¸· ÅäÅ«ÀÌ lastÀÏ °æ¿ì ¸®½ºÆ®¸¦ °»½ÅÇÑ´Ù.
			String lastCheck = st.nextToken();
			if (lastCheck.equals("last"))
				updateUserInfo();
			break;
			
		// #°ÔÀÓ¹æ ÀÔÀå, È¤Àº ·Î±×¾Æ¿ôÀ¸·ÎÀÎÇÑ ÇØ´ç À¯Àú¸¦ À¯Àú ¸®½ºÆ®¿¡¼­ Á¦°ÅÇÑ´Ù.
		case "RemoveUser":
			for(int i=0; i<user_list.size(); i++) {
				UserInfo rUser = (UserInfo)user_list.get(i);
				// ÇØ´ç À¯Àú ¾ÆÀÌµğ¸¦ Ã£´Â´Ù.
				if(rUser.getUserID().equals(mUserId)) {
					// ÇØ´ç ¾ÆÀÌµğ¸¦ ´ë±â½Ç À¯Àú¿¡¼­ Áö¿î´Ù.
					user_list.remove(i); 
					// À¯Àú ¸®½ºÆ®¸¦ ¾÷µ¥ÀÌÆ®ÇÏ¿© ÆĞ³Î¿¡ Àû¿ë½ÃÅ²´Ù.
					updateUserInfo();
					break;
				}
			}
			
			break;
			
		// #ÂÊÁö º¸³»±â
		case "Note":
			String note = st.nextToken(); // ¹ŞÀº ³»¿ë
			System.out.println(mUserId+" »ç¿ëÀÚ·ÎºÎÅÍ ¿Â ÂÊÁö "+note);
			JOptionPane.showMessageDialog(null, note, mUserId+"´ÔÀ¸·Î ºÎÅÍ ÂÊÁö", JOptionPane.CLOSED_OPTION);
			break;
			
		// #¹æ »ı¼º
		case "CreateRoom":
			int room_No = Integer.parseInt(st.nextToken()); // ¹æ ¹øÈ£
			
			send_message("EntryRoom/"+userInfo.getUserID());
			// WaitingRoom Ã¢À» Á¾·áÇÏ°í °ÔÀÓÃ¢À» ¿¬´Ù. ÀÌ¶§, ¹æ Á¦¸ñ°ú ¹æ ¹øÈ£¸¦ °°ÀÌ º¸³½´Ù.
			dispose();
			bgm.close();
			break;
		
		// #±âÁ¸¿¡ »ı¼ºµÈ ¹æÀÇ Á¤º¸¸¦ ³Ñ°Ü¹ŞÀ½(ÃÊ±â ¼¼ÆÃ ÀÛ¾÷)
		case "OldRoom":
			// ÇØ´ç ÇÁ·ÎÅäÄİ·Î ¹Ş´Â ¹æ Á¤º¸´Â Ã¹ ·Î±×ÀÎ ÇÑ ¹ø¸¸ Àû¿ëµÈ´Ù.
			// ÀÌÀü¿¡ °³¼³µÈ ¸ğµç ¹æÀÇ Á¤º¸¸¦ °¡Á®¿Â´Ù.
			room_No = Integer.parseInt(st.nextToken()); // ¹æ ¹øÈ£
			String room_name = st.nextToken(); // ¹æ ÀÌ¸§
			String state = st.nextToken(); // ºñ¹Ğ¹øÈ£ À¯¹«
			String room_PW = st.nextToken(); // ¹æ ºñ¹Ğ¹øÈ£
			int fixed_User = Integer.parseInt(st.nextToken()); // À¯Àú Á¤¿ø
			// °ÔÀÓ¹æ¿¡ µé¾î°¡ÀÖ´Â À¯Àú °´Ã¼ÀÇ Á¤º¸±îÁø ÇÊ¿ä¾øÀ¸¹Ç·Î ÀÎ¿ø ¼ö¸¸ ¹Ş´Â´Ù.
			int room_UCount = Integer.parseInt(st.nextToken()); // ÇöÀç À¯Àú¼ö
			
			//³Ñ°Ü ¹ŞÀº Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			RoomInfo oldRoom = new RoomInfo(room_No, room_name, state, room_PW, room_UCount, fixed_User);
			//ÇØ´ç °´Ã¼¸¦ room_list¿¡ Ãß°¡
			
			room_list.add(oldRoom);
			
			// ¸¶Áö¸· ÅäÅ«ÀÌ lastÀÏ °æ¿ì ¹æ ÆĞ³ÎÀ» °»½ÅÇÑ´Ù.
			String lastroomCheck = st.nextToken();
			if (lastroomCheck.equals("last")) 
				relocationRoom(); //¹æ ¾÷µ¥ÀÌÆ®
			break;
			
		// #»õ·Î¿î ¹æ »ı¼º ¾Ë¸²
		case "NewRoom":
			room_No = Integer.parseInt(st.nextToken()); // ¹æ ¹øÈ£
			room_name = st.nextToken(); // ¹æ ÀÌ¸§
			state = st.nextToken(); // ºñ¹Ğ¹øÈ£ À¯¹«
			room_PW = st.nextToken(); // ¹æ ºñ¹Ğ¹øÈ£
			fixed_User = Integer.parseInt(st.nextToken()); // À¯Àú Á¤¿ø
			// °ÔÀÓ¹æ¿¡ µé¾î°¡ÀÖ´Â À¯Àú °´Ã¼ÀÇ Á¤º¸±îÁø ÇÊ¿ä¾øÀ¸¹Ç·Î ÀÎ¿ø ¼ö¸¸ ¹Ş´Â´Ù.
			room_UCount = Integer.parseInt(st.nextToken()); // ÇöÀç À¯Àú¼ö
			
			//³Ñ°Ü ¹ŞÀº Á¤º¸·Î °´Ã¼¸¦ »ı¼º
			RoomInfo NewRoom = new RoomInfo(room_No, room_name, state, room_PW, room_UCount, fixed_User);
			//ÇØ´ç °´Ã¼¸¦ room_list¿¡ Ãß°¡
			room_list.add(NewRoom);
			
			//¹æ ¾÷µ¥ÀÌÆ®
			relocationRoom();
			break;
		
		// #¹æ ÀÔÀå Çã°¡¸¦ ¹ŞÀ½
		case "EntryRoom":
			// ÇØ´ç Ã¢À» Á¾·áÇÑ´Ù. ¹æ¿¡ ´ëÇÑ Á¤º¸´Â ÀÌ¾î¼­ MainView·Î ¹Ş´Â´Ù.
			dispose();
			bgm.close();
			break;
			
		// #ºñ¹Ğ¹øÈ£ ÀÔ·Â ¿äÃ»
		case "InputPW":
			/*
			 *  ÆĞ½º¿öµå ÀÔ·Â ÈÄ ÀÔÀåÇÏ´Â ±¸Á¶!
			 */
			// ¹æ¹øÈ£¿Í ÆĞ½º¿öµå¸¦ ¹Ş´Â´Ù.
			room_No = Integer.parseInt(st.nextToken());
			room_PW = st.nextToken();
			new inputPw(room_No,room_PW);
			
			break;
			
		// #Ã¤ÆÃ
		case "ChattingWR":
			st = new StringTokenizer(str,"/@", true); // ±¸È¹¹®ÀÚ"/"µµ ÅäÅ«À¸·Î °£ÁÖÇÑ´Ù.
			for(int i=0; i<4; i++) { 
				st.nextToken(); // ÅäÅ« Á¦°Å¿ë
			}
			ArrayList<String> chattingMsgList = new ArrayList<String>(); // Ã¤ÆÃ¸Ş½ÃÁö ÀúÀåÇÒ ¸®½ºÆ®
			String totalChattingMsg = ""; // ÀüÃ¼ Ã¤ÆÃ ¸Ş½ÃÁö ÀúÀå º¯¼ö
			String tempMsg = "";
			while(st.hasMoreTokens()) { // ¸®ÅÏÇÒ ´ÙÀ½ ÅäÅ«ÀÌ ÀÖÀ¸¸é true¸¦ ¾øÀ¸¸é false¸¦ ¸®ÅÏÇÑ´Ù.
				tempMsg=st.nextToken();
				System.out.println("Ã¤ÆÃ ÅäÅ«µé Ãâ·Â:"+tempMsg);
				chattingMsgList.add(tempMsg); // ¸Ş½ÃÁö ÅäÅ«À» ArrayList¿¡ Ãß°¡
			}

			for(int i=0; i<chattingMsgList.size(); i++) { // chattingMsgListÀÇ ¸ğµç ¸Ş½ÃÁö¸¦ totalChattingMsg¿¡ ÀúÀåÇÑ´Ù.
				totalChattingMsg += chattingMsgList.get(i);
			}
			
			System.out.println("WaingRoom Ã¤ÆÃ ³»¿ë : " + totalChattingMsg);
			chattingArea.append("["+mUserId+"] : "+totalChattingMsg+"\n");
			break;
			
		// # ¹æ ÀÎ¿ø ¾÷µ¥ÀÌÆ® Á¤º¸°¡ ÀÖÀ» °æ¿ì
		case "RoomInfoUpdate":
			room_No = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());

			if (count > 0) {
				for (int i = 0; i < room_list.size(); i++) {
					RoomInfo r = (RoomInfo) room_list.get(i);
					if (r.getRoom_No() == room_No) {
						r.setRoom_UCount(count);
						break;
					}
				}
			} else {
				for (int i = 0; i < room_list.size(); i++) {
					RoomInfo r = (RoomInfo) room_list.get(i);
					if (r.getRoom_No() == room_No) {
						room_list.remove(i);
						break;
					}
				}
			}

			// ¹æ ÆĞ³ÎÀ» °»½ÅÇØ¼­ ´Ù½Ã ±×¸°´Ù.
			relocationRoom();
			break;
		}
	}
	
	private void send_message(String str) // ¼­¹ö¿¡°Ô ¸Ş¼¼Áö¸¦ º¸³»´Â ºÎºĞ
	{
		try {
			dos.writeUTF(str);
		} catch (IOException e) // ¿¡·¯ Ã³¸® ºÎºĞ
		{
			e.printStackTrace();
		}
	}
	
	// MainView Å¬·¡½º¿¡¼­ WaitingRoom Å¬·¡½º·Î ¸Ş½ÃÁö¸¦ Àü´ŞÇÏ±â À§ÇØ »ç¿ëÇÏ´Â ¸Ş¼Òµå
	public void wr_Inmessage(String str) {
		Inmessage(str);
	}
	
	// ´ë±â½Ç 24°³ÀÇ ¹æÀ» »ı¼ºÇÏ±â À§ÇÑ ¸Ş¼Òµå (¹æ °³¼öÀÇ º¯µ¿ÀÌ »ı±æ¶§¸¶´Ù µ¿ÀÛ)
	private void allocationRoom() {
		// ÃßÈÄ »ı¼ºµÈ ¹æ¿¡°Ô¸¸ ÀÌº¥Æ®¸¦ ÇÒ´çÇÏµµ·Ï º¯°æ
		for(int i=0; i<gameRoom.length; i++) {
			// ³»ºÎ Å¬·¡½º GameRoomPanel Å¬·¡½º¸¦ ÀÌ¿ëÇØ¼­ gameRoom PanelÀ» »ı¼º
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			gameRoom[i] = grp;
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	
	// ÆĞ³Î¿¡ ¹æÀ» »ı¼ºÇÏ´Â ¸Ş¼Òµå(¹æ ¹øÈ£¿Í ÇØ´ç ÆĞ³ÎÀÇ ÀÎµ¦½º °ªÀ» ¹Ş¾Æ¼­ »ı¼ºÇÑ´Ù.)
	// ¸Ş¼Òµå ¼öÁ¤, ÇØ´ç ¸Ş¼Òµå·Î ÀüÃ¼ ¹æÀ» °³¼³ÇÏ´Â ÂÊÀ¸·Î ÇØº¸ÀÚ. 
	private void createRoom() {
		Font roomFont = new Font("Inconsolata",Font.BOLD,17); // ÆùÆ® ¼³Á¤
		
		// ±âÁ¸¿¡ ¶ç¿öµĞ ÆĞ³ÎÀ» Áö¿î´Ù.
		gameRoomView.removeAll();
		
		// ¸ğµç gameRoom¿¡ Àû¿ëÇÒ ÄÚµå. ¸®½ºÆ®¿¡ µî·ÏµÈ °´Ã¼ ¼ö ¸¸Å­ ÆĞ³Î¿¡ ÀÌº¥Æ® Àû¿ë
		for(int i=0; i<gameRoom.length; i++) {
			// ¹æ¿¡ Àû¿ëÇÒ ÆĞ³ÎÀ» ¸¸µé¾î ¸®½º³Ê¸¦ Àû¿ë½ÃÅ²´Ù(±âÁ¸¿¡ ¶ç¿öµĞ ÆĞ³ÎµéÀ» »èÁ¦ÇÏ°í ´Ù½Ã ±×¸®´Â Çü½ÄÀ¸·Î ±×¸®±â¶§¹®¿¡ ´Ù½Ã ¼³Á¤ÇÑ´Ù.)
			GameRoomPanel grp = new GameRoomPanel(gamgeRoomBasicImage.getImage());
			gameRoom[i] = grp; // ÆĞ³Î »óÅÂ Àû¿ë
			
			// room_listÀÇ Å©±âº¸´Ù ÀÛÀ» ¶§, room_listÀÇ Á¤º¸¸¦ ¹æ¿¡ Ãß°¡ÇÑ´Ù.
			if(i<room_list.size()) {
				// room_list¿¡¼­ °´Ã¼ ÇÏ³ª¸¦
				RoomInfo r = (RoomInfo)room_list.get(i);
				// ÇØ´ç grp¿¡ RoomInfo °´Ã¼¸¦ ÀúÀå½ÃÅ²´Ù.(grp¿¡ room_listÀÇ °´Ã¼¸¦ ÀúÀå½ÃÅ°´Â °úÁ¤)
				grp.setRoomInfo(r);
				grp.addMouseListener(new MouseAdapter() {
					// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
					@Override
					public void mouseEntered(MouseEvent e) {
						grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
						grp.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
						if(muteSel==false) {
							buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
							buttonEnteredBGM.start();
						}
					}
					
					// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
					@Override
					public void mouseExited(MouseEvent e) {
						grp.setGRImage(gamgeRoomBasicImage.getImage()); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
						grp.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
					}
					
					// ¸¶¿ì½º·Î ¹öÆ°À» ´­·¶À» ¶§ ÀÌº¥Æ®
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == 1) {
							grp.setGRImage(gamgeRoomPressedImage.getImage()); // ¸¶¿ì½º¸¦ ´­·¶À» ¶§ ÀÌ¹ÌÁö º¯°æ(Pressed Image)
							
						}
					}
					
					// ¸¶¿ì½º·Î ¹öÆ°À» ´©¸¥ ÈÄ ¶¼¾úÀ» ¶§ ÀÌº¥Æ®
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == 1) {
							grp.setGRImage(gamgeRoomEnteredImage.getImage()); // ´©¸¥ ¹öÆ°ÀÌ ¶¼¾îÁ³À» ¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image) - ¸¶¿ì½º´Â ÀÌ¹Ì ÆĞ³Î¿¡ ¿Ã·Á³õ¿©Áø »óÅÂÀÌ±â ¶§¹®¿¡
							// ÇØ´ç ÆĞ³Î¿¡ ÀúÀåµÈ °´Ã¼ Á¤º¸¸¦ °¡Á®¿Í¼­ roomInfo¿¡ ÀúÀåÇÑ´Ù.
							RoomInfo roomInfo = grp.getRoomInfo();
							
		                     //¸¸¾à ÀÎ¿ø¼ö°¡ °¡µæ Ã¡´Ù¸é ´õÀÌ»ó ÀÔÀåÇÏÁö ¸øÇÏµµ·Ï ÇÑ´Ù.
		                     if(roomInfo.getRoom_UCount() == roomInfo.getFixed_User()) {
		                        JOptionPane.showMessageDialog(null, "¹æÀÌ ²Ë Ã¡½À´Ï´Ù.", "¾Ë¸²", JOptionPane.ERROR_MESSAGE);
		                     } else {
		                        // ÇÁ·ÎÅäÄİÀº EnterRoomÀ¸·Î À¯Àú id¿Í ¹æ ¹øÈ£¸¦ ¼­¹ö¿¡ º¸³½´Ù.
		                        send_message("EnterRoom/"+userInfo.getUserID()+"/"+roomInfo.getRoom_No());
		                     }
						}
					}
				});
				gameRoom[i] = grp; // °»½ÅµÈ ÆĞ³Î·Î ´Ù½Ã Àû¿ë
				
				// °ÔÀÓ¹æ ¹øÈ£¸¦ ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
				JLabel gameRoomNumber_lb = new JLabel();
				// ¹æ¹øÈ£ ¼³Á¤. room_No°ª¿¡ µû¶ó ÇØ´ç ¹æ¹øÈ£¸¦ ÁöÁ¤. 000 ÀÇ ÇüÅÂ·Î ¼³Á¤. 
				String number = "";
				int temp = r.getRoom_No(), tempI;
				for (int j = 1; j <= 3; j++) {
					if ((tempI=temp % 10)==0) {
						number = "" + 0 + number;
					} else {
						number = "" + tempI + number;
					}
					temp /= 10;
				}
				// gameRoomNumber_lb¸¦ ¼¼ÆÃÇÑ´Ù.
				gameRoomNumber_lb.setText(number);
				gameRoomNumber_lb.setFont(roomFont);
				gameRoomNumber_lb.setBounds(38, 21, 40, 20);
				gameRoomNumber_lb.setForeground(Color.DARK_GRAY);
				gameRoomNumber_lb.setLayout(null);
				gameRoom[i].add(gameRoomNumber_lb);
				
				// °ÔÀÓ¹æ Á¦¸ñÀ» ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
				JLabel gameRoomTitle_lb = new JLabel();
				gameRoomTitle_lb.setText(r.getRoom_name());
				gameRoomTitle_lb.setFont(roomFont);
				gameRoomTitle_lb.setBounds(95, 21, 200, 20);
				gameRoomTitle_lb.setForeground(Color.DARK_GRAY);
				gameRoom[i].add(gameRoomTitle_lb);
				
				// °ÔÀÓ¹æ ÀÎ¿ø¼ö¸¦ ¼³Á¤ÇÒ JLabelÀ» ÇÒ´ç
				JLabel gameRoomPlayerCount_lb = new JLabel();
				String userCount = "" + r.getRoom_UCount() + " / " + r.getFixed_User();
				gameRoomPlayerCount_lb.setText(userCount);
				gameRoomPlayerCount_lb.setFont(roomFont);
				gameRoomPlayerCount_lb.setBounds(250, 65, 50, 20);
				gameRoomPlayerCount_lb.setForeground(Color.DARK_GRAY);
				gameRoom[i].add(gameRoomPlayerCount_lb);
			}
			// ÀÌ·¸°Ô »ı¼ºÇÑ ÆĞ³ÎÀ» ºä¿¡ Ãß°¡ÇÑ´Ù.
			gameRoomView.add(gameRoom[i]);
		}
	}
	
	// #¹æÀÌ »ı¼º/Á¦°ÅµÉ ¶§¸¶´Ù ¹æ¹øÈ£¿¡ µû¶ó ¼ø¼­¸¦ Àç¹èÄ¡ÇÏ´Â ¸Ş¼Òµå(¾÷µ¥ÀÌÆ® ¸Ş¼Òµå)
	public void relocationRoom() {
				
		// room_list¸¦ AscendingRoomInfo Á¤·Ä Á¶°Ç¿¡ µû¶ó ÀçÁ¤·ÄÇÑ´Ù.
		Collections.sort(room_list, new AscendingRoomInfo());
		
		// ±âº»ÀûÀÎ ¹è°æ ÆĞ³Î¸¸ »ı¼ºµÈ ¹æ¿¡ list¿¡ µî·ÏµÈ ¹æ¸¸ Å­ Á¤º¸¸¦ Àû¿ë½ÃÅ²´Ù.
		createRoom();
		
		System.out.println("¹æ °³¼ö : " +room_list.size());
		
		// ÆĞ³ÎÀÇ º¯°æ»çÇ×À» Àû¿ëÇÏ±âÀ§ÇÑ ¸Ş¼Òµå
		gameRoomView.revalidate(); // ·¹ÀÌ¾Æ¿ô º¯È­¸¦ ÀçÈ®ÀÎ ½ÃÅ²´Ù.
		gameRoomView.repaint(); // removeAll()¿¡ ÀÇÇØ Á¦°Å µÈ ¿À·¡µÈ ÀÚ½ÄÀÇ ÀÌ¹ÌÁö¸¦ Áö¿ì´Â µ¥ ÇÊ¿äÇÏ´Ù.
	}
	
	// #50¸íÀÇ À¯Àú ¸®½ºÆ®¸¦ »ı¼ºÇÏ±â À§ÇÑ ¸Ş¼Òµå
	private void allocationUserInfo() {
		for(int i=0; i<userList.length; i++) {
			// ³»ºÎ Å¬·¡½º GameRoomPanel Å¬·¡½º¸¦ ÀÌ¿ëÇØ¼­ User List PanelÀ» »ı¼º
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
			// ¸¶¿ì½º ¸®½º³Ê Ãß°¡ ¿¹Á¤ => ¾÷µ¥ÀÌÆ® ¸Ş¼Òµå¿¡¼­ ÇØ¾ßÇÒµí?
//			grp.addMouseListener( new MouseAdapter() {
//			});
			userList[i] = grp;
			
			userListView.add(userList[i]);
		}
	}
	
	
	// #À¯Àú ¸®½ºÆ® ¾÷µ¥ÀÌÆ® ¸Ş¼Òµå - À¯Àú ¸®½ºÆ®¿¡ º¯µ¿ÀÌ »ı±â°ÔµÇ¸é ½ÇÇàÇÑ´Ù.
	private void updateUserInfo() {
		Font infoFont = new Font("Inconsolata",Font.BOLD,17); // ÆùÆ® ¼³Á¤
		// ±âÁ¸¿¡ ¶ç¿öµĞ ÆĞ³ÎÀ» Áö¿î´Ù.
		userListView.removeAll();
		
		for(int i=0; i<userList.length; i++) {
			// ³»ºÎ Å¬·¡½º GameRoomPanel Å¬·¡½º¸¦ ÀÌ¿ëÇØ¼­ User List PanelÀ» »ı¼º
			GameRoomPanel grp = new GameRoomPanel(userInfoPanelImage.getImage());
			// ¸¶¿ì½º ¸®½º³Ê Ãß°¡ ¿¹Á¤ => ¾÷µ¥ÀÌÆ® ¸Ş¼Òµå¿¡¼­ ÇØ¾ßÇÒµí?
//			grp.addMouseListener( new MouseAdapter() {
//			});
			
			userList[i] = grp;
			
			if(i<user_list.size()) {
				// JPanel,JLabelÀ» ¼±¾ğ ¹× ÇÒ´ç
				// JLabelÀ» ¼±¾ğ ¹× ÇÒ´ç
				JLabel userID_lb = new JLabel();
				// ÇöÀç Á¢¼ÓµÈ À¯ÀúÀÇ ¸®½ºÆ® ¸¸Å­ ÅØ½ºÆ®¸¦ ÁöÁ¤ÇØÁØ´Ù. (ÀúÀå°ªÀº À¯Àúid)
				// userID_lb ¼ÂÆÃ
				userID_lb.setFont(infoFont);
				userID_lb.setBounds(40, 1, 160, 30);
				userID_lb.setForeground(Color.black);
				userID_lb.setLayout(null);
				userList[i].add(userID_lb);
				// ÇöÀç Á¢¼ÓµÈ À¯ÀúÀÇ ¸®½ºÆ® ¸¸Å­ ÅØ½ºÆ®¸¦ ÁöÁ¤ÇØÁØ´Ù. (ÀúÀå°ªÀº À¯Àúid)
				UserInfo u = (UserInfo) user_list.get(i);
				userID_lb.setText(u.getUserID());
				// userID_lb ¼ÂÆÃ
				userList[i].add(userID_lb);
			}
			userListView.add(userList[i]);
		}
		
		// ÆĞ³ÎÀÇ º¯°æ»çÇ×À» Àû¿ëÇÏ±âÀ§ÇÑ ¸Ş¼Òµå
		userListView.revalidate(); // ·¹ÀÌ¾Æ¿ô º¯È­¸¦ ÀçÈ®ÀÎ ½ÃÅ²´Ù.
		userListView.repaint(); // removeAll()¿¡ ÀÇÇØ Á¦°Å µÈ ¿À·¡µÈ ÀÚ½ÄÀÇ ÀÌ¹ÌÁö¸¦ Áö¿ì´Â µ¥ ÇÊ¿äÇÏ´Ù.
	}
	
	// ÅØ½ºÆ® ÇÊµå ±ÛÀÚ ¼ö Á¦ÇÑÀ» À§ÇÑ Å¬·¡½º ¹× ¸Ş¼Òµå
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
	} // JTextFieldLimit class ³¡
	
	// Å° ÀÌº¥Æ®¸¦ ÁÖ±âÀ§ÇÑ Å¬·¡½º
	public class keyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// ¿£ÅÍ¸¦ ´©¸£¸é Àü¼ÛÀÌ µÇ°Ô ÇÏ±âÀ§ÇÑ ¸Ş¼Òµå
				String message = chatting_tf.getText();
				if(message.equals("")) { //¾Æ¹«°Íµµ ÀÔ·ÂÇÏÁö ¾Ê¾ÒÀ» ½Ã ¾Ë¸²Ã¢À» ¶ç¿ò
					JOptionPane.showMessageDialog(null, 
							"³»¿ëÀ» ÀÔ·ÂÇÏ½Ã±â ¹Ù¶ø´Ï´Ù.","¾Ë¸²",JOptionPane.NO_OPTION);
				} else {
					send_message("ChattingWR/"+id+"/"+message);
					chatting_tf.setText("");
				}
			}
		}
	} // keyAdapter class ³¡
	
	
	/* ¾Æ·¡ paint() ¸Ş¼Òµå´Â GUI ApplicationÀÌ ½ÇÇàµÇ°Å³ª 
	 * È°¼º/ºñÈ°¼ºÀ¸·Î ÀÎÇÑ º¯µ¿ ¿µ¿ªÀ» °¨ÁöÇßÀ»¶§, ½ÇÇàµÇ´Â ¸Ş¼ÒµåÀÌ´Ù. */
	
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
	
	
	public static void main(String[] args) {
		
	}
	
	
	// ¹æ¸¸µé±â ÇÁ·¹ÀÓ
	class CreateRoom extends JFrame{
	// TextField
		private JTextField roomTitel_tf = new JTextField(); // ¹æ Á¦¸ñ
		private JTextField roomPw_tf = new JTextField();  // ¹æ ºñ¹Ğ¹øÈ£
	
	// ComboBox
		private String[] state = {"°ø°³","ºñ°ø°³"};
		private Integer[] player = {2,4,6}; // ÃÖ´ëÀÎ¿ø 6¸íÀ¸·Î ¼³Á¤(°øÆòÇÑ ¹®Á¦¼ö ¹èºĞÀ» À§ÇØ 2,4,6¸í¸¸ ¹ŞÀ» ¿¹Á¤)
		private JComboBox<String> roomState_tf = new JComboBox<String>(state); // °ø°³/ºñ°ø°³ ¼³Á¤À» À§ÇÑ ÄŞº¸¹Ú½º
		private JComboBox<Integer> rPlayer_tf = new JComboBox<Integer>(player); // ÀÎ¿ø¼ö ¼³Á¤À» À§ÇÑ ÄŞº¸¹Ú½º
		
	// °¢Á¾ º¯¼ö º¯¼ö
		private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
		private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
		private int mouseX; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
		private int mouseY; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
		
	// Image
		// # CreateRoom ¹è°æ
		private Image crbackgroundImage = 
				new ImageIcon(Main.class.getResource("/images/CreateRoom.png")).getImage();
		// #¹æ¸¸µé±â ÇÁ·¹ÀÓ ³»ºÎ ¹öÆ°¿ë ÀÌ¹ÌÁö
		private ImageIcon crCancelBasicImage = new ImageIcon(Main.class.getResource("/images/cancelButtonBasic.png"));
		private ImageIcon crCancelEnteredImage = new ImageIcon(Main.class.getResource("/images/cancelButtonEntered.png")); 
		private ImageIcon createBasicImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonBasic.png"));
		private ImageIcon createEnteredImage = new ImageIcon(Main.class.getResource("/images/createRoomButtonEntered.png"));
		// Button
		private JButton cancelButton = new JButton(crCancelBasicImage); // Ãë¼Ò ¹öÆ°
		private JButton createButton = new JButton(createBasicImage); // ¹æ¸¸µé±â ¹öÆ°
		
		public CreateRoom() {
			Font font = new Font("Inconsolata",Font.PLAIN,11); 
			setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ) - ±â´É ¿Ï¼º ÈÄ Ãß°¡ ¿¹Á¤
			setSize(360,213); // nullÀº ÃÖ´ñ°ª
			setBackground(new Color(0,0,0,0));
			setAlwaysOnTop(true); // Ç×»ó ¸ğµç À©µµ¿ì À§¿¡ À§Ä¡ÇÏµµ·Ï ÇÔ
			setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
			setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
			setVisible(true); // À©µµ¿ì¸¦ º¼ ¼ö ÀÖÀ½.
			setLayout(null);	
			
			// ¸¶¿ì½º·Î Ã¢À» ¿òÁ÷ÀÏ ¼ö ÀÖ´Ù.
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				// #ÇØ´ç Ã¢À» µå·¡±× ½Ã, ¿òÁ÷ÀÏ ¼ö ÀÖ°Ô ÇÑ´Ù.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
		
		// TextField / ComboBox
			// # Á¦¸ñ ÀÔ·Â
			roomTitel_tf.setBounds(88,47,244,20);
			roomTitel_tf.setFont(font);
			roomTitel_tf.setDocument(new JTextFieldLimit(20)); // Á¦¸ñ 20ÀÚ Á¦ÇÑ 	 
			add(roomTitel_tf);
			
			// # °ø°³/ºñ°ø°³ »óÅÂ ÄŞº¸¹Ú½º
			roomState_tf.setBounds(88,78,78,20);
			roomState_tf.setFont(font);
			add(roomState_tf);
			roomState_tf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String state = roomState_tf.getSelectedItem().toString();
					if(state.equals("°ø°³")) {
						roomPw_tf.setEnabled(false);
					} else if (state.equals("ºñ°ø°³")) {
						roomPw_tf.setEnabled(true);
					}
				}
			});
			
			// # ¹æ ºñ¹Ğ¹øÈ£ ÀÔ·Â(ÄŞº¸¹Ú½º ÀÌº¥Æ®¿¡ µû¶ó È°¼³/ºñÈ°¼º)
			roomPw_tf.setBounds(88,103,78,20);
			roomPw_tf.setFont(font);
			roomPw_tf.setDocument(new JTextFieldLimit(10)); // ºñ¹Ğ¹øÈ£ 10ÀÚ Á¦ÇÑ 	
			roomPw_tf.setEnabled(false); // ÃÊ±â¿¡ "°ø°³"¼³Á¤ÀÌ±â¶§¹®¿¡ ºñÈ°¼º »óÅÂ·Î µĞ´Ù.
			add(roomPw_tf);
			
			// # ÀÎ¿ø¼ö ¼³Á¤ ÄŞº¸¹Ú½º
			rPlayer_tf.setBounds(88,128,78,20);
			roomPw_tf.setFont(font);
			add(rPlayer_tf);
			
			
		// Button
			// #¯M¼Ò ¹öÆ°
			cancelButton.setBounds(187, 180, 72, 24);
			add(cancelButton);
			cancelButton.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					cancelButton.setIcon(crCancelEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
					cancelButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
					if(muteSel==false) {
						buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
						buttonEnteredBGM.start();
					}
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					cancelButton.setIcon(crCancelBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
					cancelButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); 
					}
				}
			});
			
			// #¸¸µé±â ¹öÆ°
			createButton.setBounds(101, 180, 72, 24);
			add(createButton);
			createButton.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					createButton.setIcon(createEnteredImage); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
					createButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
					if(muteSel==false) {
						buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
						buttonEnteredBGM.start();
					}
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					createButton.setIcon(createBasicImage); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
					createButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
				// ¹öÆ°À» ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						// ¸¸µé±â ¹öÆ°À» ´©¸£¸é ¼³Á¤»çÇ×À» ±×´ë·Î ¼­¹ö¿¡ Àü¼ÛÇÑ´Ù.
						room_name = roomTitel_tf.getText().trim(); // ¹æ Á¦¸ñ
						String state = roomState_tf.getSelectedItem().toString(); // °ø°³/ºñ°ø°³
						roomPW = null; // ¹æ ºñ¹Ğ¹øÈ£ (°ø°³¸é null·Î, ºñ°ø°³¸é ÀÔ·Â¹ŞÀº ÆĞ½º¿öµå¸¦ ÀúÀå)
						if(state.equals("ºñ°ø°³")) {
							roomPW = roomPw_tf.getText().trim();
						} 
						fixed_User = Integer.parseInt(rPlayer_tf.getSelectedItem().toString()); // rPlayer_tfÀÇ Á¦³×¸¯À» Integer·Î ÇØ³õÀ½
						
						// ºñ°ø°³ ¼³Á¤À» ÇÏ¿´´Âµ¥µµ roomPW¿¡ °ø¶õÀ» ÀÔ·ÂÇßÀ» °æ¿ì ¾Ë¸²Ã¢À» ¶ç¿î´Ù.
							if( state.equals("ºñ°ø°³") && roomPW.equals("")) {
		                     /*
		                      *  ¸ğ´ŞÃ¢ÀÌ ¾ÈµÇ³×? ´Ù¸¥¹æ¹ı ½á¾ßÇÒµí..
		                      */
//		                     JOptionPane.showMessageDialog(null, 
//		                           "ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇÏ½Ã±â ¹Ù¶ø´Ï´Ù.","¾Ë¸²",JOptionPane.NO_OPTION);
		                  } else {
		                     // ÀÔ·Â¹ŞÀº °ªÀ» ¼­¹ö¿¡ Àü¼ÛÇÑ´Ù.
		                     send_message("CreateRoom/"+id+"/"+room_name+"/"+state+"/"+roomPW+"/"+fixed_User);
		                     
		                     /* ÇØ´ç °ªÀ» °¡Áö°í RoomInfo °´Ã¼¸¦ »ı¼ºÇÑ´Ù. ÀÌ¶§, ¹æ¹øÈ£´Â 0¹øÀ¸·Î ÃÊ±âÈ­ÇÏ¿© »ı¼º
		                      * ÀÌ¶§, room_list¿¡´Â µî·ÏÇÏÁö ¾Ê´Â´Ù.(¹æ ¹øÈ£¸¦ ÇÒ´ç ¹Ş°í µî·Ï)    */
		                     roomInfo = new RoomInfo(0,room_name, roomPW, fixed_User, userInfo); 
		                     dispose();
		                  }
					}
				}
			});
			
		}
		
		
		@Override
		public void paint(Graphics g) {
			viewImage = createImage(360,213);
			viewGraphics = viewImage.getGraphics();
			screenDraw(viewGraphics);
			g.drawImage(viewImage,0,0, null);
		}
		
		public void screenDraw(Graphics g) {
			g.drawImage(crbackgroundImage, 0, 0, null);
			paintComponents(g);
			this.repaint();
		}	
	}
	
	
	class inputPw extends JFrame {
//		JPanel inputPW = new GameRoomPanel(inputPwPanelImage.getImage());
		private JLabel inputPW_lb;
		private JButton inputPW_bt;
		private JButton cancel_bt;
		private JPasswordField inputPW_tf;
		
		// °¢Á¾ º¯¼ö º¯¼ö
//		private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
//		private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
		private String input_Pw; // ¹æ ºñ¹Ğ¹øÈ£ 
		
		public inputPw(int room_No, String room_Pw) {
			setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ) - ±â´É ¿Ï¼º ÈÄ Ãß°¡ ¿¹Á¤
			setSize(1024,768); // nullÀº ÃÖ´ñ°ª
			setBackground(new Color(40,40,40,40));
//			setAlwaysOnTop(true); // Ç×»ó ¸ğµç À©µµ¿ì À§¿¡ À§Ä¡ÇÏµµ·Ï ÇÔ
			setPreferredSize(new Dimension(1024,768));
			setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
			setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
			setVisible(true); // À©µµ¿ì¸¦ º¼ ¼ö ÀÖÀ½.
			setLayout(null);

			inputPW_lb = new JLabel();
			inputPW_lb.setBounds(430, 310,200,30);
			inputPW_lb.setText("ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä.");
			inputPW_lb.setFont(wrFont);
			add(inputPW_lb);
			
			inputPW_tf  = new JPasswordField();
			inputPW_tf.setBounds(410, 350,200,30);
			inputPW_tf.setBorder(null); // Å×µÎ¸® Á¦°Å
			inputPW_tf.setBackground(new Color(40,40,40,40)); // ¹è°æ ¹İÅõ¸í
			inputPW_tf.setEchoChar('*'); // È­¸é¿¡ Ç¥±âÇÒ ¹®ÀÚ¸¦ '*'·Î ÁöÁ¤
			inputPW_tf.setFont(wrFont);
			add(inputPW_tf);
			
			
			inputPW_bt = new JButton();
			inputPW_bt.setText("ÀÔ·Â");
			inputPW_bt.setBounds(412, 400,80,30);
			inputPW_bt.setBorder(null); // Å×µÎ¸® Á¦°Å
			inputPW_bt.setBackground(new Color(0,0,0,0)); // ¹è°æ Åõ¸í
			inputPW_bt.setFont(wrFont);
			add(inputPW_bt);
			inputPW_bt.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					inputPW_bt.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
					if(muteSel==false) {
						buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
						buttonEnteredBGM.start();
					}
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					inputPW_bt.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
//				
				// ÇØ´ç ¹öÆ°À» Å¬¸¯ÇßÀ» ¶§
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						// JPasswordField´Â getText()¸Ş¼Òµå¸¦ ±ÇÇÏÁö ¾Ê´Â´Ù ÇÏ¿© ¾Æ·¡¿Í °°Àº ¹æ¹ıÀ¸·Î ÀúÀå
						input_Pw = "";
						char[] tempPw = inputPW_tf.getPassword();
						for (char a : tempPw) {
							input_Pw += a;
						}

						// pw¿Í ÀÔ·ÂÇÑ °ªÀÌ °°À¸¸é
						if (room_Pw.equals(input_Pw)) {
							// ¹æ¿¡ ÀÔÀåÇÔÀ» ¼­¹ö¿¡°Ô ¾Ë¸°´Ù.
							send_message("PassPW/" + userInfo.getUserID() + "/" + room_No);
							// WaitingRoom Ã¢À» Á¾·áÇÑ´Ù.
							dispose();
						} else {
							// ºñ¹Ğ¹øÈ£°¡ ÀÏÄ¡ÇÏÁö ¾ÊÀ» ¶§
							JOptionPane.showMessageDialog(null, "ºñ¹Ğ¹øÈ£°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.", "¾Ë¸²", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			
			
			cancel_bt = new JButton();
			cancel_bt.setText("Ãë¼Ò");
			cancel_bt.setBounds(532, 400,80,30);
			cancel_bt.setBorder(null); // Å×µÎ¸® Á¦°Å
			cancel_bt.setBackground(new Color(0,0,0,0)); // ¹è°æ Åõ¸í
			cancel_bt.setFont(wrFont);
			add(cancel_bt);
			cancel_bt.addMouseListener(new MouseAdapter() {
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
				@Override
				public void mouseEntered(MouseEvent e) {
					cancel_bt.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
					if(muteSel==false) {
						buttonEnteredBGM = new Music("buttonEnteredBGM.mp3", false);
						buttonEnteredBGM.start();
					}
				}
				
				// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
				@Override  
				public void mouseExited(MouseEvent e) {
					cancel_bt.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
				}
				
//				// ¹öÆ°À» Å¬¸¯ÇÒ ½Ã	
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()==1) {
						dispose(); // Ã¢ Á¾·á
					}
				}
			});
			
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(inputPwPanelImage.getImage(), 262, 268, null);
			paintComponents(g);
			this.repaint();
		}
	}
	
	
	// °ÔÀÓ¹æ&À¯Àúinfo ÇÏ³ªÇÏ³ª¸¦ JPanelÀ» »ó¼Ó¹ŞÀº GameRoomPanel Å¬·¡½º·Î »ı¼ºÇÑ´Ù.
	class GameRoomPanel extends JPanel{
		private Image img;
		private RoomInfo roomInfo;
		
		public GameRoomPanel(Image img) {
			this.img = img;
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // nullÀº ÃÖ´ñ°ª
			setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
			setOpaque(false);
			setLayout(null); // ÆĞ³Î¿¡ Ãß°¡ÇÏ´Â ¿ä¼ÒµéÀÇ À§Ä¡¸¦ ÀÚÀ¯·Ó°Ô ¼³Á¤ÇÏ±â À§ÇØ LayoutÀ» null·Î ÇØÁØ´Ù.
		}
		
		// ÆĞ³ÎÀ» ¿­¾úÀ» ¶§ ÀÚµ¿À¸·Î ÀÌ¹ÌÁö¸¦ ±×·ÁÁÖ´Â ¸Ş¼Òµå
		public void paintComponent(Graphics g)  {
			g.drawImage(img, 0, 0, null);
		}
		
		// ÀÌ¹ÌÁö¸¦ ¹Ù²ãÁÖ±â À§ÇÑ ¸Ş¼Òµå
		public void setGRImage(Image img) {
			this.img = img;
		}
		
		public void setRoomInfo(RoomInfo roomInfo) {
			this.roomInfo = roomInfo;
		}
		
		public RoomInfo getRoomInfo() {
			return roomInfo;
		}
	}
	
	// RoomInfoÀÇ Vector¸¦ ÀçÁ¤·ÄÇÏ±â À§ÇÑ Å¬·¡½º(¹æ¹øÈ£¸¦ ±âÁØÀ¸·Î ÀçÁ¤·Ä)
	class AscendingRoomInfo implements Comparator<RoomInfo> {

		@Override
		public int compare(RoomInfo o1, RoomInfo o2) {
			//¹æ¹øÈ£ »ı¼º ·ÎÁ÷¿¡¼­´Â Áßº¹À» Çã¿ëÇÏÁö ¾Ê±â ¶§¹®¿¡ µÎ °¡Áö¸¸ »ı°¢ÇÑ´Ù.
			if(o1.getRoom_No() > o2.getRoom_No()) return 1;
			else return -1;
		} 
	}
	
	
	
	
	
	
	
	
	
	
	
//	/====================================================================/
	

	
}
