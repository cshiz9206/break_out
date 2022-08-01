package OwnGameClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class TcpClient extends Thread {
	Socket clientSocket;
	BufferedReader in;
	static BufferedWriter out;
//	ObjectInputStream in;
//	ObjectOutputStream out;
	int[] data;
	ArrayList<Integer> users;
	int userIdx;
	
	final int PORT = 9999;
	final String HOSTIP = "210.102.142.30";
	
	Bar[] bars;
	Ball ball;
	int frameX, frameY;
	
	static boolean allConnected = false;
	
	public TcpClient(Bar[] bars, Ball ball, int frameX, int frameY) {
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress()); //https://onmay.tistory.com/18
			clientSocket = new Socket(HOSTIP, PORT);
			System.out.println("서버와 연결이 되었습니다.\n");
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//			out = new ObjectOutputStream(clientSocket.getOutputStream());
//			in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println();
			e.printStackTrace();
		}
		
		this.bars = bars;
		this.ball = ball;
		this.frameX = frameX;
		this.frameY = frameY;
	}
	
	public void run() {
		data = new int[7];
		users = new ArrayList<Integer>();
		while(true) {
			try {
				String tmp = in.readLine(); // line 중단점(\n) 주의
				//System.out.println(tmp);
				
				if(tmp.contentEquals("저장완료")) {
					BreakOutFrame.saveBtn.setEnabled(false);
					new SavedInfoFrame(frameX, frameY);
					BreakOutFrame.dataUpdate();
				}
				else if(tmp != null) {
					//System.out.println(tmp.split(",")[0]);
					data[0] = Integer.parseInt(tmp.split(",")[0]); // 발신자 번호
					data[1] = Integer.parseInt(tmp.split(",")[1]); // bar
					data[2] = Integer.parseInt(tmp.split(",")[2]); // bar moveLeftAmt
					data[3] = Integer.parseInt(tmp.split(",")[3]); // bar moveRightAmt
					data[4] = Integer.parseInt(tmp.split(",")[4]); // ball x
					data[5] = Integer.parseInt(tmp.split(",")[5]); // ball y
					data[6] = Integer.parseInt(tmp.split(",")[6]);
					
					if(!users.contains(data[0]) && (data[0] != -1)) {
						users.add(data[0]);
						Arrays.sort(users.toArray());
					}
					
					if(data[0] == -1) {
						allConnected = true;
					}
					else {
						userIdx = users.indexOf(data[0]) + 1;
						bars[userIdx].moveByConnect(data[1], data[5]);
						if(data[2] != -1) bars[userIdx].setMovLeftAmt(data[2]);
						if(data[3] != -1) bars[userIdx].setMovRightAmt(data[3]);
						ball.moveByConnect(data[4], data[5]);
						if(data[6] != -1) ball.crash(data[6]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendMsg(String data) {
		try {
			//System.out.println(barX + "," + ballX + "," + ballY);
			out.write(data);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
