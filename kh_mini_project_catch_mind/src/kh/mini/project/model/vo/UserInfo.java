package kh.mini.project.model.vo;

import javax.swing.ImageIcon;

import kh.mini.project.main.view.Main;

//Server와는 다르게 서버로부터 받은 유저들의 정보를 임시적으로 저장하기 위한 클래스(정보 저장용 클래스)
public class UserInfo {
	/* 해당 클래스는 서버를 제외한 클라이언트에서 사용하는 용도로
	 * 서버가 가지고 있는 정보들을 받아와 필요한 정보만 사용하기 위한 클래스이다.
	 * 게임을 진행하면서 업데이트 되는 부분과 레벨구간마다 경험치 양 등을 지정하기 위한 클래스이기도 하다.
	 */
	
	private String userID; // 사용자의 ID 
	private int level; // 레벨
	private int exp; // 경험치
	private int corAnswer; // 누적 정답 개수
	private int nowAnswer=0; // 현재 정답 개수
	
	private ImageIcon charImg; // 레벨에 따른 캐릭터 이미지
	private ImageIcon gradeImg; // 레벨에 따른 등급 이미지
	
	// 등급 이미지 저장
	private static ImageIcon[] gradeImgArr = new ImageIcon[13];
	private static ImageIcon[] charImgArr = new ImageIcon[5];
	
	
	static
	{
		// 등급 이미지 초기화 
		gradeImgArr[0] = new ImageIcon(Main.class.getResource("/images/F.PNG"));
		gradeImgArr[1] = new ImageIcon(Main.class.getResource("/images/Dm.PNG"));
		gradeImgArr[2] = new ImageIcon(Main.class.getResource("/images/D.PNG"));
		gradeImgArr[3] = new ImageIcon(Main.class.getResource("/images/Dp.PNG"));
		gradeImgArr[4] = new ImageIcon(Main.class.getResource("/images/Cm.PNG"));
		gradeImgArr[5] = new ImageIcon(Main.class.getResource("/images/C.PNG"));
		gradeImgArr[6] = new ImageIcon(Main.class.getResource("/images/Cp.PNG"));
		gradeImgArr[7] = new ImageIcon(Main.class.getResource("/images/Bm.PNG"));
		gradeImgArr[8] = new ImageIcon(Main.class.getResource("/images/B.PNG"));
		gradeImgArr[9] = new ImageIcon(Main.class.getResource("/images/Bp.PNG"));
		gradeImgArr[10] = new ImageIcon(Main.class.getResource("/images/Am.PNG"));
		gradeImgArr[11] = new ImageIcon(Main.class.getResource("/images/A.PNG"));
		gradeImgArr[12] = new ImageIcon(Main.class.getResource("/images/Ap.PNG"));
		
		// 캐릭터 이미지 초기화
		charImgArr[0] = new ImageIcon(Main.class.getResource("/images/character_1.PNG"));
		charImgArr[1] = new ImageIcon(Main.class.getResource("/images/character_2.PNG"));
		charImgArr[2] = new ImageIcon(Main.class.getResource("/images/character_3.PNG"));
		charImgArr[3] = new ImageIcon(Main.class.getResource("/images/character_4.PNG"));
		charImgArr[4] = new ImageIcon(Main.class.getResource("/images/character_5.PNG"));
	}
	
	public UserInfo(String userID, int level, int exp, int corAnswer) {
		super();
		this.userID = userID;
		this.level = level;
		this.exp = exp;
		this.corAnswer = corAnswer;
		
		// 레벨에 따른 이미지 저장
		setCharImg(level);
		setGradeImg(level);
	}

	public String getUserID() {
		return userID;
	}

	public int getLevel() {
		return level;
	}

	public void setlevel(int level) {
		this.level = level;
	}
	
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCorAnswer() {
		return corAnswer;
	}

	public void setCorAnswer(int corAnswer) {
		this.corAnswer += corAnswer;
	}
	
	public int getNowAnswer() {
		return nowAnswer;
	}

	public void setNowAnswer(int nowAnswer) {
		this.nowAnswer = nowAnswer;
	}
	
	public ImageIcon getCharImg() {
		setCharImg(level);
		return charImg;
	}

	public void setCharImg(int level) {
		/* 레벨에 따른 캐릭터 이미지 저장 방식 */
		switch(level) {
		case 1 : charImg = charImgArr[0]; break;
		case 2 : case 3 : case 4 : charImg = charImgArr[1]; break;
		case 5 : case 6 : case 7 : charImg = charImgArr[2]; break;
		case 8 : case 9 : case 10 : charImg = charImgArr[3]; break;
		case 11 : case 12 : case 13 : charImg = charImgArr[4]; break;
		}
	}

	public ImageIcon getGradeImg() {
		setGradeImg(level);
		return gradeImg;
	}

	public void setGradeImg(int level) {
		/* 레벨에 따른 등급 이미지 저장 방식 */
		gradeImg = getGrade(level);
	}

	// 일정 경험치에 도달하여 레벨업을 할 시 동작을 위한 메소드
	public void levelUp() {
		
		// 레벨업 시 변동이 있을 수 있으므로
		// 레벨에 따른 이미지 저장
		setCharImg(level);
		setGradeImg(level);
	}
	
	// 레벨에 해당하는 등급 이미지를 반환한다.
	public static ImageIcon getGrade(int level) {
		return gradeImgArr[level-1];
	}
	
	@Override
	public String toString() {
		return "UserInfo [userID=" + userID + ", level=" + level + ", exp=" + exp + ", corAnswer=" + corAnswer + "]";
	}
	
	
}
