package kh.mini.project.main.SERVER;

// Github Ǫ���Ҷ� �ڵ����� �빮�ڷ� �ٲ� �׳� �빮�ڷ� ����..
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import kh.mini.project.GameRoom.Question;
import kh.mini.project.db.UserController;
import kh.mini.project.main.view.Main;
import kh.mini.project.model.vo.User;

public class MainServer extends JPanel {
   private static final long serialVersionUID = 1216070372320522836L;
// Frame, Panel
   private JScrollPane statusView; // ��Ʈ�� ���� ���� ����â ��ũ����
   private JTextArea statusArea = new JTextArea(); // statusView�� ���� �ؽ�Ʈ �����
   private JTextArea userListArea = new JTextArea(); // userListView�� ���� �ؽ�Ʈ �����
   private JScrollPane allUserListView = new JScrollPane(); // ��� ���� ����Ʈ�� ���̰� �� ��ũ����
   private JScrollPane loginUserListView = new JScrollPane(); // ���� ���� ����Ʈ�� ���̰� �� ��ũ����

// Label
   private JLabel mainMenuBar = new JLabel();

// List 
   private JList<UserInfo> userList = new JList<UserInfo>(); // �̰� �׳� ��� ���� ó���ϴ°����� ����/������ ���¸� �߰��ؼ� ó���ϴ°͵� ����ϵ� �ϴ�.
   private JList<RoomInfo> roomList = new JList<RoomInfo>(); // ���� ����/���ȣ/�ο�/��й�ȣ ǥ��?

// Textfield
   private JTextField port_tf; // ��Ʈ��ȣ�� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
   private JTextField allUserSearch_tf; // ��ü ���� ���̺��� �˻��� �ϱ� ���� �ؽ�Ʈ �ʵ�
   private JTextField loginUserSearch_tf; // ���� ���� ���̺��� �˻��� �ϱ� ���� �ؽ�Ʈ �ʵ�

// Table
   private DefaultTableModel allUserModel;
   private DefaultTableModel loginUserModel;
   private JTable allUserInfo_Table = new JTable();
   private JTable loginUserInfo_Table = new JTable();
   private static String[] allUserRow = { "ID", "Password", "�̸�", "�������", "����", "E-mail", "Level", "Exp", "���� ���� ����" };
   private static String[] loginUserRow = {"ID", "Level", "Exp", "���� ���� ����", "�������� ��"};
   private static Object[][] allUsercolumn;
   private static Object[][] loginUsercolumn;

// Network �ڿ�
   private ServerSocket server_Socket; // ���� ����
   private Socket socket; // ����ڷκ��� ���� ����
   private int port; // ��Ʈ��ȣ

// ���� ����
   private Image viewImage; // �̹��� ����� ����
   private Graphics viewGraphics; // �׷��� ����� ����
   private boolean portConection = false; // ��Ʈ ���� ���� �� MainView ���ῡ ���� ����
   private boolean connectCk = false; // �α��ο� ���� ����
   /* Multi Thread ȯ�濡���� Vector�� �� �����Ű��Ƽ� Vector�� ��߰ڴ�. */
   private Vector<User> allUser_vc = new Vector<User>(); // ��� ȸ���� ������ ��Ƶδ� Vector
   private Vector<UserInfo> user_vc = new Vector<UserInfo>(); // ����� Vector
   private Vector<UserInfo> wRoom_vc = new Vector<UserInfo>(); // ���� ����� Vector => ��������/�ΰ��� ���� �������ϴϱ� �����ؾ���.
   private Vector<RoomInfo> room_vc = new Vector<RoomInfo>(); // ���ӹ� Vector(���� ����Ʈ ����)
   private StringTokenizer st; // �������� ������ ���� �ʿ���. �������� �Է¹��� �޽����� �и��ϴµ� ����.
   private boolean scrollpanemove = false; // ��ũ�� ���ο� ���Ǵ� ����(��ũ�� ��� ����)
   private Intro intro;
   private MainServer main_Server;
   
//Image   
   // #MainView ���
   private Image mainBackgroundImage = new ImageIcon(Main.class.getResource("/images/ServerMainViewBackground.png")).getImage();
   private ImageIcon exitBasicImage = new ImageIcon(Main.class.getResource("/images/exit.png"));
   private ImageIcon exitEnteredImage = new ImageIcon(Main.class.getResource("/images/exite.png"));

//Button
   private JButton exitButton = new JButton(exitBasicImage); // ������ ��ư

   MainServer() {
      intro = new Intro();
   }

	private void main_View() { // ���� ���� ȭ��
		this.removeAll();
		
		intro.add(this);

		// ��� ȸ���� ������ �ε��ؿ´�.
		allUser_vc = new UserController().dataLoadAll();

		// ������ ����/���� ����� ������ �ʾұ⿡ �ӽ÷� ����.
		for (int i = 0; i < allUser_vc.size(); i++) {
			User u = (User) allUser_vc.get(i);
			u.setLoginState(false);
		}

		setBounds(280, 0, 744, 768);
		setBackground(new Color(0, 0, 0, 0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setVisible(true); // �����츦 �� �� ����.
		setLayout(null);

		// ���̺� �ҷ��� ��� ���� ������ �ҷ��´�.
		//
		allUserInfo_Table.removeAll();
		//
		allUsercolumn = new Object[allUser_vc.size()][allUserRow.length];
		allUserColumnUppate(allUser_vc);
		allUserModel = new DefaultTableModel(allUsercolumn, allUserRow) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		allUserInfo_Table = new JTable(allUserModel);

		// ���̺� ������ ���� �ڵ�
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // ����Ʈ���̺��������� ����
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // �������� ���������� CENTER��
		TableColumnModel tcm = allUserInfo_Table.getColumnModel(); // ������ ���̺��� �÷����� ������

		// #��� ���� ����Ʈ ��ũ�� ����
		allUserListView = new JScrollPane(allUserInfo_Table);
		allUserListView.setBounds(12, 124, 726, 250);
		
		allUserInfo_Table.getTableHeader().setReorderingAllowed(false); // �÷��� �̵� �Ұ�
		allUserInfo_Table.getTableHeader().setResizingAllowed(false); // �÷� ũ�� ���� �Ұ�
		allUserInfo_Table.getColumnModel().getColumn(2).setPreferredWidth(50); // �ش� ���� ũ�⸦ ����
		allUserInfo_Table.getColumnModel().getColumn(3).setPreferredWidth(50);
		allUserInfo_Table.getColumnModel().getColumn(4).setPreferredWidth(30);
		allUserInfo_Table.getColumnModel().getColumn(5).setPreferredWidth(120);
		allUserInfo_Table.getColumnModel().getColumn(6).setPreferredWidth(30);
		allUserInfo_Table.getColumnModel().getColumn(7).setPreferredWidth(50);
		allUserInfo_Table.setAutoCreateRowSorter(true); // ���� Ŭ���ϸ� �ڵ� ����
		tcm.getColumn(4).setCellRenderer(dtcr); // �ش� ���� ������ ����
		tcm.getColumn(6).setCellRenderer(dtcr);
		add(allUserListView);
		
		// ���̺� ���͸� ���� TableRowSorter ����
		TableRowSorter rowSorter = new TableRowSorter(allUserInfo_Table.getModel());
		allUserInfo_Table.setRowSorter(rowSorter);

		// �˻� �ؽ�Ʈ �ʵ�
		allUserSearch_tf = new JTextField();
		allUserSearch_tf.getDocument().addDocumentListener(new DocumentListener() {
			// ���̺� ���� �����ϴ� �޼ҵ�
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = allUserSearch_tf.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					// �ؽ�Ʈ �ʵ忡 �Է��� ���� �°� ���͸��Ѵ�.
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			// ���̺� ���� �����ϴ� �޼ҵ�
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = allUserSearch_tf.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					// �ؽ�Ʈ �ʵ忡 �Է��� ���� �°� ���͸��Ѵ�.
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			// ���� �ٲٴ� �޼ҵ�� ������� ����.
			@Override
			public void changedUpdate(DocumentEvent e) {

			}

		});

		allUserSearch_tf.setBounds(265, 84, 220, 25);
		allUserSearch_tf.setBorder(null);
		allUserSearch_tf.setBackground(new Color(0,0,0,0));
		add(allUserSearch_tf);
		
		
		// ���̺� �ҷ��� ��� ���� ������ �ҷ��´�.
		
		//
		loginUserInfo_Table.removeAll();
		//
		
		System.out.println("user_vc ������ : " + user_vc.size());
		loginUsercolumn = new Object[user_vc.size()][loginUserRow.length];
		loginUserColumnUppate(user_vc);
		loginUserModel = new DefaultTableModel(loginUsercolumn, loginUserRow) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		loginUserInfo_Table = new JTable(loginUserModel);

		// ���̺� ������ ���� �ڵ�
//		DefaultTableCellRenderer dtcr2 = new DefaultTableCellRenderer(); // ����Ʈ���̺��������� ����
//		dtcr2.setHorizontalAlignment(SwingConstants.CENTER); // �������� ���������� CENTER��
//		TableColumnModel tcm2 = loginUserInfo_Table.getColumnModel(); // ������ ���̺��� �÷����� ������

		// #��� ���� ����Ʈ ��ũ�� ����
		loginUserListView = new JScrollPane(loginUserInfo_Table);
		loginUserListView.setBounds(12, 502, 726, 250);

		loginUserInfo_Table.getTableHeader().setReorderingAllowed(false); // �÷��� �̵� �Ұ�
		loginUserInfo_Table.getTableHeader().setResizingAllowed(false); // �÷� ũ�� ���� �Ұ�
		loginUserInfo_Table.setAutoCreateRowSorter(true); // ���� Ŭ���ϸ� �ڵ� ����
		add(loginUserListView);

		loginUserModel.fireTableDataChanged();

		loginUserListView.revalidate();
		loginUserListView.repaint();
		System.out.println("loginUserTableUpdate ���� �Ϸ�");
		
		
		// ���̺� ���͸� ���� TableRowSorter ����
		TableRowSorter rowSorter2 = new TableRowSorter(loginUserInfo_Table.getModel());
		loginUserInfo_Table.setRowSorter(rowSorter2);

		// �˻� �ؽ�Ʈ �ʵ�
		loginUserSearch_tf = new JTextField();
		loginUserSearch_tf.getDocument().addDocumentListener(new DocumentListener() {
			// ���̺� ���� �����ϴ� �޼ҵ�
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = loginUserSearch_tf.getText();

				if (text.trim().length() == 0) {
					rowSorter2.setRowFilter(null);
				} else {
					// �ؽ�Ʈ �ʵ忡 �Է��� ���� �°� ���͸��Ѵ�.
					rowSorter2.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			// ���̺� ���� �����ϴ� �޼ҵ�
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = loginUserSearch_tf.getText();

				if (text.trim().length() == 0) {
					rowSorter2.setRowFilter(null);
				} else {
					// �ؽ�Ʈ �ʵ忡 �Է��� ���� �°� ���͸��Ѵ�.
					rowSorter2.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			// ���� �ٲٴ� �޼ҵ�� ������� ����.
			@Override
			public void changedUpdate(DocumentEvent e) {

			}

		});

		loginUserSearch_tf.setBounds(265, 459, 220, 25);
		loginUserSearch_tf.setBorder(null);
		loginUserSearch_tf.setBackground(new Color(0,0,0,0));
		add(loginUserSearch_tf);
		
		// #������ ��ư
		exitButton.setBounds(870, 690, 100, 30);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
			}

			// ���콺�� ��ư���� �������� �̺�Ʈ
			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
			}

			// ���콺�� ��ư�� ������ �� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0); // ���μ��� ����
			}
		});
		
