package OwnGameServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TcpServer extends Thread {
	static ArrayList<ClientConnection> clientConnections;
	ServerSocket listener;
	final int clientMax = 3;
	int connectedCnt = 0;
	int connectIdx = 0;
	
	public TcpServer(int port) {
		clientConnections = new ArrayList<ClientConnection>();
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				if(ServerFrame.isDisposed) break;
				
				for(ClientConnection cc : clientConnections) {
					if(!cc.isAlive()) {
						cc = null;
					}
				}
				clientConnections.add(connectIdx, new ClientConnection(connectIdx + 1, this, listener.accept()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientConnections.get(connectIdx).start();
			connectIdx += 1;
			if(connectIdx == clientMax) {
				connectedCnt = connectIdx;
				sendMsg(-1, "-1,-1,-1,-1,-1,-1"); // ��� ���� �Ϸ�
			}
			if(connectIdx >= clientMax) connectIdx = 0;
		}
	}
	
	public synchronized void sendMsg(int sentNo, String data) {
		
		// ���� server�� ���� �����͸� ���� Ŭ���̾�Ʈ�� ������ Ŭ���̾�Ʈ �����Ͽ� ������ ����
		for(int i = 0; i < connectedCnt; i++) {
			if(i == (sentNo - 1)) {
				//System.out.println(i);
				continue;
			}
			//System.out.println(i);
			clientConnections.get(i).sendMsg(sentNo, data);
		}
		
		if(sentNo >= 0) clientConnections.get(sentNo - 1).setIsSendNow();
	}
}
