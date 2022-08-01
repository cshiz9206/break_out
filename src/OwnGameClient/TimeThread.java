package OwnGameClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class TimeThread extends Thread {
	static int score = 0;
	int maxTime = 100;
	JLabel scoreLbl, timerLbl;
	static boolean timeEnd = false;
	
	public TimeThread(JLabel scoreLbl, JLabel timerLbl) {
		this.scoreLbl = scoreLbl;
		this.timerLbl = timerLbl;
	}
	
	public void run() {
		LocalDateTime start = LocalDateTime.now();
		while(true) {
			LocalDateTime now = LocalDateTime.now();
			Duration time = Duration.between(start, now);
			int sec = (int)(maxTime - time.getSeconds());
			timerLbl.setText(sec + "s");
			scoreLbl.setText("" + Bar.score);
			if(sec == 0) {
				timeEnd = true;
				Bar.score = Bar.score + sec;
				//jlbHead.setText("Your score : " + Bar.score);
				break;
			}
			if(Ball.isDead || Ball.isAllBroken) {
				//jlbHead.setText("Your score : " + Bar.score);
				break;
			}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
