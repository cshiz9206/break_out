package OwnGameServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerFrame extends JFrame implements ActionListener, MouseMotionListener {
	JTextField jtfServPort;
	
	JButton jbGeneration;
	
	int cursorX, cursorY;
	
	TcpServer ts;
	
	static boolean isDisposed = false;
	
	public ServerFrame() {
		setUndecorated(true);
		setBackground(new Color(100,100,100, 180));
		setBounds(100, 100, 400, 120);
		this.addMouseMotionListener(this);
		
		Container ct = getContentPane();
		ct.setLayout(null);
		
		JLabel infoLbl = new JLabel("> PORT");
		infoLbl.setBackground(Color.black);
		infoLbl.setForeground(Color.white);
		infoLbl.setFont(new Font("DungGeunMo", Font.PLAIN, 15));
		infoLbl.setBackground(new Color(100,100,100,0));
		infoLbl.setBounds(35, 35, 50, 50);
		infoLbl.setOpaque(true);
		ct.add(infoLbl);
		jtfServPort = new JTextField();
		jtfServPort.setForeground(Color.white);
		jtfServPort.setFont(new Font("DungGeunMo", Font.PLAIN, 13));
		jtfServPort.setHorizontalAlignment(JLabel.CENTER);
		jtfServPort.setBackground(new Color(100,100,100,150));
		jtfServPort.setBounds(110, 47, 175, 25);
		jtfServPort.setOpaque(true);
		ct.add(jtfServPort);
		jbGeneration = new JButton("Enter");
		jbGeneration.addActionListener(this);
		jbGeneration.setBackground(new Color(100,100,100,150));
		jbGeneration.setBorderPainted(false);
		jbGeneration.setFont(new Font("DungGeunMo", Font.PLAIN, 13));
		jbGeneration.setForeground(Color.white);
		jbGeneration.setBounds(285, 47, 70, 25);
		ct.add(jbGeneration);
		JButton closeBtn = new JButton("×");
		closeBtn.addActionListener(this);
		closeBtn.setBackground(new Color(100,100,100,150));
		closeBtn.setBorderPainted(false);
		closeBtn.setFont(new Font("DungGeunMo", Font.PLAIN, 16));
		closeBtn.setForeground(Color.white);
		closeBtn.setBounds(358, 0, 42, 42);
		ct.add(closeBtn);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().contentEquals(jbGeneration.getText())) {
			// 서버 객체 생성 / 연결 기다림(
			ts = new TcpServer(Integer.parseInt(jtfServPort.getText()));
			ts.start();
		}
		else {
			isDisposed = true;
			//for(ClientConnection cc : TcpServer.clientConnections) cc.interrupted();
			try {
				if(ts != null) ts.listener.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.dispose();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setLocation(arg0.getXOnScreen() - cursorX, arg0.getYOnScreen() - cursorY);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		cursorX = arg0.getX();
		cursorY = arg0.getY();
	}
}
