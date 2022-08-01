package OwnGameClient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SavedInfoFrame extends JFrame implements ActionListener {
	public SavedInfoFrame(int frameX, int frameY) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBackground(new Color(100,100,100, 180));
		setBounds(frameX + 170, frameY + 400, 350, 120);
		
		Container ct = getContentPane();
		ct.setLayout(null);
		
		JLabel infoLbl = new JLabel("> 상대방이 기록을 저장하였습니다.");
		infoLbl.setBackground(Color.black);
		infoLbl.setForeground(Color.white);
		infoLbl.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		infoLbl.setBackground(new Color(100,100,100,0));
		infoLbl.setBounds(35, 35, 250, 50);
		infoLbl.setOpaque(true);
		ct.add(infoLbl);
		JButton closeBtn = new JButton("×");
		closeBtn.addActionListener(this);
		closeBtn.setBackground(new Color(100,100,100,150));
		closeBtn.setBorderPainted(false);
		closeBtn.setFont(new Font("DungGeunMo", Font.PLAIN, 16));
		closeBtn.setForeground(Color.white);
		closeBtn.setBounds(308, 0, 42, 42);
		ct.add(closeBtn);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.dispose();
	}
}
