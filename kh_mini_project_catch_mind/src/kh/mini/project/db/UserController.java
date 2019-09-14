package kh.mini.project.db;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import kh.mini.project.main.view.Main;
import kh.mini.project.model.vo.User;


public class UserController {
		
		
	//userDB�� ��� ���� �����͸� �����ϴ� �޼ҵ�
	public int dataSaveAll(Vector allUser_vc) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userDB.dat"))) {
			User [] users = new User[allUser_vc.size()]; //allUser_vc�� ũ�⸸ŭ User ��ü �迭�� ����
			// user_vc�� �ִ� User ��ü ������ users�� �����Ѵ�.
			for(int i=0; i<users.length; i++) {
				users[i] = (User)allUser_vc.get(i);
			}
			// users�� ����� User ��ü�� userDB.dat�� �����Ѵ�.
			for(User u : users) {
				oos.writeObject(u);
			}
			return 1; // ���� �ε� �Ǿ��� ��� 1�� ����
		} catch (FileNotFoundException e) {
			e.printStackTrace(); return -1; 
			// ���ܰ� �߻��� ��� -1�� �����Ѵ�.
		} catch (IOException e) {
			e.printStackTrace(); return -1;
		}
	}
	
	//userDB�� �����͸� ã�ƿ��� �޼ҵ�
	public User dataSearch(String id) {
		Vector users = dataLoadAll();
		for(int i=0; i<users.size(); i++) 
		{
			User u = (User)users.elementAt(i);
			if(u.getId().equals(id)) {
				return u;
			}
		}
		return null;
	}
	
	//userDB�� ��� �����͸� �ҷ����� �޼ҵ�
	public Vector dataLoadAll() {
		Vector users = new Vector();
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("userDB.dat"))) {
			User user = new User();
			while((user=(User)(ois.readObject()))!=null) {
				users.add(user);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (EOFException e) {
			System.out.println("��� ȸ�� ���� �ε� �Ϸ�!");
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
		return users;
	}
	
	public static void main(String[] args) {
		// db ���� �о���� �׽�Ʈ
		Vector<User> v =  new UserController().dataLoadAll();
		User[] u = new User[v.size()];
		
		for(int i=0; i<v.size(); i++) {
			u[i] = (User)v.get(i);
		}
		
		for(User user : u) {
			System.out.println(user);
		}
	}
}