		this.revalidate();
		this.repaint();
	}

   void Server_start() // // MainServer���� ����� �� �ֵ��� default�� ����
   {
      try {
         server_Socket = new ServerSocket(port);
         if (portConection) { // ������Ʈ�� �����û �� �ÿ� main_View�� ���
            try {
               Thread.sleep(500); // 0.5�ʵ� ����
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            }
            main_View();
            portConection = false; // �ߺ����� ������ ���� �ʱ�ȭ
         }
      } catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
         statusArea.append("�ٽ� �Է��ϼ���.\n");
      }

      if (server_Socket != null) // ���������� ��Ʈ�� ������ ���
      {
         Connection();
      }
   }

	private void Connection() {
		// 1������ �����忡���� 1������ �ϸ� ó���� �� �ִ�.
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() { // �����忡�� ó���� ���� �����Ѵ�.

				while (true) {
					try { // ���������� try-catch
						statusArea.append("����� ���� �����(���� ��Ʈ��ȣ : " + port + ")\n");
						socket = server_Socket.accept(); // ����� ���� ���� ��� => ������ �ٸ� ��ɵ��� �������� �ʰ� �׾������. => ��Ƽ ������� �ذ�!
						statusArea.append("����� ���� ���� �Ϸ�!\n");
						connectCk = true; // �α����� ���� ���� ����

						// ����ڰ� accept�� ������ �ϰԵǸ� UserInfo ��ü�� �����ϰ� �ȴ�.
						// UserInfo �����ڴ� �Ű������� �Ѿ�� socket�� �޾��ְ� UserNetwork()�� �����Ͽ� ���ἳ���� �ϰԵȴ�.
						UserInfo user = new UserInfo(socket);

						user.start(); // ��ü�� �����带 ���� ����
					} catch (IOException e) {
						
						JOptionPane.showMessageDialog(null, "������ �����Ǿ����ϴ�.", "�˸�", JOptionPane.ERROR_MESSAGE);
						break; // ������ ������ �� run �޼ҵ带 �����Ѵ�.
					}
				} // while�� ��

			}
		});

		th.start();
	}

   public static void main(String[] args) {
      new MainServer();
   }

   @Override
   public void paint(Graphics g) {
      viewImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
      viewGraphics = viewImage.getGraphics();
      screenDraw(viewGraphics);
      g.drawImage(viewImage, 0, 0, null);
   }

   public void screenDraw(Graphics g) {
      g.drawImage(mainBackgroundImage, 0, 0, null);
      this.paintComponents(g);
      this.repaint();
   }

	// {"ID","Password","�̸�","�������","����","E-mail","Level","Exp","���� ���� ����"};
	// ��ü ���� ���̺� ������Ʈ�� ���� �޼ҵ�
	public void allUserColumnUppate(Vector allUser_vc) {
		for (int i = 0; i < allUser_vc.size(); i++) {
			String pw="";
			User u = (User) allUser_vc.get(i);
			allUsercolumn[i][0] = u.getId();
			for(int j=0; j<u.getPw().length(); j++) {
				pw += "*";
			}
			allUsercolumn[i][1] = pw;
			allUsercolumn[i][2] = u.getName();
			allUsercolumn[i][3] = u.getDateOfBirth();
			allUsercolumn[i][4] = u.getAge();
			allUsercolumn[i][5] = u.geteMail();
			allUsercolumn[i][6] = u.getLevel();
			allUsercolumn[i][7] = u.getExp();
			allUsercolumn[i][8] = u.getCorAnswer();
		}
	}
	
	// {"ID", "Level", "Exp", "���� ���� ����", "�������� ��"};
	// ���� ���� ���̺� ������Ʈ�� ���� �޼ҵ�
	public void loginUserColumnUppate(Vector user_vc) {
		try {
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo u = (UserInfo) user_vc.get(i);
				System.out.println(i+", "+u);
				loginUsercolumn[i][0] = u.userID;
				loginUsercolumn[i][1] = u.level;
				loginUsercolumn[i][2] = u.exp;
				loginUsercolumn[i][3] = u.corAnswer;
				if(u.room_No==0) {
					loginUsercolumn[i][4] = "����";
				} else {
					loginUsercolumn[i][4] = u.room_No;
				}
				
			}
		} catch(NullPointerException e) {
			System.out.println("�ε� �Ϸ�");
		}
	}

	// ���� ���� �� �ش� â�� ������Ʈ�� �����Ѵ�.(���̺�)
	private void removeMainView() {
		this.removeAll();
	}

   // ��Ʈ�� ȭ��� ���� Ŭ����
   class Intro extends JFrame {
      // Frame, Panel
      private JFrame introView = new JFrame("Server"); // ��Ʈ�� ������ (��Ʈ��ȣ�� �Է��Ͽ� ������ �����ϸ� ���� ������ â�� ��� �Ѿ�� ����)
      private JLabel port_lb = new JLabel("��Ʈ ��ȣ : ");
      private JLabel introMenuBar = new JLabel();

      // ���� ����
      private Image viewImage; // �̹��� ����� ����
      private Graphics viewGraphics; // �׷��� ����� ����
      private int mouseX, mouseY; // ���콺 ��ǥ�� ����
      private boolean state = true; // ���� �ߺ� ���� ������

      // Image
      // #MainView ���
      private Image introBackgroundImage = new ImageIcon(Main.class.getResource("/images/ServerIntoBackground.png")).getImage();
      // #��ư �̹���
      private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/msExitButtonEntered.png"));
      private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("/images/msExitButtonBasic.png"));
      private ImageIcon serverStartButtonBasicImage = new ImageIcon(Main.class.getResource("/images/serverStartButtonBasic.png"));
      private ImageIcon serverStartButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/serverStartButtonEntered.png"));
      private ImageIcon serverStopButtonBasicImage = new ImageIcon(Main.class.getResource("/images/serverStopButtonBasic.png"));
      private ImageIcon serverStopButtonEnteredImage = new ImageIcon(Main.class.getResource("/images/serverStopButtonEntered.png"));
     
      

      // Button
      private JButton start_btn = new JButton(serverStartButtonBasicImage); // ���� ���� ��ư
      private JButton stop_btn = new JButton(serverStopButtonBasicImage); // ���� ���� ��ư
      private JButton exitButton = new JButton(exitButtonBasicImage); // ������ ��ư

		Intro() {
			Font font = new Font("�޸�����ü", Font.BOLD, 14);
			setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������) - ��� �ϼ� �� �߰� ����
			setTitle("Server"); // ������ Ÿ��Ʋ �� �̸�(Ÿ��Ʋ �ٸ� ���� �����̱� ������ ��� �Ǵ� �ڵ�)
			setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // Main���� ������Ų ȭ�� �ػ󵵸� ���
			setResizable(false); // ������ ũ�� ����
			setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
			setBackground(new Color(0, 0, 0, 0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
			setForeground(new Color(0, 0, 0, 0));
			setVisible(true); // �����츦 �� �� ����.
			setLayout(null);

			// ScrollPane
			statusArea.setBackground(new Color(80, 80, 80, 0));
			statusArea.setFont(font);
			statusArea.setForeground(Color.white);
			statusArea.setLineWrap(true); // �ڵ� �ٹٲ�
			statusView = new JScrollPane(statusView, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			statusView.setBounds(10, 20, 260, 330);
			statusView.setBorder(null);
			statusView.setBackground(new Color(0, 0, 0, 0));
			statusView.getVerticalScrollBar().setValue(statusView.getVerticalScrollBar().getMaximum());
			statusView.setComponentZOrder(statusView.getVerticalScrollBar(), 0);
			statusView.setComponentZOrder(statusView.getViewport(), 1);
			statusView.getVerticalScrollBar().setOpaque(false);
			statusView.setViewportView(statusArea);
			statusView.setLayout(new ScrollPaneLayout() {
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
			statusView.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
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

			/* ���� �ڵ�� ������ ȯ�濡���� �ڵ� ��ũ���� �ǰ��Ϸ��� �޼ҵ��̴�. */
			statusView.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					scrollpanemove = true;
				}
			});
			statusView.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) { // ���������ʿ��� ����(���� ����,��ġ)�� ����ɽ� �޼ҵ� �ۼ�
					if (scrollpanemove) { // ���� ��ũ�� ���갡 ����������
						scrollpanemove = false; // ������ ������ ���� �����ʰ�, ��������� �ٲ۴�.
					} else {
						JScrollBar src = (JScrollBar) e.getSource();
						src.setValue(src.getMaximum());
					}
				}
			});
			add(statusView);

			// label
			// #��Ʈ��ȣ
