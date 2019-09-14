package kh.mini.project.model.vo;

import java.util.Vector;

// Server�ʹ� �ٸ��� �����κ��� ���� �������� ������ �ӽ������� �����ϱ� ���� Ŭ����(���� ����� Ŭ����)
public class RoomInfo {
   /* ������ ���� ���� ������ �����ϴ� �뵵�� Ŭ����  */
   
   private int room_No; // ���ӹ� ��ȣ
   private String room_name; // ���ӹ� �̸�
   private String state; // ��й�ȣ ����
   private String room_PW; // ���ӹ� ��й�ȣ(������ ��� null)
   private int fixed_User; // ���� ����
   // �������� Room_user_vc�� ������� �������� ����� �����
   private Vector<UserInfo> Room_user_vc = new Vector<UserInfo>(); // ���ӹ� ���� Vector
   private int room_UCount; // ���� ��(User Count)
   private String roomCaptainID; // ���� id
   private String trun; // ���� �׸��� ����id
   private int round; // ���� ���� round
   
   /* ������ �ʱ� ���� �۾������� ���� ���ӹ濡 ���ִ� ��� ������ ���������� �ʿ�����Ƿ�
    * �ο����� ���޹޾Ƽ� ���� ǥ���ϴ� �۾��� ���� �Ʒ� �����ڰ� �ʿ��ϴ� �Ǵ�.*/
   public RoomInfo(int room_No, String room_name, String state, String room_PW, int room_UCount, int fixed_User) {
      this.room_No = room_No;
      this.room_name = room_name;
      this.state = state;
      this.room_PW = room_PW;
      this.room_UCount = room_UCount;
      this.fixed_User = fixed_User;
   }

   public RoomInfo(int room_No, String room_name, String room_PW,int fixed_User, UserInfo u) {
      this.room_No = room_No;
      this.room_name = room_name;
      this.room_PW = room_PW;
      this.fixed_User = fixed_User;
      
      Room_user_vc.add(u);
   }
   
   
   public int getRoom_No() {
      return room_No;
   }

   public String getRoomCaptainID() {
      return roomCaptainID;
   }

   public void setRoomCaptainID(String roomCaptainID) {
      this.roomCaptainID = roomCaptainID;
   }

	public String getTrun() {
		return trun;
	}

   public void setTrun(String trun) {
      this.trun = trun;
   }

   public int getRound() {
      return round;
   }

   public void setRound(int round) {
      this.round = round;
   }

   public void setRoom_No(int room_No) {
      this.room_No = room_No;
   }

   public String getRoom_name() {
      return room_name;
   }

   public void setRoom_name(String room_name) {
      this.room_name = room_name;
   }

   public String getRoom_PW() {
      return room_PW;
   }

   public void setRoom_PW(String room_PW) {
      this.room_PW = room_PW;
   }

   public int getRoom_UCount() {
      return room_UCount;
//      return Room_user_vc.size();
   }

   public void setRoom_UCount(int room_UCount) {
      this.room_UCount = room_UCount;
   }

   public int getFixed_User() {
      return fixed_User;
   }

   public void setFixed_User(int fixed_User) {
      this.fixed_User = fixed_User;
   }

	public void setRoom_user_vc(Vector<UserInfo> room_user_vc) {
		Room_user_vc = room_user_vc;
	}
   
   public void addRoom_user_vc(UserInfo u) {
      Room_user_vc.add(u);
   }
   
   public Vector getRoom_user_vc() {
      return Room_user_vc;
   }

   @Override
   public String toString() {
      return "RoomInfo [room_No=" + room_No + ", room_name=" + room_name + ", room_PW=" + room_PW + ", fixed_User="
            + fixed_User + ", Room_user_vc=" + Room_user_vc + ", room_UCount=" + room_UCount + "]";
   }
   

}