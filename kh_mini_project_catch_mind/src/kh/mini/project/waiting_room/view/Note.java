package kh.mini.project.waiting_room.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import kh.mini.project.main.view.Main;

public class Note extends JFrame {
	private Image img = 
			new ImageIcon(Main.class.getResource("/images/Note2.png")).getImage();
	// ���� ����
		private Image viewImage; // �̹��� ����� ����
		private Graphics viewGraphics; // �׷��� ����� ����	
		private int mouseX, mouseY; // ���콺 ��ǥ�� ����
	
	public Note() {
		setUndecorated(true); // ������ Ÿ��Ʋ �� ����(�����츦 ������)
		setResizable(false); // ������ ũ�� ����
		setLocationRelativeTo(null); // �����츦 ȭ�� ���߾ӿ� ���� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ ����� �����ִ� ���μ����� �����ϰ� �����ϱ� ����
		setBackground(new Color(0,0,0,0)); // ������ �����ϰ� �Ѵ�.(paint()�޼ҵ�� �׸��� ����� ���̰� �ϱ� ����)
		setSize(352,223); // null�� �ִ�
//		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null); // �гο� �߰��ϴ� ��ҵ��� ��ġ�� �����Ӱ� �����ϱ� ���� Layout�� null�� ���ش�.
		setVisible(true); // �����츦 �� �� ����.
		
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

	}
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(352,223);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(img, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}	
	
	public static void main(String[] args) {
		new Note();
	}
}