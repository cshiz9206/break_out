package OwnGameClient;

import java.awt.Point;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ball extends JLabel implements Runnable {
	int maxWidth, maxHeight;
	
	Bar[] bars;
	Wall[] walls;
	JPanel mainPnl;
	
	double xSpeed, ySpeed;
	static boolean isDead = false;
	static boolean isAllBroken = false;
	final int MAXSPEED = 10;
	
	public Ball(int stage, int startX, int maxWidth, int maxHeight, Bar[] bars, Wall[] walls, JPanel mainPnl) {
		ImageIcon ii = new ImageIcon("..\\BreakOut_figure\\ball.png");
		setIcon(ii);
		setSize(ii.getIconWidth(), ii.getIconHeight());
		setLocation(startX + maxWidth / 2 - (getWidth() / 2), maxHeight / 2);
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.bars = bars;
		this.walls = walls;
		this.mainPnl = mainPnl;
		
		Random rd = new Random();
		this.xSpeed = 0;
		this.ySpeed = 3 + stage * 2;//rd.nextInt(MAXSPEED - 5) + 5;
		if(ySpeed > MAXSPEED) ySpeed = MAXSPEED;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(checkWallsRemoved()) {
				mainPnl.remove(this);
				isAllBroken = true;
				break;
			}
			if(isDead || TimeThread.timeEnd || isAllBroken) {
				mainPnl.remove(this);
				break;
			}
			
			checkBumpedBorder();
			for(int i = 0; i < bars.length; i++) checkBumpedUser(bars[i]);
			checkBumpedWall();
			
			if(bars.length == 1) {
				setLocation((int)(getX() + xSpeed), (int)(getY() + ySpeed));
			}
			
			mainPnl.repaint();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void moveByConnect(int moveX, int moveY) {
		setLocation(moveX, moveY);
	}
	
	boolean checkWallsRemoved() {
		for(int i = 0; i < walls.length; i++) {
			if(walls[i] != null) return false;
		}
		return true;
	}
	
	void checkBumpedBorder() {
		if(getX() <= 0) xSpeed = Math.abs(xSpeed);
		if((getX() + getWidth()) >= maxWidth) xSpeed = -Math.abs(xSpeed);
		if(getY() <= 0) ySpeed = Math.abs(ySpeed);
		if((getY() + getHeight()) >= maxHeight) isDead = true;
	}
	
	void checkBumpedUser(Bar bar) {
		if(bar.getBounds().contains(getBounds().getCenterX(), getBounds().getMaxY())) {
			ySpeed = -Math.abs(ySpeed);
			
			xSpeed += bar.moveRightAmt;
			xSpeed -= bar.moveLeftAmt;
			if(xSpeed > MAXSPEED) xSpeed = MAXSPEED;
			if(xSpeed < (-1) * MAXSPEED) xSpeed = (-1) * MAXSPEED;
			
			bar.resetMovLeftAmt();
			bar.resetMovRightAmt();
		}
	}
//	
//	void checkBumpedUser2() {
//		if(user2.getBounds().contains(getBounds().getCenterX(), getBounds().getMaxY())) {
//			ySpeed = -Math.abs(ySpeed);
//			
//			xSpeed += user2.moveRightAmt;
//			xSpeed -= user2.moveLeftAmt;
//			if(xSpeed > MAXSPEED) xSpeed = MAXSPEED;
//			if(xSpeed < (-1) * MAXSPEED) xSpeed = (-1) * MAXSPEED;
//			
//			user2.resetMovLeftAmt();
//			user2.resetMovRightAmt();
//		}
//	}
	
	void checkBumpedWall() {
		boolean isBumped;
		Point topP = new Point((int)getBounds().getCenterX(), getY());
		Point rightP = new Point((int)getBounds().getMaxX(), (int)getBounds().getCenterY());
		Point leftP = new Point((int)getBounds().getMinX(), (int)getBounds().getCenterY());
		Point bottomP = new Point((int)getBounds().getCenterX(), (int)getBounds().getMaxY());
		Point leftTopP = new Point((int)getBounds().getMinX(), (int)getBounds().getMinY());
		Point leftBotP = new Point((int)getBounds().getMinX(), (int)getBounds().getMaxY());
		Point rightTopP = new Point((int)getBounds().getMaxX(), (int)getBounds().getMinY());
		Point rightBotP = new Point((int)getBounds().getMaxX(), (int)getBounds().getMaxY());
		for(int i = 0; i < walls.length; i++) {
			isBumped = false;
			if(walls[i] == null) continue;
			if(walls[i].getBounds().contains(topP)) {
				ySpeed *= -1;
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(rightP)) {
				xSpeed *= -1;
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(leftP)) {
				xSpeed *= -1;
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(bottomP)) {
				ySpeed *= -1;
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(leftTopP)) {
				xSpeed = Math.abs(xSpeed);
				ySpeed = Math.abs(ySpeed);
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(leftBotP)) {
				xSpeed = Math.abs(xSpeed);
				ySpeed = -Math.abs(ySpeed);
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(rightBotP)) {
				xSpeed = -Math.abs(xSpeed);
				ySpeed = -Math.abs(ySpeed);
				isBumped = true;
			}
			else if(walls[i].getBounds().contains(rightTopP)) {
				xSpeed = -Math.abs(xSpeed);
				ySpeed = Math.abs(ySpeed);
				isBumped = true;
			}
			
			if(isBumped) {
				walls[i].scoring();
				crash(i);
			}
		}
	}
	
	void crash(int i) {
		try {
			if(walls[i] != null) {
				for(int j = 0; j < 100; j++) {
					Fragment fg = new Fragment(walls[i], mainPnl);
					new Thread(fg).start();
				}
				
				mainPnl.remove(walls[i]);
				mainPnl.repaint();
				
				walls[i] = null;
			}
		} catch (NullPointerException e) {}
	}
	
//	void calcDirSpeed(int i) {
//		Vector<Double> p1v = new Vector<Double>(Arrays.asList(xSpeed, ySpeed));
//		Vector<Double> p1r = new Vector<Double>(Arrays.asList((double)getBounds().getCenterX(), (double)getBounds().getCenterY()));
//		Vector<Double> p2r = new Vector<Double>(Arrays.asList((double)walls[i].getBounds().getCenterX(), (double)walls[i].getBounds().getCenterY()));
//		Vector<Double> p2v = new Vector<Double>(Arrays.asList((double)0, (double)0));
//		Vector<Double> p1rSp2r = new Vector<Double>(Arrays.asList((double)p1r.get(0) - (double)p2r.get(0), (double)p1r.get(1) - (double)p2r.get(1)));
//		Vector<Double> p2rSp1r = new Vector<Double>(Arrays.asList((double)p2r.get(0) - (double)p1r.get(0), (double)p2r.get(1) - (double)p1r.get(1)));
//		Vector<Double> p1vSp2v = new Vector<Double>(Arrays.asList((double)p1v.get(0) - (double)p2v.get(0), (double)p1v.get(1) - (double)p2v.get(1)));
//		Vector<Double> dTmp = new Vector<Double>(Arrays.asList((double)p1rSp2r.get(0) / (double)p1rSp2r.get(0), (double)p1rSp2r.get(1) / (double)p1rSp2r.get(1)));
//		Vector<Double> d = new Vector<Double>(Arrays.asList((double)dTmp.get(0) * (double)dTmp.get(0), ((double)dTmp.get(1) * (double)dTmp.get(1))));
//		Vector<Double> dotTmp = new Vector<Double>(Arrays.asList(Math.abs((double)p1vSp2v.get(0) * (double)p1rSp2r.get(0)), Math.abs((double)p1vSp2v.get(1) * (double)p1rSp2r.get(1))));
//		double dot = (double)dotTmp.get(0) + (double)dotTmp.get(1);
//		Vector<Double> div = new Vector<Double>(Arrays.asList((double)dot / (double)d.get(0), (double)dot / (double)d.get(1)));
//		Vector<Double> mul = new Vector<Double>(Arrays.asList((double)div.get(0) * (double)p2rSp1r.get(0), (double)div.get(1) * (double)p2rSp1r.get(1)));
//		Vector<Double> sub = new Vector<Double>(Arrays.asList((double)p2v.get(0) - (double)mul.get(0), (double)p2v.get(1) - (double)mul.get(1)));
//		Vector<Double> u1 = sub;
//		xSpeed = u1.get(0) % 5;
//		ySpeed = u1.get(1) % 5;
//	}
}
