package OwnGameClient;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class LoadThread extends JLabel implements Runnable {
	String loading = "Loading";
	boolean allConnected;
	
	public LoadThread(int frameWidth, int frameHeight, boolean allConnected) {
		setText(loading);
		setFont(new Font("DungGeunMo", Font.PLAIN, 25));
		setSize(150, 50);
		setForeground(Color.LIGHT_GRAY);
		setBackground(Color.black);
		setOpaque(true);
		setLocation(frameWidth / 2 - (getWidth() / 2), 600);
		
		this.allConnected = allConnected;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int cnt = 0;
		while(true) {
			if(cnt == 3) {
				loading = "Loading";
				setText(loading);
				cnt = 0;
			}
			loading += ".";
			setText(loading);
			cnt += 1;
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(TcpClient.allConnected) break;
		}
	}
	
}