//			port_lb.setBounds(10, 360, 100, 30);
//			add(port_lb);

			// Button
			// #���� ��ư
			start_btn.setBounds(10, 400, 100, 30);
			add(start_btn);
			start_btn.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					start_btn.setIcon(serverStartButtonEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
					start_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}

				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override
				public void mouseExited(MouseEvent e) {
					start_btn.setIcon(serverStartButtonBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
					start_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}

				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override
				public void mousePressed(MouseEvent e) {
					if (state) {
						System.out.println("��ŸƮ ��ư Ŭ��");
						/*
						 * portCheck() �޼ҵ带 ���� ������ ��ȯ ���� ��� ������ ������������. ������ ��ȯ �Ǿ��ٴ� ���� �������� ��Ʈ��ȣ�� �ƴ���
						 * �ǹ�.
						 */
						if (portCheck(port_tf.getText().trim()) >= 0) {
							portConection = true;
							System.out.println("��Ʈ��ȣ : " + port);
							Server_start(); // ���� ���� �� ����� ���� ���
							state = false; // �ߺ� ���� ������ ���� false�� ����

						}
					} else {
						statusArea.append("������ �̹� ������ �Դϴ�.\n");
					}
				}
			});

			// #���� ��ư
			stop_btn.setBounds(170, 400, 100, 30);
			add(stop_btn);
			stop_btn.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					stop_btn.setIcon(serverStopButtonEnteredImage); // ���콺�� �÷������� �̹��� ����(Entered Image)
					stop_btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}

				// ���콺�� ��ư���� �������� �̺�Ʈ
				@Override
				public void mouseExited(MouseEvent e) {
					stop_btn.setIcon(serverStopButtonBasicImage); // ���콺�� �������� �̹��� ����(Basic Image)
					stop_btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ���콺 Ŀ���� �⺻ Ŀ���� ����
				}

				// ���콺�� ��ư�� ������ �� �̺�Ʈ
				@Override
				public void mousePressed(MouseEvent e) {
					state = true; // ���� ������ ������ ���·� ���� => �����Ѱ�?

					try {
						// ���� ������ �ݱ� ���� �������� ��� �����鿡�� ������ �������� �˸���.
						for (int i = 0; i < user_vc.size(); i++) // ���� ���ӵ� ����ڿ��� ����
						{
							UserInfo u = (UserInfo) user_vc.elementAt(i); // i������ �ִ� ����ڿ��� �޼����� ����
							u.send_Message("ServerStop/pass");
						}
						
						// ������ �ݱ� ��, 0.5���� �����ð��� �д�.
						try {
							Thread.sleep(500); // 0.5�ʵ� ����
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
						
						// ���� ������ �ݴ´�.
						server_Socket.close();

						// ������ ��� ��ҵ��� �����Ѵ�.
						allUser_vc.removeAllElements();
						user_vc.removeAllElements();
						wRoom_vc.removeAllElements();
						room_vc.removeAllElements();

						// ���� ���� ���̺��� �����Ѵ�.
						removeMainView();

						statusArea.append("������ �����Ǿ����ϴ�.\n");

					} catch (IOException e1) {
					}

					System.out.println("���� ��ž ��ư Ŭ��");
				}
			});

			// #������ ��ư
			exitButton.setBounds(125, 400, 30, 30);
			exitButton.setBorderPainted(false);
			exitButton.setContentAreaFilled(false);
			exitButton.setFocusPainted(false);
			exitButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					exitButton.setIcon(exitButtonEnteredImage);
					exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
					// ���콺�� �÷��� �� �ո������ ��Ÿ����

				}

				@Override
				public void mouseExited(MouseEvent e) {
					exitButton.setIcon(exitButtonBasicImage);
					exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// ���콺�� ���� ���� ���·�
				}

				@Override
				public void mousePressed(MouseEvent e) {
					try {
						Thread.sleep(500); // 0.5�ʵ� ����
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}
			});
			add(exitButton);

			// TextField
			// ��Ʈ��ȣ �Է�
			port_tf = new JTextField("12345"); // ��Ʈ��ȣ �⺻�� 12345
			port_tf.setBounds(110, 365, 165, 30);
			port_tf.setBorder(null);
			port_tf.setBackground(new Color(0,0,0,0));
			add(port_tf);
			port_tf.setColumns(10);

			// �޴���
			introMenuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
			introMenuBar.addMouseListener(new MouseAdapter() {
				// ���콺�� ��ư�� �÷����� �� �̺�Ʈ
				@Override
				public void mouseEntered(MouseEvent e) {
					introMenuBar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // ���콺 Ŀ���� �ո�� Ŀ���� ����
				}

				@Override
				public void mousePressed(MouseEvent e) {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			});
			introMenuBar.addMouseMotionListener(new MouseMotionAdapter() {
				// #�Ŵ��� �巡�� ��, ������ �� �ְ� �Ѵ�.
				@Override
				public void mouseDragged(MouseEvent e) {
					int x = e.getXOnScreen();
					int y = e.getYOnScreen();
					setLocation(x - mouseX, y - mouseY);
				}
			});
			add(introMenuBar);

		}


      @Override
      public void paint(Graphics g) {
         g.drawImage(introBackgroundImage, 0, 0, null);
         this.paintComponents(g);
         this.repaint();
      }

      // ��ȿ ��Ʈ��ȣ üũ �޼ҵ�
      private int portCheck(String str) {
         try {
            // int������ ������ ������ ���ڿ��� �ԷµǾ����� try-catch�� Ȯ��
            int tempPort = Integer.parseInt(str);
            // �����ϴٸ� ��ȿ ��Ʈ��ȣ�� �Է��Ͽ����� Ȯ��.
            if (tempPort >= 0 && tempPort <= 65535) {
               return port = Integer.parseInt(str); // ��Ʈ��ȣ ������ 0~65535
            } else { // ��ȿ ��Ʈ��ȣ�� �ƴϸ� ������ ��ȯ
               statusArea.append("�߸��� ��Ʈ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.\n");
               return -1;
            }
         } catch (NumberFormatException e) { // ���� ó�� ����� ��쿡�� ������ ��ȯ
            statusArea.append("�߸��� ��Ʈ��ȣ�Դϴ�. �ٽ� �Է��ϼ���.\n");
            return -1;
         }
      }
   }

   // ������ ���� �����带 �����ϱ� ���� Ŭ����
   class UserInfo extends Thread {
      private OutputStream os;
      private InputStream is;
      private ObjectOutputStream oos;
      private ObjectInputStream ois;
      private DataOutputStream dos;
      private DataInputStream dis;

      private Socket user_socket; // ������� ID�� �޴´�.
      private String userID = null; // ������� ID ����
      // id�� �������� User ��ü�� ������ ã��, �Ʒ� ������ ������ �����Ѵ�.
      private int level; // ����
      private int exp; // ����ġ
      private int corAnswer; // ���� ���� ����
      private int room_No=0; // ���ӹ� ��ȣ
      private boolean loginState = false; // �α��� ����

      private boolean RoomCh = true;

      UserInfo(Socket socket) // ������ �޼ҵ�
      {
         this.user_socket = socket;
         UserNetwork();
      }

      /*
       * [Protocol ����] - ����� �߰� = NewUser/�����ID - ���� ����� = OldUser/�����ID - ���� =
       * Note/User@���� Client ���� => Note/�޴»��@���� Server ���� => Note/�޴»��@���� (�޼��� ���ö�)
       * => Note/�������@���� (�޼��� ������) => Client ���� => Note/�������@���� (�޼��� ������)
       */
      private void UserNetwork() // ��Ʈ��ũ �ڿ� ����
      {
         try {
            is = user_socket.getInputStream();
            dis = new DataInputStream(is);

            os = user_socket.getOutputStream();
            dos = new DataOutputStream(os);

         } catch (IOException e) {
         }
      }

      // ������ �������� ��, ���� ����ڵ鿡�� �˸��� ����Ʈ�� �߰��ϱ� ���� �޼ҵ�
      private void userAdd(String str) {
         boolean checkID = true;
         // ���� ���� �Ŀ� ������� �г����� �޾Ƶ��δ�.
         userID = str;
         System.out.println(userID);
         statusArea.append(userID + " : ����� ����!\n");

         // ���� ��â ó�� �޼ҵ�
         enteredWaitingRoom();

         user_vc.add(this); // ����ڿ��� �˸� �� Verctor�� �ڽ��� �߰�
         // Vector�� �������� �þ�� �迭�� �����ϸ� �Ǵµ�, ��ü�� ������ ����� ������ Vector�� �����Ѵ�.
         // �ߺ��� ������� �߰���������.(�ذ� ���ؼ� ���� �ڵ�..)
         for (int i = 0; i < wRoom_vc.size(); i++) {
            UserInfo checkUser = (UserInfo) wRoom_vc.get(i);
            if (checkUser.getUserID().equals(userID)) {
               // �ߺ��� ������� false
               checkID = false;
               break;
            }
         }

         if (checkID) {
            // �ش� ��ü�� Vector�� �߰�
            wRoom_vc.add(this);
         }

         // ���� �� ����
         main_View();
         
         statusArea.append("���� ���ӵ� ����� �� : " + user_vc.size() + "\n");
      }

      // ������ �α׾ƿ��� ���, ����� �޼ҵ�� ���� ������
      private void userSub(String str) {
         // ������ ������ �� ��� ������� �г����� �޾Ƶ��δ�.
         userID = str;
         System.out.println(userID);
         statusArea.append(userID + " : ����� ��������!");

         // ���� ����ڵ鿡�� ����� ���� ���� �˸�(broadcast)
         BroadCast("WaitingRoom/pass/SubUser@" + userID);

         // ���� �������� ������� ����Ʈ�� �ڽſ��� �˸�
         for (int i = 0; i < user_vc.size(); i++) {
            UserInfo u = (UserInfo) user_vc.elementAt(i);
            // ������ ����� ����ڿ��� ������ �κ�
            send_Message("WaitingRoom/pass/OldUser@" + u.userID);
         }
      }

      private void enteredWaitingRoom() {
         // ����� ������ �Ϸ�Ǹ�, �ش� ������ ������ �ε��ؿ´�.
         for (int i = 0; i < allUser_vc.size(); i++) {
            // ��� ���� ��ü�� �ϳ� �����ͼ�
            User u = (User) allUser_vc.elementAt(i);
            // �������� ���̵�� ���� ���̵� ã��
            if (u.getId().equals(userID)) {
               // ������ ������ �����ϰ�
               level = u.getLevel();
               exp = u.getExp();
               corAnswer = u.getCorAnswer();
               // �α��� ���·� ��ȯ
               loginState = true;
               // �ڽſ��� �� ������ �˸���.
               send_Message("WaitingRoom/pass/UserInfo@" + userID + "@" + level + "@" + exp + "@" + corAnswer);
               break; // ������� ������ ã�����Ƿ� �ݺ��� ����
            }
         }

         // ���� �������� ������� ����Ʈ�� �ڽſ��� �˸�
         for (int i = 0; i < wRoom_vc.size(); i++) {
            // ���� ������ ���� ��ü�� �ϳ� �����ͼ�
            UserInfo u = (UserInfo) wRoom_vc.elementAt(i);
            System.out.println("���� üũ :" + u);
            /*
             * ���� ������� ������ �о�´�. ������ ����� ����ڿ��� ������ �κ�
             */
            String msg = "";
            if (i == wRoom_vc.size() - 1) {
               msg = "WaitingRoom/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer + "@"
                     + "last";
            } else {
               msg = "WaitingRoom/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + u.corAnswer
                     + "@_";
            }
            send_Message(msg);
         }

         // ���� ������ ���� ����Ʈ�� �ڽſ��� �˸�
         for (int i = 0; i < room_vc.size(); i++) {
            // ���� ������ �� ��ü�� �ϳ� �����ͼ�
            RoomInfo r = (RoomInfo) room_vc.elementAt(i);
            System.out.println("�� üũ : " + r);

            /*
             * ������ ������ ���� ������ �о�´�. ������ ����� ����ڿ��� ������ �κ�
             */
            String msg = "";
            if (i == room_vc.size() - 1) {
               msg = "WaitingRoom/pass/OldRoom@" + userID + "@" + r.room_No + "@" + r.room_name + "@" + r.pwStat
                     + "@" + r.room_PW + "@" + r.fixed_User + "@" + r.Room_user_vc.size() + "@last";
            } else {
               msg = "WaitingRoom/pass/OldRoom@" + userID + "@" + r.room_No + "@" + r.room_name + "@" + r.pwStat
                     + "@" + r.room_PW + "@" + r.fixed_User + "@" + r.Room_user_vc.size() + "@_";
            }
            send_Message(msg);
         }

         // ���� ����ڵ鿡�� ���ο� ����� �˸�(broadcast)
         BroadCast("WaitingRoom/pass/NewUser@" + userID + "@" + level + "@" + exp + "@" + corAnswer);
         // ���� ����ڿ��� �ڽ��� �˸���. �������� ��� [NewUser/�����ID]

      }

      @Override
      public void run() // Thread���� ó���� ����
      {
         while (true) {
            try {
               // ��Ʈ�������� ���߱� ������ �ش� ��Ʈ�� ������ �����ڿ��� �ǽ�
               String msg = dis.readUTF();
               if (connectCk) { // �̰��� true�̸� �����ϰ� �α���/ȸ�������� �ϱ����� ����
                  statusArea.append("����ڷκ��� ���� �޽��� : \n" + msg + "\n");
               } else {
                  statusArea.append("[" + userID + "]�� ���κ��� ���� �޼��� : \n" + msg + "\n");
               }
               Inmessage(msg);
            } catch (IOException e) {
//            	JOptionPane.showMessageDialog(null, "���� ������ �߻��߽��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
//            	break; // ������ ������ �� run �޼ҵ带 �����Ѵ�.
            }
         }
      } // run �޼ҵ� ��

      private String getUserID() {
         return userID;
      }

      /*
       * [�������� ���� if���� ���������, switch������ ����] if���� ��� ���ǹ��� ���ؼ� cmp(Compare;��) ������ ��ġ�Ƿ�
       * ������ Ȯ���ϱ� ���� �ν�Ʈ������ ����ؼ� �ʿ�������. switch���� ���� ���� ���� �Ѿ�� Jump Table�� ����� �� �ȿ���
       * ���� Ȯ���ϰ� �ٷ� �ش� �ڵ�� �Ѿ�� ������� �۵��Ѵ�. ������ �Է¹��� ���� Ȯ���ϴ� �ν�Ʈ���Ǹ� ������ �ȴ�. Jump Table��
       * �����ϴµ� ������尡 �����Ƿ� ������ ������ Protocol������ ������带 �߻���ų ���� ������� �����Ƿ� switch������ �ۼ��Ѵ�.
       */
      // Ŭ���̾�Ʈ�κ��� ������ �޼��� ó��
      private void Inmessage(String str) {
         System.out.println(str);
         st = new StringTokenizer(str, "/"); // �ѱ�� ������ ���� ��� �ϱ����� /�� @�� ��������

         String protocol = st.nextToken();
         String mUserId = st.nextToken(); // ���� ����id�� ����ȴ�.

         System.out.println("�������� : " + protocol);
         System.out.println("�޼��� : " + mUserId);

         // protocol ��û ó��
         switch (protocol) {

         // #�α��� ��û�� ������ ��
         case "LoginCheck":
            String pw = st.nextToken();
            // ���̵�� �н����尡 �´��� Ȯ���Ѵ�.
            boolean findID = false; // ��ġ�ϴ� ������ �ִ��� üũ�ϴ� ����
            for (int i = 0; i < allUser_vc.size(); i++) {
               User user = (User) allUser_vc.elementAt(i);
               // ID�� PW�� ��ġ�ϰ� ���� �α��� ���°� �ƴ����� Ȯ���Ѵ�
               if (user.getId().equals(mUserId) && user.getPw().equals(pw) && !user.isLoginState()) {
                  userID = mUserId;
                  connectCk = false;
                  findID = true;

                  // �α��� ���·� ��ȯ
                  user.setLoginState(true);

                  send_Message("LoginOK/ok");
                  userAdd(userID);
                  UserNetwork();
                  
                  // ���� ���� �α��� ���¶��
               } else if (user.getId().equals(mUserId) && user.getPw().equals(pw) && user.isLoginState()) {
                  findID = true;
                  // ���� �α��� ���̶�� �޽����� ������.
                  send_Message("SigningIn/" + userID);
               }
            }
            if (!findID)
               send_Message("LoginFail/fail"); // ã�� ���Ͽ����� �ش� �޽����� ������.
            break;
            
         // # ȸ������ ��û�� ������ ��
         case "JoinRequest":
            pw = st.nextToken();
            String name = st.nextToken();
            String dateOfBirth = st.nextToken();
            String email = st.nextToken();
            char gender = st.nextToken().charAt(0);

            // ���� ������ ��ü�� ����
            User us = new User(mUserId, pw, name, dateOfBirth, email, gender);

            // ������ ��ü�� allUser_vc�� ����
            allUser_vc.add(us);

            // db�� ����.
            new UserController().dataSaveAll(allUser_vc);

            // ���� �� ����
            main_View();
            
            break;
            
         // #���� ������ ��û�� ������ ��
         case "Note":
            // protocol = Note
            // message = user
            // note = �޴� ����
            String note = st.nextToken();

            System.out.println("�޴� ��� : " + mUserId);
            System.out.println("���� ���� : " + note);

            // ���Ϳ��� �ش� ����ڸ� ã�Ƽ� �޼��� ����
            for (int i = 0; i < wRoom_vc.size(); i++) {
               UserInfo u = (UserInfo) wRoom_vc.elementAt(i);

               if (u.userID.equals(mUserId)) {
                  u.send_Message("Note/" + userID + "/" + note);
                  // Note/User1/~~~
               }
            }
            break;
			// # ���ӹ� ������
			case "GameRoomOut":
				room_No = Integer.parseInt(st.nextToken());
				// �г� �ٽ� �׸��Ⱑ ��ƴ�. �ð��� ��� ���� �ٽ� �����Ű�� ����� ����� ��.
				Vector<UserInfo> tempUser = new Vector<UserInfo>();

				// �ش� ������ ���ӹ� �������� �����Ѵ�.
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.get(i);
					// �� ��ȣ�� ��ġ�ϸ�
					if (r.room_No == room_No) {
						// ���� ���� ��Ͽ��� �ش� ������ �����Ѵ�.
						for (int j = 0; j < r.Room_user_vc.size(); j++) {
							UserInfo u = (UserInfo) r.Room_user_vc.get(j);
							if (u.userID.equals(mUserId)) {
								r.Room_user_vc.remove(j);
							}
						}
						// �ӽ� ���Ϳ� ��������Ʈ�� �����Ѵ�.(���� ���� ��)
						tempUser = r.Room_user_vc;
						// �ӽ� �� ��ü�� �����Ѵ�.
						RoomInfo tempRoom = new RoomInfo(r.room_No, r.room_name, r.pwStat, r.room_PW, r.fixed_User);
						// ������ ���� �����Ѵ�.
						room_vc.remove(i);
						// �ӽ� �� ��ü�� �߰��Ѵ�.
						room_vc.add(tempRoom);
					}
				}

				// �ش� �濡 ������ �������� �˸���.(�� �޽����� ������ ���� �ݴ´�.)
				for (int i = 0; i < tempUser.size(); i++) {
					UserInfo u = (UserInfo) tempUser.get(i);
					u.send_Message("Paint/pass/UserOut@pass");
					u.send_Message("EntryGameRoom/" + u.userID + "/" + room_No);
				}

				// �ش� ������ ������ ������Ͽ��� �����Ѵ�.
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.get(i);
					if (u.userID.equals(mUserId)) {
						// �ش� ������ ã������ ����Ʈ���� �����Ѵ�.
						user_vc.remove(i);
						System.out.println("������ : " + user_vc.size());
					}
				}

				for (int i = 0; i < allUser_vc.size(); i++) {
					User user = (User) allUser_vc.elementAt(i);
					// �ش� ������ ã�´�.
					System.out.println(user);
					System.out.println("user.getId : " + user.getId() + ", mUserId : " + mUserId);
					if (user.getId().equals(mUserId)) {
						System.out.println(mUserId + " üũ�� !!!");
						send_Message("LoginOK/ok");
						userAdd(mUserId);
						break;
					}
				}

				BroadCast("WaitingRoom/pass/RoomInfoUpdate@pass@" + room_No + "@" + tempUser.size());

				break;

         // #�� ���� ��û�� ������ ��
         case "CreateRoom":
            // ���޹��� �޽����� ��ũ����¡�� �Ͽ� ������ ���� ����
            String title = st.nextToken();
            String state = st.nextToken();
            String roomPW = st.nextToken();
            int fixed_User = Integer.parseInt(st.nextToken());
            int roomNo = 0; // ���ȣ�� 1~999�̹Ƿ� 0�� �ʱⰪ���� ��������.

            // ���ȣ�� �����Ѵ�.(����) ������ ������ ���� ���Ͽ� ���� ��ȣ�� ���� ��� ������Ѵ�.
            Pointer: while (true) {
               roomNo = (int) (Math.random() * 999) + 1; // ���� 24������ �� ���������� ��ȣ�� 999���� �Ҵ�!

               for (int i = 0; i < room_vc.size(); i++) {
                  RoomInfo r = (RoomInfo) room_vc.elementAt(i);
                  if (r.room_No == roomNo) { // ���� �� ��ȣ�� ������ ��
                     continue Pointer; // �ٽ� ��ȣ �̱�
                  }
               } // for�� ��
               break; // continue�� �������� �ʴ´ٴ°� ���� ���ȣ�� �������� �ʴ´ٴ� ��.
            }

            // ���޹��� ������ RoomInfo��ü�� �����ϰ� room_vc�� �߰�
            RoomInfo new_room = new RoomInfo(roomNo, title, state, roomPW, fixed_User);
            // ���� ������ �����̹Ƿ� ���� id�� �����Ų��.
            new_room.roomCaptainID = mUserId;
            
            room_vc.add(new_room); // ��ü ä�� �� Vector�� ���� �߰�
            	
            // �ش� ������ user_vc������ ������ �����Ѵ�.
            for(int i=0; i<user_vc.size(); i++) {
            	UserInfo u = (UserInfo)user_vc.get(i);
            	if(u.userID.equals(mUserId)) {
            		u.room_No = roomNo;
            	}
            }

            // ���� ����������� BroadCast�� �˸���.
            BroadCast("WaitingRoom/pass/NewRoom@" + mUserId + "@" + roomNo + "@" + title + "@" + state + "@"
                  + roomPW + "@" + fixed_User + "@" + new_room.Room_user_vc.size() + "@");
            // ���� ������ �������� �� ������ �������� �˸��� �Ҵ��� �� ��ȣ�� �Ѱ��ش�.
            send_Message("WaitingRoom/pass/CreateRoom@" + mUserId + "@" + roomNo);
            // MainView�� Paintâ�� ����� �˸���.
            send_Message("EntryGameRoom/" + mUserId + "/" + roomNo);

            // ���̺� ����
            main_View();
            
            break;
            
         // #�� ���� ��û
			case "EnterRoom":
				int room_No = Integer.parseInt(st.nextToken()); // ���ȣ

				// room_vc���� �ش� �� ��ȣ�� ��ġ�ϴ� ��ü�� ã�´�.
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.get(i);
					// ���ȣ�� ���� ��ü�� ã�Ҵٸ� pw�� �����ϴ��� Ȯ���Ѵ�.
					/* room_PW�� ���� null�̿��� �Ѿ�ö� String ������ �޴´�. ������ �񱳹��� "null"�� ���Ѵ�. */
					if (r.room_No == room_No && !(r.room_PW.equals("null"))) {
						// ��й�ȣ�� null�� �ƴ϶�� pw�� �Է��϶�� �޽����� ������.(��й�ȣ�� ���� ������ �ش� â���� ������ üũ�ϵ����Ѵ�.)
						send_Message("WaitingRoom/pass/InputPW@" + mUserId + "@" + room_No + "@" + r.room_PW);
						break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
						// ��й�ȣ�� null�� ���
					} else if (r.room_No == room_No && r.room_PW.equals("null")) {
						// ��й�ȣ�� null�̶�� �ٷ� �����ϵ��� �Ѵ�.
						// �������� �� ������ �㰡�޾Ҵ� �˸�. ������ �� �޽����� WaitingRoomâ�� �����Ѵ�.
						send_Message("WaitingRoom/pass/EntryRoom@" + mUserId);

						// �̾ MainView�� �� ��ȣ�� �ѱ�鼭 Paintâ�� ������ �����Ѵ�.
						send_Message("EntryGameRoom/" + mUserId + "/" + r.room_No);

						// ��� �������� �ش� ������ ���ӹ濡 ������ ����Ʈ���� �����϶�� �˸���.
						BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
						/* ��й�ȣ ���� �ٷ� �����ϴ� �ڵ� */

						// �ش� ������ user_vc������ ������ �����Ѵ�.
						for (int j = 0; j < user_vc.size(); j++) {
							UserInfo u = (UserInfo) user_vc.get(j);
							if (u.userID.equals(mUserId)) {
								u.room_No = room_No;
							}
						}

						break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
					}
				}

				// ���̺� ����
				main_View();

				break;

         // #��й�ȣ�� �°� �Է����� ���
			case "PassPW":
				room_No = Integer.parseInt(st.nextToken()); // ���ȣ

				// room_vc���� �ش� �� ��ȣ�� ��ġ�ϴ� ��ü�� ã�´�.
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.get(i);
					// ���ȣ�� ���� ��ü�� ã�Ƽ� ���� ó���� �Ѵ�.
					if (r.room_No == room_No) {
//                              // ���� �㰡���� ������ Vector�� �߰�
//                               r.Room_user_vc.add(this);

						// �������� �� ������ �㰡�޾Ҵ� �˸�. ������ �� �޽����� WaitingRoomâ�� �����Ѵ�.
						send_Message("WaitingRoom/pass/EntryRoom@" + mUserId);

						// �̾ MainView�� �� ��ȣ�� �ѱ�鼭 Paintâ�� ������ �����Ѵ�.
						send_Message("EntryGameRoom/" + mUserId + "/" + r.room_No);

						// ��� �������� �ش� ������ ���ӹ濡 ������ ����Ʈ���� �����϶�� �˸���.
						BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);

						// �ش� ������ user_vc������ ������ �����Ѵ�.
						for (int j = 0; j < user_vc.size(); j++) {
							UserInfo u = (UserInfo) user_vc.get(j);
							if (u.userID.equals(mUserId)) {
								u.room_No = room_No;
							}
						}
						break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
					}
				}
				// ���̺� ����
				main_View();

				break;

         // #�� ���� �˸�
         case "EntryRoom":
            // ���ӹ濡 �����Ͽ� ���� ���� �������� ���ŵǾ�� �ȴ�.
            for (int i = 0; i < wRoom_vc.size(); i++) {
               UserInfo u = (UserInfo) wRoom_vc.get(i);
               // �ش� �������̵� ã�´�.
               if (u.getUserID().equals(mUserId)) {
                  // �ش� ���̵� ���� �������� �����.
                  wRoom_vc.remove(i);
                  // ���������� EntryRoom�̸�
                  /*
                   * �ش� ������ ���ӷ� ������Ͽ� �߰��Ѵ�.
                   */

                  // �ش� ������ ����Ʈ���� �����϶�� ��ε�ĳ��Ʈ�� ������.
                  BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
                  break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
               }
            }
            
            // ���̺� ����
            main_View();
            
            break;

         // #���� �α׾ƿ�
         case "UserLogout":
            // �α׾ƿ� �Ͽ� ���� ���� ����� ����Ʈ���� ���ŵǾ�� �ȴ�.
            for (int i = 0; i < user_vc.size(); i++) {
               UserInfo u = (UserInfo) user_vc.get(i);
               // �ش� �������̵� ã�´�.
               if (u.getUserID().equals(mUserId)) {
                  // �ش� ���̵� ������ �������� �����.
                  user_vc.remove(i);
                  break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
               }
            }
            // �α׾ƿ� �Ͽ� ���� ���� ����Ʈ���� ���ŵǾ�� �ȴ�.
            for (int i = 0; i < wRoom_vc.size(); i++) {
               UserInfo u = (UserInfo) wRoom_vc.get(i);
               // �ش� �������̵� ã�´�.
               if (u.getUserID().equals(mUserId)) {
                  // �ش� ���̵� ���� �������� �����.
                  wRoom_vc.remove(i);
                  break; // �۾��� �Ϸ������Ƿ� for���� Ż���Ѵ�.
               }

            }

            // ������ alluser���� �ش� ������ �α׾ƿ� ó���Ѵ�.
            for (int i = 0; i < allUser_vc.size(); i++) {
               User user = (User) allUser_vc.elementAt(i);
               // ���� ���¸� �α׾ƿ� ���·� �ٲ۴�.
               user.setLoginState(false);
            }

            // �ش� ������ ����Ʈ���� �����϶�� ��ε�ĳ��Ʈ�� ������.
            BroadCast("WaitingRoom/pass/RemoveUser@" + mUserId);
            
            // ���̺� ����
            main_View();
            
            break;
            

         // #���� ä�� ��û�� ������ ��
         case "ChattingWR":
            st = new StringTokenizer(str, "/", true); // ��ȹ����"/"�� ��ū���� �����Ѵ�.
            for (int i = 0; i < 4; i++) {
               st.nextToken(); // ��ū ���ſ�
            }
            ArrayList<String> chattingMsgList = new ArrayList<String>(); // ä�ø޽��� ������ ����Ʈ
            String totalChattingMsg = ""; // ��ü ä�� �޽��� ���� ����
            String tempMsg = "";
