package kh.mini.project.GameRoom;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class ShapeSave {

	private int thick = 8;
	
	private int size=0;
	Color mypencolor = Color.black;
	ArrayList<Point> sketchSP = new ArrayList<Point>();
	

	public ShapeSave() {}
	
	public int getThick() {
		return thick;
	}
	
	public void setThick(int thick) {
		this.thick=thick;
	}

	
	
}
