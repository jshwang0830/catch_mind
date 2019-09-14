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
	private JTextField id_tf; // ID¸¦ ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ® ÇÊµå
	private JPasswordField pw_tf; // PW¸¦ ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ® ÇÊµå
	private JPasswordField pwRe_tf; 
	private JTextField name_tf; // ÀÌ¸§¸¦ ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ®ÇÊµå
	private JTextField dateOfBirth_tf; // »ı³â¿ùÀÏÀ» ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ®ÇÊµå
	private JTextField eMail_tf; // ÀÌ¸ŞÀÏÀ» ÀÔ·Â¹Ş±â À§ÇÑ ÅØ½ºÆ®ÇÊµå

// Network ÀÚ¿ø º¯¼ö
	private Socket socket;// »ç¿ëÀÚ ¼ÒÄÏ
	private int port; // Æ÷Æ®¹øÈ£
	

// °¢Á¾ º¯¼ö
	private Image viewImage; // ÀÌ¹ÌÁö ÀúÀå¿ë º¯¼ö
	private Graphics viewGraphics; // ±×·¡ÇÈ ÀúÀå¿ë º¯¼ö	
	private int mouseX; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
	private int mouseY; // ¸¶¿ì½º ÁÂÇ¥ º¯¼ö
	private DataOutputStream dos = null; 
	//Ä¿¼­
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Image cursorBasic = tk.getImage(Main.class.getResource("/images/pencilCursor.png"));
	private Image cursorClicked = tk.getImage(Main.class.getResource("/images/pencilCursorClicked.png"));
	private Cursor myCursor = tk.createCustomCursor(cursorBasic, new Point(10,10), "Pencil Cursor");
	private Cursor myCursorClicked = tk.createCustomCursor(cursorClicked, new Point(10,10), "Pencil Cursor Clicked");
	

// È¸¿ø°¡ÀÔ ÀÔ·ÂÁ¤º¸
	private String id="";
	private String pw="";
	private String pwRe=""; // ÆĞ½º¿öµå È®ÀÎ¿ë
	private String name="";
	private String dateOfBirth=""; // »ı³â¿ùÀÏ(19990101)
	private String eMail="";
	private int age; // dateOfBirth·Î °è»êÇÏ¿© ÀúÀå
	private char gender;

//Image	
	// #MainView ¹è°æ
	private Image backgroundImage = 
			new ImageIcon(Main.class.getResource("/images/loginmain.png")).getImage();
			//Main Å¬·¡½ºÀÇ À§Ä¡¸¦ ±âÁØÀ¸·Î ÀÌ¹ÌÁö ÆÄÀÏÀÇ À§Ä¡¸¦ Ã£Àº ´ÙÀ½¿¡ ÀÌ¹ÌÁö ÀÎ½ºÅÏ½º¸¦ ÇØ´ç º¯¼ö¿¡ ÃÊ±âÈ­ ÇØÁÜ(»ó´ë°æ·Î °°Àº Àı´ë°æ·Î)
 