//                        String chattingMsg = st.nextToken(); // �޼��� �κ��� �߶� ����
            while (st.hasMoreTokens()) { // ������ ���� ��ū�� ������ true�� ������ false�� �����Ѵ�.
               tempMsg = st.nextToken();
               System.out.println("ä�� ��ū�� ���:" + tempMsg);
               chattingMsgList.add(tempMsg); // �޽��� ��ū�� ArrayList�� �߰�
            }

            for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList�� ��� �޽����� totalChattingMsg�� �����Ѵ�.
               totalChattingMsg += chattingMsgList.get(i);
            }

            System.out.println("MainServer Inmessage���� protocol�� ChattingWR �϶� ���� ���̵�:" + mUserId);
            BroadCast("WaitingRoom/pass/ChattingWR@" + mUserId + "@" + totalChattingMsg);
            break;

         // #���ӹ� ä�� ��û�� ������ ��
         case "ChattingPA":
            // �켱 ���ȣ�� �޴´�.
            room_No = Integer.parseInt(st.nextToken());

            // RoomInfo ��ü ����� => ���� üũ�� ���ؼ� ���
            RoomInfo r = null;
            for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.get(i);
               // ���ȣ�� ���� ��ü�� ã�Ƽ� ������ �� break;
               if (r.room_No == room_No) {
                  break;
               }
            }

            st = new StringTokenizer(str, "/", true); // ��ȹ����"/"�� ��ū���� �����Ѵ�.
            for (int i = 0; i < 6; i++) {
               st.nextToken(); // ��ū ���ſ�
            }
            chattingMsgList = new ArrayList<String>(); // ä�ø޽��� ������ ����Ʈ
            totalChattingMsg = ""; // ��ü ä�� �޽��� ���� ����
            tempMsg = "";
