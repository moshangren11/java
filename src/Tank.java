import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;


public class Tank {

	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int LIVE = 100;
	
	private boolean live = true;
	private BloodBar bb = new BloodBar();
	
	private int life = LIVE;
	private static int diff = 38;

	TankClient tc = null;
	
	private boolean good;

	private int x = 0;
	private int y = 0;
	private int oldX = 0;
	private int oldY = 0;
	
	private static Random r = new Random();
	
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	
	private boolean bL = false;
	private boolean bU = false;
	private boolean bR = false;
	private boolean bD = false;
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	private int step = r.nextInt(20)+3;
	
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x,int y,TankClient tc,boolean good,Direction dir){
		this(x, y,good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if(good)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.fillOval(this.x, this.y, WIDTH, HEIGHT);
		g.setColor(c);
		
		if(good)
			bb.draw(g);
		
		Color cc = g.getColor();
		g.setColor(Color.BLACK);
		switch(ptDir){
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		case STOP:
			break;
		default:
			break;
		}
		g.setColor(cc);
		
		move();
	}
	
	private void move(){
		
		this.oldX = this.x;
		this.oldY = this.y;
		
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		default:
			break;
		}
		
		if(this.dir != Direction.STOP)
			this.ptDir = this.dir;
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good && step <= 0) {
			step = r.nextInt(20)+3;
			Direction[] dirs = Direction.values();
			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
		}
		
		--step;
		if(!good){
			if(r.nextInt(40) > diff)
				this.fire();
		}

	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_F2:
			if(!this.live){
				this.live = true;
				this.life = LIVE;
			}
			break;
		case KeyEvent.VK_LEFT:
			this.bL = true;
			break;
		case KeyEvent.VK_UP:
			this.bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			this.bR = true;
			break;
		case KeyEvent.VK_DOWN:
			this.bD = true;
			break;
		default:
			break;
		}
		locateDirection();
	}
	
	private void locateDirection(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_Q:
			if(diff>30)
				--diff;
			break;
		case KeyEvent.VK_W:
			if(diff<39)
				++diff;
			break;
		case KeyEvent.VK_LEFT:
			this.bL = false;
			break;
		case KeyEvent.VK_UP:
			this.bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			this.bR = false;
			break;
		case KeyEvent.VK_DOWN:
			this.bD = false;
			break;
		case KeyEvent.VK_A:
			this.superFire();
			break;
		default:
			break;
		}
		locateDirection();
	}
	
	public Missile fire(){
		if(!live)
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y,this.good, this.ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir){
		if(!live)
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y,this.good, dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0; i<8; ++i){
			fire(dirs[i]);
		}
	}
	
	private void stay(){
		this.x = this.oldX;
		this.y = this.oldY;
	}
	
	public boolean collidesWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collodesWithWall(java.util.List<Tank> tanks){
		for(int i=0; i<tanks.size(); ++i){
			Tank t = tc.tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean eat(Blood b){
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = LIVE;
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,this.WIDTH,this.HEIGHT);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public boolean isGood() {
		return good;
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life / 100 ;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}

}
