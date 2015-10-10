
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


public class TankClient extends Frame{
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(50,50,this,true,Tank.Direction.STOP);
	
	Wall w1 = new Wall(300, 200, 20, 150, this);
	Blood bd = new Blood();
	
	List<Tank> tanks = new ArrayList<Tank>();
	List<Explode> explode = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	Image offScreenImage = null;

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.LaunchFrame();
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawString("diff      count:" + myTank.getDiff(), 10, 50);
		g.drawString("missiles 	count:" + missiles.size(), 10, 70);
		g.drawString("explodes 	count:" + explode.size(), 10, 90);
		g.drawString("tanks		count:" + tanks.size(), 10, 110);
		g.drawString("x: " + myTank.getX()+"   y: "+myTank.getY(), 10, 130);
		g.drawString(""+myTank.getLife(), myTank.getX()+5, myTank.getY() - 15);
		
		if(tanks.size() <= 0){
			for(int i = 1; i<=5; i++){
				tanks.add(new Tank(50 + i*50, 50, this, false,Tank.Direction.D));
			}
		}
		
		if(missiles != null)
			for(int i=0; i<missiles.size(); ++i){
				Missile m = missiles.get(i);
				m.hitTanks(tanks);
				m.hitTank(myTank);
				m.hitWall(w1);
				m.draw(g);
				//if(!m.isLive())
				//	missiles.remove(i);
				//else
				//	m.draw(g);
			}
		
		if(explode != null)
			for(int j=0; j<explode.size(); j++){
				Explode e = explode.get(j);
				e.draw(g);
			}
		if(tanks != null){
			for(int i=0; i<tanks.size();i++){
				Tank t = tanks.get(i);
				t.collidesWithWall(w1);
				t.collodesWithWall(tanks);
				t.draw(g);
			}
		}
		myTank.draw(g);
		myTank.collodesWithWall(tanks);
		myTank.eat(bd);
		w1.draw(g);
		bd.draw(g);
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void LaunchFrame(){
		
		//Ìí¼ÓµÐ·½Ì¹¿Ë
		for(int i = 1; i<=10; i++){
			tanks.add(new Tank(50 + i*50, 50, this, false,Tank.Direction.D));
		}
		
		
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();
	}

	private class PaintThread implements Runnable{

		@Override
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}