//                        String chattingMsg = st.nextToken(); // �޼��� �κ��� �߶� ����
            while (st.hasMoreTokens()) { // ������ ���� ��ū�� ������ true�� ������ false�� �����Ѵ�.
               tempMsg = st.nextToken();
               System.out.println("ä�� ��ū�� ���:" + tempMsg);
               chattingMsgList.add(tempMsg); // �޽��� ��ū�� ArrayList�� �߰�
            }

            for (int i = 0; i < chattingMsgList.size(); i++) { // chattingMsgList�� ��� �޽����� totalChattingMsg�� �����Ѵ�.
               totalChattingMsg += chattingMsgList.get(i);
            }

            System.out.println("MainServer Inmessage���� protocol�� ChattingPA �϶� ���� ���̵�:" + mUserId);
            gBroadCast(room_No, "Paint/pass/ChattingPA@" + mUserId + "@" + totalChattingMsg);

            // ���� �������� ��� ���þ�� ��ġ�ϴ� Ȯ���Ѵ�.
            if (r.state && r.suggest.equals(totalChattingMsg)) {
               // �ش� �� �����鿡�� �������� ���̵� �����鼭 ���尡 �������� �˸���.(���带 ���� ����)
               // �������� �� ��� true ����
               if(userExpUpdate(r.descriptor, mUserId, room_No)) {
                  gBroadCast(room_No,
                        "Paint/pass/EndRound@" + mUserId + "@" + r.descriptor + "@" + r.round + "@false@true");
               
               } else {
                  gBroadCast(room_No,
                        "Paint/pass/EndRound@" + mUserId + "@" + r.descriptor + "@" + r.round + "@false@false");
               }
               
               r.state = false; // ���� ����

               // �����ڿ� �����ڿ��� ����ġ 10�� ������Ű��, ���� ���� ���� �ϳ��� ������Ų��.
            }
            break;

         // #�ʱ� ���ӹ濡 ���������� ������ �䱸�Ѵ�
         case "GameRoomCheck":
            /*
             * ����ڰ� ó���� ���ӹ濡 �������� �� �ʿ��� �κ��̴�. ���� �濡 �����ϰ� �ִ� �ٸ� ������� ������ ���ӹ��� ������ �ٽ� �����ش�.
             */
            room_No = Integer.parseInt(st.nextToken());

            // ���� ó��, ���� ������ �����ش�.
            for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.elementAt(i);
               if (r.room_No == room_No) { // ���� �� ��ȣ�� ������ ��
                  send_Message("Paint/pass/RoomInfo@" + mUserId + "@" + r.room_name + "@" + r.pwStat + "@"
                        + r.room_PW + "@" + r.fixed_User + "@" + r.Room_user_vc.size() + "@" + r.roomCaptainID);
                  break;
               }
            }

            // �̹� �濡 ������ �ִ� �������� �ڽ��� ������ ������.
            for (int i = 0; i < user_vc.size(); i++) {
               UserInfo u = (UserInfo) user_vc.elementAt(i);
               // ������� ���̵�� ���� ���̵� ã��
               if (mUserId.equals(u.userID)) {
                  // �ش� ������ ������ �� �濡 �ִ� ��ο��� ������.
                  gBroadCast(room_No, "Paint/pass/NewUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + 0); 
                  // ������ ������ 0���� ����
               }
            }

            // �ش� ������ ���ӹ� Room_user_vc�� �߰��Ѵ�.
            Pointer: for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.get(i);
               for (int j = 0; j < user_vc.size(); j++) {
                  UserInfo u = (UserInfo) user_vc.get(j);
                  // �ش� ���̵� ã����
                  if (mUserId.equals(u.getUserID())) {
                     // �ش� ������ �� ���� ����Ʈ�� �߰��Ѵ�.
                     r.Room_user_vc.add(u);
                     break Pointer;
                  }
               }
            }

            // �̹� ���� ���� ������ ������ �ش� �������� ������ ���� ������.(���⿡ �ڽ��� ������ ���ԵǾ� ����)
            for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.elementAt(i);
               if (r.room_No == room_No) { // ���� �� ��ȣ�� ������ ��
                  // �ش� �濡 �ִ� �������� ������ ������.
                  for (int j = 0; j < r.Room_user_vc.size(); j++) {
                     UserInfo u = (UserInfo) r.Room_user_vc.get(j);
                     // ������ ������ 0���� �����Ѵ�.
                     send_Message("Paint/pass/OldUser@" + u.userID + "@" + u.level + "@" + u.exp + "@" + 0); 

                     // �׽�Ʈ �ڵ�
                     System.out.println("�ִ� :" + r.fixed_User + ", ���� : " + r.Room_user_vc.size());

                     // �ִ� �ο��� ���� �ο��� ���� ���, �޽����� ������ �������� �� ��ü �ο��� ������� ���� ������ �˸���.
                     if (r.fixed_User == r.Room_user_vc.size() && j == r.Room_user_vc.size() - 1) {
                        gBroadCast(room_No, "Paint/pass/GameStart@pass@");
                     }
                  }
                  break;
               }
            }

            // ������ ������ ������ ���ǿ��� �ο��� ������ �˷���� �Ѵ�.
            for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.elementAt(i);
               if (r.room_No == room_No) { // ���� �� ��ȣ�� ������ ��
                  // �ش� ��ε� ĳ��Ʈ�� �޴°� ������ ���ӹ� �г��� �����Ѵ�.
                  BroadCast("WaitingRoom/pass/RoomInfoUpdate@pass@"+r.room_No+"@"+r.Room_user_vc.size());
               }
            }

            break;

         // # ���带 �����Ѵ�.
         case "RoundStart":
            // ���� ������ ��û�� ���ӹ��� ��ȣ�� �޴´�.
            room_No = Integer.parseInt(st.nextToken());
            UserInfo nextU = null;

            // �ش� ���� ������ �����´�.
            r = null;
            for (int i = 0; i < room_vc.size(); i++) {
               r = (RoomInfo) room_vc.get(i);
               // ���� ���ȣ�� ã���� ������ �� break
               if (r.room_No == room_No) {
                  break;
               }
            }
            r.state = true; // ������ ���·� ��ȯ

            r.round++; // round 1�� ����
            // ���尡 12 ���ϸ� ���� ������ ����������
            if (r.round <= 12) {
               if (r.round == 1) { // 1���� �ϋ�,
                  r.question = new Question(); // �� ��ü�� Question ��ü�� ����
               }

               // ���þ�� ���� ���ڸ� �����Ѵ�. 1������� �����ϹǷ� �μ��� 1
               r.suggest = r.question.selQuestion(r.round);

               // ������ �˾Ƴ������� ������ ����Ѵ�. (��� ���忡 ���밡��)
               int index = r.Room_user_vc.size() - (12 - r.round) % r.Room_user_vc.size() - 1;

               // �˾Ƴ� �ε��� ��ȣ�� ������ �����´�.
               UserInfo u = (UserInfo) r.Room_user_vc.get(index);

               // �ش� ������ descriptor��� ���� �����Ѵ�.
               r.descriptor = u.userID;

               if (r.round != 12) {
                  // ���� ���� ������ ������ ����.
                  int nextIndex = r.Room_user_vc.size() - (12 - (r.round + 1)) % r.Room_user_vc.size() - 1;
                  nextU = (UserInfo) r.Room_user_vc.get(nextIndex);

                  // �ش� �������Ը� �ڽ��� �������� �˸��� ������ �����鿡�Դ� ������ ���ߵ��� �Ѵ�.(������ ���� ������׸� ���þ �ѱ�)
                  gSelectiveCast(room_No, u.userID, true,
                        "Paint/pass/YourTurn@pass@" + u.userID + "@" + nextU.userID + "@" + r.suggest); // ������
                                                                                    // �������Ը�

                  gSelectiveCast(room_No, u.userID, false,
                        "Paint/pass/Solve@pass@" + u.userID + "@" + nextU.userID); // ������ �������Ը�

               } else { // ������ ������ ���
                  gSelectiveCast(room_No, u.userID, true,
                        "Paint/pass/YourTurn@pass@" + u.userID + "@[����������]@" + r.suggest); // ������ �������Ը�
                  gSelectiveCast(room_No, u.userID, false, "Paint/pass/Solve@pass@" + u.userID + "@[����������]"); // ������
                                                                                          // �������Ը�

               }

            } else {
               // �濡 �ִ� �����鿡�� ������ �������� �˸���.
               for (int i = 0; i < room_vc.size(); i++) {
                  RoomInfo room = (RoomInfo) room_vc.get(i);
                  if (room.room_No == room_No) {
                     for (int j = 0; j < room.Room_user_vc.size(); j++) {
                        UserInfo user = (UserInfo) room.Room_user_vc.get(j);
                        System.out.println(user);

                        user.send_Message("Paint/pass/GameOver@" + user.userID);

                     }
                  }
               }
            }

            break;

         case "GiveUp":
            room_No = Integer.parseInt(st.nextToken());
            int round = Integer.parseInt(st.nextToken());
            String giveUp_Sel = st.nextToken();

            gBroadCast(room_No, "Paint/pass/EndRound@pass@pass@" + round + "@" + giveUp_Sel + "@false");
            break;

         // #������ �������Ƿ� ���Ƿ� �̵���Ų��.
         case "GameOver":
            room_No = Integer.parseInt(st.nextToken());
            String roomCaptain = st.nextToken(); // �������� �ƴ���

            // �ڲ� ���� �ҷ����� �����ϴµ�, ó���� �α����ϴ� ������� ���� �����Ű�� ����� �������.

            // �ش� ������ ������ ������Ͽ��� �����Ѵ�.
            for (int i = 0; i < user_vc.size(); i++) {
               UserInfo u = (UserInfo) user_vc.get(i);
               if (u.userID.equals(mUserId)) {
                  // �ش� ������ ã������ ����Ʈ���� �����Ѵ�.
                  user_vc.remove(i);
                  System.out.println("������ : " + user_vc.size());
               }
            }

            for (int i = 0; i < room_vc.size(); i++) {
               // ���� ������ ������
               r = (RoomInfo) room_vc.get(i);
               System.out.println(r);
               if (r.room_No == room_No) {
                  // �ش� ���� ã�Ƽ� �����Ѵ�.
                  room_vc.remove(i);
                  System.out.println("�� ���� :" + room_vc.size());
               }
            }

            for (int i = 0; i < allUser_vc.size(); i++) {
               User user = (User) allUser_vc.elementAt(i);
               // �ش� ������ ã�´�.
               System.out.println(user);
               System.out.println("user.getId : " + user.getId() + ", mUserId : " + mUserId);
               if (user.getId().equals(mUserId)) {
                  System.out.println(mUserId + " üũ�� !!!");
                  send_Message("LoginOK/ok");
                  userAdd(mUserId);
                  break;
               }
            }
            
            // ���� ���� �� �������� ������ db�� �����Ѵ�.
            new UserController().dataSaveAll(allUser_vc);
            
            break;

         // #����ڰ� �׸��� �׸���
         case "GameRoomPaint":
            int gameRoomNo = Integer.parseInt(st.nextToken());
            String mouseState = st.nextToken();
            if (mouseState.equals("mousePress")) {
               String receiveColor = st.nextToken();
               System.out.println("MainServer���� ���� ���ȣ: " + gameRoomNo + "�� �÷�:" + receiveColor);
               gSelectiveCast(gameRoomNo, mUserId, false, "GameRoomPaint/pass/mousePress/" + receiveColor);
            } else if (mouseState.equals("mouseRelease"))
               gSelectiveCast(gameRoomNo, mUserId, false, "GameRoomPaint/pass/mouseRelease");
            else if (mouseState.equals("mouseDrag")) {
               String pointX1 = st.nextToken();
               String pointY1 = st.nextToken();
               String pointX2 = st.nextToken();
               String pointY2 = st.nextToken();
               String receiveThick = st.nextToken();
               String receiveEraserSel = st.nextToken();

               System.out.println("MainServer���� ���� ���ȣ:" + gameRoomNo + ", x1��ǥ:" + pointX1 + ", y1��ǥ:" + pointY1
                     + ", x2��ǥ:" + pointX2 + ", y2��ǥ:" + pointY2 + ", �� ����: " + receiveThick + ", ���찳 ���ÿ���: "
                     + receiveEraserSel);
               gSelectiveCast(gameRoomNo, mUserId, false, "GameRoomPaint/pass/mouseDrag/" + pointX1 + "/" + pointY1
                     + "/" + pointX2 + "/" + pointY2 + "/" + receiveThick + "/" + receiveEraserSel);

            } else if (mouseState.equals("canvasClear"))
               gSelectiveCast(gameRoomNo, mUserId, false, "GameRoomPaint/pass/canvasClear");
            break;
         }
      }

      // ���ڿ� ���� �޼ҵ�
      // ���� ����ڿ��� �޽����� ������ �κ�
      private void BroadCast(String str) {
         for (int i = 0; i < wRoom_vc.size(); i++) // ���� ���ӵ� ����ڿ��� ����
         {
            UserInfo u = (UserInfo) wRoom_vc.elementAt(i); // i������ �ִ� ����ڿ��� �޼����� ����
            System.out.println("[" + u.getUserID() + "]���� : " + str);
            u.send_Message(str);
         }
      }

      // ���ӹ濡 �ִ� �����鿡�� �޽����� ������ �κ�
      private void gBroadCast(int room_No, String str) {
         for (int i = 0; i < room_vc.size(); i++) // ���ӹ濡 �ִ� ����ڿ��� ����
         {
            RoomInfo r = (RoomInfo) room_vc.elementAt(i); // i��°�� �ִ� ���� ã��
            // �Է¹��� �� ��ȣ�� ��ġ�ϴ� �� ��ȣ�� ã����
            if (room_No == r.room_No) {
               // �ش� �濡 �ִ� ���� �������� �޽����� ������.
               for (int j = 0; j < r.Room_user_vc.size(); j++) {
                  // ���ӹ��� �ִ� ���� �Ѹ��� ã�Ƽ�
                  UserInfo u = (UserInfo) r.Room_user_vc.get(j);
                  System.out.println("[" + u.getUserID() + "]���� : " + str);
                  // �޽����� ������.
                  u.send_Message(str);
               }
            }
         }
      }

      // �ΰ��ӿ��� �׸��� �׸��� ���, Ȥ�� ���� �� Ư���� �ο��� �����ϰ� Ȥ�� �� �ο��� ���������� �Ҷ� ���
      /*
       * [�޼ҵ� ����] room_No�� �ش� ���ӹ��� ã�� �� ����ϴ� �μ� id�� �������� �ɼǿ� ������ ��� id flag�� 'true'�̸�
       * �ش� �������Ը�, 'false'�̸� �ش������� ������ ���������� str�� ���� �޽����� �����Ѵ�.
       */
      private void gSelectiveCast(int room_No, String id, boolean flag, String str) {
         Pointer: for (int i = 0; i < room_vc.size(); i++) // ���ӹ濡 �ִ� ����ڿ��� ����
         {
            RoomInfo r = (RoomInfo) room_vc.elementAt(i); // i��°�� �ִ� ���� ã��
            // �Է¹��� �� ��ȣ�� ��ġ�ϴ� �� ��ȣ�� ã����
            if (room_No == r.room_No) {
               // �ش� �濡 �ִ� �������� �޽����� ������.
               for (int j = 0; j < r.Room_user_vc.size(); j++) {
                  // ���ӹ��� �ִ� ���� �Ѹ��� ã�Ƽ�
                  UserInfo u = (UserInfo) r.Room_user_vc.get(j);

                  // �ش� �������Ը� �޽����� ���� ��
                  if (id.equals(u.getUserID()) && flag) {
                     System.out.println("[" + u.getUserID() + "]���� : " + str);
                     // �޽����� ������.
                     u.send_Message(str);
                     break Pointer; // �ش� �������Ը� �޽����� ������ �ǹǷ� ��ü break ó��
                  }
                  // �ش� ������ ������ ������ �ο����� �޽����� ���� ��
                  else if (!id.equals(u.getUserID()) && !flag) {
                     System.out.println("[" + u.getUserID() + "]���� : " + str);
                     // �޽����� ������.
                     u.send_Message(str);
                  }

               }
            }
         }
      }

      // �����ʿ����� Ŭ���̾�Ʈ�� ��ȭ�� �� �ִ� �޼ҵ�
      private void send_Message(String str) // ���ڿ��� �޾Ƽ� ����
      {
         try {
            dos.writeUTF(str);

         } catch (IOException e) {
            e.printStackTrace();
         }

      }
      
      
      // �����ڿ� �������� ����ġ�� ���� ������ �÷��ֱ� ���� �޼ҵ�
      private boolean userExpUpdate(String descriptor, String userID, int room_No) {
         boolean dLevelUp = false;
         boolean uLevelUp = false;
         User desUser = null;
         User user = null;

         // ȸ�� ��ü ������ �߿���
         for (int i = 0; i < allUser_vc.size(); i++) {
            user = (User) allUser_vc.get(i);
            // �ش� ������ ã�Ƽ�
            if (userID.equals(user.getId())) {
               // �������� Ȯ���Ѵ�.
               System.out.println(user.getId() + "��, ó�� �� ���� : " + user.getLevel() + ", ����ġ : " + user.getExp()
                     + ", ���� ����� :" + user.getCorAnswer());
               dLevelUp = user.expUpdate(true);
               System.out.println(user.getId() + "��, ó�� �� ���� : " + user.getLevel() + ", ����ġ : " + user.getExp()
                     + ", ���� ����� :" + user.getCorAnswer());
               allUser_vc.set(i, user); // ����
               break;
            }
         }
         for (int i = 0; i < allUser_vc.size(); i++) {
            desUser = (User) allUser_vc.get(i);
            // �ش� ������ ã�Ƽ�
            if (descriptor.equals(desUser.getId())) {
               // �������� Ȯ���Ѵ�.
               System.out.println(desUser.getId() + "��, ó�� �� ���� : " + desUser.getLevel() + ", ����ġ : "
                     + desUser.getExp() + ", ���� ����� :" + desUser.getCorAnswer());
               uLevelUp = desUser.expUpdate(false);
               System.out.println(desUser.getId() + "��, ó�� �� ���� : " + desUser.getLevel() + ", ����ġ : "
                     + desUser.getExp() + ", ���� ����� :" + desUser.getCorAnswer());
               allUser_vc.set(i, desUser); // ����
               break;
            }
         }
         // ���� �� ���̶� �������� �� ��, ������ �̺�Ʈ�� ����

         // �Է¹��� ���ȣ�� ���� ã�´�.
         for (int i = 0; i < room_vc.size(); i++) {
            RoomInfo r = (RoomInfo) room_vc.get(i);
            // ��ġ�ϴ� ���� ã����
            if (room_No == r.room_No) {
               // ������
               for (int j = 0; j < r.Room_user_vc.size(); j++) {
                  UserInfo ui = (UserInfo) r.Room_user_vc.get(j);
                  // �ش� ������ ã�Ƽ�
                  if (userID.equals(ui.userID)) {
                     // ����ġ�� �����Ǿ����� �˸���.(DB�� �����ִ� ���ͷκ��� �����´�. ���� DB)
                     gBroadCast(room_No, "Paint/pass/ExpUpdate@" + ui.userID + "@" + user.getLevel() + "@"
                           + user.getExp() + "@" + user.getCorAnswer() + "@1"); // �����ڴ� ���䰹�� 1����
                     System.out.println(user.getId() + "��, �޽��� üũ ���� : " + user.getLevel() + ", ����ġ : "
                           + user.getExp() + ", ���� ����� :" + user.getCorAnswer());
                     break;
                  }
               }

               // ������
               for (int j = 0; j < r.Room_user_vc.size(); j++) {
                  UserInfo ui = (UserInfo) r.Room_user_vc.get(j);
                  // �ش� ������ ã�Ƽ�
                  if (descriptor.equals(ui.userID)) {
                     // ����ġ�� �����Ǿ����� �˸���.(DB�� �����ִ� ���ͷκ��� �����´�. ���� DB)
                     gBroadCast(room_No, "Paint/pass/ExpUpdate@" + ui.userID + "@" + desUser.getLevel() + "@"
                           + desUser.getExp() + "@" + user.getCorAnswer() + "@0"); // �����ڴ� ���䰳�� 0 ����
                     // �������� �Ͽ��� ���
                     System.out.println(desUser.getId() + "��, �޽��� üũ ���� : " + desUser.getLevel() + ", ����ġ : "
                           + desUser.getExp() + ", ���� ����� :" + desUser.getCorAnswer());
                     break;
                  }
               }
            }
         }
         // �������� �Ͽ��� ���
         if (uLevelUp || dLevelUp) {
            System.out.println("������ �������� �Ͽ����ϴ�");
            return true;
//            gBroadCast(room_No, "Paint/pass/UserLevelUp@pass@");
         } else {
            return false;
         }
      }

	@Override
	public String toString() {
		return "UserInfo [os=" + os + ", is=" + is + ", oos=" + oos + ", ois=" + ois + ", dos=" + dos + ", dis=" + dis
				+ ", user_socket=" + user_socket + ", userID=" + userID + ", level=" + level + ", exp=" + exp
				+ ", corAnswer=" + corAnswer + ", room_No=" + room_No + ", loginState=" + loginState + ", RoomCh="
				+ RoomCh + "]";
	}
      
      
      
      
   } // UserInfo class ��

   // ���ӹ�
   class RoomInfo {
      private int room_No; // ���ӹ� ��ȣ
      private String room_name; // ���ӹ� �̸�
      private String pwStat; // ����/ �����
      private String room_PW; // ���ӹ� ��й�ȣ(������ ��� null)
      private int fixed_User; // ���� ����

      private Question question; // ���þ� ��ü ����� ����
      private String roomCaptainID; // ���� id
      private String descriptor; // ������ �������ִ� ���
      private String trun; // ���� �׸��� ����id
      private String suggest; // ���� ���þ�
      private int round = 0; // ���� ���� round(�ʱⰪ 0)
      private boolean state = false; // ���� ���� ���¸� Ȯ���Ѵ�.(true - ������ )

      /*
       * ������ �� ������ ������ ��ü�� ���޹޾� ����Ʈ�� �����ϰ� �����ϴ� �������� ���� ��ü�� user_vc���� �����ϰ� room_vc�� �ű��
       * �۾��� �Ѵ�. �׸��� �ش� ���� ���� ���� �����ؾ��� ��, Room_user_vc�� ����� �����Ѵ�.
       */
      private Vector<UserInfo> Room_user_vc = new Vector<UserInfo>(); // ���ӹ� ���� Vector

      RoomInfo(int room_No, String room_name, String pwStat, String room_PW, int fixed_User) // ���ȣ�� ��������!
      {
         this.room_No = room_No;
         this.room_name = room_name;
         this.pwStat = pwStat;
         this.room_PW = room_PW;
         this.fixed_User = fixed_User;
      }

//         public void BroadCast_Room(String str) // ���� ���� ��� ������� �˸���.
//         {
//            for (int i = 0; i < Room_user_vc.size(); i++) {
//               UserInfo u = (UserInfo) Room_user_vc.elementAt(i);
      //
//               u.send_Message(str); // �Ѿ�� ���ڿ��� �����ش�.
//            }
//         }
   }
}