//Button
	ImageIcon exitBasic = new ImageIcon(Main.class.getResource("/images/loginExitButtonBasic.png"));
	ImageIcon exitEntered = new ImageIcon(Main.class.getResource("/images/loginExitButtonEntered.png"));
	private JButton exitButton = new JButton(exitBasic); // ³ª°¡±â ¹öÆ°
	ImageIcon joinBasic = new ImageIcon(Main.class.getResource("/images/loginJoinButtonBasic.png"));
	ImageIcon joinEntered = new ImageIcon(Main.class.getResource("/images/loginJoinButtonEntered.png"));
	private JButton joinButton = new JButton(joinBasic); // °¡ÀÔ ¹öÆ°	
	
	ImageIcon checkBasic = new ImageIcon(Main.class.getResource("/images/checkButtonBasic.png"));
	ImageIcon checkEntered = new ImageIcon(Main.class.getResource("/images/checkButtonEntered.png"));
	private JButton idCheck = new JButton(checkBasic);
	
	
	
	public JoinView() {
		//½ÇÇà°ú µ¿½Ã¿¡ socket,port,ID¸¦ MainView·ÎºÎÅÍ ÀÌ¾î¹Ş¾Æ¿Â´Ù.
		socket = MainView.getSocket();
		port = MainView.getPort();
		dos = MainView.getDos(); // MainView Output½ºÆ®¸²À» ÀÌ¾î ¹Ş¾Æ¿Â´Ù.
		
		
		Font font = new Font("Inconsolata",Font.BOLD,15);
	// JFrame mainView
		setUndecorated(true); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù Á¦°Å(À©µµ¿ì¸¦ Á¦°ÅÇÔ) - ±â´É ¿Ï¼º ÈÄ Ãß°¡ ¿¹Á¤
		setTitle("Catch Mind"); // ÇÁ·¹ÀÓ Å¸ÀÌÆ² ¹Ù ÀÌ¸§(Å¸ÀÌÆ² ¹Ù¸¦ ¾ø¾Ù ¿¹Á¤ÀÌ±â ¶§¹®¿¡ ¾ø¾îµµ µÇ´Â ÄÚµå)
		setSize(342, 405);
		setResizable(false); // ÇÁ·¹ÀÓ Å©±â °íÁ¤
		setLocationRelativeTo(null); // À©µµ¿ì¸¦ È­¸é Á¤Áß¾Ó¿¡ ¶ç¿ì±â À§ÇÔ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // À©µµ¿ì Á¾·á½Ã ³²¾ÆÀÖ´Â ÇÁ·Î¼¼½ºµµ ±ú²ıÇÏ°Ô Á¾·áÇÏ±â À§ÇÔ
		setBackground(new Color(0,0,0,0)); // ¹è°æ»öÀ» Åõ¸íÇÏ°Ô ÇÑ´Ù.(paint()¸Ş¼Òµå·Î ±×¸®´Â ¹è°æÀ» º¸ÀÌ°Ô ÇÏ±â À§ÇÔ)
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
			// #¸Å´º¹Ù µå·¡±× ½Ã, ¿òÁ÷ÀÏ ¼ö ÀÖ°Ô ÇÑ´Ù.
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - mouseX, y - mouseY);
			}
		});
 
		// Áßº¹È®ÀÎ¹öÆ°
		idCheck.setBounds(255, 62, 34, 17);
		idCheck.setBorder(null);
		idCheck.setBackground(new Color(0, 0, 0, 0));
		add(idCheck);
		idCheck.addMouseListener(new MouseAdapter() {
 
			@Override
			public void mouseEntered(MouseEvent e) {
				idCheck.setIcon(checkEntered); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				idCheck.setCursor(myCursorClicked);
 
			}
 
			@Override
			public void mouseExited(MouseEvent e) {
				idCheck.setIcon(checkBasic); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				idCheck.setCursor(myCursor);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(e.getButton() == 1) {
					if(id_tf.getText().length()<6) {
						JOptionPane.showMessageDialog(null,"¾ÆÀÌµğ´Â 6~12ÀÚ¸¸ ³ÖÀ» ¼ö ÀÖ½À´Ï´Ù.");
					} else {
						Boolean idCheckStatus=false; // ¸¶¿ì½º ÁÂÃø Å¬¸¯ ÇÒ¶§¸¶´Ù false·Î ÃÊ±âÈ­
						String idCheck=id_tf.getText(); // ID ÀÔ·ÂÃ¢(TextField)¿¡¼­ °ª °¡Á®¿Í¼­ idCheck¿¡ ÀúÀå
						String existId = ""; // DB¿¡ ÀúÀåµÈ ¾ÆÀÌµğ ÀúÀåÇÒ ¹®ÀÚ¿­ º¯¼ö
						System.out.println("ÀÔ·ÂÇÑ ¾ÆÀÌµğ:"+idCheck);	// ÀÔ·ÂÇÑ ID Ãâ·Â
						Vector allUser=new UserController().dataLoadAll(); // userDB.dat¿¡¼­ ¸ğµç Á¤º¸ °¡Á®¿Í¼­ Vector allUser º¯¼ö¿¡ ÀúÀå
						
						for(int i=0; i<allUser.size(); i++) { // allUser(DB¿¡¼­ °¡Á®¿Â ¸ğµç »ç¿ëÀÚ Á¤º¸) ¸¸Å­ ¹İº¹¹® ½ÇÇà
							existId=((User)allUser.get(i)).getId(); // allUserÀÇ 0¹øÂ°ºÎÅÍ Â÷·Ê´ë·Î id °¡Á®¿Í¼­ existId¿¡ ÀúÀå
							System.out.println(i+"¹øÂ° ÀÎµ¦½º¿¡ Á¸ÀçÇÏ´Â ¾ÆÀÌµğ:"+existId);
							if(idCheck.equals(existId)) { // ¾ÆÀÌµğ°¡ Áßº¹µÇ¸é
								idCheckStatus=true;	// idCheckStatus true·Î ¹Ù²Ù±â(´ëÀÔ)
								break; // Áßº¹µÇ´Â ID°¡ ÀÖÀ¸¸é for¹®(¹İº¹¹®) ³ª°¡±â(Á¾·á), ´õ ÀÌ»ó ºñ±³ÇÒ ÇÊ¿ä°¡ ¾øÀ¸¹Ç·Î!!
							}
						}
						
						if(idCheckStatus) { // À§ÀÇ for¹®¿¡¼­ ºñ±³ÇÏ¿© idCheckStatus°¡ trueÀÌ¸é (¾ÆÀÌµğ°¡ Áßº¹µÇ¸é)
							JOptionPane.showMessageDialog(null, "Áßº¹µÈ ¾ÆÀÌµğÀÔ´Ï´Ù. \n´Ù¸¥ ¾ÆÀÌµğ¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä.");
						}else { // À§ÀÇ for¹®¿¡¼­ ºñ±³ÇÏ¿© idCheckStatus°¡ falseÀÌ¸é (¾ÆÀÌµğ°¡ Áßº¹µÇ´Â °ÍÀÌ ¾øÀ¸¸é)
							JOptionPane.showMessageDialog(null, "»ç¿ë °¡´ÉÇÑ ¾ÆÀÌµğ ÀÔ´Ï´Ù.");
						}
					}
				}
			}
			
		});
		
	// TextField
		//ID ÀÔ·Â
		id_tf = new JTextField();
		id_tf.setBounds(165, 55, 138, 30);
		id_tf.setBorder(null);
		id_tf.setBackground(new Color(0,0,0,0));
		add(id_tf);
		id_tf.setDocument(new JTextFieldLimit(12)); //¾ÆÀÌµğ ÃÖ´ë 12ÀÚ Á¦ÇÑ
		
		//PW ÀÔ·Â
		pw_tf = new JPasswordField();
		pw_tf.setBounds(165, 96, 138, 30);
		pw_tf.setBorder(null);
		pw_tf.setBackground(new Color(0,0,0,0));
		add(pw_tf);
		pw_tf.setDocument(new JTextFieldLimit(12)); // ºñ¹Ğ¹øÈ£ ÃÖ´ë 12ÀÚ Á¦ÇÑ
		
		//PW RE
		pwRe_tf = new JPasswordField();
		pwRe_tf.setBounds(164, 137, 138, 30);
		pwRe_tf.setBorder(null);
		pwRe_tf.setBackground(new Color(0,0,0,0));
		add(pwRe_tf);
		pwRe_tf.setDocument(new JTextFieldLimit(12));
		
		//165, 137, 138, 30
		//ÀÌ¸§ ÀÔ·Â
		name_tf = new JTextField();
		name_tf.setBounds(165, 178, 138, 30);
		name_tf.setBorder(null);
		name_tf.setBackground(new Color(0,0,0,0));
		add(name_tf);
		name_tf.setDocument(new JTextFieldLimit(5)); //ÀÌ¸§ ÃÖ´ë 5ÀÚ Á¦ÇÑ
		
		//»ı³â¿ùÀÏ ÀÔ·Â
		dateOfBirth_tf = new JTextField();
		dateOfBirth_tf.setBounds(165, 221, 138, 30);
		dateOfBirth_tf.setBorder(null);
		dateOfBirth_tf.setBackground(new Color(0,0,0,0));
		add(dateOfBirth_tf);
		dateOfBirth_tf.setDocument(new JTextFieldLimit(6)); //»ı³â¿ùÀÏ ÃÖ´ë 6ÀÚ Á¦ÇÑ

		
		//e-Mail ÀÔ·Â
		eMail_tf = new JTextField();
		eMail_tf.setBounds(165, 262, 138, 30);
		eMail_tf.setBorder(null);
		eMail_tf.setBackground(new Color(0,0,0,0));
		add(eMail_tf);
		eMail_tf.setDocument(new JTextFieldLimit(30)); //ÀÌ¸ŞÀÏ ÃÖ´ë 30ÀÚ Á¦ÇÑ
	
	// RadioButton
		//¼ºº° ¼±ÅÃ
		JRadioButton  genderMale = new JRadioButton("³²"); // JRadioButton »ı¼º
		genderMale.setFont(font); 
		JRadioButton  genderFemale = new JRadioButton("¿©"); 
		genderFemale.setFont(font);
		ButtonGroup  genderGroup = new ButtonGroup(); //¶óµğ¿À¹öÆ° ±×·ìÈ­¸¦ À§ÇÑ ¹öÆ°±×·ì ¼³Á¤. °°Àº ±×·ì³¢¸®´Â ±×·ìÁß¿¡ 1°³¸¸ ¼±ÅÃµÈ´Ù.
		genderGroup.add(genderMale);  
		genderGroup.add(genderFemale); //±×·ì¿¡ ±×·ìÈ­½ÃÅ³ ¹öÆ°µéÀ» Ãß°¡	
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
		// #³ª°¡±â ¹öÆ°
		exitButton.setBounds(210, 350, 88, 35);
		exitButton.setBorder(null);
		exitButton.setBackground(new Color(0,0,0,0));
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§ ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEntered); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				exitButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}
			
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡¼­ ¶¼¾úÀ»¶§ ÀÌº¥Æ®
			@Override  
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasic); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				exitButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			
			// ¸¶¿ì½º·Î ¹öÆ°À» ´­·¶À» ¶§ ÀÌº¥Æ®
			@Override 
			public void mousePressed(MouseEvent e) {
				dispose(); // ÇÏ³ªÀÇ ÇÁ·¹ÀÓ¸¸ Á¾·áÇÏ±â À§ÇÑ ¸Ş¼Òµå
			}
		});
		
		
		
		// #°¡ÀÔ ¹öÆ°
		joinButton.setBounds(60, 350, 88, 35);
		joinButton.setBorder(null);
		joinButton.setBackground(new Color(0,0,0,0));
		add(joinButton);
		joinButton.addMouseListener(new MouseAdapter() {
			// ¸¶¿ì½º¸¦ ¹öÆ°¿¡ ¿Ã·Á³ùÀ» ¶§(µé¾î¿ÔÀ» ¶§) ÀÌº¥Æ®
			@Override
			public void mouseEntered(MouseEvent e) {
				joinButton.setIcon(joinEntered); // ¸¶¿ì½º¸¦ ¿Ã·Á³ùÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Entered Image)
				joinButton.setCursor(myCursorClicked); // ¸¶¿ì½º Ä¿¼­¸¦ ¼Õ¸ğ¾ç Ä¿¼­·Î º¯°æ
			}
			
			// ¸¶¿ì½º°¡ ¹öÆ°¿¡¼­ ³ª°¬À» ¶§(¹ş¾î ³µÀ» ¶§) ÀÌº¥Æ®
			@Override  
			public void mouseExited(MouseEvent e) {
				joinButton.setIcon(joinBasic); // ¸¶¿ì½º¸¦ ¶¼¾úÀ»¶§ ÀÌ¹ÌÁö º¯°æ(Basic Image)
				joinButton.setCursor(myCursor); // ¸¶¿ì½º Ä¿¼­¸¦ ±âº» Ä¿¼­·Î º¯°æ
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String emailCheck = "^[a-zA-Z0-9]+@[a-zA-Z0-9]*+.([a-zA-Z0-9])+$";
				String nuII = "^[\\s]*$";
				String dateCheck = "^[0-9]*$";
				String pwCheck = "^[a-zA-Z0-9]*$";
				String nameCheck = "^[°¡-ÆR]*$";
				String idCheck = "^[a-zA-Z0-9]*$";
				
				boolean ch = false;
				boolean formatcheck = true;
				String msg = null;
				
				// È¸¿ø °¡ÀÔ ÀÔ·Â °ªÀ» º¯¼öµé¿¡ ÀúÀå
				
				// ¾ÆÀÌµğ Format check
				id=id_tf.getText();
				if(formatcheck && id.matches(nuII)) {
					msg = "¾ÆÀÌµğ¿¡ °ø¹éÀ» ³ÖÀ» ¼ö ¾ø½À´Ï´Ù.";
					formatcheck = false;
				} else if (formatcheck && id.length()<6) {
					msg = "¾ÆÀÌµğ´Â 6~12ÀÚ¸¸ ³ÖÀ» ¼ö ÀÖ½À´Ï´Ù.";
					formatcheck = false;
				} else if (formatcheck && !id.matches(idCheck)) {
					msg = "¾ÆÀÌµğ´Â ¿µ¹® ¹× ¼ıÀÚ¸¸ ³ÖÀ» ¼ö ÀÖ½À´Ï´Ù.";
					formatcheck = false;
				}
				
				//  ºñ¹Ğ¹øÈ£ Format check
				char[] tempPw = pw_tf.getPassword();
				for(char a : tempPw) {
					pw += a;
				}
				char[] tempPwRe = pwRe_tf.getPassword();
				for(char a : tempPwRe) {
					pwRe += a;
				}
				if(formatcheck && (pw.length()<6 || !pw.matches(pwCheck) || pw.matches(nuII))) {
					msg = "ÆĞ½º¿öµå´Â 6~12ÀÚ ¼Ò/´ë¹®ÀÚ, ¼ıÀÚ¸¸ ³ÖÀ» ¼ö ÀÖ½À´Ï´Ù.";
					formatcheck = false;
				} else if(formatcheck && !pw.equals(pwRe)) {
					msg = "ÆĞ½º¿öµå°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.";
					formatcheck = false;
				}  
				
				
				name=name_tf.getText();
				if(formatcheck && name.matches(nuII)) {
					msg = "ÀÌ¸§¿¡ °ø¹éÀ» ³ÖÀ» ¼ö ¾ø½À´Ï´Ù.";
					formatcheck = false;
				} else if (formatcheck && !name.matches(nameCheck)) {
					msg = "ÀÌ¸§Àº ÇÑ±Û¸¸ »ç¿ë °¡´ÉÇÕ´Ï´Ù. (ex.È«±æµ¿)";
					formatcheck = false;
				}
				
				dateOfBirth=dateOfBirth_tf.getText();
				if(formatcheck && dateOfBirth.matches(nuII)) {
					msg = "»ı³â¿ùÀÏ¿¡ °ø¹éÀ» ³ÖÀ» ¼ö ¾ø½À´Ï´Ù.";
					formatcheck = false;
				} else if(formatcheck && !dateOfBirth.matches(dateCheck)) {
					msg = "¼ıÀÚ Çü½ÄÀ¸·Î ÀÔ·ÂÇÏ½Ê½Ã¿À. (ex.901010)";
					formatcheck = false;
				}
				
				eMail=eMail_tf.getText();
				if(formatcheck && !eMail.matches(emailCheck)) {
					msg = "ÀÌ¸ŞÀÏ Çü½Ä¿¡ ¸Â°Ô ÀÔ·ÂÇÏ½Ê½Ã¿À.";
					formatcheck = false;
				}
				
				if(genderMale.isSelected()) {
					gender = (genderMale.getText()).charAt(0);
				} else if (genderFemale.isSelected()) {
					gender = (genderFemale.getText()).charAt(0);
				}
				
				
				if(formatcheck) {
					// ÀÔ·Â¹ŞÀº Á¤º¸¸¦ ¼­¹ö·Î º¸³½´Ù.
					send_message("JoinRequest/"+id +"/"+ pw +"/" + name+"/" + dateOfBirth+"/" + eMail+"/" + gender);
					JOptionPane.showMessageDialog(null, "Á¤»óÀûÀ¸·Î °¡ÀÔµÇ¾ú½À´Ï´Ù.");
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
	
	
	
	/* ¾Æ·¡ paint() ¸Ş¼Òµå´Â GUI ApplicationÀÌ ½ÇÇàµÇ°Å³ª 
	 * È°¼º/ºñÈ°¼ºÀ¸·Î ÀÎÇÑ º¯µ¿ ¿µ¿ªÀ» °¨ÁöÇßÀ»¶§, ½ÇÇàµÇ´Â ¸Ş¼ÒµåÀÌ´Ù. */
	
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