package kh.mini.project.model.vo;

import javax.swing.ImageIcon;

import kh.mini.project.main.view.Main;

//Server�ʹ� �ٸ��� �����κ��� ���� �������� ������ �ӽ������� �����ϱ� ���� Ŭ����(���� ����� Ŭ����)
public class UserInfo {
	/* �ش� Ŭ������ ������ ������ Ŭ���̾�Ʈ���� ����ϴ� �뵵��
	 * ������ ������ �ִ� �������� �޾ƿ� �ʿ��� ������ ����ϱ� ���� Ŭ�����̴�.
	 * ������ �����ϸ鼭 ������Ʈ �Ǵ� �κа� ������������ ����ġ �� ���� �����ϱ� ���� Ŭ�����̱⵵ �ϴ�.
	 */
	
	private String userID; // ������� ID 
	private int level; // ����
	private int exp; // ����ġ
	private int corAnswer; // ���� ���� ����
	private int nowAnswer=0; // ���� ���� ����
	
	private ImageIcon charImg; // ������ ���� ĳ���� �̹���
	private ImageIcon gradeImg; // ������ ���� ��� �̹���
	
	// ��� �̹��� ����
	private static ImageIcon[] gradeImgArr = new ImageIcon[13];
	private static ImageIcon[] charImgArr = new ImageIcon[5];
	
	
	static
	{
		// ��� �̹��� �ʱ�ȭ 
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
		
		// ĳ���� �̹��� �ʱ�ȭ
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
		
		// ������ ���� �̹��� ����
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
		/* ������ ���� ĳ���� �̹��� ���� ��� */
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
		/* ������ ���� ��� �̹��� ���� ��� */
		gradeImg = getGrade(level);
	}

	// ���� ����ġ�� �����Ͽ� �������� �� �� ������ ���� �޼ҵ�
	public void levelUp() {
		
		// ������ �� ������ ���� �� �����Ƿ�
		// ������ ���� �̹��� ����
		setCharImg(level);
		setGradeImg(level);
	}
	
	// ������ �ش��ϴ� ��� �̹����� ��ȯ�Ѵ�.
	public static ImageIcon getGrade(int level) {
		return gradeImgArr[level-1];
	}
	
	@Override
	public String toString() {
		return "UserInfo [userID=" + userID + ", level=" + level + ", exp=" + exp + ", corAnswer=" + corAnswer + "]";
	}
	
	
}
