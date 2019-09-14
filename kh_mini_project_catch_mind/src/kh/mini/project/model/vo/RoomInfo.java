package kh.mini.project.model.vo;

import java.util.Vector;

// Server와는 다르게 서버로부터 받은 유저들의 정보를 임시적으로 저장하기 위한 클래스(정보 저장용 클래스)
public class RoomInfo {
   /* 개설된 게임 방의 정보를 저장하는 용도의 클래스  */
   
   private int room_No; // 게임방 번호
   private String room_name; // 게임방 이름
   private String state; // 비밀번호 유무
   private String room_PW; // 게임방 비밀번호(공개일 경우 null)
   private int fixed_User; // 유저 정원
   // 유저수를 Room_user_vc의 사이즈로 가져오는 방법을 고안중
   private Vector<UserInfo> Room_user_vc = new Vector<UserInfo>(); // 게임방 유저 Vector
   private int room_UCount; // 유저 수(User Count)
   private String roomCaptainID; // 방장 id
   private String trun; // 현재 그리는 유저id
   private int round; // 현재 게임 round
   
   /* 유저의 초기 세팅 작업에서는 기존 게임방에 들어가있는 모든 유저의 정보까지는 필요없으므로
    * 인원수만 전달받아서 방을 표시하는 작업을 위해 아래 생성자가 필요하다 판단.*/
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