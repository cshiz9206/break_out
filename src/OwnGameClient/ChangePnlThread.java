package OwnGameClient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChangePnlThread extends Thread {
	
	public void run() {
		while(true) {
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(BreakOutFrame.isRestart || BreakOutFrame.isStart) {
				break;
			}
		}
	}
}


// while�� �ȿ� Ư�� �۾��� ���� if���� ������ ó���� �ȵ