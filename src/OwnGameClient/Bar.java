package OwnGameClient;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bar extends JLabel {
	int maxWidth, maxHeight;
	int moveLeftAmt = 0;
	int moveRightAmt = 0;
	int beforeMoved, beforeballMoved;
	final int SPEED = 20;
	
	static int score = 0;
	static String userName = "none";
	
	public Bar(int startX, int maxWidth, int maxHeight, String imagePath) {
		ImageIcon ii = new ImageIcon(imagePath);
		setIcon(ii);
		setSize(ii.getIconWidth(), ii.getIconHeight());
		setLocation(startX + maxWidth / 2 - (getWidth() / 2), maxHeight - 70);
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		beforeMoved = getX();
		beforeballMoved = maxHeight / 2;
	}
	
	public void moveRight() { 
		setLocation(getX() + SPEED, getY());
		if(getBounds().getMaxX() > maxWidth) setLocation(maxWidth - getWidth(), getY());
	}
	public void moveLeft() { 
		setLocation(getX() - SPEED, getY()); 
		if(getX() < 0) setLocation(0, getY());
	}
	public void moveByConnect(int moveX, int ballY) { 
		setLocation(moveX, getY());
//		if(moveX - beforeMoved > 0) {
//			if((ballY >= 900 - 150) && ((ballY - beforeballMoved) > 0)) setMovRightAmt((moveX - beforeMoved) / SPEED);
//			resetMovLeftAmt();
//		}
//		else if(moveX - beforeMoved < 0) {
//			if((ballY >= 900 - 150) && ((ballY - beforeballMoved) < 0)) setMovLeftAmt((beforeMoved - moveX) / SPEED);
//			resetMovRightAmt();
//		}
//		beforeMoved = moveX;
//		beforeballMoved = ballY;
	}
	
	public void resetMovRightAmt() { moveRightAmt = 0; }
	public void resetMovLeftAmt() { moveLeftAmt = 0; }
	public void setMovRightAmt(int amount) { 
		moveRightAmt += amount;
		if(moveRightAmt > 3) moveRightAmt = 3;
	}
	public void setMovLeftAmt(int amount) { 
		moveLeftAmt += amount;
		if(moveLeftAmt > 3) moveLeftAmt = 3;
	}
}
