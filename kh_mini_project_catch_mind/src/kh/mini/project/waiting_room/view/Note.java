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
	// 각종 변수
		private Image viewImage; // 이미지 저장용 변수
		private Graphics viewGraphics; // 그래픽 저장용 변수	
		private int mouseX, mouseY; // 마우스 좌표용 변수
	
	public Note() {
		setUndecorated(true); // 프레임 타이틀 바 제거(윈도우를 제거함)
		setResizable(false); // 프레임 크기 고정
		setLocationRelativeTo(null); // 윈도우를 화면 정중앙에 띄우기 위함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 종료시 남아있는 프로세스도 깨끗하게 종료하기 위함
		setBackground(new Color(0,0,0,0)); // 배경색을 투명하게 한다.(paint()메소드로 그리는 배경을 보이게 하기 위함)
		setSize(352,223); // null은 최댓값
//		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null); // 패널에 추가하는 요소들의 위치를 자유롭게 설정하기 위해 Layout을 null로 해준다.
		setVisible(true); // 윈도우를 볼 수 있음.
		
		// 마우스로 창을 움직일 수 있다.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			// #매뉴바 드래그 시, 움직일 수 있게 한다.
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