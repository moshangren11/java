import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Wall {

	private int x;
	private int y;
	private int w;
	private int h;
	TankClient tc;
	
	public Wall(int x, int y, int w, int h, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
	
	
}
