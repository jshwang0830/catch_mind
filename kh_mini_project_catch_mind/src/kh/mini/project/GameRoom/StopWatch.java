package kh.mini.project.GameRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kh.mini.project.main.view.Main;

import javax.swing.ImageIcon;

public class StopWatch extends JPanel{
	
	private static long currentTime=0l, preTime=0l;
	private int minute=0;
	private int sec=0;
	private int mSec=0;
	private JLabel minuteLb;
	private JLabel secLb;
	private JLabel mSecLb;
	private Image viewImage; // �̹��� ����� ����
	private Graphics viewGraphics;
	
	//�ӽ� ����� ����(�ҿ�ð� ��ȯ�� ���� ����)
	private String tempMinute;
	private String temSec;
	private String tempMSec;

	Image background = new ImageIcon(Main.class.getResource("/images/stopwatchBackground.png")).getImage();
	
	timeThread timeT;
	
	public StopWatch() {		
		setBounds(100,100,252,99);
		setOpaque(true);
		setBackground(Color.white);
		setLayout(null);
		
		minuteLb = new JLabel(String.valueOf(minute));
		minuteLb.setBounds(14, 10, 71, 80);
		minuteLb.setFont(new Font("Dialog", Font.BOLD, 40));
		add(minuteLb);
		minuteLb.setVisible(true);
		
		secLb = new JLabel(String.valueOf(sec));
		secLb.setBounds(109, 10, 71, 80);
		add(secLb);
		secLb.setFont(new Font("Dialog", Font.BOLD, 40));
		secLb.setVisible(true);
		
		mSecLb = new JLabel(String.valueOf(mSec));
		mSecLb.setBounds(194, 31, 50, 44);
		mSecLb.setFont(new Font("Times", Font.BOLD, 30));
		add(mSecLb);
		
		JLabel label = new JLabel(":");
		label.setFont(new Font("����", Font.PLAIN, 40));
		label.setBounds(65, 18, 25, 61);
		add(label);
		
		JLabel label_1 = new JLabel(".");
		label_1.setFont(new Font("����", Font.PLAIN, 40));
		label_1.setBounds(160, 8, 71, 80);
		add(label_1);
		mSecLb.setVisible(true);
		

		
		setVisible(true);
	}

	class timeThread extends Thread{
		@Override
		public void run() {
			try{	
				while(!Thread.currentThread().isInterrupted()) {
					currentTime=System.currentTimeMillis() - preTime;	
					
					printTime(currentTime);
					repaint();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
			
	}
	
	public void printTime(long currentTime){
		
		mSec=(int)currentTime%1000/10;
		sec=(int)currentTime /1000 %60;
		minute=(int)currentTime /60000 %60;
		
		minuteLb.setText(String.valueOf(minute));
		secLb.setText(String.valueOf(sec));
		mSecLb.setText(String.valueOf(mSec));
		
		//�׽�Ʈ
		//System.out.println(minute + " : " +sec+"."+mSec);
		
	}

	public String getTime() {
		return tempMinute + ":" + temSec + "." + tempMSec;
	}

	//��ž��ġ start �޼ҵ�
	public void startClock() {
		preTime = System.currentTimeMillis();
		timeT= new timeThread();
		timeT.start();
	}
	
	//��ž��ġ stop �޼ҵ�
	public void stopClock() {
		if(timeT.isAlive()) {
			timeT.interrupt();
			currentTime=0l;
			
			// �ҿ�ð� ��ȯ�� ���� �ش� ���� �ӽ÷� �����Ѵ�.
			tempMinute = String.valueOf(minute);
			temSec = String.valueOf(sec);
			tempMSec = String.valueOf(mSec);
			
			printTime(currentTime); //��ž��ġ ���߸� �ٽ� 0���� ����
		}
	}
	
	@Override
	public void paint(Graphics g) {
		viewImage = createImage(252, 99);
		viewGraphics = viewImage.getGraphics();
		screenDraw(viewGraphics);
		g.drawImage(viewImage,0,0, null);
	}
	
	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		this.repaint();
	}
}

	



	