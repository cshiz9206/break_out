package OwnGameHost;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.SpinnerUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BreakOutFrame extends JFrame implements KeyListener, ActionListener, MouseListener {
	JPanel mainPnl;
	JPanel endPnl;
	JPanel startPnl;
	static JTable jtb;
	JLabel titleLbl, endLbl, timerLbl, scoreLbl, stageLbl;
	static JButton saveBtn;
	JButton restartBtn;
	JLabel startBtn, playerSetLbl;
	JTextField nameJtf;
	JSpinner playerSetSpnr;
	
	Container ct;
//	static ScoreBoard db;
	ChangePnlThread st;
	
	Wall[] walls;
	Bar[] bars;
	Ball ball;
	JLabel info;
	
	Thread ballMove;
	TimeThread timer;
	LoadThread loadLbl;
	Thread load;
	
	TcpClient tcpConnection;
	
	int floor = 1;
	int wallCntByFloor = 1;
	int wallCount = floor * wallCntByFloor;
	int stage = 1;
	int playerCnt;
	
	final int frameWidth = 700;
	final int frameHeight = 900;
	
	static boolean isStart = false;
	static boolean isRestart = false;
	
	public BreakOutFrame() {
		// initialize Window
		initFrame();
		initContainer();
		
		// create DataBase
//		db = new ScoreBoard();
	}
	
	void initContainer() {
		ct = getContentPane();
		ct.setLayout(null);
		ct.setFocusable(true);
		ct.addKeyListener(this);
		ct.setSize(700, 900);
		ct.setBackground(Color.black);
	}
	
	void initFrame() {
		setTitle("Break out");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(frameWidth, frameHeight);
		setVisible(true);
	}
	
	void createStartPnl() {
		startPnl = new JPanel(null);
		startPnl.setBackground(Color.black);
		startPnl.setBounds(0, 0, frameWidth, frameHeight);
		
		createTitleLabel();
		startPnl.add(titleLbl);
		
		createPSpinner();
		startPnl.add(playerSetSpnr);
		
		createPlayerSetLbl();
		startPnl.add(playerSetLbl);
		
		createStartBtn();
		startPnl.add(startBtn);
	}
	
	void createMainPnl(int playerCnt) {
		mainPnl = new JPanel(null);
		mainPnl.setBackground(Color.black);
		mainPnl.setBounds(0, 0, frameWidth, frameHeight);
		
		createStageLbl();
		mainPnl.add(stageLbl);
		
		floor = stage;
		wallCntByFloor = stage * 2;
		wallCount = floor * wallCntByFloor;
		walls = createWalls(0, frameWidth, frameHeight);
		for(Wall wall : walls) mainPnl.add(wall);
		
		bars = new Bar[playerCnt];
		for(int i = 0; i < playerCnt; i++) {
			bars[i] = new Bar(0, frameWidth - 20, frameHeight, "..\\BreakOut_figure\\bar" + i + ".png");
			mainPnl.add(bars[i]);
		}
//		
//		bar2 = new Bar(0, frameWidth - 20, frameHeight, "..\\BreakOut_figure\\userClient.png");
//		mainPnl.add(bar2);

		ball = new Ball(stage, 0, frameWidth - 20, frameHeight, bars,  walls, mainPnl);
		Ball.isDead = false;
		mainPnl.add(ball);
		
		timerLbl = createTimeLabel();
		mainPnl.add(timerLbl);
		
		scoreLbl = createScoreLabel();
		mainPnl.add(scoreLbl);
//		info = createInfoLabel();
//		mainPnl.add(info);
	}
	
	void createEndPnl() {
		endPnl = new JPanel(null);
		endPnl.setBackground(Color.black);
		endPnl.setBounds(0, 0, frameWidth, frameHeight);
		
		createEndLabel();
		endPnl.add(endLbl);
		
		createNameTextField();
		endPnl.add(nameJtf);
		
		createScoreTbl();
		endPnl.add(jtb);
		
		createSaveBtn();
		endPnl.add(saveBtn);
		
		createRestartBtn();
		endPnl.add(restartBtn);
		
		scoreLbl.setText("SCORE  " + scoreLbl.getText());
		scoreLbl.setHorizontalAlignment(JLabel.CENTER);
		scoreLbl.setFont(new Font("DungGeunMo", Font.BOLD, 70));
		scoreLbl.setBounds(40, 200, 600, 150);
		endPnl.add(scoreLbl);
		
		dataUpdate();
	}
	
	void createStageLbl() {
		stageLbl = new JLabel("STAGE" + String.valueOf(stage));
		stageLbl.setFont(new Font("DungGeunMo", Font.BOLD, 35));
		stageLbl.setBackground(new Color(0,0,0,0));
		stageLbl.setForeground(new Color(255,255,255,80));
		stageLbl.setOpaque(true);
		stageLbl.setSize(150, 50);
		stageLbl.setLocation(280, 500);
	}
	
	void createTitleLabel() {
		titleLbl = new JLabel();
		ImageIcon ii = new ImageIcon("..\\BreakOut_figure\\title2.png");
		titleLbl.setIcon(ii);
		titleLbl.setSize(ii.getIconWidth(), ii.getIconHeight());
		titleLbl.setLocation(40, 230);
	}
	
	
	void createPlayerSetLbl() {
		playerSetLbl = new JLabel("  player numbers");
		playerSetLbl.setHorizontalAlignment(JLabel.LEFT);
		playerSetLbl.setBackground(Color.DARK_GRAY);
		playerSetLbl.setForeground(new Color(150, 150, 150));
		playerSetLbl.setOpaque(true);
		playerSetLbl.setLayout(null);
		playerSetLbl.setBounds(100, 450, 480, 30);
	}
	
	void createPSpinner() {
		playerSetSpnr = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
		playerSetSpnr.setBackground(Color.DARK_GRAY);
		playerSetSpnr.setForeground(new Color(150, 150, 150));
		playerSetSpnr.setOpaque(true);
		playerSetSpnr.setBounds(545, 455, 30, 20);
	}
	
	void createStartBtn() {
		startBtn = new JLabel();
		ImageIcon ii = new ImageIcon("..\\BreakOut_figure\\startBtn2.png");
		startBtn.setIcon(ii);
		startBtn.setSize(ii.getIconWidth(), ii.getIconHeight());
		startBtn.setBackground(Color.black);
		startBtn.setLocation(240, 550);
		startBtn.addMouseListener(this);
	}
	
	void createScoreTbl() {
		String[] header = {"No", "User", "Score"};
		DefaultTableModel dtm = new DefaultTableModel(header, 0);
		jtb = new JTable(dtm);
		jtb.setBackground(Color.black);
		jtb.setForeground(Color.white);
		jtb.setGridColor(Color.black);
		jtb.setFont(new Font(null, Font.PLAIN, 15));
		DefaultTableCellRenderer tmp = new DefaultTableCellRenderer();
		tmp.setHorizontalAlignment(JLabel.CENTER);
		for(String colName : header) jtb.getColumn(colName).setCellRenderer(tmp);
//		jtb.setBounds(85, 200, 500, 450);
		jtb.setBounds(85, 400, 500, 250);
		dtm.addRow(header);
	}
	
	Wall[] createWalls(int startX, int ctWidth, int ctHeight) {
		Wall[] walls = new Wall[wallCount];
		int tmpWallCnt = 0;
		ctWidth -= 15;
		ctHeight -= 15;
		a : for(int i = 0; i < wallCntByFloor; i++) {
				for(int j = 0; j < floor; j++) {
					walls[tmpWallCnt] = new Wall(ctWidth, ctHeight);
					walls[tmpWallCnt].setLocation(((i * ctWidth / wallCntByFloor) + 
							(ctWidth / wallCntByFloor / 2) - (walls[tmpWallCnt].getWidth() / 2)), 
							50 + j * (walls[tmpWallCnt].getHeight() + 10));
					walls[tmpWallCnt].setLocation(walls[tmpWallCnt].getX() + startX, walls[tmpWallCnt].getY());
					for(WallColor wc : WallColor.values()) {
						if(wc.floorNum == (floor - j)) {
							walls[tmpWallCnt].setIcon(new ImageIcon(wc.path));
							walls[tmpWallCnt].color = wc;
						}
					}
					tmpWallCnt += 1;
					if(tmpWallCnt == wallCount) break a;
				}
		}
		return walls;
	}
	
	JLabel createTimeLabel() {
		JLabel timerLbl = new JLabel("0s");
		timerLbl.setHorizontalAlignment(JLabel.CENTER);
		timerLbl.setBounds(600, 0, 100, 50);
		timerLbl.setForeground(Color.GRAY);
		timerLbl.setBackground(Color.black);
		timerLbl.setFont(new Font("DungGeunMo", Font.BOLD, 20));
		timerLbl.setOpaque(true);
		return timerLbl;
	}
	
	JLabel createScoreLabel() {
		JLabel scoreLbl = new JLabel("" + Bar.score);
		//scoreLbl.setHorizontalAlignment(JLabel.CENTER);
		scoreLbl.setBounds(10, 0, 100, 50);
		scoreLbl.setForeground(Color.GRAY);
		scoreLbl.setBackground(Color.black);
		scoreLbl.setFont(new Font("DungGeunMo", Font.BOLD, 20));
		scoreLbl.setOpaque(true);
		return scoreLbl;
	}
	
//	JLabel createInfoLabel() {
//		JLabel info = new JLabel("time : " + 0 + "s, score : " + Bar.score);
//		info.setHorizontalAlignment(JLabel.CENTER);
//		info.setBounds(135, 550, 430, 100);
//		info.setForeground(Color.GRAY);
//		info.setBackground(Color.black);
//		info.setFont(new Font("Times", Font.BOLD, 20));
//		info.setOpaque(true);
//		return info;
//	}
	
	void createEndLabel() {
		ImageIcon ii = new ImageIcon("..\\BreakOut_figure\\gameover.jpg");
		endLbl = new JLabel();
		endLbl.setIcon(ii);
		endLbl.setSize(ii.getIconWidth(), ii.getIconHeight());
		endLbl.setLocation(210, 30);
	}
	
	void createNameTextField() {
		nameJtf = new JTextField(10);
		nameJtf.setBackground(Color.DARK_GRAY);
		nameJtf.setForeground(Color.LIGHT_GRAY);
		nameJtf.setBounds(120, 700, 250, 30);
	}
	
	void createSaveBtn() {
		saveBtn = new JButton("Save");
		saveBtn.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
		saveBtn.setBorderPainted(false);
		saveBtn.setBackground(Color.BLACK);
		saveBtn.setForeground(Color.white);
		saveBtn.setBounds(415, 700, 150, 30);
		saveBtn.addActionListener(this);
	}
	
	void createRestartBtn() {
		restartBtn = new JButton("Restart");
		restartBtn.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
		restartBtn.setBorderPainted(false);
		restartBtn.setBackground(Color.BLACK);
		restartBtn.setForeground(Color.white);
		restartBtn.setBounds(265, 770, 150, 30);
		restartBtn.addActionListener(this);
	}
	
	void process() {
		createStartPnl();
		ct.add(startPnl);
		ct.revalidate();
		ct.repaint();
		
		st = new ChangePnlThread();
		st.start();
		
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isStart = false;
		
		playerCnt = Integer.parseInt(playerSetSpnr.getValue().toString());
		
		while(true) {
			if(isRestart) stage = 1;
			System.out.println(stage);
			
			if(playerCnt == 1) init1PGame();
			else if(playerCnt >= 2) initNPGame();
			
			isRestart = false;
			
			try {
				ballMove.join();
				timer.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!Ball.isDead && !TimeThread.timeEnd) {
				stage += 1;
				Ball.isAllBroken = false;
				continue;
			}
			
			endGame();
			
			try {
				st.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TcpClient.allConnected = false;
		}
	}
	
	void init1PGame() {
		ct.removeAll();
		ct.revalidate();
		ct.repaint();
		
		createMainPnl(1);
		ct.add(mainPnl);
		ct.revalidate();
		ct.repaint();
		ct.requestFocus();
		
		this.ballMove = null;
		ballMove = new Thread(ball);
		ballMove.start();
		
		this.timer = null;
		timer = new TimeThread(scoreLbl, timerLbl);
		if(isRestart) bars[0].score = 0;
		timer.timeEnd = false;
		timer.start();
	}
	
	void initNPGame() {
		ct.removeAll();
		ct.revalidate();
		ct.repaint();
		
		createMainPnl(playerCnt);
		ct.add(mainPnl);
		ct.revalidate();
		ct.repaint();
		ct.requestFocus();
		
		// create connection
		tcpConnection = new TcpClient(bars, ball, getX(), getY());
		tcpConnection.start();
//		new TcpSendThread(bar, ball).start();
		
		if(!TcpClient.allConnected) {
			loadLbl = new LoadThread(frameWidth, frameHeight, tcpConnection.allConnected);
			mainPnl.add(loadLbl);
			load = new Thread(loadLbl);
			load.start();
		}
		
		try {
			load.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainPnl.remove(loadLbl);
		
		//this.ballMove = null;
		ballMove = new Thread(ball);
		ballMove.start();
		
		//this.timer = null;
		timer = new TimeThread(scoreLbl, timerLbl);
		if(isRestart) bars[0].score = 0;
		timer.timeEnd = false;
		timer.start();
	}
	
	void endGame() {
		ct.removeAll();
		ct.revalidate();
		ct.repaint();
		
		createEndPnl();
		ct.add(endPnl);
		ct.revalidate();
		ct.repaint();
		
		st = new ChangePnlThread();
		st.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveLeftProcess();
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveRightProcess();
		}
		
		try {
			tcpConnection.sendMsg(bars[0].getX() + "," + bars[0].moveLeftAmt + "," + bars[0].moveRightAmt + "," + ball.getX() + "," + ball.getY() + "," + -1);
		} catch(NullPointerException a) {}
	}
	
	void moveRightProcess() {
		bars[0].moveRight();
		if((ball.getY() >= ct.getHeight() - 150) && (ball.ySpeed > 0)) bars[0].setMovRightAmt(1);
		bars[0].resetMovLeftAmt();
	}
	
	void moveLeftProcess() {
		bars[0].moveLeft();
		if((ball.getY() >= ct.getHeight() - 150) && (ball.ySpeed > 0)) bars[0].setMovLeftAmt(1);
		System.out.println(ball.ySpeed);
		bars[0].resetMovRightAmt();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().contentEquals("Save")) {
			String userName = nameJtf.getText();
			bars[0].userName = userName;
//			db.insertBoard(bars[0].userName, bars[0].score);
			dataUpdate();
			nameJtf.setText("");
			
			try {
				TcpClient.sendMsg("저장완료");
			} catch(NullPointerException a) {}
			saveBtn.setEnabled(false);
		}
		else {
			isRestart = true;
		}
	}
	
	public static void dataUpdate() {
		clearJtable();
//		ArrayList<String[]> data = db.readBoard();
//		for(String[] userData : data) ((DefaultTableModel)jtb.getModel()).addRow(userData);
	}
	
	public static void clearJtable() {
		((DefaultTableModel)jtb.getModel()).setNumRows(0);
		String[] header = {"No", "User", "Score"};
		((DefaultTableModel)jtb.getModel()).addRow(header);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		ImageIcon ii = new ImageIcon("..\\BreakOut_figure\\startClicked2.png");
		startBtn.setIcon(ii);
		startBtn.setSize(ii.getIconWidth(), ii.getIconHeight());
		startBtn.setLocation(220, 540);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		isStart = true;
	}
}