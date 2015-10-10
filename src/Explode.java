import java.awt.Color;
import java.awt.Graphics;


public class Explode {
	private int x = 0;
	private int y = 0;
	private boolean live = true;
	
	private TankClient tc = null;
	
	private int[] diameter = {32,49,55,60,55,49,30,25,20,14,6};
	private int step = 0;
	
	public Explode(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
		
	}
	
	public void draw(Graphics g){
		if(!this.live){
			tc.explode.remove(this);
			return;
		}
		
		if(step == diameter.length){
			this.live = false;
			this.step = 0;
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		
		++this.step;
	}
}
